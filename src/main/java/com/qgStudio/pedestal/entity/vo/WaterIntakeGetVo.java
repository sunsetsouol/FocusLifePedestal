package com.qgStudio.pedestal.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "WaterIntakeGetVo", description = "获取摄水量")
public class WaterIntakeGetVo {
    @ApiModelProperty(value = "日期", required = true)
    @NotNull
    private LocalDate time;
}
