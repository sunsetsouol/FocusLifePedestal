package com.qgStudio.pedestal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qgStudio.pedestal.constant.RedisConstants;
import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
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
    public Result addTemplate(FocusOnTemplate focusOnTemplate) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
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
            stringRedisTemplate.opsForZSet().remove(RedisConstants.USER_FOCUS_TEMPLATE + userId, String.valueOf(templateId));
            return Result.success();
        }
        return Result.fail(ResultStatusEnum.TEMPLATE_NOT_EXIST);
    }
}
