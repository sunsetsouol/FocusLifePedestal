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
public class MessageSendDTO {

    private String type;

    private Integer toId;

    private String context;

    @AllArgsConstructor
    @Getter
    public enum MessageSendType {
        PRIVATE_TEXT("privateText"),
        GROUP_TEXT("groupText"),
        PRIVATE_SPACE("privateSpace"),
        GROUP_SPACE("groupSpace"),
//        GROUP_INVITE("groupInvite"),
        ;
        private final String type;
    }
}
