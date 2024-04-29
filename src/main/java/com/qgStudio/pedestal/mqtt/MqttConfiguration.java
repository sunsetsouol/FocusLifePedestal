package com.qgStudio.pedestal.mqtt;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/18
 */
@Configuration
@Slf4j
@Data
@Getter
public class MqttConfiguration {

    @Value("${mqtt.host}")
    String host;
    @Value("${mqtt.username}")
    String username;
    @Value("${mqtt.password}")
    String password;
    @Value("${mqtt.clientId}")
    String clientId;
    @Value("${mqtt.timeout}")
    int timeOut;
    @Value("${mqtt.keepalive}")
    int keepAlive;
    @Value("${mqtt.focusEventTopic}")
    String focusEventTopic;
    @Value("${mqtt.addWaterTopic}")
    String addWaterTopic;
    @Value("${mqtt.getFocusOnTemplate}")
    String getFocusOnTemplate;
    @Value("${mqtt.getWaterTopic}")
    String getWaterTopic;
    @Value("${mqtt.sendTemplateTopic}")
    String sendTemplateTopic;
    @Value("${mqtt.sendWaterTopic}")
    String sendWaterTopic;
    @Value("${mqtt.weatherTopic}")
    String weatherTopic;
    @Value("${mqtt.timeTopic}")
    String timeTopic;
    @Value("${mqtt.getSpaceMembersTopic}")
    String getSpaceMembersTopic;
    @Value("${mqtt.sendSpaceMembersTopic}")
    String sendSpaceMembersTopic;

    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient mqttClient = new MqttClient(host, clientId, new MemoryPersistence());
        return mqttClient;
    }



//    public String getTopic3() {
//        return topic3;
//    }
//
//    public void setTopic3(String topic3) {
//        this.topic3 = topic3;
//    }
//
//    public String getTopic4() {
//        return topic4;
//    }
//
//    public void setTopic4(String topic4) {
//        this.topic4 = topic4;
//    }
}
