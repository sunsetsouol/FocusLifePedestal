package com.qgStudio.pedestal.entity.vo.space;

import com.qgStudio.pedestal.entity.po.Space;
import com.qgStudio.pedestal.entity.po.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpaceInviteVO {
    /**
     * 邀请id
     */
    private Long id;

    /**
     * 邀请人
     */
    private String username;

    /**
     * space的名字
     */
    private String spaceName;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 空间人数
     */
    private Integer memberNumber;

    public SpaceInviteVO(Long id, User user, Space space) {
        this.id = id;
        this.username = user.getName();
        this.spaceName = space.getName();
        this.description = space.getDescription();
        this.createTime = space.getCreateTime();
        this.memberNumber = space.getMemberNumber();
    }
}
