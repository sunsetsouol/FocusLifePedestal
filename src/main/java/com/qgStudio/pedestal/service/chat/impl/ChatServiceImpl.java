package com.qgStudio.pedestal.service.chat.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgStudio.pedestal.entity.dto.chat.GroupChatSendDTO;
import com.qgStudio.pedestal.entity.dto.chat.PrivateChatGetDTO;
import com.qgStudio.pedestal.entity.dto.chat.PrivateChatSendDTO;
import com.qgStudio.pedestal.entity.dto.chat.GroupChatGetDTO;
import com.qgStudio.pedestal.entity.po.GroupChatHistory;
import com.qgStudio.pedestal.entity.po.PrivateChatHistory;
import com.qgStudio.pedestal.entity.vo.chat.ChatHistoryVO;
import com.qgStudio.pedestal.entity.vo.space.SpaceInviteVO;
import com.qgStudio.pedestal.entity.vo.space.SpaceVO;
import com.qgStudio.pedestal.mapper.GroupChatHistoryMapper;
import com.qgStudio.pedestal.mapper.PrivateChatHistoryMapper;
import com.qgStudio.pedestal.service.chat.ChatService;
import com.qgStudio.pedestal.service.space.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final PrivateChatHistoryMapper privateChatHistoryMapper;
    private final SpaceService spaceService;
    private final GroupChatHistoryMapper groupChatHistoryMapper;
    @Override
    public List<ChatHistoryVO> getPrivateChatHistory(Integer userId, PrivateChatGetDTO privateChatGetDTO) {
        List<Integer> userIds = new ArrayList<>();
        userIds.add(userId);
        userIds.add(privateChatGetDTO.getUserId());

        IPage<PrivateChatHistory> page = new Page<>(0,10);
        IPage<PrivateChatHistory> privateChatHistoryIPage = privateChatHistoryMapper.selectPage(page, new LambdaQueryWrapper<PrivateChatHistory>()
                .in(PrivateChatHistory::getFromId, userIds)
                .in(PrivateChatHistory::getToId, userIds)
                .le(privateChatGetDTO.getLastId() != null, PrivateChatHistory::getId, privateChatGetDTO.getLastId()));

        return privateChatHistoryIPage.getRecords().stream().map(ChatHistoryVO::new).collect(Collectors.toList());
    }

    @Override
    public Boolean sendPrivateChat(PrivateChatSendDTO privateChatSendDTO, Integer userId) {
        PrivateChatHistory privateChatHistory = new PrivateChatHistory(privateChatSendDTO, userId);
        privateChatHistoryMapper.insert(privateChatHistory);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createSpaceInPrivate(Integer toId, Integer userId) {
        SpaceVO space = spaceService.createSpace(userId, null);
        SpaceInviteVO spaceInviteVO = spaceService.invite(userId, space.getId());
        PrivateChatHistory privateChatHistory = new PrivateChatHistory(userId, toId, spaceInviteVO);
        privateChatHistoryMapper.insert(privateChatHistory);
        return true;
    }

    @Override
    public List<ChatHistoryVO> getPublicChatHistory(Integer userId, GroupChatGetDTO groupChatGetDTO) {
        // Todo：验证是否群聊人员
        IPage<GroupChatHistory> page = new Page<>(0,10);
        IPage<GroupChatHistory> groupChatHistoryIPage = groupChatHistoryMapper.selectPage(page, new LambdaQueryWrapper<GroupChatHistory>()
                .eq(GroupChatHistory::getGroupId, groupChatGetDTO.getGroupId())
                .le(groupChatGetDTO.getLastId() != null, GroupChatHistory::getId, groupChatGetDTO.getLastId()));

        return groupChatHistoryIPage.getRecords().stream().map(ChatHistoryVO::new).collect(Collectors.toList());
    }

    @Override
    public Boolean sendGroupChat(GroupChatSendDTO groupChatSendDTO, Integer userId) {
        GroupChatHistory groupChatHistory = new GroupChatHistory(groupChatSendDTO, userId);
        groupChatHistoryMapper.insert(groupChatHistory);
        return true;
    }

    @Override
    public Boolean createSpaceInGroup(Integer groupId, Integer userId) {
        SpaceVO space = spaceService.createSpace(userId, null);
        SpaceInviteVO spaceInviteVO = spaceService.invite(userId, space.getId());
        GroupChatHistory groupChatHistory = new GroupChatHistory(userId, groupId, spaceInviteVO);
        groupChatHistoryMapper.insert(groupChatHistory);
        return true;
    }
}
