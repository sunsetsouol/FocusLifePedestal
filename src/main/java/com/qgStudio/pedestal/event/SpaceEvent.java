package com.qgStudio.pedestal.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/21
 */
public class SpaceEvent extends ApplicationEvent {
    private Long spaceId;
    public SpaceEvent(Object source, Long spaceId) {
        super(source);
        this.spaceId = spaceId;
    }

    public Long getSpaceId() {
        return spaceId;
    }
}
