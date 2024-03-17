package com.qgStudio.pedestal.controller;

import com.qgStudio.pedestal.entity.po.WaterIntake;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.WaterIntakeAddVo;
import com.qgStudio.pedestal.entity.vo.WaterIntakeGetRangeVo;
import com.qgStudio.pedestal.entity.vo.WaterIntakeGetVo;
import com.qgStudio.pedestal.service.IWaterIntakeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/17
 */
@RestController
@RequestMapping("/water")
@Api(tags = "摄水量")
public class WaterController {
    private final IWaterIntakeService waterIntakeService;

    @Autowired
    public WaterController(IWaterIntakeService waterIntakeService) {
        this.waterIntakeService = waterIntakeService;
    }

    @PostMapping("/getIntake")
    @ApiOperation(value = "获取摄水量", notes = "获取摄水量")
    public Result<WaterIntake> getWater(@RequestBody @Validated @ApiParam WaterIntakeGetVo waterIntakeGetVo) {
        return waterIntakeService.getWaterIntake(waterIntakeGetVo.getTime());
    }

    @PostMapping("/getIntakeRange")
    @ApiOperation(value = "获取一段时间摄水量", notes = "获取摄水量")
    public Result<List<WaterIntake>> getRangeWater(@RequestBody @Validated @ApiParam WaterIntakeGetRangeVo waterIntakeGetRangeVo) {
        return waterIntakeService.getRangeWaterIntake(waterIntakeGetRangeVo);
    }

    // 非移动接口
    @PostMapping("/addWater")
    public Result addWater(@RequestBody @Validated WaterIntakeAddVo waterIntakeAddVo) {
        return waterIntakeService.addWaterIntake(waterIntakeAddVo.getWater());
    }
}
