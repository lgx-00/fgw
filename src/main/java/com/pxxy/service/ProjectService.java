package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.pxxy.dto.ProjectDTO;
import com.pxxy.pojo.Project;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddProjectVO;
import com.pxxy.vo.QueryProjectVO;
import com.pxxy.vo.UpdateProjectVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Validated
public interface ProjectService extends IService<Project> {

    ResultResponse<PageInfo<QueryProjectVO>> getAllProject(Integer pageNum);

    ResultResponse<PageInfo<QueryProjectVO>> getVagueProject(Integer pageNum, ProjectDTO projectDTO);

    ResultResponse<?> addProject(@Validated AddProjectVO addProjectVO);

    ResultResponse<?> updateProject(@Validated UpdateProjectVO updateProjectVO);

    ResultResponse<?> deleteProject(Integer proId);

    ResultResponse<?> reportProject(List<Integer> proIds, Integer depId);

    ResultResponse<?> importExcel(MultipartFile file);

    ResultResponse<?> accept(List<Integer> proIds);

    ResultResponse<?> reject(List<Integer> proIds);

    ResultResponse<?> markAsComplete(List<Integer> proIds);

    void updateDispatchStatus();

    ResultResponse<Integer> getDispatchingCount();

    ResultResponse<QueryProjectVO> getProject(Integer proId);
}
