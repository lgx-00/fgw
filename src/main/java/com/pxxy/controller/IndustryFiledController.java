package com.pxxy.controller;


import com.pxxy.service.IndustryFiledService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddIndustryFiledVO;
import com.pxxy.vo.UpdateIndustryFiledVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xrw
 * @since 2023-06-14
 */
@RestController
@RequestMapping("/industry-filed")
@Api(tags = "行业领域管理")
public class IndustryFiledController {

    @Autowired
    private IndustryFiledService industryFiledService;

    @PostMapping("/add")
    @ApiOperation("新增行业")
    public ResultResponse addIndustryFiled(@RequestBody AddIndustryFiledVO addIndustryFiledVO){

        return industryFiledService.addIndustryFiled(addIndustryFiledVO);
    }

    @PostMapping("/update/{infId}")
    @ApiOperation("修改行业")
    public ResultResponse updateIndustryFiled(@PathVariable Integer infId,@RequestBody UpdateIndustryFiledVO updateIndustryFiledVO){
        return industryFiledService.updateIndustryFiled(infId,updateIndustryFiledVO);
    }

    @GetMapping("/selectAll")
    @ApiOperation("查询所有行业")
    public ResultResponse selectIndustryFiled(){
        return industryFiledService.selectIndustryFiled();
    }

    @PostMapping("/delete/{infId}")
    @ApiOperation("删除行业")
    public ResultResponse deleteIndustryFiled(@PathVariable Integer infId){
        return industryFiledService.deleteIndustryFiled(infId);
    }

}

