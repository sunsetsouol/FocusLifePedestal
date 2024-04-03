package com.qgStudio.pedestal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qgStudio.pedestal.entity.dto.AddFocusOnEventDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@NoArgsConstructor
@AllArgsConstructor
public class FocusOnEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * foucus事件的id
     */
    @NotNull(message = "专注事件id不能为空")
    private Integer focusId;

    /**
     * 真实开始时间
     */
    @Past(message = "真实开始时间必须是过去的时间")
    @NotNull(message = "真实开始时间不能为空")
    private LocalDateTime realStartTime;

    /**
     * 专注时间（单位：分)
     */
    @Min(value = 1, message = "专注时间必须大于0")
    @NotNull(message = "专注时间不能为空")
    private Integer focusTime;

    /**
     * 暂停次数
     */
    @Min(value = 0, message = "暂停次数必须大于等于0")
    private Integer suspendTime;

    /**
     * 是否完成
     */
    @Range(min = 0, max = 1, message = "是否完成只能是0或1")
    @NotNull(message = "是否完成不能为空")
    private Integer isCompleted;

    /**
     * 备注
     */
    private String note;


    public FocusOnEvent(AddFocusOnEventDTO addFocusOnEventDTO) {
        this.focusId = addFocusOnEventDTO.getFocusId();
        this.realStartTime = addFocusOnEventDTO.getRealStartTime();
        this.focusTime = addFocusOnEventDTO.getFocusTime();
        this.suspendTime = addFocusOnEventDTO.getSuspendTime();
        this.isCompleted = addFocusOnEventDTO.getIsCompleted();
        this.note = addFocusOnEventDTO.getNote();
    }
}
