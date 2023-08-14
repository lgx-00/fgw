package com.pxxy.mapper;

import com.pxxy.entity.dto.ProjectDTO;
import com.pxxy.entity.pojo.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@SuppressWarnings("UnusedReturnValue")
public interface ProjectMapper extends BaseMapper<Project> {
    List<Project> getAllProjectByUser(Integer depId, Integer couId, Integer uId);

    List<Project> getProjectByUser(Integer depId, Integer couId, Integer uId);

    List<Project> getVagueProjectByUser(ProjectDTO dto);

    Integer getDispatchingCount(ProjectDTO dto);

    List<Project> getAllDispatchProjectByUser(Integer depId, Integer couId, Integer uId);

    List<Project> getVagueDispatchProjectByUser(ProjectDTO projectDTO);

    @Update("update project " +
            "set pro_dis_total=#{proDisTotal}," +
                "pro_dis_year=#{proDisYear}," +
                "pro_dis_total_percent=#{proDisTotalPercent}," +
                "pro_dis_year_percent=#{proDisYearPercent}," +
                "pro_dis_progress=#{proDisProgress}," +
                "pro_last_dis=now()," +
                "pro_dis_start=ifnull(pro_dis_start,now()) " +
            "where pro_id=#{proId}")
    int updateDispatchData(Project project);
}
