package com.qgStudio.pedestal;

import com.qgStudio.pedestal.mapper.FocusOnTemplateMapper;
import com.qgStudio.pedestal.mapper.UserMapper;
import com.qgStudio.pedestal.mapper.UserSpaceMapper;
import com.qgStudio.pedestal.mapper.WaterIntakeMapper;
import com.qgStudio.pedestal.repository.relation.GroupRelationRepository;
import com.qgStudio.pedestal.service.IWaterIntakeService;
import com.qgStudio.pedestal.utils.EmailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    private GroupRelationRepository groupRelationRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserSpaceMapper userSpaceMapper;
    @Test
    public void contextLoads() {
        System.out.println(userSpaceMapper.selectSpaceUsers(8L));
//        System.out.println(groupRelationRepository.validateRelation(3, 3));
//        System.out.println(new BCryptPasswordEncoder().encode("test"));
//        FocusOnTemplate focusOnTemplate = focusOnTemplateMapper.selectById(8);
//        focusOnTemplate.setFocusDuration(null);
//        System.out.println(focusOnTemplateMapper.updateById(focusOnTemplate));
    }
}
