package com.qgStudio.pedestal.event;

import com.qgStudio.pedestal.entity.po.PrivateChatHistory;
import org.springframework.context.ApplicationEvent;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/20
 */
public class PrivateChatEvent extends ApplicationEvent {
    private Integer fromId;
    private Integer toId;
    private PrivateChatHistory privateChatHistory;
    public PrivateChatEvent(Object source, Integer fromId, Integer toId, PrivateChatHistory privateChatHistory) {
        super(source);
        this.fromId = fromId;
        this.toId = toId;
        this.privateChatHistory = privateChatHistory;
    }
    public Integer getFromId() {
        return fromId;
    }
    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }
    public Integer getToId() {
        return toId;
    }
    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public PrivateChatHistory getPrivateChatHistory() {
        return privateChatHistory;
    }

    public void setPrivateChatHistory(PrivateChatHistory privateChatHistory) {
        this.privateChatHistory = privateChatHistory;
    }
}
