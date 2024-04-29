package com.qgStudio.pedestal.controller;

import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.dto.chat.*;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.chat.ChatHistoryContextVO;
import com.qgStudio.pedestal.entity.vo.chat.ChatHistoryVO;
import com.qgStudio.pedestal.service.chat.ChatService;
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
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    /**
     * 获取私聊信息
     * @param privateChatGetDTO 获取信息对象
     * @return 信息列表
     */
    @GetMapping("/private")
    public Result<ChatHistoryVO> getChatHistory(@RequestBody @Validated PrivateChatGetDTO privateChatGetDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return Result.success(chatService.getPrivateChatHistory(userId,privateChatGetDTO));
    }

    /**
     * 获取群聊信息
     * @param groupChatGetDTO 获取群聊信息对象
     * @return 信息列表
     */
    @GetMapping("/group")
    public Result<ChatHistoryVO> getGroupChatHistory(@RequestBody @Validated GroupChatGetDTO groupChatGetDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return Result.success(chatService.getPublicChatHistory(userId, groupChatGetDTO));
    }

    /**
     * 发送私聊消息
     * @param privateChatSendDTO 发送消息对象
     * @return 是否成功
     */
    @PostMapping("/private/send")
    public Result<Boolean> sendPrivateChat(@RequestBody@Validated PrivateChatSendDTO privateChatSendDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return Result.success(chatService.sendPrivateChat(privateChatSendDTO,userId));
    }

    /**
     * 发送群聊消息
     * @param groupChatSendDTO 发送消息对象
     * @return 是否成功
     */
    @PostMapping("/group/send")
    public Result<Boolean> sendGroupChat(@RequestBody@Validated GroupChatSendDTO groupChatSendDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return Result.success(chatService.sendGroupChat(groupChatSendDTO,userId));
    }

    /**
     * 私对私开启专注空间
     * @param spaceCreatePrivateDTO 专注空间创建信息
     * @return 是否成功
     */
    @PostMapping("/space/private")
    public Result<Boolean> initiateSpaceInPrivate(@RequestBody@Validated SpaceCreatePrivateDTO spaceCreatePrivateDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return Result.success(chatService.createSpaceInPrivate(spaceCreatePrivateDTO.getToId(),userId));
    }

    /**
     * 群聊开启专注空间
     * @param spaceCreateGroupDTO 专注空间创建信息
     * @return 是否成功
     */
    @PostMapping("/space/group")
    public Result<Boolean> initiateSpaceInGroup(@RequestBody@Validated SpaceCreateGroupDTO spaceCreateGroupDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getUser().getId();
        return Result.success(chatService.createSpaceInGroup(spaceCreateGroupDTO.getGroupId(),userId));
    }

}
