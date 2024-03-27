package com.qgStudio.pedestal.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qgStudio.pedestal.constant.RedisConstants;
import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.po.User;
import com.qgStudio.pedestal.entity.vo.IntegerVo;
import com.qgStudio.pedestal.entity.vo.LoginUserVo;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;
import com.qgStudio.pedestal.entity.vo.WaterReminderInfo;
import com.qgStudio.pedestal.mapper.UserMapper;
import com.qgStudio.pedestal.service.IUserService;
import com.qgStudio.pedestal.utils.JwtUtils;
import com.qgStudio.pedestal.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RedisCache redisCache;
    @Autowired
    public UserServiceImpl(UserMapper userMapper, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RedisCache redisCache) {
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.redisCache = redisCache;
    }

    @Override
    public Result login(LoginUserVo loginUserVo) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUserVo.getEmail(),loginUserVo.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (authenticate == null) {
            return Result.fail(ResultStatusEnum.LOGIN_FAIL);
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getUser().getId());
        String token = JwtUtils.createJwt(claims);
        redisCache.setCacheObject(RedisConstants.USER_LOGIN + userDetails.getUser().getId(), JSON.toJSONString(userDetails),30, TimeUnit.DAYS);
        return Result.success(ResultStatusEnum.LOGIN_SUCCESS, token);
    }

    @Override
    public Result register(LoginUserVo loginUserVo) {
        User user = User.builder()
                .email(loginUserVo.getEmail())
                .password(passwordEncoder.encode(loginUserVo.getPassword()))
                .build();
        try {
            userMapper.insert(user);
            return Result.success(ResultStatusEnum.REGISTER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(ResultStatusEnum.EMAIL_EXISTED);
        }
    }

    @Override
    public Result getCode(String email) {
        return null;
    }

    @Override
    public Result setIntake(IntegerVo intake) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        userMapper.setIntake(userId, intake.getNumber());
        return Result.success();
    }

    @Override
    public Result setReminderInterval(IntegerVo reminderInterval) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        userMapper.setReminderInterval(userId, reminderInterval.getNumber());
        return Result.success();
    }

    @Override
    public Result<WaterReminderInfo> getWaterReminderInfo(Integer userId) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, userId)
                .select(User::getDefaultReminderInterval, User::getDefaultWaterIntake));
        return Result.success(ResultStatusEnum.SUCCESS,new WaterReminderInfo(user.getDefaultWaterIntake(), user.getDefaultReminderInterval()));
    }
}
