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
import com.qgStudio.pedestal.entity.dto.IntegerDTO;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;
import com.qgStudio.pedestal.event.TemplateEvent;
import com.qgStudio.pedestal.mapper.FocusOnTemplateMapper;
import com.qgStudio.pedestal.mapper.PedestalMapper;
import com.qgStudio.pedestal.mapper.UserPedestalMapMapper;
import com.qgStudio.pedestal.mqtt.MqttConfiguration;
import com.qgStudio.pedestal.service.IFocusOnTemplateService;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
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
@RequiredArgsConstructor
public class FocusOnTemplateServiceImpl extends ServiceImpl<FocusOnTemplateMapper, FocusOnTemplate> implements IFocusOnTemplateService {
    private final StringRedisTemplate stringRedisTemplate;
    private final FocusOnTemplateMapper focusOnTemplateMapper;
    private final MqttClient mqttClient;
    private final MqttConfiguration mqttConfiguration;
    private final UserPedestalMapMapper userPedestalMapMapper;
    private final PedestalMapper pedestalMapper;
    private final ApplicationContext applicationContext;



    @Override
    public Result addTemplate(Integer userId, AddFocusOnTemplateDTO focusOnTemplateDTO) {

        FocusOnTemplate focusOnTemplate = new FocusOnTemplate(focusOnTemplateDTO);
        focusOnTemplate.setUserId(userId);

        if (save(focusOnTemplate)) {
            stringRedisTemplate.opsForZSet().add(RedisConstants.USER_FOCUS_TEMPLATE + userId, String.valueOf(focusOnTemplate.getId()), focusOnTemplate.getId());
        }
        applicationContext.publishEvent(new TemplateEvent(this, userId));

        return Result.success();
    }

    @Override
    public Result deleteTemplate(IntegerDTO templateId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();

        if (focusOnTemplateMapper.logicDelete(templateId.getNumber(), userId) == 1) {
            stringRedisTemplate.opsForZSet().remove(RedisConstants.USER_FOCUS_TEMPLATE + userId, String.valueOf(templateId.getNumber()));
            applicationContext.publishEvent(new TemplateEvent(this, userId));
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
            applicationContext.publishEvent(new TemplateEvent(this, userId));
            return Result.success();
        }
        return Result.fail(ResultStatusEnum.TEMPLATE_NOT_EXIST);
    }

    @EventListener
    public void templateEvent(TemplateEvent templateEvent) {
        Integer userId = templateEvent.getUserId();
        Result<List<FocusOnTemplate>> templates = getTemplates(userId);
        byte[] jsonBytes = JSON.toJSONBytes(templates);
        List<UserPedestalMap> userPedestalMaps = userPedestalMapMapper.selectList(
                new LambdaQueryWrapper<UserPedestalMap>()
                        .eq(UserPedestalMap::getUserId, userId));
        if (!userPedestalMaps.isEmpty()) {
            List<Pedestal> pedestals = pedestalMapper.selectBatchIds(userPedestalMaps.stream().map(UserPedestalMap::getPedestalId).collect(Collectors.toList()));
            pedestals.stream().forEach(pedestal -> {
                try {
                    mqttClient.publish(mqttConfiguration.getSendTemplateTopic() + pedestal.getEquipment(), jsonBytes, 2, true);
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
