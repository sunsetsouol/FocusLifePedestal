package com.qgStudio.pedestal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.qgStudio.pedestal.group.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

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
@ApiModel(value="专注模板对象", description="可重复使用的专注任务")
public class FocusOnTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "模板id不能为空",groups = Update.class)
    private Integer id;

    /**
     * 模板名字
     */
    @ApiModelProperty(value = "专注任务名", required = true)
    private String missionName;

    /**
     * 用户id
     */
    @TableField(select = false)
    @Null(message = "用户id不需要传入",groups = Update.class)
    private Integer userId;

    /**
     * 专注开始时间
     */
    @Pattern(regexp = "^[0-2][0-9]:[0-5][0-9]:[0-5][0-9]$", message = "时间格式错误")
    @ApiModelProperty(value = "专注开始时间", required = true)
    @NotNull(message = "专注开始时间不能为空")
    private String focusStartTime;

    /**
     * 专注持续时间（单位分）
     */
    @ApiModelProperty(value = "专注持续时间（单位分）", required = true)
    @Min(value = 1, message = "专注持续时间必须大于0")
    @NotNull(message = "专注持续时间不能为空")
    private Integer focusDuration;

    /**
     * 完成次数
     */
    @Null(message = "完成次数不需要传入",groups = Update.class)
    private Integer completion;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String note;

    @TableLogic
    private Integer deleted;
}
