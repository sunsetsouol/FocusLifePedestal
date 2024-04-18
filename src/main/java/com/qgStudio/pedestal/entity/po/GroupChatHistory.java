package com.qgStudio.pedestal.entity.po;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qgStudio.pedestal.entity.dto.chat.GroupChatSendDTO;
import com.qgStudio.pedestal.entity.vo.space.SpaceInviteVO;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2024-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发送者id
     */
    private Integer fromId;

    /**
     * 群聊id
     */
    private Integer groupId;

    /**
     * 类型
     */
    private String type;

    /**
     * 消息文本
     */
    private String context;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    public GroupChatHistory(GroupChatSendDTO groupChatSendDTO, Integer userId) {
        this.fromId = userId;
        this.groupId = groupChatSendDTO.getGroupId();
        this.context = groupChatSendDTO.getContext();
        this.type = Type.TEXT.value;;
    }

    public GroupChatHistory(Integer userId, Integer groupId, SpaceInviteVO spaceInviteVO) {
        this.fromId = userId;
        this.groupId = groupId;
        this.context = JSON.toJSONString(spaceInviteVO);
        this.type = Type.INVITE.value;
    }

    @AllArgsConstructor
    @Getter
    public enum Type {
        TEXT("文本"),
        INVITE("邀请"),
        ;
        private final String value;
    }
}
