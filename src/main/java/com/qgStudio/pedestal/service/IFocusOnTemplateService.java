package com.qgStudio.pedestal.service;

import com.qgStudio.pedestal.entity.po.FocusOnTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qgStudio.pedestal.entity.vo.IntegerVo;
import com.qgStudio.pedestal.entity.vo.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2024-03-16
 */
public interface IFocusOnTemplateService extends IService<FocusOnTemplate> {

    /**
     * 添加专注模板
     * @param focusOnTemplate 专注模板
     * @return 结果
     */
    Result addTemplate(FocusOnTemplate focusOnTemplate);

    Result deleteTemplate(IntegerVo templateId);

    Result<List<FocusOnTemplate>> getTemplates(Integer userId);

    Result updateTemplate(FocusOnTemplate focusOnTemplate);
}
