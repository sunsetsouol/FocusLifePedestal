package com.qgStudio.pedestal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FocusOnEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * foucus事件的id
     */
    private Integer focusId;

    /**
     * 真实开始时间
     */
    private LocalDateTime realStartTime;

    /**
     * 专注时间（单位：分)
     */
    private Integer focusTime;

    /**
     * 暂停次数
     */
    private Integer suspendTime;

    /**
     * 是否完成
     */
    private Integer isCompleted;

    /**
     * 备注
     */
    private String note;


}
