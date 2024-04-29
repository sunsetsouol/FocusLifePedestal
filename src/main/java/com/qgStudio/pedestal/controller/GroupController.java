package com.qgStudio.pedestal.controller;

import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.dto.group.GroupCreateDTO;
import com.qgStudio.pedestal.entity.dto.group.GroupInviteDTO;
import com.qgStudio.pedestal.entity.po.FocusGroup;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.service.GroupSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 群组接口
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupSerivce groupService;

    /**
     * 创建群聊
     * @param groupCreateDTO 群聊信息
     * @return 是否创建成功
     */
    @PostMapping("/create")
    public Result<Boolean> createGroup(@RequestBody GroupCreateDTO groupCreateDTO){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return Result.success(groupService.createGroup(userId, groupCreateDTO));
    }

    /**
     * 退出群聊
     */
    @PostMapping("/quit")
    public Result<Boolean> quitGroup(@RequestParam Integer groupId){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return Result.success(groupService.quitGroup(userId, groupId));
    }
    /**
     * 邀请加入群聊
     */
    @PostMapping("invite")
    public Result<Boolean> inviteGroup(@RequestBody GroupInviteDTO groupInviteDTO){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer fromId = userDetails.getUser().getId();
        return Result.success(groupService.inviteGroup(fromId, groupInviteDTO));
    }

    /**
     * 查找群聊
     */
    @GetMapping("/search")
    public Result<List<FocusGroup>> searchGroup(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return Result.success(groupService.searchGroup(userId));
    }
}
