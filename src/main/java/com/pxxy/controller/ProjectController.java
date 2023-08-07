package com.pxxy.controller;


import com.github.pagehelper.PageInfo;
import com.pxxy.entity.dto.ProjectDTO;
import com.pxxy.service.ProjectService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddProjectVO;
import com.pxxy.entity.vo.Page;
import com.pxxy.entity.vo.QueryProjectVO;
import com.pxxy.entity.vo.UpdateProjectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static com.pxxy.constant.ResponseMessage.FAIL_MSG;

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
@RequestMapping("/project")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @GetMapping("/storage/all")
    @ApiOperation("分页查询所有项目")
    public ResultResponse<PageInfo<QueryProjectVO>> getAllProject(@ModelAttribute @Validated Page page) {
        return projectService.getAllProject(page);
    }

    @GetMapping("/dispatch/all")
    @ApiOperation("分页查询所有调度库项目")
    public ResultResponse<PageInfo<QueryProjectVO>> getAllDispatchProject(@ModelAttribute @Validated Page page) {
        return projectService.getAllDispatchProject(page);
    }

    @GetMapping("/dispatch/vague")
    @ApiOperation("分页模糊查询调度库项目")
    public ResultResponse<PageInfo<QueryProjectVO>> getVagueDispatchProject(
            @ModelAttribute @Validated Page page,
            @Validated ProjectDTO projectDTO) {
        return projectService.getVagueDispatchProject(page, projectDTO);
    }

    @GetMapping("/vague")
    @ApiOperation("分页模糊查询项目")
    public ResultResponse<PageInfo<QueryProjectVO>> getVagueProject(
            @ModelAttribute @Validated Page page, @Validated ProjectDTO projectDTO) {
        return projectService.getVagueProject(page, projectDTO);
    }

    @GetMapping
    @ApiOperation("查询一个项目")
    public ResultResponse<QueryProjectVO> getProject(Integer proId) {
        return projectService.getProject(proId);
    }

    @PostMapping
    @ApiOperation("新增项目")
    public ResultResponse<?> addProject(@RequestBody @Validated AddProjectVO addProjectVO) {
        return projectService.addProject(addProjectVO);
    }

    @PutMapping
    @ApiOperation("修改项目")
    public ResultResponse<?> updateProject(@RequestBody UpdateProjectVO updateProjectVO) {
        return projectService.updateProject(updateProjectVO);
    }

    @PutMapping("/report")
    @ApiOperation("上报项目")
    public ResultResponse<?> reportProject(@RequestParam List<Integer> proIds, Integer depId) {
        return projectService.reportProject(proIds, depId);
    }

    @PutMapping("/report/more")
    @ApiOperation("上报项目到多个科室")
    public ResultResponse<?> reportProject(@RequestParam List<Integer> proIds, @RequestParam List<Integer> depIds) {
        return projectService.reportProject(proIds, depIds);
    }

    @PostMapping("/import")
    @ApiOperation("批量导入")
    public ResultResponse<?> importExcel(@RequestPart("file") MultipartFile file) {
        return projectService.importExcel(file);
    }

    @GetMapping("/template")
    @ApiOperation("下载批量导入模板")
    public ResponseEntity<InputStreamResource> template() {
        return projectService.downloadTemplate();
    }

    @PutMapping("/accept")
    @ApiOperation("批准项目")
    public ResultResponse<?> accept(@RequestParam @NotEmpty(message = FAIL_MSG) List<Integer> proIds) {
        return projectService.accept(proIds);
    }

    @PutMapping("/reject")
    @ApiOperation("驳回项目")
    public ResultResponse<?> reject(@RequestParam @NotEmpty(message = FAIL_MSG) List<Integer> proIds) {
        return projectService.reject(proIds);
    }

    @PutMapping("/mark")
    @ApiOperation("标记为已竣工")
    public ResultResponse<?> markAsComplete(@RequestParam @NotEmpty(message = FAIL_MSG) List<Integer> proIds) {
        return projectService.markAsComplete(proIds);
    }

    @GetMapping("/dispatching")
    @ApiOperation("获取待调度项目的数量")
    public ResultResponse<Integer> dispatching() {
        return projectService.getDispatchingCount();
    }

    @DeleteMapping
    @ApiOperation("删除项目")
    public ResultResponse<?> deleteProject(@RequestParam Integer proId) {
        return projectService.deleteProject(proId);
    }


}

