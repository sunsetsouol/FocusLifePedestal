package com.qgStudio.pedestal.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/20
 */
public class TemplateEvent extends ApplicationEvent {
    private Integer userId;

    public TemplateEvent(Object source, Integer userId) {
        super(source);
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
