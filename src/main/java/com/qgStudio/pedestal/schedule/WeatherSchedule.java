package com.qgStudio.pedestal.schedule;

import com.qgStudio.pedestal.mqtt.MqttConfiguration;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/24
 */
@Component
public class WeatherSchedule {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MqttClient mqttClient;
    @Autowired
    private MqttConfiguration mqttConfiguration;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Scheduled(initialDelay = 1000, fixedRate = 1000 * 60)
    public void getWeather() {
        try {
            String forObject = restTemplate.getForObject("https://restapi.amap.com/v3/weather/weatherInfo?" +
                    "city=440100&extensions=base&output=JSON&key=153065d65dd4149fa379f8414682642a", String.class);
            if (mqttClient.isConnected()){
                mqttClient.publish(mqttConfiguration.getWeatherTopic(),forObject.getBytes(),2,true);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(initialDelay = 1000, fixedRate = 1000 * 60)
    public void setTime() {
        try {
            if (mqttClient.isConnected()){
                mqttClient.publish(mqttConfiguration.getTimeTopic(), dateTimeFormatter.format(LocalDateTime.now()).getBytes(),2,true);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
