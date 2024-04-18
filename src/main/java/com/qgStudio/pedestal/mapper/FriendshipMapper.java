package com.qgStudio.pedestal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgStudio.pedestal.entity.po.Friendship;
import com.qgStudio.pedestal.entity.vo.friend.FriendApplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2024-04-16
 */
@Mapper
public interface FriendshipMapper extends BaseMapper<Friendship> {

    @Select("select f.id as id, u.name as name ,u.head_image as headImage, u.email as email, f.create_time as applyTime ,f.status as status" +
            " from friendship as f join user as u on f.to_id = u.id where f.to_id = #{userId} order by create_time desc")
    List<FriendApplyVO> selectApply(Integer userId);
}
