package com.qgStudio.pedestal.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "WaterIntakeGetRangeVo", description = "获取摄水量范围")
public class WaterIntakeGetRangeVo {
    @ApiModelProperty(value = "开始时间", required = true)
    @NotNull
    private String startTime;
    @ApiModelProperty(value = "结束时间", required = true)
    @NotNull
    private String endTime;
}
