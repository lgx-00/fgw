package com.pxxy.controller;

import com.pxxy.service.SummaryService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.SummaryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @Author: XRW
 * @CreateTime: 2023-07-01  13:54
 */
@Validated
@RestController
@Api(tags = "统计汇总")
@RequestMapping("/summary")
public class SummaryController {

    @Resource
    private SummaryService summaryService;

    @GetMapping(value = "/all")
    @ApiOperation(value = "统计汇总所有项目")
    public ResultResponse getSummary(Integer summaryType,
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date beginTime,
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endTime,
                                         Integer infId,
                                         Integer prcId) {
        return summaryService.getSummary(summaryType, beginTime, endTime, prcId, infId);
    }

    @PostMapping(value = "/exportExcel")
    @ApiOperation(value = "导出excel")
    public ResultResponse exportSummaryExcel(HttpServletResponse response,@RequestBody List<SummaryVO> summaryVOList) {
        return summaryService.exportSummaryExcel(response, summaryVOList);
    }


}