package com.qgStudio.pedestal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgStudio.pedestal.entity.bo.FocusEventBO;
import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.dto.AddFocusOnEventDTO;
import com.qgStudio.pedestal.entity.po.FocusOnEvent;
import com.qgStudio.pedestal.entity.po.FocusOnTemplate;
import com.qgStudio.pedestal.entity.po.UserSpace;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;
import com.qgStudio.pedestal.event.SpaceEvent;
import com.qgStudio.pedestal.event.TemplateEvent;
import com.qgStudio.pedestal.mapper.FocusOnEventMapper;
import com.qgStudio.pedestal.mapper.FocusOnTemplateMapper;
import com.qgStudio.pedestal.mapper.UserMapper;
import com.qgStudio.pedestal.mapper.UserSpaceMapper;
import com.qgStudio.pedestal.service.IFocusOnEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class FocusOnEventServiceImpl extends ServiceImpl<FocusOnEventMapper, FocusOnEvent> implements IFocusOnEventService {

    private final FocusOnEventMapper focusOnEventMapper;
    private final FocusOnTemplateMapper focusOnTemplateMapper;
    private final UserMapper userMapper;
    private final UserSpaceMapper userSpaceMapper;
    private final ApplicationContext applicationContext;

    @Override
    @Transactional
    public Result addEvent(Integer userId, AddFocusOnEventDTO addFocusOnEventDTO) {

        if (focusOnTemplateMapper.increase(addFocusOnEventDTO.getFocusId(), addFocusOnEventDTO.getFocusTime(), userId) == 1) {
            userMapper.increaseFocusTime(userId, addFocusOnEventDTO.getFocusTime());
            FocusOnEvent focusOnEvent = new FocusOnEvent(addFocusOnEventDTO);
            focusOnEventMapper.insert(focusOnEvent);
            applicationContext.publishEvent(new TemplateEvent(this, userId));
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

    private void startFocusEvent(Integer userId, Integer templateId) {
        List<Integer> templates = focusOnTemplateMapper.selectList(new LambdaQueryWrapper<FocusOnTemplate>().eq(FocusOnTemplate::getUserId, userId)).stream().map(FocusOnTemplate::getId).collect(Collectors.toList());

        List<String> status = new ArrayList<>();
        status.add(FocusOnEvent.StatusType.COMPLETED.getStatus());
        status.add(FocusOnEvent.StatusType.NOT_COMPLETED.getStatus());
        if (focusOnEventMapper.selectCount(new LambdaQueryWrapper<FocusOnEvent>().in(FocusOnEvent::getFocusId, templates.toArray()).notIn(FocusOnEvent::getIsCompleted,status))!=0) {
            // 有其他专注未结束
            return;
        }

        FocusOnEvent focusOnEvent = new FocusOnEvent();
        focusOnEvent.setFocusId(templateId);
        focusOnEvent.setRealStartTime(LocalDateTime.now());

        UserSpace userSpace = userSpaceMapper.selectOne(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getUserId, userId));
        if (userSpace != null){
            focusOnEvent.setSpaceId(userSpace.getSpaceId());
            FocusOnTemplate focusOnTemplate = focusOnTemplateMapper.selectById(templateId);
            userSpace.setTemplateName(focusOnTemplate.getMissionName());
            userSpace.setEventId(focusOnEvent.getId());
            userSpaceMapper.updateById(userSpace);
        }
        focusOnEventMapper.insert(focusOnEvent);

    }

    private void heartBeatFocusEvent(Integer userId, Integer templateId){
        // TODO：心跳停了需要处理
    }

    private void suspendFocusEvent(Integer templateId){
        focusOnEventMapper.suspend(templateId, FocusOnEvent.StatusType.IN_PROGRESS.getStatus(), FocusOnEvent.StatusType.PAUSE.getStatus());
    }

    private void continueFocusEvent(Integer templateId){
        FocusOnEvent focusOnEvent = new FocusOnEvent();
        focusOnEvent.setIsCompleted(FocusOnEvent.StatusType.IN_PROGRESS.getStatus());
        focusOnEventMapper.update(focusOnEvent, new LambdaQueryWrapper<FocusOnEvent>().eq(FocusOnEvent::getFocusId, templateId).eq(FocusOnEvent::getIsCompleted, FocusOnEvent.StatusType.PAUSE.getStatus()));
    }

    private void finishFocusEvent(Integer userId, Integer templateId, Integer focusTime){
        // TODO： 限制同时只能开启一个专注，不然会查出不止一个
        FocusOnEvent focusOnEvent = focusOnEventMapper.selectOne(new LambdaQueryWrapper<FocusOnEvent>()
                .eq(FocusOnEvent::getFocusId, templateId)
                .eq(FocusOnEvent::getIsCompleted, FocusOnEvent.StatusType.IN_PROGRESS.getStatus()));


        if (focusOnEvent != null) {
            focusOnEvent.setIsCompleted(FocusOnEvent.StatusType.COMPLETED.getStatus());
            focusOnEvent.setFocusTime(focusTime);
            focusOnEventMapper.updateById(focusOnEvent);

            UserSpace userSpace = userSpaceMapper.selectOne(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getUserId, userId));
            if (userSpace != null){
                userSpaceMapper.completeById(userSpace.getId(), focusTime);
            }
        }

    }
    private void cancelFocusEvent(Integer userId, Integer templateId){
        focusOnEventMapper.delete(new LambdaQueryWrapper<FocusOnEvent>().eq(FocusOnEvent::getFocusId, templateId).eq(FocusOnEvent::getIsCompleted, FocusOnEvent.StatusType.PAUSE.getStatus()));
        userSpaceMapper.cancel(userId);
    }
    @Override
    public void dealPedestalFocusEvent(Integer userId, FocusEventBO focusEventBO) {
        if (focusEventBO.getType().equals(FocusEventBO.FocusEventType.START.getValue())) {
            startFocusEvent(userId, focusEventBO.getTemplateId());
        } else if (focusEventBO.getType().equals(FocusEventBO.FocusEventType.PING.getValue())) {
            heartBeatFocusEvent(userId, focusEventBO.getTemplateId());
        } else if (focusEventBO.getType().equals(FocusEventBO.FocusEventType.SUSPEND.getValue())) {
            suspendFocusEvent(focusEventBO.getTemplateId());
        } else if (focusEventBO.getType().equals(FocusEventBO.FocusEventType.CONTINUE.getValue())) {
            continueFocusEvent(focusEventBO.getTemplateId());
        } else if (focusEventBO.getType().equals(FocusEventBO.FocusEventType.FINISH.getValue())) {
            finishFocusEvent(userId, focusEventBO.getTemplateId(), focusEventBO.getFocusTime());
        } else if (focusEventBO.getType().equals(FocusEventBO.FocusEventType.CANCEL.getValue())) {
            cancelFocusEvent(userId, focusEventBO.getTemplateId());
        }

        UserSpace userSpace = userSpaceMapper.selectOne(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getUserId, userId));
        if (userSpace != null) {
            applicationContext.publishEvent(new SpaceEvent(this, userSpace.getSpaceId()));
        }

    }
}
