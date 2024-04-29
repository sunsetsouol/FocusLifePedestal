package com.qgStudio.pedestal.handler.websocket;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qgStudio.pedestal.entity.po.UserSpace;
import com.qgStudio.pedestal.event.SpaceEvent;
import com.qgStudio.pedestal.mapper.UserSpaceMapper;
import com.qgStudio.pedestal.service.space.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.concurrent.ConcurrentHashMap;

import static com.qgStudio.pedestal.handler.websocket.ChatWebsocketHandler.getUserId;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/21
 */
@Component
@RequiredArgsConstructor
public class SpaceWebsocketHandler implements WebSocketHandler {
    private final UserSpaceMapper userSpaceMapper;
    private final SpaceService spaceService;
    private static final ConcurrentHashMap<Integer, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        Integer userId = getUserId(webSocketSession);
        sessionMap.put(userId, webSocketSession);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        Integer userId = getUserId(webSocketSession);
        UserSpace userSpace = userSpaceMapper.selectOne(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getUserId, userId));
        String jsonString = JSON.toJSONString(spaceService.getSpaceMembers(userSpace.getSpaceId()));
        webSocketSession.sendMessage(new TextMessage(jsonString));
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

    @EventListener
    public void handleSpaceEvent(SpaceEvent spaceEvent) {
        Long spaceId = spaceEvent.getSpaceId();
        String jsonString = JSON.toJSONString(spaceService.getSpaceMembers(spaceId));
        try {
            userSpaceMapper.selectList(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getSpaceId, spaceId).select(UserSpace::getUserId)).forEach(userSpace -> {
                Integer userId = userSpace.getUserId();
                WebSocketSession webSocketSession = sessionMap.get(userId);
                if (webSocketSession != null) {
                    try {
                        webSocketSession.sendMessage(new TextMessage(jsonString));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
