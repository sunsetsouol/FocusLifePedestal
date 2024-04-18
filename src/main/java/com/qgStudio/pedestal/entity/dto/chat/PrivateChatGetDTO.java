package com.qgStudio.pedestal.entity.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrivateChatGetDTO {

    /**
     * 私聊用户id
     */
    @NotNull(message = "用户id不能为空")
    @Min(value = 1, message = "用户id错误")
    private Integer userId;


    /**
     * 最早的消息id，如果从最后开始获取则设为null
     */
    private Long lastId;
}
