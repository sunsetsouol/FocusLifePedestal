package com.qgStudio.pedestal.controller;

import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.dto.DealFriendApplyDTO;
import com.qgStudio.pedestal.entity.dto.StringDTO;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;
import com.qgStudio.pedestal.entity.vo.UserVo;
import com.qgStudio.pedestal.entity.vo.friend.FriendApplyVO;
import com.qgStudio.pedestal.service.IUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/friend")
public class FriendController {
    private final IUserService userService;
    /**
     * 添加好友
     * @param friendId 好友uid
     * @return 添加结果
     */
    @PostMapping("/add")
    @ApiOperation("添加好友申请")
    public Result addFriend(@RequestBody @Validated @ApiParam("好友id") StringDTO friendId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return userService.addFriend(userId, friendId.getParam());
    }

    /**
     * 获取好友申请
     * @return 好友申请
     */
    @GetMapping("/getFriendApply")
    @ApiOperation("获取好友申请")
    public Result<List<FriendApplyVO>> getFriendApply() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return Result.success(ResultStatusEnum.SUCCESS,userService.getFriendApply(userId));
    }

    /**
     * 处理好友申请
     * @param dealFriendApplyDTO 处理好友申请信息
     * @return 处理结果
     */
    @PostMapping("/dealFriendApply")
    @ApiOperation("处理好友申请")
    public Result dealFriendApply(@RequestBody @Validated DealFriendApplyDTO dealFriendApplyDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return userService.dealFriendApply(userId, dealFriendApplyDTO);
    }

    /**
     * 搜索我的好友
     * @return 我的好友
     */
    @GetMapping("/searchMyFriends")
    @ApiOperation("搜索我的好友")
    public Result<List<UserVo>> searchMyFriends() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return userService.searchMyFriends(userId);
    }
}
