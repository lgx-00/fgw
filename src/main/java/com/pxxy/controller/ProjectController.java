package com.pxxy.controller;


import com.pxxy.service.ProjectService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddProjectVO;
import com.pxxy.vo.UpdateProjectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Validated
@RestController
@Api(tags = "项目")
@RequestMapping("/project")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @GetMapping("/all")
    @ApiOperation("分页查询所有项目")
    public ResultResponse getAllProject(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        return projectService.getAllProject(pageNum);
    }


    @GetMapping(value = "/vague")
    @ApiOperation(value = "分页模糊查询项目")
    public ResultResponse getVagueProject(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          String proName,
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date beginTime,
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endTime,
                                          Integer couId,
                                          Integer townId,
                                          Integer prcId,
                                          Integer infId,
                                          Integer proStatus,
                                          Integer projectStage) {
        return projectService.getVagueProject(pageNum, proName, beginTime, endTime, couId, townId, prcId, infId, proStatus, projectStage);
    }

    @PostMapping
    @ApiOperation(value = "新增项目")
    public ResultResponse addProject(@RequestBody @Validated AddProjectVO addProjectVO) {
        return projectService.addProject(addProjectVO);
    }

    @PutMapping
    @ApiOperation(value = "修改项目")
    public ResultResponse updateProject(@RequestBody @Validated UpdateProjectVO updateProjectVO) {
        return projectService.updateProject(updateProjectVO);
    }


    @DeleteMapping
    @ApiOperation(value = "删除项目")
    public ResultResponse deleteProject(@RequestParam Integer proId){
        return projectService.deleteProject(proId);
    }

    @PutMapping
    @ApiOperation(value = "上报项目")
    public ResultResponse reportProject(Integer proId,Integer depId) {
        return projectService.reportProject(proId,depId);
    }

}

