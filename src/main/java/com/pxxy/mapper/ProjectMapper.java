package com.pxxy.mapper;

import com.pxxy.dto.ProjectDTO;
import com.pxxy.pojo.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
public interface ProjectMapper extends BaseMapper<Project> {
    List<Project> getAllProjectByUser(Integer depId, Integer couId, Integer uId);

    List<Project> getProjectByUser(Integer depId, Integer couId, Integer uId);

    List<Project> getVagueProjectByUser(ProjectDTO dto);

    Integer getDispatchingCount(ProjectDTO dto);

    List<Project> getAllDispatchProjectByUser(Integer depId, Integer couId, Integer uId);

}
