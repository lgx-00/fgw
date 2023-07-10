package com.pxxy.controller;


import com.github.pagehelper.PageInfo;
import com.pxxy.dto.ProjectDTO;
import com.pxxy.service.ProjectService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddProjectVO;
import com.pxxy.vo.QueryProjectVO;
import com.pxxy.vo.UpdateProjectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static com.pxxy.enums.ProjectStatusEnum.PENDING_REVIEW;
import static com.pxxy.enums.ProjectStatusEnum.TO_BE_SCHEDULED;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Validated
@CrossOrigin
@RestController
@Api(tags = "项目")
@RequestMapping("/project/storage")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @GetMapping("/all")
    @ApiOperation("分页查询所有项目")
    public ResultResponse<PageInfo<QueryProjectVO>> getAllProject(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        return projectService.getAllProject(pageNum);
    }

    @GetMapping("/vague")
    @ApiOperation("分页模糊查询项目")
    public ResultResponse<PageInfo<QueryProjectVO>> getVagueProject(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @Validated ProjectDTO projectDTO) {
        return projectService.getVagueProject(pageNum, projectDTO);
    }

    @GetMapping
    @ApiOperation("查询一个项目")
    public ResultResponse<QueryProjectVO> getProject(Integer proId) {
        return projectService.getProject(proId);
    }

    @GetMapping("/examine")
    @ApiOperation("分页查询用户要审核的项目")
    public ResultResponse<PageInfo<QueryProjectVO>> getExamineProject(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @Validated ProjectDTO projectDTO) {
        projectDTO.setProStatus(PENDING_REVIEW);
        return getVagueProject(pageNum, projectDTO);
    }

    @GetMapping("/dispatch")
    @ApiOperation("分页查询用户要调度的项目")
    public ResultResponse<PageInfo<QueryProjectVO>> getDispatchProject(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @Validated ProjectDTO projectDTO) {
        projectDTO.setProStatus(TO_BE_SCHEDULED);
        return getVagueProject(pageNum, projectDTO);
    }

    @PostMapping
    @ApiOperation("新增项目")
    public ResultResponse<?> addProject(@RequestBody @Validated AddProjectVO addProjectVO) {
        return projectService.addProject(addProjectVO);
    }

    @PutMapping
    @ApiOperation("修改项目")
    public ResultResponse<?> updateProject(@RequestBody @Validated UpdateProjectVO updateProjectVO) {
        return projectService.updateProject(updateProjectVO);
    }


    @DeleteMapping
    @ApiOperation("删除项目")
    public ResultResponse<?> deleteProject(@RequestParam Integer proId) {
        return projectService.deleteProject(proId);
    }

    @PutMapping("/report")
    @ApiOperation("上报项目")
    public ResultResponse<?> reportProject(@RequestParam List<Integer> proIds, Integer depId) {
        return projectService.reportProject(proIds, depId);
    }

    @PostMapping("/import")
    @ApiOperation("导入Excel")
    public ResultResponse<?> importExcel(@RequestPart("file") MultipartFile file) {
        return projectService.importExcel(file);
    }

    @PutMapping("/accept")
    @ApiOperation("批准项目")
    public ResultResponse<?> accept(@RequestParam @NotEmpty(message = "操作失败") List<Integer> proIds) {
        return projectService.accept(proIds);
    }

    @PutMapping("/reject")
    @ApiOperation("驳回项目")
    public ResultResponse<?> reject(@RequestParam @NotEmpty(message = "操作失败") List<Integer> proIds) {
        return projectService.reject(proIds);
    }

    @PutMapping("/mark")
    @ApiOperation("标记为已竣工")
    public ResultResponse<?> markAsComplete(@RequestParam @NotEmpty(message = "操作失败") List<Integer> proIds) {
        return projectService.markAsComplete(proIds);
    }

    @GetMapping("/dispatching")
    @ApiOperation("获取待调度项目的数量")
    public ResultResponse<Integer> dispatching() {
        return projectService.getDispatchingCount();
    }



}

