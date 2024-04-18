package com.qgStudio.pedestal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgStudio.pedestal.entity.po.FocusGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2024-04-15
 */
@Mapper
public interface FocusGroupMapper extends BaseMapper<FocusGroup> {

    @Update("update focus_group set member_number = member_number - 1 where id = #{groupId}")
    void decreaseMemberCount(Integer groupId);
}
