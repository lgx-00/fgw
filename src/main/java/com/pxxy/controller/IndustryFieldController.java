package com.pxxy.controller;


import com.pxxy.service.IndustryFieldService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddIndustryFieldVO;
import com.pxxy.vo.UpdateIndustryFieldVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xrw
 * @since 2023-06-14
 */
@RestController
@Api(tags = "行业")
@RequestMapping("/industry")
public class IndustryFieldController {

    @Resource
    private IndustryFieldService IndustryFieldService;

    @PostMapping
    @ApiOperation("新增行业")
    public ResultResponse addIndustryField(@RequestBody AddIndustryFieldVO addIndustryFieldVO) {

        return IndustryFieldService.addIndustryField(addIndustryFieldVO);
    }

    @PutMapping
    @ApiOperation("修改行业")
    public ResultResponse updateIndustryField(@RequestBody UpdateIndustryFieldVO updateIndustryFieldVO) {
        return IndustryFieldService.updateIndustryField(updateIndustryFieldVO);
    }

    @GetMapping
    @ApiOperation("查询所有行业")
    public ResultResponse selectIndustryField(){
        return IndustryFieldService.selectIndustryField();
    }

    @DeleteMapping
    @ApiOperation("删除行业")
    public ResultResponse deleteIndustryField(@RequestParam Integer infId) {
        return IndustryFieldService.deleteIndustryField(infId);
    }

}

