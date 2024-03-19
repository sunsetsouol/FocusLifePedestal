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
    public Result addWaterIntake(Integer userId, Integer intakeWater) {
        RLock lock = redissonClient.getLock(RedisConstants.WATER_INTAKE_LOCK + userId);
        LocalDate now = LocalDate.now();
        try {
            if (lock.tryLock(30,30, TimeUnit.SECONDS)) {
                try {
                    LambdaQueryWrapper<WaterIntake> waterIntakeLambdaQueryWrapper
                            = new LambdaQueryWrapper<>();
                    waterIntakeLambdaQueryWrapper.eq(WaterIntake::getUserId, userId)
                                    .eq(WaterIntake::getIntakeDate, now);
                    if (waterIntakeMapper.selectOne(waterIntakeLambdaQueryWrapper) == null) {
                        User user = userMapper.selectById(userId);
                        WaterIntake waterIntake = new WaterIntake(userId, now,user.getDefaultWaterIntake(), intakeWater);
                        waterIntakeMapper.insert(waterIntake);
                    } else {
                        waterIntakeMapper.intake(intakeWater,userId,now);
                    }
                } finally {
                    lock.unlock();
                }
            }
            return Result.success();
        } catch (InterruptedException e) {
            // Todo:mqtt返回队列
            throw new RuntimeException(e);
        }
//        stringRedisTemplate.opsForList().leftPush(RedisConstants.WATER_INTAKE + LocalDate.now() + ":" + waterIntakeVo.getUserId(), String.valueOf(waterIntakeVo.getWater()));
    }

    @Override
    public Result<WaterIntake> getWaterIntake(Integer userId, LocalDate time) {
//        List<String> range = stringRedisTemplate.opsForList().range(RedisConstants.WATER_INTAKE + LocalDate.now() + ":" + userId, 0, -1);
//        Integer sum = range.stream().map(Integer::parseInt).reduce(0, Integer::sum);
        LambdaQueryWrapper<WaterIntake> waterIntakeLambdaQueryWrapper
                = new LambdaQueryWrapper<>();
        waterIntakeLambdaQueryWrapper.eq(WaterIntake::getUserId, userId)
                .eq(WaterIntake::getIntakeDate, time)
                .select(WaterIntake::getIntakeReal, WaterIntake::getIntakeTarget, WaterIntake::getIntakeDate);
        return Result.success(ResultStatusEnum.SUCCESS, waterIntakeMapper.selectOne(waterIntakeLambdaQueryWrapper));
    }

    @Override
    public Result<List<WaterIntake>> getRangeWaterIntake(Integer userId, WaterIntakeGetRangeVo waterIntakeGetRangeVo) {
        LambdaQueryWrapper<WaterIntake> waterIntakeLambdaQueryWrapper
                = new LambdaQueryWrapper<>();
        waterIntakeLambdaQueryWrapper.eq(WaterIntake::getUserId, userId)
                .between(WaterIntake::getIntakeDate, waterIntakeGetRangeVo.getStartTime(), waterIntakeGetRangeVo.getEndTime())
                .select(WaterIntake::getIntakeReal, WaterIntake::getIntakeTarget, WaterIntake::getIntakeDate);
        return Result.success(ResultStatusEnum.SUCCESS, waterIntakeMapper.selectList(waterIntakeLambdaQueryWrapper));
    }
}
