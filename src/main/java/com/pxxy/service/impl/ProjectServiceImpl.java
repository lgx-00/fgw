package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.dto.UserDTO;
import com.pxxy.mapper.ProjectMapper;
import com.pxxy.pojo.Project;
import com.pxxy.service.*;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import com.pxxy.vo.AddProjectVO;
import com.pxxy.vo.UpdateProjectVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Resource
    private DepartmentService departmentService;

    @Resource
    private CountyService countyService;

    @Resource
    private TownService townService;

    @Resource
    private ProjectCategoryService projectCategoryService;

    @Resource
    private IndustryFieldService industryFieldService;

    @Resource
    private UserService userService;

    @Override
    public ResultResponse getAllProject(Integer pageNum) {
        return null;
    }

    @Override
    public ResultResponse getVagueProject(Integer pageNum, String proName, Date beginTime, Date endTime, Integer couId, Integer townId, Integer prcId, Integer infId, Integer proStatus, Integer projectStage) {
        return null;
    }

    @Override
    public ResultResponse addProject(AddProjectVO addProjectVO) {
        Project project = new Project();
        BeanUtil.copyProperties(addProjectVO,project);
        UserDTO user = UserHolder.getUser();
        project.setUId(user.getUId());
        save(project);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse updateProject(UpdateProjectVO updateProjectVO) {
        Project project = query().eq("pro_id", updateProjectVO.getProId()).one();
        if (project == null){
            return ResultResponse.fail("非法操作！");
        }
        BeanUtil.copyProperties(updateProjectVO,project);
        updateById(project);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse deleteProject(Integer proId) {
        Project project = query().eq("pro_id", proId).one();
        removeById(proId);
        return ResultResponse.ok();
    }
}
