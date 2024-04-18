package com.qgStudio.pedestal.service;

import com.qgStudio.pedestal.entity.dto.group.GroupCreateDTO;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
public interface GroupSerivce {
    Boolean createGroup(Integer userId, GroupCreateDTO groupCreateDTO);

    Boolean quitGroup(Integer userId, Integer groupId);

    Boolean inviteGroup(Integer ownerId, Integer groupId, Integer userId);
}
