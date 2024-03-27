package com.qgStudio.pedestal.controller;

import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.dto.AddFocusOnTemplateDTO;
import com.qgStudio.pedestal.entity.dto.UpdateFocusOnTemplateDTO;
import com.qgStudio.pedestal.entity.po.FocusOnEvent;
import com.qgStudio.pedestal.entity.po.FocusOnTemplate;
import com.qgStudio.pedestal.entity.vo.IntegerVo;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.service.IFocusOnEventService;
import com.qgStudio.pedestal.service.IFocusOnTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result addTemplate(@RequestBody @Validated @ApiParam("添加模板对象") AddFocusOnTemplateDTO addFocusOnTemplateDTO) {
        return focusOnTemplateService.addTemplate(addFocusOnTemplateDTO);
    }

    @PostMapping("/deleteTemplate")
    @ApiOperation("删除专注模板")
    public Result deleteTemplate(@RequestBody @Validated @ApiParam("模板id") IntegerVo templateId) {
        return focusOnTemplateService.deleteTemplate(templateId);
    }

    @PostMapping("/updateTemplate")
    @ApiOperation("更新专注模板")
    public Result updateTemplate(@RequestBody @ApiParam("更新模板对象")@Validated UpdateFocusOnTemplateDTO updateFocusOnTemplateDTO) {
        return focusOnTemplateService.updateTemplate(updateFocusOnTemplateDTO);
    }
    @PostMapping("/addFocusEvent")
    @ApiOperation("添加专注事件（专注一次）")
    public Result addFocusEvent(@RequestBody @Validated @ApiParam("事件对象") FocusOnEvent focusOnEvent) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return focusOnEventService.addEvent(id, focusOnEvent);
    }

    @GetMapping("/getTemplate")
    @ApiOperation("获取专注模板")
    public Result<List<FocusOnTemplate>> getTemplate() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return focusOnTemplateService.getTemplates(id);
    }

    @GetMapping("/getFocusEvent/{templateId}")
    @ApiOperation("获取模板的历史专注事件")
    public Result<List<FocusOnEvent>> getFocusEvent(@PathVariable("templateId") @ApiParam("模板id")@Min(value = 1,message = "模板id只能为正数") Integer templateId) {
        return focusOnEventService.getEvents(templateId);
    }
}
