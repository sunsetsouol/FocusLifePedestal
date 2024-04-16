package com.qgStudio.pedestal.controller;

import com.qgStudio.pedestal.entity.bo.UserDetailsImpl;
import com.qgStudio.pedestal.entity.dto.AddFocusOnEventDTO;
import com.qgStudio.pedestal.entity.dto.AddFocusOnTemplateDTO;
import com.qgStudio.pedestal.entity.dto.LocalDateDTO;
import com.qgStudio.pedestal.entity.dto.UpdateFocusOnTemplateDTO;
import com.qgStudio.pedestal.entity.po.FocusOnEvent;
import com.qgStudio.pedestal.entity.po.FocusOnTemplate;
import com.qgStudio.pedestal.entity.dto.IntegerDTO;
import com.qgStudio.pedestal.entity.vo.Result;
import com.qgStudio.pedestal.service.IFocusOnEventService;
import com.qgStudio.pedestal.service.IFocusOnTemplateService;
import com.qgStudio.pedestal.service.IUserService;
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
    private final IUserService userService;

    @Autowired
    public FocusController(IFocusOnTemplateService focusOnTemplateService, IFocusOnEventService focusOnEventService, IUserService userService) {
        this.focusOnTemplateService = focusOnTemplateService;
        this.focusOnEventService = focusOnEventService;
        this.userService = userService;
    }

    @PostMapping("/addTemplate")
    @ApiOperation("添加专注模板")
    public Result addTemplate(@RequestBody @Validated @ApiParam("添加模板对象") AddFocusOnTemplateDTO addFocusOnTemplateDTO) {
        return focusOnTemplateService.addTemplate(addFocusOnTemplateDTO);
    }

    @PostMapping("/deleteTemplate")
    @ApiOperation("删除专注模板")
    public Result deleteTemplate(@RequestBody @Validated @ApiParam("模板id") IntegerDTO templateId) {
        return focusOnTemplateService.deleteTemplate(templateId);
    }

    @PostMapping("/updateTemplate")
    @ApiOperation("更新专注模板")
    public Result updateTemplate(@RequestBody @ApiParam("更新模板对象")@Validated UpdateFocusOnTemplateDTO updateFocusOnTemplateDTO) {
        return focusOnTemplateService.updateTemplate(updateFocusOnTemplateDTO);
    }
    @PostMapping("/addFocusEvent")
    @ApiOperation("添加专注事件（专注一次）")
    public Result addFocusEvent(@RequestBody @Validated @ApiParam("事件对象") AddFocusOnEventDTO addFocusOnEventDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return focusOnEventService.addEvent(id, addFocusOnEventDTO);
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

    @GetMapping("/getHistoryFocusTime")
    @ApiOperation("获取历史专注时间")
    public Result<Integer> getHistoryFocusTime() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return userService.getHistoryFocusTime(id);
    }

    @PostMapping("/getOneDayFocusTime")
    @ApiOperation("获取某天专注时间")
    public Result<Integer> getOneDayFocusTime(@ApiParam("日期")@RequestBody @Validated LocalDateDTO date) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return focusOnEventService.getOneDayFocusTime(id, date.getDate());
    }

    @GetMapping("/getTotalFocusTimeDetails")
    @ApiOperation("获取历史专注时间饼状图")
    public Result<List<FocusOnTemplate>> getHistoryFocusTimeDetails() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return focusOnEventService.getHistoryFocusTimeDetails(id);
    }

    @GetMapping("/getYearFocusTimeDetails")
    @ApiOperation("获取年度专注时间饼状图")
    public Result<List<FocusOnTemplate>> getYearFocusTimeDetails() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return focusOnEventService.getYearFocusTimeDetails(id);
    }

    @GetMapping("/getMonthFocusTimeDetails")
    @ApiOperation("获取月度专注时间饼状图")
    public Result<List<FocusOnTemplate>> getMonthFocusTimeDetails() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = userDetails.getUser().getId();
        return focusOnEventService.getMonthFocusTimeDetails(id);
    }
}
