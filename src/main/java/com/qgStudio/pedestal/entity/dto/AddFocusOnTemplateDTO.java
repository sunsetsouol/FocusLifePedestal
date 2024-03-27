package com.qgStudio.pedestal.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="添加专注模板对象", description="添加可重复使用的专注任务")
public class AddFocusOnTemplateDTO {

    private static final long serialVersionUID = 1L;


    /**
     * 模板名字
     */
    @ApiModelProperty(value = "专注任务名", required = true)
    private String missionName;



    /**
     * 专注开始时间
     */
    @ApiModelProperty(value = "专注开始时间", required = true)
    @NotNull(message = "专注开始时间不能为空")
    private LocalDateTime focusStartTime;

    /**
     * 专注持续时间（单位分）
     */
    @ApiModelProperty(value = "专注持续时间（单位分）", required = true)
    @Min(value = 1, message = "专注持续时间必须大于0")
    @NotNull(message = "专注持续时间不能为空")
    private Integer focusDuration;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String note;

}
