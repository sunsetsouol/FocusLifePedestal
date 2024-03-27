package com.qgStudio.pedestal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qgStudio.pedestal.entity.po.FocusOnEvent;
import com.qgStudio.pedestal.entity.vo.Result;

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

    Result addEvent(Integer userId, FocusOnEvent focusOnEvent);

    Result<List<FocusOnEvent>> getEvents(Integer templateId);
}
