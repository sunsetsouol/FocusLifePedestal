package com.qgStudio.pedestal.handler.websocket;

import com.alibaba.fastjson2.JSON;
import com.qgStudio.pedestal.constant.Constants;
import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.dto.chat.ws.MessageGetDTO;
import com.qgStudio.pedestal.entity.dto.chat.ws.MessageSendDTO;
import com.qgStudio.pedestal.entity.dto.chat.ws.RequestMessageDTO;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.chat.ChatHistoryVO;
import com.qgStudio.pedestal.exception.ServiceException;
import com.qgStudio.pedestal.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/20
 */
@Component
@RequiredArgsConstructor
public class ChatWebsocketHandler implements WebSocketHandler {
    private final ChatService chatService;

    private static final ConcurrentHashMap<Integer, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        Integer userId = getUserId(webSocketSession);
        sessionMap.put(userId, webSocketSession);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        try {
            String message = webSocketMessage.getPayload().toString();
            RequestMessageDTO requestMessageDTO = JSON.parseObject(message, RequestMessageDTO.class);
            Integer userId = getUserId(webSocketSession);
            if(requestMessageDTO.getType().equals(RequestMessageDTO.RequestType.GET_MESSAGE.getType())){
                MessageGetDTO messageGetDTO = requestMessageDTO.getMessageGetDTO();
                List<ChatHistoryVO> historyVOList = chatService.getMessage(messageGetDTO, userId);
                webSocketSession.sendMessage(new TextMessage(JSON.toJSONString(historyVOList)));
            }else if (requestMessageDTO.getType().equals(RequestMessageDTO.RequestType.SEND_MESSAGE.getType())){
                MessageSendDTO messageSendDTO = requestMessageDTO.getMessageSendDTO();
                Boolean aBoolean = chatService.sendMessage(messageSendDTO, userId);
                webSocketSession.sendMessage(new TextMessage(JSON.toJSONString(Result.success(aBoolean))));
            }
        } catch (ServiceException e) {
            webSocketSession.sendMessage(new TextMessage(JSON.toJSONString(Result.fail(e.getCode(), e.getMessage()))));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        sessionMap.remove(getUserId(webSocketSession));
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private static Integer getUserId(WebSocketSession webSocketSession) {
        return (Integer) webSocketSession.getAttributes().get(Constants.USER_ID);
    }
}
