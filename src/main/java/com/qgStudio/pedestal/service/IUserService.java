package com.qgStudio.pedestal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qgStudio.pedestal.entity.po.User;
import com.qgStudio.pedestal.entity.po.WaterIntake;
import com.qgStudio.pedestal.entity.vo.IntegerVo;
import com.qgStudio.pedestal.entity.vo.LoginUserVo;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.WaterReminderInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
public interface IUserService extends IService<User> {

    Result login(LoginUserVo loginUserVo);

    Result register(LoginUserVo loginUserVo);

    Result getCode(String email);

    Result setIntake(IntegerVo intake);

    Result setReminderInterval(IntegerVo reminderInterval);

    Result<WaterReminderInfo> getWaterReminderInfo(Integer userId);

    Result<Integer> getHistoryWaterIntake(Integer id);

    Result<Integer> getHistoryFocusTime(Integer id);
}
