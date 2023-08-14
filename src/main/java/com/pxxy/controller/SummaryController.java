package com.pxxy.controller;

import com.pxxy.service.SummaryService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.DashboardVO;
import com.pxxy.entity.vo.SummaryDetailsVO;
import com.pxxy.entity.vo.SummaryVO;
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
 * @author XRW
 * @CreateTime: 2023-07-01  13:54
 */
@Validated
@CrossOrigin
@RestController
@Api(tags = "统计")
@RequestMapping("/summary")
public class SummaryController {

    @Resource
    private SummaryService summaryService;

    @GetMapping("/all")
    @ApiOperation("统计汇总所有项目")
    public ResultResponse<List<SummaryVO>> getSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date beginTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endTime,
            Integer infId,
            Integer prcId) {
        return summaryService.getSummary(beginTime, endTime, prcId, infId);
    }

    @GetMapping("/details")
    @ApiOperation("统计汇总详情")
    public ResultResponse<List<SummaryDetailsVO>> detailsSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date beginTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endTime,
            Integer infId,
            Integer prcId) {
        return summaryService.detailsSummary(beginTime, endTime, prcId, infId);
    }

    @PostMapping("/exportExcel")
    @ApiOperation("导出excel")
    public ResultResponse<?> exportSummaryExcel(HttpServletResponse response, @RequestBody List<SummaryVO> summaryVOList) {
        return summaryService.exportSummaryExcel(response, summaryVOList);
    }

    @GetMapping("/dashboard")
    @ApiOperation("获取仪表板的数据")
    public ResultResponse<DashboardVO> dashboard() {
        return summaryService.dashboard();
    }

}