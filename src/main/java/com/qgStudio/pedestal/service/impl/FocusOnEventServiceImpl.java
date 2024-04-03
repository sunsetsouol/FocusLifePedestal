package com.qgStudio.pedestal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.dto.AddFocusOnEventDTO;
import com.qgStudio.pedestal.entity.po.FocusOnEvent;
import com.qgStudio.pedestal.entity.po.FocusOnTemplate;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;
import com.qgStudio.pedestal.mapper.FocusOnEventMapper;
import com.qgStudio.pedestal.mapper.FocusOnTemplateMapper;
import com.qgStudio.pedestal.mapper.UserMapper;
import com.qgStudio.pedestal.service.IFocusOnEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class FocusOnEventServiceImpl extends ServiceImpl<FocusOnEventMapper, FocusOnEvent> implements IFocusOnEventService {

    private final FocusOnEventMapper focusOnEventMapper;
    private final FocusOnTemplateMapper focusOnTemplateMapper;
    private final UserMapper userMapper;

    @Autowired
    public FocusOnEventServiceImpl(FocusOnEventMapper focusOnEventMapper, FocusOnTemplateMapper focusOnTemplateMapper, UserMapper userMapper) {
        this.focusOnEventMapper = focusOnEventMapper;
        this.focusOnTemplateMapper = focusOnTemplateMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public Result addEvent(Integer userId, AddFocusOnEventDTO addFocusOnEventDTO) {

        if (focusOnTemplateMapper.increase(addFocusOnEventDTO.getFocusId(), addFocusOnEventDTO.getFocusTime(), userId) == 1) {
            userMapper.increaseFocusTime(userId, addFocusOnEventDTO.getFocusTime());
            FocusOnEvent focusOnEvent = new FocusOnEvent(addFocusOnEventDTO);
            focusOnEventMapper.insert(focusOnEvent);
            return Result.success();
        }
        return Result.fail(ResultStatusEnum.NOT_AUTHORIZATION);
    }

    @Override
    public Result<List<FocusOnEvent>> getEvents(Integer templateId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        FocusOnTemplate focusOnTemplate = focusOnTemplateMapper.selectOne(new LambdaQueryWrapper<FocusOnTemplate>()
                .eq(FocusOnTemplate::getId, templateId)
                .eq(FocusOnTemplate::getDeleted, 0)
                .eq(FocusOnTemplate::getUserId, userId));
        if (focusOnTemplate != null) {
            List<FocusOnEvent> focusOnEvents = focusOnEventMapper.selectList(new LambdaQueryWrapper<FocusOnEvent>()
                    .eq(FocusOnEvent::getFocusId, templateId));
            return Result.success(ResultStatusEnum.SUCCESS, focusOnEvents);
        }
        return Result.fail(ResultStatusEnum.NOT_AUTHORIZATION);
    }

    @Override
    public Result<Integer> getOneDayFocusTime(Integer id, LocalDate date) {
        List<FocusOnTemplate> focusOnTemplates = focusOnTemplateMapper.selectList(new LambdaQueryWrapper<FocusOnTemplate>()
                .eq(FocusOnTemplate::getUserId, id));

        List<FocusOnEvent> focusOnEvents = focusOnEventMapper.selectList(new LambdaQueryWrapper<FocusOnEvent>()
                .in(FocusOnEvent::getFocusId, focusOnTemplates.stream().mapToInt(FocusOnTemplate::getId).toArray())
                .between(FocusOnEvent::getRealStartTime, date.atStartOfDay(),date.plusDays(1).atStartOfDay()));
        int sum = focusOnEvents.stream().mapToInt(FocusOnEvent::getFocusTime).sum();
        return Result.success(ResultStatusEnum.SUCCESS, sum);
    }

    @Override
    public Result<List<FocusOnTemplate>> getHistoryFocusTimeDetails(Integer userId) {
        List<FocusOnTemplate> focusOnTemplates = focusOnTemplateMapper.selectList(new LambdaQueryWrapper<FocusOnTemplate>()
                .eq(FocusOnTemplate::getUserId, userId)
                .select(FocusOnTemplate::getMissionName,FocusOnTemplate::getCompletionTotalTime, FocusOnTemplate::getCompletion));

        return Result.success(ResultStatusEnum.SUCCESS, focusOnTemplates);
    }

    @Override
    public Result<List<FocusOnTemplate>> getYearFocusTimeDetails(Integer userId) {
        List<FocusOnTemplate> focusOnTemplates = focusOnTemplateMapper.selectList(new LambdaQueryWrapper<FocusOnTemplate>()
                .eq(FocusOnTemplate::getUserId, userId)
                .select(FocusOnTemplate::getMissionName,FocusOnTemplate::getCompletionTotalTime, FocusOnTemplate::getCompletion));
        focusOnTemplates.forEach(focusOnTemplate -> {
            focusOnTemplate.setCompletionTotalTime(focusOnEventMapper.selectList(new LambdaQueryWrapper<FocusOnEvent>()
                    .eq(FocusOnEvent::getFocusId, focusOnTemplate.getId())
                    .between(FocusOnEvent::getRealStartTime, LocalDateTime.now().minusYears(1), LocalDateTime.now()))
                    .stream().mapToInt(FocusOnEvent::getFocusTime).sum());
        });
        return Result.success(ResultStatusEnum.SUCCESS, focusOnTemplates);
    }

    @Override
    public Result<List<FocusOnTemplate>> getMonthFocusTimeDetails(Integer userId) {
        List<FocusOnTemplate> focusOnTemplates = focusOnTemplateMapper.selectList(new LambdaQueryWrapper<FocusOnTemplate>()
                .eq(FocusOnTemplate::getUserId, userId)
                .select(FocusOnTemplate::getMissionName,FocusOnTemplate::getCompletionTotalTime, FocusOnTemplate::getCompletion));
        focusOnTemplates.forEach(focusOnTemplate -> {
            focusOnTemplate.setCompletionTotalTime(focusOnEventMapper.selectList(new LambdaQueryWrapper<FocusOnEvent>()
                            .eq(FocusOnEvent::getFocusId, focusOnTemplate.getId())
                            .between(FocusOnEvent::getRealStartTime, LocalDateTime.now().minusMonths(1), LocalDateTime.now()))
                    .stream().mapToInt(FocusOnEvent::getFocusTime).sum());
        });
        return Result.success(ResultStatusEnum.SUCCESS, focusOnTemplates);
    }
}
