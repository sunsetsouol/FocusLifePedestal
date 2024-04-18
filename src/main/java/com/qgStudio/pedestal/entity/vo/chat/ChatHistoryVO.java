package com.qgStudio.pedestal.entity.vo.chat;

import com.qgStudio.pedestal.entity.po.GroupChatHistory;
import com.qgStudio.pedestal.entity.po.PrivateChatHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatHistoryVO {

    /**
     * 用户
     */
    private Integer userId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 时间
     */
    private LocalDateTime time;

    /**
     * 类型
     */
    private String type;

    public ChatHistoryVO(PrivateChatHistory privateChatHistory) {
        this.userId = privateChatHistory.getFromId();
        this.content = privateChatHistory.getContext();
        this.time = privateChatHistory.getCreateTime();
        this.type = privateChatHistory.getType();
    }

    public ChatHistoryVO(GroupChatHistory groupChatHistory) {
        this.userId = groupChatHistory.getFromId();
        this.content = groupChatHistory.getContext();
        this.time = groupChatHistory.getCreateTime();
        this.type = groupChatHistory.getType();
    }
}
