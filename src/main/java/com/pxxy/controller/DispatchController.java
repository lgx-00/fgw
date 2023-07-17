package com.pxxy.controller;


import com.github.pagehelper.PageInfo;
import com.pxxy.service.DispatchService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddDispatchVO;
import com.pxxy.vo.Page;
import com.pxxy.vo.QueryDispatchVO;
import com.pxxy.vo.UpdateDispatchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

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
    @GetMapping("/{proId}/download")
    public ResponseEntity<InputStreamResource> appendix(
            @PathVariable @ApiParam(value = "项目编号") Integer proId,
            @RequestParam @ApiParam(value = "调度编号") Integer disId
    ) {
        return dispatchService.download(disId, proId);
    }

    private static final String notes =
            "使用 swagger 请求会失败，可以使用以下方式测试发送请求。\n" +
            "window.ondrop = e => {\n" +
            "  var s = '{\"disTotal\":300,\"disPlanYear\":3000,\"disYear\":300,\"disTotal" +
                    "Percent\":500,\"disYearPercent\":1000,\"disProgress\":\"主要形象进度" +
                    "\",\"stageId\":1,\"disInvest\":300,\"disApply\":300,\"disSituation" +
                    "\":\"工程进展状况\",\"disToDep\":\"报送单位\",\"disSource\":\"项目来源" +
                    "\",\"disGuarantee\":\"包保责任领导\",\"disFiled\":\"所属行业领域\",\"d" +
                    "isIssue\":\"存在问题\",\"disRemark\":\"备注\",\"proId\":1360}'\n" +
            "  var fd = new FormData();\n" +
            "  fd.append('vo', new Blob([s], {type:'application/json'}));\n" +
            "  fd.append('disAppendix', e.dataTransfer.files[0]);\n" +
            "  axios.post('/project/dispatch', fd,\n" +
            "             {headers:{'X-Token':'J691b4cpSA91QjQ0j30Uvyw6sHOdaOJ1'}})\n" +
            "    .then(a => console.log(a.data))\n" +
            "    .catch(e => console.error(e.response.data))\n" +
            "}";

    @PostMapping
    @ApiOperation(value = "新增调度", notes = notes)
    public ResultResponse<?> add(
            @RequestPart("vo") @Validated AddDispatchVO vo,
            @RequestPart(value = "disAppendix", required = false) MultipartFile disAppendix
    ) {
        vo.setDisAppendix(disAppendix);
        return dispatchService.add(vo);
    }

    @ApiOperation(value = "修改调度", notes = "使用 swagger 请求会失败，可以使用类似新增调度的方式测试发送请求。")
    @PutMapping(consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResultResponse<?> update(
            @RequestPart("vo") @Validated UpdateDispatchVO vo,
            @RequestPart(value = "disAppendix", required = false) MultipartFile disAppendix
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

