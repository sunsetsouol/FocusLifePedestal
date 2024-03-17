package com.qgStudio.pedestal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@ApiModel(value="专注事件对象", description="专注事件")
public class FocusOnEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * foucus事件的id
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
     * 是否完成
     */
    @ApiModelProperty(value = "是否完成（1完成，0未完成）", required = true)
    @Range(min = 0, max = 1, message = "是否完成只能是0或1")
    @NotNull(message = "是否完成不能为空")
    private Integer isCompleted;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String note;


}
