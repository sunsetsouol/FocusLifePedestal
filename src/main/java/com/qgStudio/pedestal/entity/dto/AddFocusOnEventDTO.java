package com.qgStudio.pedestal.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qgStudio.pedestal.entity.po.FocusOnEvent;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/4/2
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class AddFocusOnEventDTO {

    /**
     * focus事件的id
     */
    @ApiModelProperty(value = "专注事件模板的id", required = true)
    @NotNull(message = "专注事件id不能为空")
    private Integer focusId;

    /**
     * 真实开始时间
     */
    @ApiModelProperty(value = "真实开始时间", required = true)
    @Past(message = "真实开始时间必须是过去的时间")
    @NotNull(message = "真实开始时间不能为空")
    private LocalDateTime realStartTime;

    /**
     * 专注时间（单位：分)
     */
    @ApiModelProperty(value = "专注时间（单位：分)", required = true)
    @Min(value = 1, message = "专注时间必须大于0")
    @NotNull(message = "专注时间不能为空")
    private Integer focusTime;

    /**
     * 暂停次数
     */
    @ApiModelProperty(value = "暂停次数")
    @Min(value = 0, message = "暂停次数必须大于等于0")
    private Integer suspendTime;



    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String note;

    public AddFocusOnEventDTO(FocusOnEvent focusOnEvent) {
        this.focusId = focusOnEvent.getFocusId();
        this.realStartTime = focusOnEvent.getRealStartTime();
        this.focusTime = focusOnEvent.getFocusTime();
        this.suspendTime = focusOnEvent.getSuspendTime();
        this.note = focusOnEvent.getNote();
    }
}
