package com.qgStudio.pedestal.controller;

import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.dto.DealFriendApplyDTO;
import com.qgStudio.pedestal.entity.dto.IntegerDTO;
import com.qgStudio.pedestal.entity.dto.LoginUserDTO;
import com.qgStudio.pedestal.entity.dto.StringDTO;
import com.qgStudio.pedestal.entity.vo.*;
import com.qgStudio.pedestal.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/16
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result login(@RequestBody LoginUserDTO loginUserDTO) {
        return userService.login(loginUserDTO);
    }
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result register(@RequestBody @Validated @ApiParam("注册用户对象") LoginUserDTO loginUserDTO) {
        return userService.register(loginUserDTO);
    }
    //Todo：验证码
    @GetMapping("/code")
    public Result getCode(@RequestParam("email") String email) {
        return userService.getCode(email);
    }

    @PostMapping("/setDefaultIntake")
    @ApiOperation("设置默认摄水量")
    public Result setIntake(@RequestBody @Validated@ApiParam("摄水量") IntegerDTO intake) {
        return userService.setIntake(intake);
    }

    @PostMapping("/setDefaultReminderInterval")
    @ApiOperation("设置默认提醒间隔")
    public Result setReminderInterval(@RequestBody @Validated@ApiParam("提醒间隔") IntegerDTO reminderInterval) {
        return userService.setReminderInterval(reminderInterval);
    }

    @GetMapping("/getWaterReminderInfo")
    @ApiOperation("获取喝水量和提醒间隔")
    public Result<WaterReminderInfo> getWaterReminderInfo() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return userService.getWaterReminderInfo(id);
    }

    @GetMapping("/find/{uid}")
    @ApiOperation("搜索用户")
    public Result<List<UserVo>> findUser(@PathVariable("uid") String uid) {
        return userService.getByUId(uid);
    }

    @PostMapping("/add")
    @ApiOperation("添加好友申请")
    public Result addFriend(@RequestBody @Validated@ApiParam("好友id") StringDTO friendId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return userService.addFriend(userId, friendId.getParam());
    }

    @GetMapping("/getFriendApply")
    @ApiOperation("获取好友申请")
    public Result<List<UserVo>> getFriendApply() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return Result.success(ResultStatusEnum.SUCCESS,userService.getFriendApply(userId));
    }

    @PostMapping("/dealFriendApply")
    @ApiOperation("处理好友申请")
    public Result dealFriendApply(@RequestBody @Validated DealFriendApplyDTO dealFriendApplyDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return userService.dealFriendApply(userId, dealFriendApplyDTO);
    }

    @GetMapping("/searchMyFriends")
    @ApiOperation("搜索我的好友")
    public Result<List<UserVo>> searchMyFriends() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return userService.searchMyFriends(userId);
    }
}
