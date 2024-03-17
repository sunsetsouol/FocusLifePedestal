package com.qgStudio.pedestal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgStudio.pedestal.entity.po.FocusOnTemplate;
import org.apache.ibatis.annotations.Mapper;
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
public interface FocusOnTemplateMapper extends BaseMapper<FocusOnTemplate> {

    @Update("update focus_on_template set completion = completion + 1 where id = #{id} and user_id = #{userId}")
    int increase(Integer id, Integer userId);
}
