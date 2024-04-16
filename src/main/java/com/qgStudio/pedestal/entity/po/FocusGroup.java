package com.qgStudio.pedestal.entity.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2024-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class FocusGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 群组名
     */
    private String name;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 群组描述
     */
    private String description;

    /**
     * 成员人数
     */
    private Integer memberNumber;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

    /**
     * 逻辑删除
     */
    @TableLogic(delval = "NULL")
    private Integer deleted;


}
