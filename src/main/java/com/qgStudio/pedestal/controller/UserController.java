package com.qgStudio.pedestal.controller;

import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.dto.DealFriendApplyDTO;
import com.qgStudio.pedestal.entity.dto.IntegerDTO;
import com.qgStudio.pedestal.entity.dto.LoginUserDTO;
import com.qgStudio.pedestal.entity.dto.StringDTO;
import com.qgStudio.pedestal.entity.vo.*;
import com.qgStudio.pedestal.entity.vo.friend.FriendApplyVO;
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
 * 用户接口
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

    /**
     * 用户登录
     * @param loginUserDTO 登录用户信息
     * @return 登录结果
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result login(@RequestBody LoginUserDTO loginUserDTO) {
        return userService.login(loginUserDTO);
    }

    /**
     * 注册
     * @param loginUserDTO 注册用户信息
     * @return 注册结果
     */
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

    /**
     * 设置默认摄水量
     * @param intake 摄水量
     * @return 设置结果
     */
    @PostMapping("/setDefaultIntake")
    @ApiOperation("设置默认摄水量")
    public Result setIntake(@RequestBody @Validated@ApiParam("摄水量") IntegerDTO intake) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return userService.setIntake(intake, userId);
    }

    /**
     * 设置默认提醒间隔
     * @param reminderInterval 提醒间隔
     * @return 设置结果
     */
    @PostMapping("/setDefaultReminderInterval")
    @ApiOperation("设置默认提醒间隔")
    public Result setReminderInterval(@RequestBody @Validated@ApiParam("提醒间隔") IntegerDTO reminderInterval) {
        return userService.setReminderInterval(reminderInterval);
    }

    /**
     * 获取喝水量和时间间隔
     * @return 喝水量和时间间隔
     */
    @GetMapping("/getWaterReminderInfo")
    @ApiOperation("获取喝水量和提醒间隔")
    public Result<WaterReminderInfo> getWaterReminderInfo() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return userService.getWaterReminderInfo(id);
    }

    /**
     * 搜索用户
     * @param uid 用户uid
     * @return 用户信息
     */
    @GetMapping("/find/{uid}")
    @ApiOperation("搜索用户")
    public Result<List<UserVo>> findUser(@PathVariable("uid") String uid) {
        return userService.getByUId(uid);
    }


}
