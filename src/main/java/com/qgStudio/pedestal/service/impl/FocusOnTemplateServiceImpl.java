package com.qgStudio.pedestal.service.impl;

import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.po.FocusOnTemplate;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.mapper.FocusOnTemplateMapper;
import com.qgStudio.pedestal.service.IFocusOnTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgStudio.pedestal.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final RedisCache redisCache;

    @Autowired
    public FocusOnTemplateServiceImpl(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public Result addTemplate(FocusOnTemplate focusOnTemplate) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        focusOnTemplate.setUserId(userId);
        if (save(focusOnTemplate)) {

        }

        return Result.success();
    }
}
