package com.qgStudio.pedestal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgStudio.pedestal.entity.po.WaterIntake;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
@Mapper
public interface WaterIntakeMapper extends BaseMapper<WaterIntake> {

    @Update("update water_intake set intake_real = intake_real + #{water} where user_id = #{userId} and intake_date = #{date}")
    void intake(Integer water, Integer userId, LocalDate date);
}
