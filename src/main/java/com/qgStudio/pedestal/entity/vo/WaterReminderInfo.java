package com.qgStudio.pedestal.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("喝水提醒信息")
public class WaterReminderInfo {
    @ApiModelProperty("喝水量")
    private Integer waterIntake;
    @ApiModelProperty("提醒间隔（单位/分钟）")
    private Integer reminderInterval;
}
