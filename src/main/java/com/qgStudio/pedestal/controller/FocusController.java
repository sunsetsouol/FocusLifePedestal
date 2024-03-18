package com.qgStudio.pedestal.controller;

import com.qgStudio.pedestal.entity.po.FocusOnEvent;
import com.qgStudio.pedestal.entity.po.FocusOnTemplate;
import com.qgStudio.pedestal.entity.vo.IntegerVo;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.group.Update;
import com.qgStudio.pedestal.service.IFocusOnEventService;
import com.qgStudio.pedestal.service.IFocusOnTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

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
    private final IFocusOnEventService focusOnEventService;

    @Autowired
    public FocusController(IFocusOnTemplateService focusOnTemplateService, IFocusOnEventService focusOnEventService) {
        this.focusOnTemplateService = focusOnTemplateService;
        this.focusOnEventService = focusOnEventService;
    }

    @PostMapping("/addTemplate")
    @ApiOperation("添加专注模板")
    public Result addTemplate(@RequestBody @Validated @ApiParam("模板对象") FocusOnTemplate focusOnTemplate) {
        return focusOnTemplateService.addTemplate(focusOnTemplate);
    }

    @PostMapping("/deleteTemplate")
    @ApiOperation("删除专注模板")
    public Result deleteTemplate(@RequestBody @Validated @ApiParam("模板id") IntegerVo templateId) {
        return focusOnTemplateService.deleteTemplate(templateId);
    }

    @PostMapping("/updateTemplate")
    @ApiOperation("更新专注模板")
    public Result updateTemplate(@RequestBody @ApiParam("模板对象")@Validated(Update.class) FocusOnTemplate focusOnTemplate) {
        return focusOnTemplateService.updateTemplate(focusOnTemplate);
    }
    @PostMapping("/addFocusEvent")
    @ApiOperation("添加专注事件（专注一次）")
    public Result addFocusEvent(@RequestBody @Validated @ApiParam("事件对象") FocusOnEvent focusOnEvent) {
        return focusOnEventService.addEvent(focusOnEvent);
    }

    @GetMapping("/getTemplate")
    @ApiOperation("获取专注模板")
    public Result<List<FocusOnTemplate>> getTemplate() {
        return focusOnTemplateService.getTemplates();
    }

    @GetMapping("/getFocusEvent/{templateId}")
    @ApiOperation("获取模板的历史专注事件")
    public Result<List<FocusOnEvent>> getFocusEvent(@PathVariable("templateId") @ApiParam("模板id")@Min(value = 1,message = "模板id只能为正数") Integer templateId) {
        return focusOnEventService.getEvents(templateId);
    }
}
