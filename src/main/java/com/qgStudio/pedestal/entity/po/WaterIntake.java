package com.qgStudio.pedestal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2024-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
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
    private LocalDate intakeDate;

    /**
     * 目标摄水量
     */
    private Integer intakeTarget;

    /**
     * 实际摄水量
     */
    private Integer intakeReal;


    public WaterIntake(Integer userId, LocalDate now, Integer defaultWaterIntake, Integer intakeWater) {
        this.userId = userId;
        this.intakeDate = now;
        this.intakeTarget = defaultWaterIntake;
        this.intakeReal = intakeWater;
    }
}
