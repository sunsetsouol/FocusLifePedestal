package com.qgStudio.pedestal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qgStudio.pedestal.entity.dto.DealFriendApplyDTO;
import com.qgStudio.pedestal.entity.dto.IntegerDTO;
import com.qgStudio.pedestal.entity.dto.LoginUserDTO;
import com.qgStudio.pedestal.entity.po.User;
import com.qgStudio.pedestal.entity.vo.*;

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

    Result login(LoginUserDTO loginUserDTO);

    Result register(LoginUserDTO loginUserDTO);

    Result getCode(String email);

    Result setIntake(IntegerDTO intake);

    Result setReminderInterval(IntegerDTO reminderInterval);

    Result<WaterReminderInfo> getWaterReminderInfo(Integer userId);

    Result<Integer> getHistoryWaterIntake(Integer id);

    Result<Integer> getHistoryFocusTime(Integer id);

    Result<List<UserVo>> getByUId(String uid);

    Result addFriend(Integer userId, String uid);

    List<UserVo> getFriendApply(Integer userId);

    Result dealFriendApply(Integer userId, DealFriendApplyDTO dealFriendApplyDTO);

    Result<List<UserVo>> searchMyFriends(Integer userId);
}
