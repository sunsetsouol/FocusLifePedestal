package com.qgStudio.pedestal.mqtt;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/18
 */
@RestController
@RequestMapping
public class TestController {
    @Autowired
    private MyMQTTClient myMQTTClient;
//    Queue<String> msgQueue = new LinkedList<>();
    @Value("${mqtt.focusEventTopic}")
    String focusEventTopic;
    @Value("${mqtt.addWaterTopic}")
    String addWaterTopic;
//    int count = 1;
    @RequestMapping("/test")
    public String test() {
        MqttMsg mqttMsg = new MqttMsg();
//        mqttMsg.setName("name");
//        mqttMsg.setContent("hello");
//        mqttMsg.setTime("2024-03-18 15:00:00");
//        System.out.println("队列元素数量：" + msgQueue.size());
//        System.out.println("***************" + mqttMsg.getName() + ":" + mqttMsg.getContent() + "****************");

        //时间格式化
        mqttMsg.setEquipmentNumber("123456");
        mqttMsg.setData("hello");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date());
//        mqttMsg.setTime(time);

//        mqttMsg.setContent(mqttMsg.getContent() + "——后台编号：" + count);
//        count++;

        //map转json

        String sendMsg = JSON.toJSONString(mqttMsg);
        System.out.println(sendMsg);

        //队列添加元素
//        boolean flag = msgQueue.offer(sendMsg);
//        if (flag) {
//            //发布消息  addWaterTopic 是你要发送到那个通道里面的主题 比如我要发送到topic2主题消息
//            myMQTTClient.publish(msgQueue.poll(), addWaterTopic);
//            System.out.println("时间戳" + System.currentTimeMillis());
//        }
//        System.out.println("队列元素数量：" + msgQueue.size());
        myMQTTClient.publish(sendMsg, addWaterTopic,2,false);
        return "test";
    }
}
