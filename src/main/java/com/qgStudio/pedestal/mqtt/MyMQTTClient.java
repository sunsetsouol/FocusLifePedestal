package com.qgStudio.pedestal.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/18
 */
@Slf4j
@Component
public class MyMQTTClient {
    @Autowired
    private MyMQTTCallback myMQTTCallback;
    @Autowired
    private MqttClient client;
    @Autowired
    private MqttConfiguration mqttConfiguration;


    /**
     * 设置mqtt连接参数
     *
     * @param username
     * @param password
     * @param timeout
     * @param keepalive
     * @return
     */
    public MqttConnectOptions setMqttConnectOptions(String username, String password, int timeout, int keepalive) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setConnectionTimeout(timeout);
        options.setKeepAliveInterval(keepalive);
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        return options;
    }

    /**
     * 连接mqtt服务端，得到MqttClient连接对象
     */
    @PostConstruct
    public void connect() throws MqttException {
        client.setCallback(myMQTTCallback);
        MqttConnectOptions mqttConnectOptions = setMqttConnectOptions(mqttConfiguration.username, mqttConfiguration.password, mqttConfiguration.timeOut, mqttConfiguration.keepAlive);
        if (!client.isConnected()) {
            client.connect(mqttConnectOptions);
        } else {
            client.disconnect();
            client.connect(mqttConnectOptions);
        }
        client.subscribe(mqttConfiguration.focusEventTopic, 2);
        client.subscribe(mqttConfiguration.addWaterTopic, 2);
        client.subscribe(mqttConfiguration.getWaterTopic, 2);
        client.subscribe(mqttConfiguration.getFocusOnTemplate, 2);
        client.subscribe(mqttConfiguration.getSpaceMembersTopic, 2);
        log.info("MQTT connect success");//未发生异常，则连接成功
    }

    /**
     * 发布，默认qos为0，非持久化
     *
     * @param pushMessage
     * @param topic
     */
    public void publish(String pushMessage, String topic) {
        publish(pushMessage, topic, 0, false);
    }

    /**
     * 发布消息
     *
     * @param pushMessage
     * @param topic
     * @param qos
     * @param retained:留存
     */
    public void publish(String pushMessage, String topic, int qos, boolean retained) {
        MqttMessage message = new MqttMessage();
        message.setPayload(pushMessage.getBytes());
        message.setQos(qos);
        message.setRetained(retained);
        MqttTopic mqttTopic = client.getTopic(topic);
        if (null == mqttTopic) {
            log.error("topic is not exist");
        }
        MqttDeliveryToken token;//Delivery:配送
        synchronized (this) {//注意：这里一定要同步，否则，在多线程publish的情况下，线程会发生死锁，分析见文章最后补充
            try {
                token = mqttTopic.publish(message);//也是发送到执行队列中，等待执行线程执行，将消息发送到消息中间件
                token.waitForCompletion(1000L);
            } catch (MqttPersistenceException e) {
                e.printStackTrace();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 订阅某个主题
     *
     * @param topic
     * @param qos
     */
    public void subscribe(String topic, int qos) {
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    /**
     * 取消订阅主题
     *
     * @param topic 主题名称
     */
    public void cleanTopic(String topic) {
        if (client != null && client.isConnected()) {
            try {
                client.unsubscribe(topic);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("取消订阅失败！");
        }
    }
}
