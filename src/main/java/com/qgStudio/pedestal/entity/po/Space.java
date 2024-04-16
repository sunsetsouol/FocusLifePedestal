package com.qgStudio.pedestal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Space implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 空间拥有者用户id
     */
    private Integer ownerUserId;

    /**
     * 空间描述
     */
    private String description;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 空间人数
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
