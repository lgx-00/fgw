package com.pxxy.controller;


import com.github.pagehelper.PageInfo;
import com.pxxy.service.DispatchService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddDispatchVO;
import com.pxxy.entity.vo.Page;
import com.pxxy.entity.vo.QueryDispatchVO;
import com.pxxy.entity.vo.UpdateDispatchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import static com.pxxy.constant.ResponseMessage.*;
import static com.pxxy.utils.ResultResponse.fail;
import static com.pxxy.utils.ResultResponse.ok;

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
        return ok(dispatchService.getAllDispatch(page, proId));
    }

    @GetMapping("/{proId}")
    @ApiOperation("根据调度编号查询一次调度记录")
    public ResultResponse<QueryDispatchVO> get(
            @PathVariable @ApiParam(value = "项目编号") Integer proId,
            @RequestParam @ApiParam(value = "调度编号") Integer disId
    ) {
        return ok(dispatchService.get(disId, proId));
    }

    @ApiOperation("下载附件")
    @GetMapping("/{proId}/download")
    public ResponseEntity<InputStreamResource> appendix(
            @PathVariable @ApiParam(value = "项目编号") Integer proId,
            @RequestParam @ApiParam(value = "调度编号") Integer disId
    ) {
        return dispatchService.download(disId, proId);
    }

    @PostMapping
    @ApiOperation(value = "新增调度")
    public ResultResponse<?> add(@RequestBody @Validated AddDispatchVO vo) {
        return dispatchService.add(vo) ? ok() : fail(ADD_FAILED);
    }

    @PostMapping("upload")
    @ApiOperation(value = "直接上传附件")
    public ResultResponse<String> upload(@RequestPart MultipartFile disAppendix) {
        if (disAppendix == null || disAppendix.isEmpty()) {
            return fail(FAIL_MSG);
        }
        return ok(dispatchService.upload(disAppendix));
    }

    @PutMapping
    @ApiOperation(value = "修改调度")
    public ResultResponse<?> update(@RequestBody @Validated UpdateDispatchVO vo) {
        return dispatchService.update(vo) ? ok() : fail(UPDATE_FAILED);
    }

    @PutMapping("/{proId}/{disId}")
    @ApiOperation(value = "根据项目编号和调度编号上传附件")
    public ResultResponse<?> upload(
            @PathVariable @ApiParam("项目编号") Integer proId,
            @PathVariable @ApiParam("调度编号") Integer disId,
            @RequestPart MultipartFile disAppendix
    ) {
        if (disAppendix == null || disAppendix.isEmpty()) {
            return fail(FAIL_MSG);
        }
        return dispatchService.upload(disAppendix, proId, disId) ? ok() : fail(FAIL_MSG);
    }

    @PutMapping("/{proId}/lock")
    @ApiOperation("锁定调度")
    public ResultResponse<?> lock(
            @PathVariable @ApiParam(value = "项目编号") Integer proId,
            @RequestParam @ApiParam(value = "调度编号") Integer disId
    ) {
        return dispatchService.lock(disId, proId) ? ok() : fail(LOCK_FAILED);
    }

    @PutMapping("/{proId}/unlock")
    @ApiOperation("解除锁定调度")
    public ResultResponse<?> unlock(
            @PathVariable @ApiParam(value = "项目编号") Integer proId,
            @RequestParam @ApiParam(value = "调度编号") Integer disId
    ) {
        return dispatchService.unlock(disId, proId) ? ok() : fail(UNLOCK_FAILED);
    }

    @DeleteMapping("/{proId}")
    @ApiOperation("删除调度")
    public ResultResponse<?> del(
            @PathVariable @ApiParam(value = "项目编号") Integer proId,
            @RequestParam @ApiParam(value = "调度编号") Integer disId
    ) {
        return dispatchService.del(disId, proId) ? ok() : fail(DELETE_FAILED);
    }

}

