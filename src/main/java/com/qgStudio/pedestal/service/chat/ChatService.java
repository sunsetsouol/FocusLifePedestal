package com.qgStudio.pedestal.service.chat;

import com.qgStudio.pedestal.entity.dto.chat.GroupChatSendDTO;
import com.qgStudio.pedestal.entity.dto.chat.PrivateChatGetDTO;
import com.qgStudio.pedestal.entity.dto.chat.PrivateChatSendDTO;
import com.qgStudio.pedestal.entity.dto.chat.GroupChatGetDTO;
import com.qgStudio.pedestal.entity.dto.chat.ws.MessageGetDTO;
import com.qgStudio.pedestal.entity.dto.chat.ws.MessageSendDTO;
import com.qgStudio.pedestal.entity.vo.chat.ChatHistoryContextVO;
import com.qgStudio.pedestal.entity.vo.chat.ChatHistoryVO;

import java.util.List;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
public interface ChatService {
    ChatHistoryVO getPrivateChatHistory(Integer userId, PrivateChatGetDTO privateChatGetDTO);

    Boolean sendPrivateChat(PrivateChatSendDTO privateChatSendDTO, Integer userId);

    Boolean createSpaceInPrivate(Integer toId, Integer userId);

    ChatHistoryVO getPublicChatHistory(Integer userId, GroupChatGetDTO groupChatGetDTO);

    Boolean sendGroupChat(GroupChatSendDTO groupChatSendDTO, Integer userId);

    Boolean createSpaceInGroup(Integer groupId, Integer userId);

    ChatHistoryVO getMessage(MessageGetDTO messageGetDTO, Integer userId);

    Boolean sendMessage(MessageSendDTO messageSendDTO, Integer userId);
}
