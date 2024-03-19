package com.qgStudio.pedestal.mqtt;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qgStudio.pedestal.entity.po.FocusOnEvent;
import com.qgStudio.pedestal.entity.po.FocusOnTemplate;
import com.qgStudio.pedestal.entity.po.Pedestal;
import com.qgStudio.pedestal.entity.po.UserPedestalMap;
import com.qgStudio.pedestal.entity.po.WaterIntake;
import com.qgStudio.pedestal.entity.vo.IntegerVo;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.mapper.PedestalMapper;
import com.qgStudio.pedestal.mapper.UserPedestalMapMapper;
import com.qgStudio.pedestal.service.IFocusOnEventService;
import com.qgStudio.pedestal.service.IFocusOnTemplateService;
import com.qgStudio.pedestal.service.IWaterIntakeService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/18
 */
@Slf4j
@Component
public class MyMQTTCallback implements MqttCallbackExtended {

    @Autowired
    private MqttClient mqttClient;
    //手动注入
    @Autowired
    private MqttConfiguration mqttConfiguration;

    @Autowired
    private IWaterIntakeService waterIntakeService;
    @Autowired
    private IFocusOnEventService focusOnEventService;
    @Autowired
    private IFocusOnTemplateService focusOnTemplateService;
    @Autowired
    private PedestalMapper pedestalMapper;
    @Autowired
    private UserPedestalMapMapper userPedestalMapMapper;



    /**
     * 丢失连接，可在这里做重连
     * 只会调用一次
     *
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.error("mqtt connectionLost 连接断开，5S之后尝试重连: {}", throwable.getMessage());
        long reconnectTimes = 1;
        while (true) {
            try {
                if (mqttClient.isConnected()) {
                    //判断已经重新连接成功  需要重新订阅主题 可以在这个if里面订阅主题  或者 connectComplete（方法里面）  看你们自己选择
                    log.warn("mqtt reconnect success end  重新连接  重新订阅成功");
                    return;
                }
                reconnectTimes+=1;
                log.warn("mqtt reconnect times = {} try again...  mqtt重新连接时间 {}", reconnectTimes, reconnectTimes);
                mqttClient.reconnect();
            } catch (MqttException e) {
                log.error("mqtt断连异常", e);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
            }
        }
    }

    /**
     * @param topic
     * @param mqttMessage
     * @throws Exception
     * subscribe后得到的消息会执行到这里面
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        log.info("接收消息主题 : {}，接收消息内容 : {}", topic, new String(mqttMessage.getPayload()));
        MqttMsg mqttMsg = JSON.parseObject(mqttMessage.getPayload(), MqttMsg.class);
        String equipmentNumber = mqttMsg.getEquipmentNumber();
        Pedestal pedestal = pedestalMapper.selectOne(new LambdaQueryWrapper<Pedestal>().eq(Pedestal::getEquipment, equipmentNumber));
        UserPedestalMap userPedestalMap = userPedestalMapMapper.selectOne(new LambdaQueryWrapper<UserPedestalMap>().eq(UserPedestalMap::getPedestalId, pedestal.getId()));

        try {
            //喝水
            if(topic.equals(mqttConfiguration.addWaterTopic)){
                IntegerVo integerVo = JSON.parseObject(mqttMsg.getData(), IntegerVo.class);
                waterIntakeService.addWaterIntake(userPedestalMap.getUserId(), integerVo.getNumber());
                Result<WaterIntake> waterIntake = waterIntakeService.getWaterIntake(userPedestalMap.getUserId(), LocalDate.now());
                mqttClient.publish(mqttConfiguration.sendWaterTopic + equipmentNumber, JSON.toJSONBytes(waterIntake), 2, false);
            } else if(topic.equals(mqttConfiguration.focusEventTopic)){
                //专注一次
                FocusOnEvent focusOnEvent = JSON.parseObject(mqttMsg.getData(), FocusOnEvent.class);
                focusOnEventService.addEvent(userPedestalMap.getUserId(), focusOnEvent);
                Result<List<FocusOnTemplate>> templates = focusOnTemplateService.getTemplates(userPedestalMap.getUserId());
                mqttClient.publish(mqttConfiguration.sendTemplateTopic + equipmentNumber, JSON.toJSONBytes(templates), 2, false);
            } else if(topic.equals(mqttConfiguration.getFocusOnTemplate)){
                //获取模板
                Result<List<FocusOnTemplate>> templates = focusOnTemplateService.getTemplates(userPedestalMap.getUserId());
                mqttClient.publish(mqttConfiguration.sendTemplateTopic + equipmentNumber, JSON.toJSONBytes(templates), 2, false);
            } else if(topic.equals(mqttConfiguration.getWaterTopic)){
                //获取今日喝水量
                Result<WaterIntake> waterIntake = waterIntakeService.getWaterIntake(userPedestalMap.getUserId(), LocalDate.now());
                mqttClient.publish(mqttConfiguration.sendWaterTopic + equipmentNumber, JSON.toJSONBytes(waterIntake), 2, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        //发布消息主题
//        if (topic.equals("embed/resp")){
//            Map maps = (Map) JSON.parse(new String(mqttMessage.getPayload(), StandardCharsets.UTF_8));
//            //你自己的业务接口
//
//            System.out.println("================================================");
//            System.out.println(maps);
//        }
//        //接收报警主题
//        if (topic.equals("embed/warn")){
//            Map maps = (Map) JSON.parse(new String(mqttMessage.getPayload(), StandardCharsets.UTF_8));
//            //你自己的业务接口
//            System.out.println("================================================");
//            System.out.println(maps);
//        }
    }


    /**
     *连接成功后的回调 可以在这个方法执行 订阅主题  生成Bean的 MqttConfiguration方法中订阅主题 出现bug
     *重新连接后  主题也需要再次订阅  将重新订阅主题放在连接成功后的回调 比较合理
     * @param reconnect
     * @param serverURI
     */
    @Override
    public  void  connectComplete(boolean reconnect,String serverURI){
        log.info("MQTT 连接成功，连接方式：{}",reconnect?"重连":"直连");
        //订阅主题
        try {
            mqttClient.subscribe(mqttConfiguration.focusEventTopic, 2);
            mqttClient.subscribe(mqttConfiguration.addWaterTopic, 2);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
//        myMQTTClient.subscribe(mqttConfiguration.topic3, 1);
//        myMQTTClient.subscribe(mqttConfiguration.topic4, 1);
    }

    /**
     * 消息到达后
     * subscribe后，执行的回调函数
     *
     * @param s
     * @param mqttMessage
     * @throws Exception
     */
    /**
     * publish后，配送完成后回调的方法
     *
     * @param iMqttDeliveryToken
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("==========deliveryComplete={}==========", iMqttDeliveryToken.isComplete());
    }
}
