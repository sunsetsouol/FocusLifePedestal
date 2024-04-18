package com.qgStudio.pedestal.entity.po;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qgStudio.pedestal.entity.dto.chat.PrivateChatSendDTO;
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
public class PrivateChatHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发送者id
     */
    private Integer fromId;

    /**
     * 接收者id
     */
    private Integer toId;

    /**
     * 类型（文本或邀请）
     */
    private String type;

    /**
     * 内容
     */
    private String context;

    /**
     * 发送时间
     */
    private LocalDateTime createTime;

    public PrivateChatHistory(PrivateChatSendDTO privateChatSendDTO, Integer userId) {
        this.fromId = userId;
        this.toId = privateChatSendDTO.getToId();
        this.context = privateChatSendDTO.getContext();
        this.type = Type.TEXT.getValue();
    }

    public PrivateChatHistory(Integer userId, Integer toId, SpaceInviteVO spaceInviteVO) {
        this.fromId = userId;
        this.toId = toId;
        this.context = JSON.toJSONString(spaceInviteVO);
        this.type = Type.INVITE.getValue();
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
