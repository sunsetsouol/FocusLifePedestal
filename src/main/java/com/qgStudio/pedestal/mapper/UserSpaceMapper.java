package com.qgStudio.pedestal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgStudio.pedestal.entity.po.UserSpace;
import com.qgStudio.pedestal.entity.vo.space.SpaceUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2024-04-15
 */
@Mapper
public interface UserSpaceMapper extends BaseMapper<UserSpace> {

    void insertList(@Param("userSpaces") List<UserSpace> userSpaces);

    @Update("update user_space set deleted = NULL, deleted_time = #{now} where space_id = #{spaceId}")
    void logicDelete(Integer spaceId, LocalDateTime now);

    @Select("select u.name as username, u.head_image as headImage, u.email as email ,u.uid as uid ,ft.mission_name as eventName, " +
            "sp.focus_start_time as focusStartTime, sp.focus_times as focusTimes, sp.total_focus_time as totalFocusTime  " +
            "from user_space as sp " +
            "left join user as u on sp.user_id=u.id " +
            "left join focus_on_template as ft on ft.id=sp.event_id where sp.space_id = #{spaceId}")
    List<SpaceUserVO> selectSpaceUsers(Integer spaceId);
}
