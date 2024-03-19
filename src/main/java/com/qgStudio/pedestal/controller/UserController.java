package com.qgStudio.pedestal.controller;

import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.vo.IntegerVo;
import com.qgStudio.pedestal.entity.vo.LoginUserVo;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.WaterReminderInfo;
import com.qgStudio.pedestal.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Result login(@RequestBody LoginUserVo loginUserVo) {
        return userService.login(loginUserVo);
    }
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result register(@RequestBody @Validated @ApiParam("注册用户对象") LoginUserVo loginUserVo) {
        return userService.register(loginUserVo);
    }
    //Todo：验证码
    @GetMapping("/code")
    public Result getCode(@RequestParam("email") String email) {
        return userService.getCode(email);
    }

    @PostMapping("/setDefaultIntake")
    @ApiOperation("设置默认摄水量")
    public Result setIntake(@RequestBody @Validated@ApiParam("摄水量") IntegerVo intake) {
        return userService.setIntake(intake);
    }

    @PostMapping("/setDefaultReminderInterval")
    @ApiOperation("设置默认提醒间隔")
    public Result setReminderInterval(@RequestBody @Validated@ApiParam("提醒间隔") IntegerVo reminderInterval) {
        return userService.setReminderInterval(reminderInterval);
    }

    @GetMapping("/getWaterReminderInfo")
    @ApiOperation("获取喝水量和提醒间隔")
    public Result<WaterReminderInfo> getWaterReminderInfo() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return userService.getWaterReminderInfo(id);
    }
}
