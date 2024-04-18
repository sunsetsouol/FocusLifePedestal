package com.qgStudio.pedestal.service.space;

import com.qgStudio.pedestal.entity.dto.space.SpaceCreateDTO;
import com.qgStudio.pedestal.entity.dto.space.SpaceRemoveMemberDTO;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.entity.vo.space.SpaceInviteVO;
import com.qgStudio.pedestal.entity.vo.space.SpaceUserVO;
import com.qgStudio.pedestal.entity.vo.space.SpaceVO;

import java.util.List;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/16
 */
public interface SpaceService {
    SpaceVO createSpace(Integer userId, SpaceCreateDTO spaceCreateDTO);

    Result<Boolean> deleteSpace(Integer userId, Integer spaceId);

//    Result<List<SpaceVO>> getSpace(Integer userId);

    Result<List<SpaceUserVO>> getSpaceMembers(Integer spaceId);

    Result<Boolean> quitSpace(Integer userId, Integer spaceId);

    SpaceInviteVO invite(Integer userId, Long spaceId);

    Result<Boolean> acceptInvite(Integer userId, Integer inviteId);

    Result<Boolean> removeSpace(SpaceRemoveMemberDTO spaceRemoveMemberDTO, Integer userId);
}
