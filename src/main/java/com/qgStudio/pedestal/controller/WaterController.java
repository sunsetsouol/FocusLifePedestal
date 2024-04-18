package com.qgStudio.pedestal.controller;

import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.po.WaterIntake;
import com.qgStudio.pedestal.entity.dto.IntegerDTO;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.WaterIntakeGetRangeVo;
import com.qgStudio.pedestal.entity.vo.WaterIntakeGetVo;
import com.qgStudio.pedestal.service.IUserService;
import com.qgStudio.pedestal.service.IWaterIntakeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 摄水量接口
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/17
 */
@RestController
@RequestMapping("/water")
@Api(tags = "摄水量")
public class WaterController {
    private final IWaterIntakeService waterIntakeService;
    private final IUserService userService;

    @Autowired
    public WaterController(IWaterIntakeService waterIntakeService, IUserService userService) {
        this.waterIntakeService = waterIntakeService;
        this.userService = userService;
    }

    /**
     * 获取摄水量
     * @param waterIntakeGetVo 获取摄水量对象
     * @return 摄水量
     */
    @PostMapping("/getIntake")
    @ApiOperation(value = "获取摄水量", notes = "获取摄水量")
    public Result<WaterIntake> getWater(@RequestBody @Validated @ApiParam WaterIntakeGetVo waterIntakeGetVo) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return waterIntakeService.getWaterIntake(id, waterIntakeGetVo.getTime());
    }

    /**
     * 获取一段时间摄水量
     * @param waterIntakeGetRangeVo 时间段
     * @return 摄水量
     */
    @PostMapping("/getIntakeRange")
    @ApiOperation(value = "获取一段时间摄水量", notes = "获取摄水量")
    public Result<List<WaterIntake>> getRangeWater(@RequestBody @Validated @ApiParam WaterIntakeGetRangeVo waterIntakeGetRangeVo) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return waterIntakeService.getRangeWaterIntake(id, waterIntakeGetRangeVo);
    }

    /**
     * 获取历史摄水量
     * @return  历史摄水量
     */
    @GetMapping("/getHistoryWaterIntake")
    @ApiOperation(value = "获取历史摄水量", notes = "获取历史摄水量")
    public Result<Integer> getHistoryWaterIntake() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return userService.getHistoryWaterIntake(id);
    }

    // 非移动接口
    @PostMapping("/addWater")
    public Result addWater(@RequestBody @Validated IntegerDTO integerDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return waterIntakeService.addWaterIntake(id, integerDTO.getNumber());
    }
}
