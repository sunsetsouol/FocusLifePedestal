package com.qgStudio.pedestal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qgStudio.pedestal.constant.RedisConstants;
import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.dto.AddFocusOnTemplateDTO;
import com.qgStudio.pedestal.entity.dto.UpdateFocusOnTemplateDTO;
import com.qgStudio.pedestal.entity.po.FocusOnTemplate;
import com.qgStudio.pedestal.entity.vo.IntegerVo;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;
import com.qgStudio.pedestal.mapper.FocusOnTemplateMapper;
import com.qgStudio.pedestal.service.IFocusOnTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
@Service
public class FocusOnTemplateServiceImpl extends ServiceImpl<FocusOnTemplateMapper, FocusOnTemplate> implements IFocusOnTemplateService {
    private final StringRedisTemplate stringRedisTemplate;
    private final FocusOnTemplateMapper focusOnTemplateMapper;

    @Autowired
    public FocusOnTemplateServiceImpl(StringRedisTemplate stringRedisTemplate, FocusOnTemplateMapper focusOnTemplateMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.focusOnTemplateMapper = focusOnTemplateMapper;
    }


    @Override
    public Result addTemplate(AddFocusOnTemplateDTO focusOnTemplateDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        FocusOnTemplate focusOnTemplate = new FocusOnTemplate(focusOnTemplateDTO);
        focusOnTemplate.setUserId(userId);

        if (save(focusOnTemplate)) {
            stringRedisTemplate.opsForZSet().add(RedisConstants.USER_FOCUS_TEMPLATE + userId, String.valueOf(focusOnTemplate.getId()), Integer.valueOf(focusOnTemplate.getFocusStartTime().replace(":","")));
        }
        return Result.success();
    }

    @Override
    public Result deleteTemplate(IntegerVo templateId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();

        LambdaQueryWrapper<FocusOnTemplate> focusOnTemplateLambdaQueryWrapper
                = new LambdaQueryWrapper<>();
        focusOnTemplateLambdaQueryWrapper.eq(FocusOnTemplate::getId, templateId.getNumber())
                .eq(FocusOnTemplate::getUserId, userId);
        if (focusOnTemplateMapper.delete(focusOnTemplateLambdaQueryWrapper) == 1) {
            stringRedisTemplate.opsForZSet().remove(RedisConstants.USER_FOCUS_TEMPLATE + userId, String.valueOf(templateId.getNumber()));
            return Result.success();
        }
        return Result.fail(ResultStatusEnum.TEMPLATE_NOT_EXIST);
    }

    @Override
    public Result<List<FocusOnTemplate>> getTemplates(Integer userId) {
        List<Integer> ids = stringRedisTemplate.opsForZSet().range(RedisConstants.USER_FOCUS_TEMPLATE + userId, 0, -1)
                .stream().map(Integer::parseInt).collect(Collectors.toList());
        return Result.success(ResultStatusEnum.SUCCESS,focusOnTemplateMapper.selectBatchIds(ids));
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
        if (focusOnTemplateMapper.update(focusOnTemplate,focusOnTemplateLambdaQueryWrapper)==1) {
            return Result.success();
        }
        return Result.fail(ResultStatusEnum.TEMPLATE_NOT_EXIST);
    }
}
