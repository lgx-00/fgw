package com.pxxy.controller;


import cn.hutool.core.io.resource.InputStreamResource;
import com.github.pagehelper.PageInfo;
import com.pxxy.service.DispatchService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */

@Validated
@CrossOrigin
@RestController
@Api(tags = "调度")
@RequestMapping("/project/dispatch")
public class DispatchController {

    @Resource
    private DispatchService dispatchService;

    @GetMapping("/{proId}/all")
    @ApiOperation("根据项目编号分页查询所有调度")
    public ResultResponse<PageInfo<QueryDispatchVO>> getAll(
            @Validated Page page,
            @PathVariable @ApiParam(value = "项目编号") Integer proId
    ) {
        return dispatchService.getAllDispatch(page, proId);
    }

    @GetMapping("/{proId}")
    @ApiOperation("根据调度编号查询一次调度记录")
    public ResultResponse<QueryDispatchVO> get(
            @PathVariable @ApiParam(value = "项目编号") Integer proId,
            @RequestParam @ApiParam(value = "调度编号") Integer disId
    ) {
        return dispatchService.get(disId, proId);
    }

    @ApiOperation("下载附件")
    @PostMapping("/{proId}/download")
    public ResponseEntity<InputStreamResource> appendix(
            @PathVariable @ApiParam(value = "项目编号") Integer proId,
            @RequestParam @ApiParam(value = "调度编号") Integer disId
    ) {
        return dispatchService.download(disId, proId);
    }

    @ApiOperation("新增调度")
    @PostMapping
    public ResultResponse<?> add(
            @RequestParam @Validated AddDispatchVO vo,
            @RequestParam(required = false) MultipartFile disAppendix
    ) {
        vo.setDisAppendix(disAppendix);
        return dispatchService.add(vo);

        //{"disTotal":300,"disPlanYear":3000,"disYear":300,"disTotalPercent":500,"disYearPercent":1000,"disProgress":"主要形象进度","stageId":1,"disInvest":300,"disApply":300,"disSituation":"工程进展状况","disToDep":"报送单位","disSource":"项目来源","disGuarantee":"包保责任领导","disFiled":"所属行业领域","disIssue":"存在问题","disRemark":"备注","proId":1360}
    }

    @ApiOperation("修改调度")
    @PutMapping(consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResultResponse<?> update(
            @RequestBody @Validated UpdateDispatchVO vo,
            @RequestPart(required = false) MultipartFile disAppendix
    ) {
        vo.setDisAppendix(disAppendix);
        return dispatchService.update(vo);
    }

    @PutMapping("/{proId}/lock")
    @ApiOperation("锁定调度")
    public ResultResponse<?> lock(
            @PathVariable @ApiParam(value = "项目编号") Integer proId,
            @RequestParam @ApiParam(value = "调度编号") Integer disId
    ) {
        return dispatchService.lock(disId, proId);
    }

    @PutMapping("/{proId}/unlock")
    @ApiOperation("解除锁定调度")
    public ResultResponse<?> unlock(
            @PathVariable @ApiParam(value = "项目编号") Integer proId,
            @RequestParam @ApiParam(value = "调度编号") Integer disId
    ) {
        return dispatchService.unlock(disId, proId);
    }

    @DeleteMapping("/{proId}")
    @ApiOperation("删除调度")
    public ResultResponse<?> del(
            @PathVariable @ApiParam(value = "项目编号") Integer proId,
            @RequestParam @ApiParam(value = "调度编号") Integer disId
    ) {
        return dispatchService.del(disId, proId);
    }

}

