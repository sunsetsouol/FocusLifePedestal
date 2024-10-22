package com.qgStudio.pedestal.service.chat.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgStudio.pedestal.entity.dto.chat.GroupChatSendDTO;
import com.qgStudio.pedestal.entity.dto.chat.PrivateChatGetDTO;
import com.qgStudio.pedestal.entity.dto.chat.PrivateChatSendDTO;
import com.qgStudio.pedestal.entity.dto.chat.GroupChatGetDTO;
import com.qgStudio.pedestal.entity.dto.chat.ws.MessageGetDTO;
import com.qgStudio.pedestal.entity.dto.chat.ws.MessageSendDTO;
import com.qgStudio.pedestal.entity.po.GroupChatHistory;
import com.qgStudio.pedestal.entity.po.PrivateChatHistory;
import com.qgStudio.pedestal.entity.po.Space;
import com.qgStudio.pedestal.entity.po.UserSpace;
import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;
import com.qgStudio.pedestal.entity.vo.chat.ChatHistoryContextVO;
import com.qgStudio.pedestal.entity.vo.chat.ChatHistoryVO;
import com.qgStudio.pedestal.entity.vo.space.SpaceInviteVO;
import com.qgStudio.pedestal.entity.vo.space.SpaceVO;
import com.qgStudio.pedestal.event.GroupChatEvent;
import com.qgStudio.pedestal.event.PrivateChatEvent;
import com.qgStudio.pedestal.exception.ServiceException;
import com.qgStudio.pedestal.mapper.GroupChatHistoryMapper;
import com.qgStudio.pedestal.mapper.PrivateChatHistoryMapper;
import com.qgStudio.pedestal.mapper.SpaceMapper;
import com.qgStudio.pedestal.mapper.UserSpaceMapper;
import com.qgStudio.pedestal.service.chat.ChatService;
import com.qgStudio.pedestal.service.space.SpaceService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
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
    private final ApplicationContext applicationContext;
    private final UserSpaceMapper userSpaceMapper;
    private final SpaceMapper spaceMapper;
    @Override
    public ChatHistoryVO getPrivateChatHistory(Integer userId, PrivateChatGetDTO privateChatGetDTO) {
        List<Integer> userIds = new ArrayList<>();
        userIds.add(userId);
        userIds.add(privateChatGetDTO.getUserId());

        IPage<PrivateChatHistory> page = new Page<>(0,10);
        IPage<PrivateChatHistory> privateChatHistoryIPage = privateChatHistoryMapper.selectPage(page, new LambdaQueryWrapper<PrivateChatHistory>()
                .in(PrivateChatHistory::getFromId, userIds)
                .in(PrivateChatHistory::getToId, userIds)
                .le(privateChatGetDTO.getLastId() != null, PrivateChatHistory::getId, privateChatGetDTO.getLastId()));

        List<ChatHistoryContextVO> chatHistoryContextVOS = privateChatHistoryIPage.getRecords().stream().map(ChatHistoryContextVO::new).collect(Collectors.toList());


        return new ChatHistoryVO(ChatHistoryVO.ChatType.PRIVATE.getType(), privateChatGetDTO.getUserId(),chatHistoryContextVOS);
    }

    @Override
    public Boolean sendPrivateChat(PrivateChatSendDTO privateChatSendDTO, Integer userId) {
        if (StringUtils.isBlank(privateChatSendDTO.getContext())){
            throw new ServiceException(ResultStatusEnum.EMPTY_MESSAGE);
        }
        PrivateChatHistory privateChatHistory = new PrivateChatHistory(privateChatSendDTO, userId);
        privateChatHistoryMapper.insert(privateChatHistory);

        applicationContext.publishEvent(new PrivateChatEvent(this, userId, privateChatSendDTO.getToId(),privateChatHistory));

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createSpaceInPrivate(Integer toId, Integer userId) {
        SpaceVO space = null;
        try {
            space = spaceService.createSpace(userId, null);
        } catch (ServiceException e) {
            UserSpace userSpace = userSpaceMapper.selectOne(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getUserId, userId));
            space = new SpaceVO(spaceMapper.selectById(userSpace.getSpaceId()));
        }
        SpaceInviteVO spaceInviteVO = spaceService.invite(userId, space.getId());
        PrivateChatHistory privateChatHistory = new PrivateChatHistory(userId, toId, spaceInviteVO);
        privateChatHistoryMapper.insert(privateChatHistory);

        applicationContext.publishEvent(new PrivateChatEvent(this, userId, toId,privateChatHistory));
        return true;
    }

    @Override
    public ChatHistoryVO getPublicChatHistory(Integer userId, GroupChatGetDTO groupChatGetDTO) {
        // Todo：验证是否群聊人员
        IPage<GroupChatHistory> page = new Page<>(0,10);
        IPage<GroupChatHistory> groupChatHistoryIPage = groupChatHistoryMapper.selectPage(page, new LambdaQueryWrapper<GroupChatHistory>()
                .eq(GroupChatHistory::getGroupId, groupChatGetDTO.getGroupId())
                .le(groupChatGetDTO.getLastId() != null, GroupChatHistory::getId, groupChatGetDTO.getLastId()));
        List<ChatHistoryContextVO> chatHistoryContextVOS = groupChatHistoryIPage.getRecords().stream().map(ChatHistoryContextVO::new).collect(Collectors.toList());

        return new ChatHistoryVO(ChatHistoryVO.ChatType.GROUP.getType(), groupChatGetDTO.getGroupId(), chatHistoryContextVOS);
    }

    @Override
    public Boolean sendGroupChat(GroupChatSendDTO groupChatSendDTO, Integer userId) {
        if(StringUtils.isBlank(groupChatSendDTO.getContext())){
            throw new ServiceException(ResultStatusEnum.EMPTY_MESSAGE);
        }
        GroupChatHistory groupChatHistory = new GroupChatHistory(groupChatSendDTO, userId);
        groupChatHistoryMapper.insert(groupChatHistory);
        applicationContext.publishEvent(new GroupChatEvent(this, groupChatSendDTO.getGroupId(), groupChatHistory));
        return true;
    }

    @Override
    public Boolean createSpaceInGroup(Integer groupId, Integer userId) {
        SpaceVO space = null;
        try {
            space = spaceService.createSpace(userId, null);
        } catch (ServiceException e) {
            UserSpace userSpace = userSpaceMapper.selectOne(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getUserId, userId));
            space = new SpaceVO(spaceMapper.selectById(userSpace.getSpaceId()));
        }
        SpaceInviteVO spaceInviteVO = spaceService.invite(userId, space.getId());
        GroupChatHistory groupChatHistory = new GroupChatHistory(userId, groupId, spaceInviteVO);
        groupChatHistoryMapper.insert(groupChatHistory);
        applicationContext.publishEvent(new GroupChatEvent(this, groupId, groupChatHistory));
        return true;
    }

    @Override
    public ChatHistoryVO getMessage(MessageGetDTO messageGetDTO, Integer userId) {
        if (messageGetDTO.getType().equals(MessageGetDTO.MessageType.PRIVATE.getType())){
            return getPrivateChatHistory(userId, new PrivateChatGetDTO( messageGetDTO.getTargetId(), messageGetDTO.getLastId()));
        }else {
            return getPublicChatHistory(userId, new GroupChatGetDTO(messageGetDTO.getTargetId(), messageGetDTO.getLastId()));
        }
    }

    @Override
    public Boolean sendMessage(MessageSendDTO messageSendDTO, Integer userId) {
        ChatService chatService = SpringUtil.getBean(ChatService.class);
        if (messageSendDTO.getType().equals(MessageSendDTO.MessageSendType.PRIVATE_TEXT.getType())) {
            return chatService.sendPrivateChat(new PrivateChatSendDTO(messageSendDTO.getToId(), messageSendDTO.getContext()), userId);
        }else if (messageSendDTO.getType().equals(MessageSendDTO.MessageSendType.GROUP_TEXT.getType())) {
            return chatService.sendGroupChat(new GroupChatSendDTO(messageSendDTO.getToId(), messageSendDTO.getContext()), userId);
        } else if (messageSendDTO.getType().equals(MessageSendDTO.MessageSendType.PRIVATE_SPACE.getType())) {
            return chatService.createSpaceInPrivate(messageSendDTO.getToId(), userId);
        } else if (messageSendDTO.getType().equals(MessageSendDTO.MessageSendType.GROUP_SPACE.getType())) {
            return chatService.createSpaceInGroup(messageSendDTO.getToId(), userId);
        }
        return null;
    }
}
