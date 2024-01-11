package com.pxxy.controller;


import com.pxxy.entity.pojo.IndustryField;
import com.pxxy.service.IndustryFieldService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddIndustryFieldVO;
import com.pxxy.entity.vo.UpdateIndustryFieldVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.pxxy.constant.ResponseMessage.*;
import static com.pxxy.utils.ResultResponse.fail;
import static com.pxxy.utils.ResultResponse.ok;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xrw
 * @since 2023-06-14
 */
@Validated
@CrossOrigin
@RestController
@Api(tags = "行业")
@RequestMapping("/basedata/industry")
public class IndustryFieldController {

    @Resource
    private IndustryFieldService IndustryFieldService;

    @PostMapping
    @ApiOperation("新增行业")
    public ResultResponse<?> addIndustryField(@RequestBody @Validated AddIndustryFieldVO addIndustryFieldVO) {
        return IndustryFieldService.addIndustryField(addIndustryFieldVO) ? ok() : fail(ADD_FAILED);
    }

    @PutMapping
    @ApiOperation("修改行业")
    public ResultResponse<?> updateIndustryField(@RequestBody @Validated UpdateIndustryFieldVO updateIndustryFieldVO) {
        return IndustryFieldService.updateIndustryField(updateIndustryFieldVO) ? ok() : fail(UPDATE_FAILED);
    }

    @GetMapping
    @ApiOperation("查询所有行业")
    public ResultResponse<List<IndustryField>> selectIndustryField(){
        return ok(IndustryFieldService.getAll());
    }

    @DeleteMapping
    @ApiOperation("删除行业")
    public ResultResponse<?> deleteIndustryField(@RequestParam Integer infId) {
        return IndustryFieldService.deleteIndustryField(infId) ? ok() : fail(DELETE_FAILED);
    }

}

