package com.qgStudio.pedestal.entity.dto.chat.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/20
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageGetDTO {

    /**
     * 最早的消息id，如果从最后开始获取则设为null
     */
    private Long lastId;

    /**
     * 消息类型
     *  {@link MessageType}
     */
    private String type;

    /**
     * 群聊或私聊的目标id
     */
    private Integer targetId;

    @AllArgsConstructor
    @Getter
    public enum MessageType {
        PRIVATE("private"),
        GROUP("group")
        ;
        private final String type;
    }
}
