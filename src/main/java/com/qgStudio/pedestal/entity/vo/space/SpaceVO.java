package com.qgStudio.pedestal.entity.vo.space;

import com.qgStudio.pedestal.entity.po.Space;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpaceVO {

    /**
     * spaceId
     */
    private Long id;

    /**
     * space的名字
     */
    private String name;

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

    public SpaceVO(Space space) {
        this.id = space.getId();
        this.name = space.getName();
        this.description = space.getDescription();
        this.createTime = space.getCreateTime();
        this.memberNumber = space.getMemberNumber();
    }
}
