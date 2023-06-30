package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.dto.UserDTO;
import com.pxxy.enums.ProjectStatusEnum;
import com.pxxy.mapper.ProjectMapper;
import com.pxxy.pojo.*;
import com.pxxy.service.*;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import com.pxxy.vo.AddProjectVO;
import com.pxxy.vo.ProjectExcelVO;
import com.pxxy.vo.QueryProjectVO;
import com.pxxy.vo.UpdateProjectVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.pxxy.constant.SystemConstant.DEFAULT_PAGE_SIZE;

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

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public ResultResponse getAllProject(Integer pageNum) {
        //先拿到用户信息
        UserDTO user = UserHolder.getUser();
        Integer uId = user.getUId();
        //管理员特殊通道
        if (uId == 1){
                Page<Project> page = query().page(new Page<>(pageNum, DEFAULT_PAGE_SIZE));
                List<QueryProjectVO> queryProjectVOS = page.getRecords().stream().map(project -> {
                QueryProjectVO queryProjectVO = new QueryProjectVO();
                BeanUtil.copyProperties(project, queryProjectVO);
                Department department = departmentService.query().eq("dep_id", project.getDepId()).one();
                County county = countyService.query().eq("cou_id", project.getCouId()).one();
                Town town = townService.query().eq("town_id", project.getTownId()).one();
                ProjectCategory projectCategory = projectCategoryService.query().eq("prc_id", project.getPrcId()).one();
                IndustryField industryField = industryFieldService.query().eq("inf_id", project.getInfId()).one();
                if (department != null){
                    queryProjectVO.setDepartmentName(department.getDepName());
                }
                if (county != null || town != null){
                    assert county != null;
                    queryProjectVO.setArea(county.getCouName() + "-" + town.getTownName());
                }
                if (projectCategory != null){
                    queryProjectVO.setPrcName(projectCategory.getPrcName());
                }
                if (industryField != null){
                    queryProjectVO.setInfName(industryField.getInfName());
                }
                Integer proStatus = project.getProStatus();
                ProjectStatusEnum[] values = ProjectStatusEnum.values();
                for (ProjectStatusEnum projectStatusEnum:values) {
                    if (proStatus == projectStatusEnum.getStatus()){
                        queryProjectVO.setProStatusContent(projectStatusEnum.getStatusContent());
                    }
                }
                return queryProjectVO;
            }).collect(Collectors.toList());
            return ResultResponse.ok(queryProjectVOS, (int) page.getTotal());
        }

        // 非管理员通道
        User u = userService.query().eq("u_id", uId).one();
        Integer depId = u.getDepId();
        Integer couId = u.getCouId();
        List<Project> projectList = projectMapper.getAllProjectByUser(depId, couId, uId);
        // 记录总条数
        Integer total = projectList.size();
        // 用流去做分页查询
        List<Project> projects = projectList.stream().skip((pageNum - 1) * 10).limit(DEFAULT_PAGE_SIZE).collect(Collectors.toList());
        List<QueryProjectVO> queryProjectVOS = projects.stream().map(project -> {
            QueryProjectVO queryProjectVO = new QueryProjectVO();
            BeanUtil.copyProperties(project, queryProjectVO);
            Department department = departmentService.query().eq("dep_id", project.getDepId()).one();
            County county = countyService.query().eq("cou_id", project.getCouId()).one();
            Town town = townService.query().eq("town_id", project.getTownId()).one();
            ProjectCategory projectCategory = projectCategoryService.query().eq("prc_id", project.getPrcId()).one();
            IndustryField industryField = industryFieldService.query().eq("inf_id", project.getInfId()).one();
            if (department != null){
                queryProjectVO.setDepartmentName(department.getDepName());
            }
            if (county != null || town != null){
                assert county != null;
                queryProjectVO.setArea(county.getCouName() + "-" + town.getTownName());
            }
            if (projectCategory != null){
                queryProjectVO.setPrcName(projectCategory.getPrcName());
            }

            if (industryField != null){
                queryProjectVO.setInfName(industryField.getInfName());
            }
            Integer proStatus = project.getProStatus();
            ProjectStatusEnum[] values = ProjectStatusEnum.values();
            for (ProjectStatusEnum projectStatusEnum:values) {
                if (proStatus == projectStatusEnum.getStatus()){
                    queryProjectVO.setProStatusContent(projectStatusEnum.getStatusContent());
                }
            }
            return queryProjectVO;
        }).collect(Collectors.toList());
        return ResultResponse.ok(queryProjectVOS, total);
    }

    @Override
    public ResultResponse getVagueProject(Integer pageNum, String proName, Date beginTime, Date endTime, Integer couId, Integer townId, Integer prcId, Integer infId, Integer proStatus, Integer projectStage) {
        //先拿到用户信息
        UserDTO user = UserHolder.getUser();
        Integer uId = user.getUId();
        // 管理员特殊通道
        if (uId == 1){
            Page<Project> page = query()
                    .like(proName!=null,"pro_name",proName)
                    .eq(couId!=null,"cou_id",couId)
                    .eq(townId!=null,"town_id",townId)
                    .eq(prcId!=null,"prc_id",prcId)
                    .eq(infId!=null,"inf_id",infId)
                    .eq(proStatus!=null,"pro_status",proStatus)
                    .page(new Page<>(pageNum, DEFAULT_PAGE_SIZE));
            List<QueryProjectVO> queryProjectVOS = page.getRecords().stream().filter(project -> {
                if (beginTime != null) {
                    return project.getProDate().after(beginTime);
                }
                return true;
            }).filter(project -> {
                if (endTime != null) {
                    return project.getProDate().before(endTime);
                }
                return true;
            }).map(project -> {
                QueryProjectVO queryProjectVO = new QueryProjectVO();
                BeanUtil.copyProperties(project, queryProjectVO);
                Department department = departmentService.query().eq("dep_id", project.getDepId()).one();
                County county = countyService.query().eq("cou_id", project.getCouId()).one();
                Town town = townService.query().eq("town_id", project.getTownId()).one();
                ProjectCategory projectCategory = projectCategoryService.query().eq("prc_id", project.getPrcId()).one();
                IndustryField industryField = industryFieldService.query().eq("inf_id", project.getInfId()).one();
                if(department != null){
                    queryProjectVO.setDepartmentName(department.getDepName());
                }
                if (county != null || town != null){
                    assert county != null;
                    queryProjectVO.setArea(county.getCouName() + "-" + town.getTownName());
                }
                if (projectCategory != null){
                    queryProjectVO.setPrcName(projectCategory.getPrcName());
                }
                if (industryField != null){
                    queryProjectVO.setInfName(industryField.getInfName());
                }
                Integer ps = project.getProStatus();
                ProjectStatusEnum[] values = ProjectStatusEnum.values();
                for (ProjectStatusEnum projectStatusEnum : values) {
                    if (ps == projectStatusEnum.getStatus()) {
                        queryProjectVO.setProStatusContent(projectStatusEnum.getStatusContent());
                    }
                }
                return queryProjectVO;
            }).collect(Collectors.toList());
            //当前时间
            Date date = new Date();
            List<QueryProjectVO> projectVOS = this.calProjectStage(queryProjectVOS,projectStage,date);
            return ResultResponse.ok(projectVOS, (int) page.getTotal());
        }

        // 非管理员通道
        User u = userService.query().eq("u_id", uId).one();
        Integer uCouId = u.getCouId();
        Integer uDepId = u.getDepId();
        List<Project> projectList = projectMapper.getVagueProjectByUser(uDepId, uCouId, uId, proName, townId, prcId, infId, proStatus);
        List<QueryProjectVO> queryProjectVOS = projectList.stream().filter(project -> {
            if (beginTime != null) {
                return project.getProDate().after(beginTime);
            }
            return true;
        }).filter(project -> {
            if (endTime != null) {
                return project.getProDate().before(endTime);
            }
            return true;
        }).map(project -> {
            QueryProjectVO queryProjectVO = new QueryProjectVO();
            BeanUtil.copyProperties(project, queryProjectVO);
            Department department = departmentService.query().eq("dep_id", project.getDepId()).one();
            County county = countyService.query().eq("cou_id", project.getCouId()).one();
            Town town = townService.query().eq("town_id", project.getTownId()).one();
            ProjectCategory projectCategory = projectCategoryService.query().eq("prc_id", project.getPrcId()).one();
            IndustryField industryField = industryFieldService.query().eq("inf_id", project.getInfId()).one();
            if (department != null) {
                queryProjectVO.setDepartmentName(department.getDepName());
            }
            if (county != null || town != null) {
                assert county != null;
                queryProjectVO.setArea(county.getCouName() + "-" + town.getTownName());
            }
            if (projectCategory != null) {
                queryProjectVO.setPrcName(projectCategory.getPrcName());
            }
            if (industryField != null) {
                queryProjectVO.setInfName(industryField.getInfName());
            }
            Integer ps = project.getProStatus();
            ProjectStatusEnum[] values = ProjectStatusEnum.values();
            for (ProjectStatusEnum projectStatusEnum : values) {
                if (ps == projectStatusEnum.getStatus()) {
                    queryProjectVO.setProStatusContent(projectStatusEnum.getStatusContent());
                }
            }
            return queryProjectVO;
        }).collect(Collectors.toList());
        //当前时间
        Date date = new Date();

        List<QueryProjectVO> projectVOS = this.calProjectStage(queryProjectVOS,projectStage,date);
        int total = projectVOS.size();
        List<QueryProjectVO> projectVOList = projectVOS.stream().skip((pageNum - 1) * 10).limit(DEFAULT_PAGE_SIZE).collect(Collectors.toList());
        return ResultResponse.ok(projectVOList,total);
    }

    // 用于计算项目阶段
    private List<QueryProjectVO> calProjectStage(List<QueryProjectVO> queryProjectVOS,Integer projectStage,Date date){
        return queryProjectVOS.stream().filter(queryProjectVO -> {
            if (projectStage != null && queryProjectVO.getProDisStart() !=null) {
                switch (projectStage) {
                    //1:前期阶段 2:在建 3:已建成
                    case 1:
                        return queryProjectVO.getProDisStart().after(date);
                    case 2:
                        return queryProjectVO.getProDisStart().before(date) && queryProjectVO.getProDisComplete().after(date);
                    case 3:
                        return queryProjectVO.getProDisComplete().before(date);
                }
            }
            return true;
        }).collect(Collectors.toList());
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

    @Override
    public ResultResponse reportProject(Integer proId, Integer depId) {
        Project project = query().eq("pro_id", proId).one();
        //未上报状态
        ProjectStatusEnum report = ProjectStatusEnum.FAILURE_TO_REPORT;
        //待审核状态
        ProjectStatusEnum pendingReview = ProjectStatusEnum.PENDING_REVIEW;

        if (project.getProStatus() != report.getStatus()){
            //说明项目已上报
            return ResultResponse.fail("已上报项目不允许再上报！");
        }
        project.setDepId(depId);
        project.setProStatus(pendingReview.getStatus());
        updateById(project);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse importExcel(MultipartFile file) {
        try {
            List<ProjectExcelVO> projectExcelVOList = EasyExcel.read(file.getInputStream())
                    .head(ProjectExcelVO.class)
                    .sheet()
                    .doReadSync();
            return ResultResponse.ok(projectExcelVOList);
        } catch (IOException e) {
            return ResultResponse.fail("导入失败！");
        }
    }
}
