package com.qgStudio.pedestal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgStudio.pedestal.entity.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("update user set default_water_intake = #{intake} where id = #{userId}")
    int setIntake(Integer userId, Integer intake);

    @Update("update user set default_reminder_interval = #{number} where id = #{userId}")
    int setReminderInterval(Integer userId, Integer number);

    @Update("update user set total_completion_time = total_completion_time + #{focusTime} where id = #{userId}")
    void increaseFocusTime(Integer userId, Integer focusTime);

    @Update("update user set total_water_intake = total_water_intake + #{intakeWater} where id = #{userId}")
    void increaseWaterIntake(Integer userId, Integer intakeWater);
}
