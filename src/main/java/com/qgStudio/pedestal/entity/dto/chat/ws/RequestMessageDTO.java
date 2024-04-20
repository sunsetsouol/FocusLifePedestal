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
public class RequestMessageDTO {

    private String type;

    private MessageGetDTO messageGetDTO;

    private MessageSendDTO messageSendDTO;
    @AllArgsConstructor
    @Getter
    public enum RequestType{
        GET_MESSAGE("getMessage"),
        SEND_MESSAGE("sendMessage")
        ;

        private final String type;
    }
}
