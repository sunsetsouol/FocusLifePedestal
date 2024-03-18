package com.qgStudio.pedestal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="摄水量对象", description="用户每日摄水量")
public class WaterIntake implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 日期
     */
    @ApiModelProperty(value = "摄水日期", required = true)
    private LocalDate intakeDate;

    /**
     * 目标摄水量
     */
    @ApiModelProperty(value = "目标摄水量", required = true)
    private Integer intakeTarget;

    /**
     * 实际摄水量
     */
    @ApiModelProperty(value = "实际摄水量", required = true)
    private Integer intakeReal;


    public WaterIntake(Integer userId, LocalDate date, Integer intakeTarget, Integer water) {
        this.userId = userId;
        this.intakeDate = date;
        this.intakeTarget = intakeTarget;
        this.intakeReal = water;
    }
}
