package com.qgStudio.pedestal.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/20
 */
@Component
@Slf4j
public class RedisListener extends KeyExpirationEventMessageListener {
    public RedisListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("message"+new String(message.getBody()));
        System.out.println("message channel"+new String(message.getChannel()));
        System.out.println("pattern"+new String(pattern));
    }
}
