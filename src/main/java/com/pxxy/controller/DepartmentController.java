package com.pxxy.controller;


import com.pxxy.service.DepartmentService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddDepartmentVO;
import com.pxxy.entity.vo.QueryDepartmentVO;
import com.pxxy.entity.vo.UpdateDepartmentVO;
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
 * @author hs
 * @since 2023-06-14
 */
@Validated
@CrossOrigin
@RestController
@Api(tags = "科室")
@RequestMapping("/basedata/department")
public class DepartmentController {
    @Resource
    private DepartmentService departmentService;

    @PostMapping
    @ApiOperation("新增科室")
    public ResultResponse<?> addDepartment(@RequestBody @Validated AddDepartmentVO addDepartmentVO) {
        return departmentService.addDepartment(addDepartmentVO) ? ok() : fail(ADD_FAILED);
    }

    @PutMapping
    @ApiOperation("修改科室")
    public ResultResponse<?> updateDepartment(@RequestBody @Validated UpdateDepartmentVO updateDepartmentVO) {
        return departmentService.updateDepartment(updateDepartmentVO) ? ok() : fail(UPDATE_FAILED);
    }

    @GetMapping
    @ApiOperation("查询所有科室")
    public ResultResponse<List<QueryDepartmentVO>> getAllDepartment(){
        return ok(departmentService.getAllDepartment());
    }

    @DeleteMapping
    @ApiOperation("删除科室")
    public ResultResponse<?> deleteDepartment(@RequestParam Integer depId) {
        return departmentService.deleteDepartment(depId) ? ok() : fail(DELETE_FAILED);
    }

}

