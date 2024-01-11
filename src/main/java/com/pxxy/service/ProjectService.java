package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.pxxy.entity.dto.ProjectDTO;
import com.pxxy.entity.pojo.Project;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddProjectVO;
import com.pxxy.entity.vo.Page;
import com.pxxy.entity.vo.QueryProjectVO;
import com.pxxy.entity.vo.UpdateProjectVO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
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

    PageInfo<QueryProjectVO> getAllProject(Page page);

    PageInfo<QueryProjectVO> getAllDispatchProject(Page page);

    PageInfo<QueryProjectVO> getVagueProject(Page page, ProjectDTO projectDTO);

    boolean addProject(AddProjectVO addProjectVO);

    boolean updateProject(UpdateProjectVO updateProjectVO);

    boolean deleteProject(Integer proId);

    boolean reportProject(List<Integer> proIds, Integer depId);

    boolean reportProject(List<Integer> proIds, List<Integer> depIds);

    Object importExcel(MultipartFile file);

    boolean accept(List<Integer> proIds);

    boolean reject(List<Integer> proIds);

    boolean markAsComplete(List<Integer> proIds);

    void updateDispatchStatus();

    Integer getDispatchingCount();

    QueryProjectVO getProject(Integer proId);

    void clearDispatch();

    PageInfo<QueryProjectVO> getVagueDispatchProject(Page page, ProjectDTO projectDTO);

    ResponseEntity<InputStreamResource> downloadTemplate();
}
