package com.qgStudio.pedestal.service.space.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qgStudio.pedestal.entity.dto.space.SpaceCreateDTO;
import com.qgStudio.pedestal.entity.dto.space.SpaceRemoveMemberDTO;
import com.qgStudio.pedestal.entity.po.Space;
import com.qgStudio.pedestal.entity.po.SpaceInvite;
import com.qgStudio.pedestal.entity.po.User;
import com.qgStudio.pedestal.entity.po.UserSpace;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.ResultStatusEnum;
import com.qgStudio.pedestal.entity.vo.space.SpaceInviteVO;
import com.qgStudio.pedestal.entity.vo.space.SpaceUserVO;
import com.qgStudio.pedestal.entity.vo.space.SpaceVO;
import com.qgStudio.pedestal.exception.ServiceException;
import com.qgStudio.pedestal.mapper.SpaceInviteMapper;
import com.qgStudio.pedestal.mapper.SpaceMapper;
import com.qgStudio.pedestal.mapper.UserMapper;
import com.qgStudio.pedestal.mapper.UserSpaceMapper;
import com.qgStudio.pedestal.service.space.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/16
 */
@Service
@RequiredArgsConstructor
public class SpaceServiceImpl implements SpaceService {
    private final SpaceMapper spaceMapper;
    private final UserSpaceMapper userSpaceMapper;
    private final SpaceInviteMapper spaceInviteMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SpaceVO createSpace(Integer userId, SpaceCreateDTO spaceCreateDTO) {

        validateNotSpace(userId);

        Space space = new Space(spaceCreateDTO, userId);
        spaceMapper.insert(space);
        UserSpace ownerSpaceMap = new UserSpace(userId, space.getId());


//        if(!Objects.isNull(spaceCreateDTO.getUserUIds())){
//            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().in(User::getUid, spaceCreateDTO.getUserUIds()).select(User::getId));
//
//            if (!Objects.isNull(users) ) {
//                List<UserSpace> userSpaces = users.stream().map(user -> new UserSpace(user.getId(), space.getId())).collect(Collectors.toList());
//                userSpaceMapper.insertList(userSpaces);
//            }
//        }
        userSpaceMapper.insert(ownerSpaceMap);
        return new SpaceVO(space);
    }

    @Override
    public Result<Boolean> deleteSpace(Integer userId, Integer spaceId) {
        if (spaceMapper.delete(new LambdaQueryWrapper<Space>().eq(Space::getId, spaceId).eq(Space::getOwnerUserId, userId)) == 1) {
            userSpaceMapper.delete(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getSpaceId, spaceId));
            return Result.success(ResultStatusEnum.SUCCESS);
        }
        return Result.fail(ResultStatusEnum.NOT_AUTHORIZATION);
    }


    //    @Override
//    public Result<List<SpaceVO>> getSpace(Integer userId) {
//        return Result.success(ResultStatusEnum.SUCCESS, spaceMapper.selectList(new LambdaQueryWrapper<Space>().eq(Space::getOwnerUserId, userId)).stream().map(SpaceVO::new).collect(Collectors.toList()));
//    }
    @Override
    public Result<List<SpaceUserVO>> getSpaceMembers(Long spaceId) {
        return Result.success(userSpaceMapper.selectSpaceUsers(spaceId));

    }

    @Override
    public Result<Boolean> quitSpace(Integer userId, Integer spaceId) {
        Space space = spaceMapper.selectById(spaceId);
        if (Objects.isNull(space)) {
            throw new ServiceException(ResultStatusEnum.SPACE_NOT_EXIT);
        }
        if (space.getOwnerUserId().equals(userId) && space.getMemberNumber() > 1) {
            throw new ServiceException(ResultStatusEnum.OWNER_CANNOT_QUIT);
        }
        userSpaceMapper.delete(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getUserId, userId).eq(UserSpace::getSpaceId, spaceId));
        userSpaceMapper.delete(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getSpaceId, spaceId));
        spaceMapper.decrease(spaceId);
        if (space.getOwnerUserId().equals(userId)) {
            spaceMapper.delete(new LambdaQueryWrapper<Space>().eq(Space::getId, spaceId));
        }
        return Result.success(ResultStatusEnum.SUCCESS);
    }

    @Override
    public SpaceInviteVO invite(Integer userId, Long spaceId) {
        Space space = spaceMapper.selectById(spaceId);
        if (Objects.isNull(space)) {
            throw new ServiceException(ResultStatusEnum.SPACE_NOT_EXIT);
        }
        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)) {
            throw new ServiceException(ResultStatusEnum.USER_NOT_EXIST);
        }
        if (userSpaceMapper.selectCount(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getUserId, userId).eq(UserSpace::getSpaceId, spaceId)) == 0) {
            throw new ServiceException(ResultStatusEnum.NOT_AUTHORIZATION);
        }
        SpaceInvite spaceInvite = new SpaceInvite();
        spaceInvite.setSpaceId(spaceId);
        spaceInvite.setInviterId(userId);
        spaceInvite.setValidatyTime(LocalDateTime.now().plusDays(7));
        spaceInviteMapper.insert(spaceInvite);
        return new SpaceInviteVO(spaceInvite.getId(), user, space);
    }

    @Override
    public Result<Boolean> acceptInvite(Integer userId, Integer inviteId) {

        validateNotSpace(userId);

        SpaceInvite spaceInvite = spaceInviteMapper.selectById(inviteId);
        if (Objects.isNull(spaceInvite)) {
            throw new ServiceException(ResultStatusEnum.INVITE_NOT_EXIT);
        }
        if (spaceInvite.getValidatyTime().isBefore(LocalDateTime.now())) {
            throw new ServiceException(ResultStatusEnum.OVERDUE);
        }
        UserSpace userSpace = new UserSpace(userId, spaceInvite.getSpaceId());
        userSpaceMapper.insert(userSpace);
        spaceMapper.increase(spaceInvite.getSpaceId());
        return Result.success();
    }

    @Override
    public Result<Boolean> removeSpace(SpaceRemoveMemberDTO spaceRemoveMemberDTO, Integer userId) {
        Space space = spaceMapper.selectById(spaceRemoveMemberDTO.getSpaceId());
        if (Objects.isNull(space)) {
            throw new ServiceException(ResultStatusEnum.SPACE_NOT_EXIT);
        }
        if (space.getOwnerUserId().equals(userId)) {
            throw new ServiceException(ResultStatusEnum.NOT_AUTHORIZATION);
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUid, spaceRemoveMemberDTO.getUserUid()));
        if (userSpaceMapper.delete(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getUserId, user.getId()).eq(UserSpace::getSpaceId, spaceRemoveMemberDTO.getSpaceId())) == 1) {
            return Result.success();
        }
        throw new ServiceException(ResultStatusEnum.USER_NOT_IN_SPACE);
    }

    @Override
    public Integer getFoucsingMembers(Integer userId) {
        UserSpace userSpace = userSpaceMapper.selectOne(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getUserId, userId));
        if (Objects.isNull(userSpace)) {
            return null;
        }
        return userSpaceMapper.selectCount(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getSpaceId, userSpace.getSpaceId()).isNotNull(UserSpace::getEventId));
    }

    private void validateNotSpace(Integer userId) {
        if (userSpaceMapper.selectCount(new LambdaQueryWrapper<UserSpace>().eq(UserSpace::getUserId, userId)) != 0) {
            throw new ServiceException(ResultStatusEnum.ALREADY_IN_SPACE);
        }
    }
}
