package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.pxxy.advice.annotations.Cached;
import com.pxxy.dto.ProjectDTO;
import com.pxxy.pojo.Project;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddProjectVO;
import com.pxxy.vo.Page;
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
public interface ProjectService extends IService<Project> {

    ResultResponse<PageInfo<QueryProjectVO>> getAllProject(Page page);

    ResultResponse<PageInfo<QueryProjectVO>> getAllDispatchProject(Page page);

    ResultResponse<PageInfo<QueryProjectVO>> getVagueProject(Page page, ProjectDTO projectDTO);

    ResultResponse<?> addProject(AddProjectVO addProjectVO);

    ResultResponse<?> updateProject(UpdateProjectVO updateProjectVO);

    ResultResponse<?> deleteProject(Integer proId);

    ResultResponse<?> reportProject(List<Integer> proIds, Integer depId);

    ResultResponse<?> reportProject(List<Integer> proIds, List<Integer> depIds);

    ResultResponse<?> importExcel(MultipartFile file);

    ResultResponse<?> accept(List<Integer> proIds);

    ResultResponse<?> reject(List<Integer> proIds);

    ResultResponse<?> markAsComplete(List<Integer> proIds);

    void updateDispatchStatus();

    ResultResponse<Integer> getDispatchingCount();

    ResultResponse<QueryProjectVO> getProject(Integer proId);

    void clearDispatch();
}
