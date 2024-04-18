package com.qgStudio.pedestal.entity.dto.space;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpaceRemoveMemberDTO {

    /**
     * 空间id
     */
    @NotNull(message = "空间id不能为空")
    private Long spaceId;

    /**
     *  被移除用户userId
     */
    @NotNull(message = "用户uid不能为空")
    private String  userUid;
}
