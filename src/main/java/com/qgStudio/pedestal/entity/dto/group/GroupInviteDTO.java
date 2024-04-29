package com.qgStudio.pedestal.entity.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/21
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupInviteDTO {

    private Integer groupId;

    private List<Integer> userids;
}
