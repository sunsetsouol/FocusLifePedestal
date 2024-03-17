package com.qgStudio.pedestal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.po.User;
import com.qgStudio.pedestal.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    private final String ADMIN = "Admin";
    private final String USER = "User";
    private final String TOURIST = "Tourist";

    @Override
    // 平台的验证
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,username);
        User user = userMapper.selectOne(queryWrapper);
        //如果没有查询到用户就抛出异常，认证失败
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或者密码错误");
        }
        //权限认证
        List<String> list = new ArrayList<>(Arrays.asList(ADMIN,TOURIST));
        //把数据封装成UserDetails返回
        return new UserDetailsImpl(user,list);
    }
}
