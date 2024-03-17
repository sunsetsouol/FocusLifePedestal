package com.qgStudio.pedestal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

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
public class FocusOnTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 模板名字
     */
    private String missionName;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 专注开始时间
     */
    @Pattern(regexp = "^[0-2][0-9]:[0-5][0-9]:[0-5][0-9]$", message = "时间格式错误")
    private String focusStartTime;

    /**
     * 专注持续时间（单位分）
     */
    @Min(value = 1, message = "专注时间必须大于0")
    private Integer focusDuration;

    /**
     * 备注
     */
    private String note;


}
