package com.qgStudio.pedestal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qgStudio.pedestal.entity.dto.AddFocusOnEventDTO;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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
     * focus事件的id
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
     * 是否完成（0：未完成，1：完成，2：进行中，3：暂停）
     * {@link StatusType}
     */
    private String isCompleted;

    /**
     * 备注（没做，不用展示）
     */
    private String note;

    /**
     * 空间id（null就不是空间内的专注）
     */
    private Long spaceId;

    public FocusOnEvent(AddFocusOnEventDTO addFocusOnEventDTO) {
        this.focusId = addFocusOnEventDTO.getFocusId();
        this.realStartTime = LocalDateTime.now();
        this.focusTime = addFocusOnEventDTO.getFocusTime();
        this.suspendTime = addFocusOnEventDTO.getSuspendTime();
        this.note = addFocusOnEventDTO.getNote();
    }

    @AllArgsConstructor
    @Getter
    public enum StatusType{
        NOT_COMPLETED( "未完成"),
        COMPLETED( "完成"),
        IN_PROGRESS( "进行中"),
        PAUSE("暂停");
        ;
        private final String status;

    }
}
