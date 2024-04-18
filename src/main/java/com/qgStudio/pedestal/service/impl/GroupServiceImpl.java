package com.qgStudio.pedestal.service.impl;

import com.qgStudio.pedestal.entity.dto.group.GroupCreateDTO;
import com.qgStudio.pedestal.entity.node.GroupNode;
import com.qgStudio.pedestal.entity.po.FocusGroup;
import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;
import com.qgStudio.pedestal.exception.ServiceException;
import com.qgStudio.pedestal.mapper.FocusGroupMapper;
import com.qgStudio.pedestal.repository.node.GroupNodeRepository;
import com.qgStudio.pedestal.repository.relation.GroupRelationRepository;
import com.qgStudio.pedestal.service.GroupSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/18
 */
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupSerivce {
    private final FocusGroupMapper focusGroupMapper;
    private final GroupNodeRepository groupNodeRepository;
    private final GroupRelationRepository groupRelationRepository;
    @Override
    public Boolean createGroup(Integer userId, GroupCreateDTO groupCreateDTO) {
        FocusGroup focusGroup = new FocusGroup(userId, groupCreateDTO);
        focusGroupMapper.insert(focusGroup);

        GroupNode groupNode = new GroupNode();
        groupNode.setGroupId(focusGroup.getId());
        groupNodeRepository.save(groupNode);

        groupRelationRepository.join(userId, focusGroup.getId());
        return true;
    }

    @Override
    public Boolean quitGroup(Integer userId, Integer groupId) {
        FocusGroup focusGroup = focusGroupMapper.selectById(groupId);
        if (focusGroup==null) {
            throw new ServiceException(ResultStatusEnum.GROUP_NOT_EXIT);
        }
        if (focusGroup.getOwnerUserId().equals(userId)){
            // 解散该群
        }
        if (groupRelationRepository.deleteRelation(userId, groupId)==1) {

            focusGroupMapper.decreaseMemberCount(groupId);
            return true;
        }
        return false;
    }

    @Override
    public Boolean inviteGroup(Integer ownerId, Integer groupId, Integer userId) {
        return null;
    }
}
