package com.pxxy.controller;

import com.github.pagehelper.PageInfo;
import com.pxxy.service.OperationService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.OperationVO;
import com.pxxy.entity.vo.Page;
import com.pxxy.entity.vo.QueryOperationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 操作记录控制器
 *
 * Create time: 2023/7/26 16:00
 *
 * @author xw
 * @version 1.0
 */
@CrossOrigin
@RestController
@Api(tags = "操作")
@RequestMapping("/sys/oper")
public class OperationController {

    @Resource
    private OperationService operService;

    @GetMapping("/all")
    @ApiOperation("分页查询所有操作记录")
    public ResultResponse<PageInfo<OperationVO>> all(@ModelAttribute @Validated Page page) {
        return operService.all(page);
    }

    @GetMapping("/vague")
    @ApiOperation("分页模糊查询所有操作记录")
    public ResultResponse<PageInfo<OperationVO>> vague(
            @ModelAttribute @Validated Page page,
            @ModelAttribute @Validated QueryOperationVO queryOperationVO
    ) {
        return operService.vague(page, queryOperationVO);
    }

}
