package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.pojo.Project;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddProjectVO;
import com.pxxy.vo.UpdateProjectVO;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Validated
public interface ProjectService extends IService<Project> {

    ResultResponse getAllProject(Integer pageNum);

    ResultResponse getVagueProject(Integer pageNum, String proName, Date beginTime, Date endTime, Integer couId, Integer townId, Integer prcId, Integer infId, Integer proStatus, Integer projectStage);

    ResultResponse addProject(@Validated AddProjectVO addProjectVO);

    ResultResponse updateProject(@Validated UpdateProjectVO updateProjectVO);

    ResultResponse deleteProject(Integer proId);
}
