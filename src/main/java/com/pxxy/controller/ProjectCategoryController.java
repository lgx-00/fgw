package com.pxxy.controller;


import com.pxxy.service.ProjectCategoryService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddProjectCategoryVO;
import com.pxxy.entity.vo.QueryProjectCategoryVO;
import com.pxxy.entity.vo.UpdateProjectCategoryVO;
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
@Api(tags = "类型")
@CrossOrigin
@RestController
@RequestMapping("/basedata/category")
public class ProjectCategoryController {
    
    @Resource
    private ProjectCategoryService projectCategoryService;
    
    
    @PostMapping
    @ApiOperation("新增项目类型")
    public ResultResponse<?> addProjectCategory(@RequestBody @Validated AddProjectCategoryVO addProjectCategoryVO) {
        return projectCategoryService.addProjectCategory(addProjectCategoryVO) ? ok() : fail(ADD_FAILED);
    }

    @PutMapping
    @ApiOperation("修改类型")
    public ResultResponse<?> updateProjectCategory(@RequestBody @Validated UpdateProjectCategoryVO updateProjectCategoryVO) {
        return projectCategoryService.updateProjectCategory(updateProjectCategoryVO) ? ok() : fail(UPDATE_FAILED);
    }

    @GetMapping
    @ApiOperation("查询所有项目类型")
    public ResultResponse<List<QueryProjectCategoryVO>> getAllProjectCategory() {
        return ok(projectCategoryService.getAllProjectCategory());
    }

    @DeleteMapping
    @ApiOperation("删除项目类型")
    public ResultResponse<?> deleteProjectCategory(@RequestParam Integer prcId) {
        return projectCategoryService.deleteProjectCategory(prcId) ? ok() : fail(DELETE_FAILED);
    }

}

