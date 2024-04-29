package com.qgStudio.pedestal.event;

import com.qgStudio.pedestal.entity.po.GroupChatHistory;
import org.springframework.context.ApplicationEvent;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/20
 */
public class GroupChatEvent extends ApplicationEvent {
    private Integer groupId;
    private GroupChatHistory chatHistory;
    public GroupChatEvent(Object source, Integer groupId, GroupChatHistory chatHistory) {
        super(source);
        this.groupId = groupId;
        this.chatHistory = chatHistory;
    }
    public Integer getGroupId() {
        return groupId;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public GroupChatHistory getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(GroupChatHistory chatHistory) {
        this.chatHistory = chatHistory;
    }
}
