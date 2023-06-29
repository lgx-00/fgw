package com.pxxy.controller;


import com.pxxy.service.ProjectCategoryService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.ProjectCategoryVO;
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
@RequestMapping("/project-category")
@Api(tags = "项目类型管理")
public class ProjectCategoryController {

    @Autowired
    private ProjectCategoryService projectCategoryService;

    @PostMapping("/add")
    @ApiOperation("新增项目类型")
    public ResultResponse addProjectCategory(@RequestBody ProjectCategoryVO projectCategoryVO){
        return projectCategoryService.addProjectCategory(projectCategoryVO);
    }

    @PostMapping("/update/{prcId}")
    @ApiOperation("修改项目类型")
    public ResultResponse updateProjectCategory(@PathVariable Integer prcId, @RequestBody ProjectCategoryVO projectCategoryVO){
        return projectCategoryService.updateProjectCategory(prcId,projectCategoryVO);
    }

    @GetMapping("/selectAll")
    @ApiOperation("查询所有项目类型")
    public ResultResponse selectProjectCategory(){
        return projectCategoryService.selectProjectCategory();
    }

    @PostMapping("/delete/{prcId}")
    @ApiOperation("删除项目类型")
    public ResultResponse deleteProjectCategory(@PathVariable Integer prcId){
        return projectCategoryService.deleteProjectCategory(prcId);
    }

}

