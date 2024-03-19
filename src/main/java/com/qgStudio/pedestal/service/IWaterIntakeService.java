package com.qgStudio.pedestal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qgStudio.pedestal.entity.po.WaterIntake;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.WaterIntakeGetRangeVo;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
public interface IWaterIntakeService extends IService<WaterIntake> {


    Result addWaterIntake(Integer userId,Integer intakeWater);

    Result<WaterIntake> getWaterIntake(Integer userId, LocalDate time);

    Result<List<WaterIntake>> getRangeWaterIntake(Integer userId, WaterIntakeGetRangeVo waterIntakeGetRangeVo);

}
