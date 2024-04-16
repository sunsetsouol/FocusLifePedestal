package com.qgStudio.pedestal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qgStudio.pedestal.entity.dto.AddFocusOnEventDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    /**
     * 空间id（null就不是空间内的专注）
     */
    private Integer spaceId;

    public FocusOnEvent(AddFocusOnEventDTO addFocusOnEventDTO) {
        this.focusId = addFocusOnEventDTO.getFocusId();
        this.realStartTime = LocalDateTime.now();
        this.focusTime = addFocusOnEventDTO.getFocusTime();
        this.suspendTime = addFocusOnEventDTO.getSuspendTime();
        this.isCompleted = addFocusOnEventDTO.getIsCompleted();
        this.note = addFocusOnEventDTO.getNote();
    }
}
