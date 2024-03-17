package com.qgStudio.pedestal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.po.FocusOnEvent;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;
import com.qgStudio.pedestal.mapper.FocusOnEventMapper;
import com.qgStudio.pedestal.mapper.FocusOnTemplateMapper;
import com.qgStudio.pedestal.service.IFocusOnEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
@Service
public class FocusOnEventServiceImpl extends ServiceImpl<FocusOnEventMapper, FocusOnEvent> implements IFocusOnEventService {

    private final FocusOnEventMapper focusOnEventMapper;
    private final FocusOnTemplateMapper focusOnTemplateMapper;
    @Autowired
    public FocusOnEventServiceImpl(FocusOnEventMapper focusOnEventMapper, FocusOnTemplateMapper focusOnTemplateMapper) {
        this.focusOnEventMapper = focusOnEventMapper;
        this.focusOnTemplateMapper = focusOnTemplateMapper;
    }

    @Override
    @Transactional
    public Result addEvent(FocusOnEvent focusOnEvent) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();

        if (focusOnTemplateMapper.increase(focusOnEvent.getFocusId(),userId) == 1) {
            focusOnEventMapper.insert(focusOnEvent);
            return Result.success();
        }
        return Result.fail(ResultStatusEnum.NOT_AUTHORIZATION);
    }
}
