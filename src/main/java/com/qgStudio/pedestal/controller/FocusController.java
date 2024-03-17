package com.qgStudio.pedestal.controller;

import com.qgStudio.pedestal.entity.po.FocusOnTemplate;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.service.IFocusOnTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/16
 */
@RestController
@RequestMapping("/focus")
@Api(tags = "专注接口")
public class FocusController {

    private final IFocusOnTemplateService focusOnTemplateService;

    @Autowired
    public FocusController(IFocusOnTemplateService focusOnTemplateService) {
        this.focusOnTemplateService = focusOnTemplateService;
    }

    @PostMapping("/addTemplate")
    @ApiOperation("添加专注模板")
    public Result addTemplate(@RequestBody @Validated FocusOnTemplate focusOnTemplate) {
        return focusOnTemplateService.addTemplate(focusOnTemplate);
    }
}
