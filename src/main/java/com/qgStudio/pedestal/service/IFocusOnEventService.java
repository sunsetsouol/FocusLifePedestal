package com.qgStudio.pedestal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qgStudio.pedestal.entity.bo.FocusEventBO;
import com.qgStudio.pedestal.entity.dto.AddFocusOnEventDTO;
import com.qgStudio.pedestal.entity.po.FocusOnEvent;
import com.qgStudio.pedestal.entity.po.FocusOnTemplate;
import com.qgStudio.pedestal.entity.vo.Result;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
public interface IFocusOnEventService extends IService<FocusOnEvent> {

    Result addEvent(Integer userId, AddFocusOnEventDTO focusOnEvent);

    Result<List<FocusOnEvent>> getEvents(Integer templateId);

    Result<Integer> getOneDayFocusTime(Integer id, LocalDate date);

    Result<List<FocusOnTemplate>> getHistoryFocusTimeDetails(Integer userId);

    Result<List<FocusOnTemplate>> getYearFocusTimeDetails(Integer userId);

    Result<List<FocusOnTemplate>> getMonthFocusTimeDetails(Integer userId);

    void dealPedestalFocusEvent(Integer userId, FocusEventBO focusEventBO);
}
