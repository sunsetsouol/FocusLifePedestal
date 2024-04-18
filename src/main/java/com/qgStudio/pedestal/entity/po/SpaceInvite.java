package com.qgStudio.pedestal.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2024-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SpaceInvite implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 邀请人id
     */
    private Integer inviterId;

    /**
     * 空间id
     */
    private Long spaceId;

    /**
     * 受邀请用户id
     */
    private Integer toUserId;

    /**
     * 邀请时间
     */
    private LocalDateTime createTime;

    /**
     * 有效期
     */
    private LocalDateTime validatyTime;


}
