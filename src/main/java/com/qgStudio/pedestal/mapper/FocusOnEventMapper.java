package com.qgStudio.pedestal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgStudio.pedestal.entity.po.FocusOnEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
@Mapper
public interface FocusOnEventMapper extends BaseMapper<FocusOnEvent> {

    @Update("update focus_on_event set is_completed = #{postStatus}, suspend_time = suspend_time + 1  where  focus_id = #{templateId} and is_completed = #{preStatus}")
    void suspend( @Param("templateId") Integer templateId ,@Param("preStatus") String preStatus,@Param("postStatus") String postStatus);

    @Update("update focus_on_event set is_completed = #{postStatus} where user_id = #{userId} and focus_id = #{templateId} and is_completed = #{preStatus}")
    void cancel(@Param("userId") Integer userId, @Param("templateId") Integer templateId ,@Param("preStatus") String preStatus,@Param("postStatus") String postStatus);
}
