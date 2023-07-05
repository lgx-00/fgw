package com.pxxy.controller;


import com.pxxy.service.DispatchService;
import com.pxxy.utils.ResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RestController
@RequestMapping("/project/dispatch")
@Api(tags = "调度")
public class DispatchController {

    @Resource
    private DispatchService dispatchService;

    @GetMapping("/all/{proId}")
    @ApiOperation("根据项目id分页查询所有调度")
    public ResultResponse getAllDispatch(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,@PathVariable Integer proId) {
        return null;//dispatchService.getAllDispatch(pageNum,proId);
    }
}

