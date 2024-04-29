package com.qgStudio.pedestal.conf;

import com.qgStudio.pedestal.handler.websocket.ChatWebsocketHandler;
import com.qgStudio.pedestal.handler.websocket.SpaceWebsocketHandler;
import com.qgStudio.pedestal.interceptor.WebSocketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/20
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private ChatWebsocketHandler chatWebsocketHandler;
    @Autowired
    private SpaceWebsocketHandler spaceWebsocketHandler;
    @Bean
    HandshakeInterceptor websocketInterceptor() {
        return new WebSocketInterceptor();
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(chatWebsocketHandler, "/chat")
                .addInterceptors(websocketInterceptor())
                .setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(spaceWebsocketHandler, "/space")
                .addInterceptors(websocketInterceptor())
                .setAllowedOrigins("*");
    }
}
