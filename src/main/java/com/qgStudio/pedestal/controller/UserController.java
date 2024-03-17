package com.qgStudio.pedestal.controller;

import com.qgStudio.pedestal.entity.vo.LoginUserVo;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/code")
    public Result getCode(@RequestParam("email") String email) {
        return userService.getCode(email);
    }
}
