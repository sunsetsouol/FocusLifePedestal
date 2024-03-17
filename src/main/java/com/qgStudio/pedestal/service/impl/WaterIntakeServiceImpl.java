package com.qgStudio.pedestal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qgStudio.pedestal.constant.RedisConstants;
import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.po.User;
import com.qgStudio.pedestal.entity.po.WaterIntake;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;
import com.qgStudio.pedestal.entity.vo.WaterIntakeGetRangeVo;
import com.qgStudio.pedestal.mapper.UserMapper;
import com.qgStudio.pedestal.mapper.WaterIntakeMapper;
import com.qgStudio.pedestal.service.IWaterIntakeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
@Service
public class WaterIntakeServiceImpl extends ServiceImpl<WaterIntakeMapper, WaterIntake> implements IWaterIntakeService {

    private final WaterIntakeMapper waterIntakeMapper;
    private final RedissonClient redissonClient;
    private final UserMapper userMapper;

    public WaterIntakeServiceImpl( WaterIntakeMapper waterIntakeMapper, RedissonClient redissonClient, UserMapper userMapper) {
        this.waterIntakeMapper = waterIntakeMapper;
        this.redissonClient = redissonClient;
        this.userMapper = userMapper;
    }

    @Override
    public Result addWaterIntake(Integer intakeWater) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        RLock lock = redissonClient.getLock(RedisConstants.WATER_INTAKE_LOCK + user.getId());
        if (lock.tryLock()) {
            try {
                LambdaQueryWrapper<WaterIntake> waterIntakeLambdaQueryWrapper
                        = new LambdaQueryWrapper<>();
                waterIntakeLambdaQueryWrapper.eq(WaterIntake::getUserId, user.getId())
                                .eq(WaterIntake::getIntakeDate, LocalDate.now());
                if (waterIntakeMapper.selectOne(waterIntakeLambdaQueryWrapper) == null) {
                    user = userMapper.selectById(user.getId());
                    WaterIntake waterIntake = new WaterIntake(user.getId(), LocalDate.now(),user.getDefaultWaterIntake(), intakeWater);
                    waterIntakeMapper.insert(waterIntake);
                } else {
                    waterIntakeMapper.intake(intakeWater,user.getId());
                }
            } finally {
                lock.unlock();
            }
        }
//        stringRedisTemplate.opsForList().leftPush(RedisConstants.WATER_INTAKE + LocalDate.now() + ":" + waterIntakeVo.getUserId(), String.valueOf(waterIntakeVo.getWater()));
        return Result.success();
    }

    @Override
    public Result<WaterIntake> getWaterIntake(LocalDate time) {
//        List<String> range = stringRedisTemplate.opsForList().range(RedisConstants.WATER_INTAKE + LocalDate.now() + ":" + userId, 0, -1);
//        Integer sum = range.stream().map(Integer::parseInt).reduce(0, Integer::sum);
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        LambdaQueryWrapper<WaterIntake> waterIntakeLambdaQueryWrapper
                = new LambdaQueryWrapper<>();
        waterIntakeLambdaQueryWrapper.eq(WaterIntake::getUserId, userId)
                .eq(WaterIntake::getIntakeDate, time)
                .select(WaterIntake::getIntakeReal, WaterIntake::getIntakeTarget, WaterIntake::getIntakeDate);
        return Result.success(ResultStatusEnum.SUCCESS, waterIntakeMapper.selectOne(waterIntakeLambdaQueryWrapper));
    }

    @Override
    public Result<List<WaterIntake>> getRangeWaterIntake(WaterIntakeGetRangeVo waterIntakeGetRangeVo) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        LambdaQueryWrapper<WaterIntake> waterIntakeLambdaQueryWrapper
                = new LambdaQueryWrapper<>();
        waterIntakeLambdaQueryWrapper.eq(WaterIntake::getUserId, userId)
                .between(WaterIntake::getIntakeDate, waterIntakeGetRangeVo.getStartTime(), waterIntakeGetRangeVo.getEndTime())
                .select(WaterIntake::getIntakeReal, WaterIntake::getIntakeTarget, WaterIntake::getIntakeDate);
        return Result.success(ResultStatusEnum.SUCCESS, waterIntakeMapper.selectList(waterIntakeLambdaQueryWrapper));
    }
}
