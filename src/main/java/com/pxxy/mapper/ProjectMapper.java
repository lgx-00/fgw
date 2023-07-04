package com.pxxy.mapper;

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

    List<Project> getVagueProjectByUser(Integer depId, Integer couId, Integer uId, String proName, Integer townId, Integer prcId, Integer infId, Integer proStatus);

    List<Project> getExamineProjectByUser(Integer depId, String proName, Integer townId, Integer prcId, Integer infId);

    List<Project> getDispatchProjectByUser(Integer depId, String proName, Integer townId, Integer prcId, Integer infId);
}
