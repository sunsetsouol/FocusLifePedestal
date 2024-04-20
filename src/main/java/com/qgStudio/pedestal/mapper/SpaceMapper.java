package com.qgStudio.pedestal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgStudio.pedestal.entity.po.Space;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2024-04-15
 */
@Mapper
public interface SpaceMapper extends BaseMapper<Space> {

    @Update("update space set deleted = null ,deleted_time = #{now} where id = #{spaceId} and owner_user_id = #{userId}")
    void logicDelete(Integer spaceId, Integer userId, LocalDateTime now);

    @Update("update space set member_number = member_number + 1 where id = #{spaceId} and deleted is not null")
    void increase(Long spaceId);

    @Update("update space set member_number = member_number - 1 where id = #{spaceId} and deleted is not null")
    void decrease(Integer spaceId);
}
