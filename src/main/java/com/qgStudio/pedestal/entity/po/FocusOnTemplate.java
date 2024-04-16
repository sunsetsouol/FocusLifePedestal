package com.qgStudio.pedestal.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.qgStudio.pedestal.entity.dto.AddFocusOnTemplateDTO;
import com.qgStudio.pedestal.entity.dto.UpdateFocusOnTemplateDTO;
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
    private String focusStartTime;

    /**
     * 专注持续时间（单位分）
     */
    private Integer focusDuration;

    /**
     * 完成次数
     */
    private Integer completion;

    /**
     * 总时长（分钟）
     */
    private Integer completionTotalTime;

    /**
     * 备注
     */
    private String note;

    /**
     * 逻辑删除
     */
    @TableLogic(delval = "NULL")
    private Integer deleted;


    public FocusOnTemplate(AddFocusOnTemplateDTO focusOnTemplateDTO) {
        this.missionName = focusOnTemplateDTO.getMissionName();
        this.focusStartTime = focusOnTemplateDTO.getFocusStartTime();
        this.focusDuration = focusOnTemplateDTO.getFocusDuration();
        this.note = focusOnTemplateDTO.getNote();
    }

    public FocusOnTemplate(UpdateFocusOnTemplateDTO updateFocusOnTemplateDTO) {
        this.id = updateFocusOnTemplateDTO.getId();
        this.missionName = updateFocusOnTemplateDTO.getMissionName();
        this.focusStartTime = updateFocusOnTemplateDTO.getFocusStartTime();
        this.focusDuration = updateFocusOnTemplateDTO.getFocusDuration();
        this.note = updateFocusOnTemplateDTO.getNote();
    }
}
