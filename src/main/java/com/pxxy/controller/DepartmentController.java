package com.pxxy.controller;


import com.pxxy.service.DepartmentService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddDepartmentVO;
import com.pxxy.vo.UpdateDepartmentVO;
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
@Api(tags = "科室")
@RequestMapping("/department")
public class DepartmentController {
    @Resource
    private DepartmentService departmentService;

    @PostMapping
    @ApiOperation("新增科室")
    public ResultResponse addDepartment(@RequestBody @Validated AddDepartmentVO addDepartmentVO) {
        return departmentService.addDepartment(addDepartmentVO);
    }

    @PutMapping
    @ApiOperation("修改科室")
    public ResultResponse updateDepartment(@RequestBody @Validated UpdateDepartmentVO updateDepartmentVO) {
        return departmentService.updateDepartment(updateDepartmentVO);
    }

    @GetMapping
    @ApiOperation("查询所有科室")
    public ResultResponse getAllDepartment(){
        return departmentService.getAllDepartment();
    }

    @DeleteMapping
    @ApiOperation("删除科室")
    public ResultResponse deleteDepartment(@RequestParam Integer depId) {
        return departmentService.deleteDepartment(depId);
    }

}

