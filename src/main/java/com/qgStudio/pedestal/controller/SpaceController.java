package com.qgStudio.pedestal.controller;

import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.dto.space.SpaceCreateDTO;
import com.qgStudio.pedestal.entity.dto.space.SpaceRemoveMemberDTO;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.space.SpaceInviteVO;
import com.qgStudio.pedestal.entity.vo.space.SpaceUserVO;
import com.qgStudio.pedestal.entity.vo.space.SpaceVO;
import com.qgStudio.pedestal.service.space.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 空间接口
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/14
 */
@RestController
@RequestMapping("/space")
@RequiredArgsConstructor
public class SpaceController {
    private final SpaceService spaceService;

    /**
     * 创建空间
     * @param spaceCreateDTO 空间创建对象
     * @return 创建结果
     */
    @PostMapping("/create")
    public Result<SpaceVO> createSpace(@RequestBody SpaceCreateDTO spaceCreateDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return Result.success(spaceService.createSpace(userId, spaceCreateDTO));
    }

    /**
     * 删除空间
     * @param spaceId 空间id
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    public Result<Boolean> deleteSpace(@RequestParam Integer spaceId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return spaceService.deleteSpace(userId, spaceId);
    }

//    /**
//     * 获取空间
//     * @return 空间列表
//     */
//    @GetMapping
//    public Result<List<SpaceVO>> getSpace() {
//        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Integer userId = userDetails.getUser().getId();
//        return spaceService.getSpace(userId);
//    }

    /**
     * 获取空间成员
     */
    @GetMapping("/members/{spaceId}")
    public Result<List<SpaceUserVO>> getSpaceMembers(@PathVariable("spaceId") Integer spaceId) {
        return spaceService.getSpaceMembers(spaceId);
    }

    /**
     * 退出空间
     */
    @PostMapping("/quit")
    public Result<Boolean> quitSpace(@RequestParam Integer spaceId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return spaceService.quitSpace(userId, spaceId);
    }

    /**
     * 邀请加入空间
     * @param spaceId 空间id
     * @return 邀请对象
     */
    @PostMapping("/invite")
    public Result<SpaceInviteVO> inviteSpace(@RequestParam Long spaceId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return Result.success(spaceService.invite(userId, spaceId));
    }

    /**
     * 接受邀请
     * @param inviteId 邀请id
     * @return 接受结果
     */
    @PostMapping("/acceptInvite")
    public Result<Boolean> acceptInvite(@RequestParam Integer inviteId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return spaceService.acceptInvite(userId, inviteId);
    }

    /**
     * 移除空间成员
     * @param spaceRemoveMemberDTO 移除成员对象
     * @return 移除结果
     */
    @PostMapping("/remove")
    public Result<Boolean> removeSpace(@RequestBody@Validated SpaceRemoveMemberDTO spaceRemoveMemberDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return spaceService.removeSpace(spaceRemoveMemberDTO,userId);
    }

}
