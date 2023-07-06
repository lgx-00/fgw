package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.pxxy.pojo.Project;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddProjectVO;
import com.pxxy.vo.QueryProjectVO;
import com.pxxy.vo.UpdateProjectVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

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

    ResultResponse<PageInfo<QueryProjectVO>> getVagueProject(Integer pageNum, String proName, Date beginTime, Date endTime, Integer couId, Integer townId, Integer prcId, Integer infId, Integer proStatus, Integer projectStage);

    ResultResponse<?> addProject(@Validated AddProjectVO addProjectVO);

    ResultResponse<?> updateProject(@Validated UpdateProjectVO updateProjectVO);

    ResultResponse<?> deleteProject(Integer proId);

    ResultResponse<?> reportProject(Integer proId, Integer depId);

    ResultResponse<?> importExcel(MultipartFile file);

    ResultResponse<PageInfo<QueryProjectVO>> getExamineProject(Integer pageNum, String proName, Date beginTime, Date endTime, Integer couId, Integer townId, Integer prcId, Integer infId, Integer projectStage);

    /**
     * @Description: 调度库
     * @Author: xrw
     * @Date: 2023/7/1 17:04
     * @Param: [pageNum, proName, beginTime, endTime, couId, townId, prcId, infId, projectStage]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse<PageInfo<QueryProjectVO>> getDispatchProject(Integer pageNum, String proName, Date beginTime, Date endTime, Integer couId, Integer townId, Integer prcId, Integer infId, Integer projectStage);
}
