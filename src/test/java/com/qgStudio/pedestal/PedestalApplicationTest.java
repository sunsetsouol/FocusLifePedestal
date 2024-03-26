package com.qgStudio.pedestal;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qgStudio.pedestal.constant.RedisConstants;
import com.qgStudio.pedestal.entity.po.FocusOnTemplate;
import com.qgStudio.pedestal.entity.po.User;
import com.qgStudio.pedestal.entity.vo.IntegerVo;
import com.qgStudio.pedestal.mapper.FocusOnTemplateMapper;
import com.qgStudio.pedestal.mapper.UserMapper;
import com.qgStudio.pedestal.mapper.WaterIntakeMapper;
import com.qgStudio.pedestal.mqtt.MqttMsg;
import com.qgStudio.pedestal.service.IWaterIntakeService;
import com.qgStudio.pedestal.utils.EmailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/16
 */
@SpringBootTest
public class PedestalApplicationTest {
    @Autowired
    JavaMailSenderImpl mailSender;

    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private IWaterIntakeService waterIntakeService;
    @Autowired
    private WaterIntakeMapper waterIntakeMapper;
    @Autowired
    private FocusOnTemplateMapper focusOnTemplateMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Test
    public void contextLoads() {
//        FocusOnTemplate focusOnTemplate = focusOnTemplateMapper.selectById(8);
//        focusOnTemplate.setFocusDuration(null);
//        System.out.println(focusOnTemplateMapper.updateById(focusOnTemplate));
    }
}
