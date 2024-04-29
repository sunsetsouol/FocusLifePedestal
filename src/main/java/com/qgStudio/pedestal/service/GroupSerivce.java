package com.qgStudio.pedestal.service;

import com.qgStudio.pedestal.entity.dto.group.GroupCreateDTO;
import com.qgStudio.pedestal.entity.dto.group.GroupInviteDTO;
import com.qgStudio.pedestal.entity.po.FocusGroup;

import java.util.List;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
public interface GroupSerivce {
    Boolean createGroup(Integer userId, GroupCreateDTO groupCreateDTO);

    Boolean quitGroup(Integer userId, Integer groupId);

    Boolean inviteGroup(Integer fromId, GroupInviteDTO groupInviteDTO);

    List<FocusGroup> searchGroup(Integer userId);
}
