package com.qgStudio.pedestal.entity.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupChatSendDTO {

    /**
     * 群id
     */
    private Integer groupId;

    /**
     * 发送内容
     */
    private String context;
}
