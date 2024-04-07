package com.qgStudio.pedestal.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgStudio.pedestal.constant.RedisConstants;
import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.dto.AddFocusOnTemplateDTO;
import com.qgStudio.pedestal.entity.dto.UpdateFocusOnTemplateDTO;
import com.qgStudio.pedestal.entity.po.FocusOnTemplate;
import com.qgStudio.pedestal.entity.po.Pedestal;
import com.qgStudio.pedestal.entity.po.UserPedestalMap;
import com.qgStudio.pedestal.entity.vo.IntegerVo;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;
import com.qgStudio.pedestal.mapper.FocusOnTemplateMapper;
import com.qgStudio.pedestal.mapper.PedestalMapper;
import com.qgStudio.pedestal.mapper.UserPedestalMapMapper;
import com.qgStudio.pedestal.mqtt.MqttConfiguration;
import com.qgStudio.pedestal.service.IFocusOnTemplateService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
@Service
public class FocusOnTemplateServiceImpl extends ServiceImpl<FocusOnTemplateMapper, FocusOnTemplate> implements IFocusOnTemplateService {
    private final StringRedisTemplate stringRedisTemplate;
    private final FocusOnTemplateMapper focusOnTemplateMapper;
    private final MqttClient mqttClient;
    private final MqttConfiguration mqttConfiguration;
    private final UserPedestalMapMapper userPedestalMapMapper;
    private final PedestalMapper pedestalMapper;

    @Autowired
    public FocusOnTemplateServiceImpl(StringRedisTemplate stringRedisTemplate, FocusOnTemplateMapper focusOnTemplateMapper, MqttClient mqttClient, MqttConfiguration mqttConfiguration, UserPedestalMapMapper userPedestalMapMapper, PedestalMapper pedestalMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.focusOnTemplateMapper = focusOnTemplateMapper;
        this.mqttClient = mqttClient;
        this.mqttConfiguration = mqttConfiguration;
        this.userPedestalMapMapper = userPedestalMapMapper;
        this.pedestalMapper = pedestalMapper;
    }


    @Override
    public Result addTemplate(AddFocusOnTemplateDTO focusOnTemplateDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        FocusOnTemplate focusOnTemplate = new FocusOnTemplate(focusOnTemplateDTO);
        focusOnTemplate.setUserId(userId);

        if (save(focusOnTemplate)) {
            stringRedisTemplate.opsForZSet().add(RedisConstants.USER_FOCUS_TEMPLATE + userId, String.valueOf(focusOnTemplate.getId()), Integer.valueOf(focusOnTemplate.getFocusStartTime().replace(":", "")));
        }
        Result<List<FocusOnTemplate>> templates = getTemplates(userId);
        byte[] jsonBytes = JSON.toJSONBytes(templates);
        List<UserPedestalMap> userPedestalMaps = userPedestalMapMapper.selectList(
                new LambdaQueryWrapper<UserPedestalMap>()
                        .eq(UserPedestalMap::getUserId, userId));
        List<Pedestal> pedestals = pedestalMapper.selectBatchIds(userPedestalMaps.stream().map(UserPedestalMap::getPedestalId).collect(Collectors.toList()));
        pedestals.stream().forEach(pedestal -> {
            try {
                mqttClient.publish(mqttConfiguration.getSendTemplateTopic() + pedestal.getEquipment(), jsonBytes, 2, true);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        });
        return Result.success();
    }

    @Override
    public Result deleteTemplate(IntegerVo templateId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();

        if (focusOnTemplateMapper.logicDelete(templateId.getNumber(), userId) == 1) {
            stringRedisTemplate.opsForZSet().remove(RedisConstants.USER_FOCUS_TEMPLATE + userId, String.valueOf(templateId.getNumber()));
            return Result.success();
        }
        return Result.fail(ResultStatusEnum.TEMPLATE_NOT_EXIST);
    }

    @Override
    public Result<List<FocusOnTemplate>> getTemplates(Integer userId) {
        List<Integer> ids = stringRedisTemplate.opsForZSet().range(RedisConstants.USER_FOCUS_TEMPLATE + userId, 0, -1)
                .stream().map(Integer::parseInt).collect(Collectors.toList());
        return Result.success(ResultStatusEnum.SUCCESS, focusOnTemplateMapper.selectBatchIds(ids));
    }

    @Override
    public Result updateTemplate(UpdateFocusOnTemplateDTO updateFocusOnTemplateDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        LambdaQueryWrapper<FocusOnTemplate> focusOnTemplateLambdaQueryWrapper
                = new LambdaQueryWrapper<>();
        focusOnTemplateLambdaQueryWrapper.eq(FocusOnTemplate::getId, updateFocusOnTemplateDTO.getId())
                .eq(FocusOnTemplate::getUserId, userId);
        FocusOnTemplate focusOnTemplate = new FocusOnTemplate(updateFocusOnTemplateDTO);
        if (focusOnTemplateMapper.update(focusOnTemplate, focusOnTemplateLambdaQueryWrapper) == 1) {
            return Result.success();
        }
        return Result.fail(ResultStatusEnum.TEMPLATE_NOT_EXIST);
    }
}
