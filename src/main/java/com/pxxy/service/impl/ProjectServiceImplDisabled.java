//package com.pxxy.service.impl;
//
//import cn.hutool.core.bean.BeanUtil;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.pxxy.dto.UserDTO;
//import com.pxxy.enums.ProjectStatusEnum;
//import com.pxxy.mapper.ProjectMapper;
//import com.pxxy.pojo.*;
//import com.pxxy.service.*;
//import com.pxxy.utils.ResultResponse;
//import com.pxxy.utils.UserHolder;
//import com.pxxy.vo.AddProjectVO;
//import com.pxxy.vo.QueryProjectVO;
//import com.pxxy.vo.UpdateProjectVO;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static com.pxxy.constant.SystemConstant.DEFAULT_PAGE_SIZE;
//import static com.pxxy.constant.SystemConstant.DELETED_STATUS;
//
///**
// * <p>
// *  服务实现类
// * </p>
// *
// * @author hs
// * @since 2023-06-14
// */
//@Service
//public class ProjectServiceImplDisabled extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
//
//    @Resource
//    private DepartmentService departmentService;
//
//    @Resource
//    private CountyService countyService;
//
//    @Resource
//    private TownService townService;
//
//    @Resource
//    private ProjectCategoryService projectCategoryService;
//
//    @Resource
//    private IndustryFieldService industryFieldService;
//
//    @Resource
//    private UserService userService;
//
//
//    @Override
//    public ResultResponse getAllProject(Integer pageNum) {
//        // 先拿到用户
//        UserDTO userDTO = UserHolder.getUser();
//        Integer userId = userDTO.getUId();
//
//        if (userId == 1){
//            // 管理员特殊通道
//            // 分页查询
//            Page<Project> page = query().ne("pro_status",DELETED_STATUS).page(new Page<>(pageNum, DEFAULT_PAGE_SIZE));
//            List<QueryProjectVO> queryProjectVOS = page.getRecords().stream().map(project -> {
//                QueryProjectVO queryProjectVO = new QueryProjectVO();
//                BeanUtil.copyProperties(project, queryProjectVO);
//                Department department = departmentService.query().eq("dep_id", project.getDepId()).one();
//                County county = countyService.query().eq("cou_id", project.getCouId()).one();
//                Town town = townService.query().eq("town_id", project.getTownId()).one();
//                ProjectCategory projectCategory = projectCategoryService.query().eq("prc_id", project.getPrcId()).one();
//                IndustryField industryField = industryFieldService.query().eq("inf_id", project.getInfId()).one();
//                if (department != null){
//                    queryProjectVO.setDepartmentName(department.getDepName());
//                }
//                queryProjectVO.setArea(county.getCouName() + "-" + town.getTownName());
//                queryProjectVO.setPrcName(projectCategory.getPrcName());
//                queryProjectVO.setInfName(industryField.getInfName());
//                Integer proStatus = project.getProStatus();
//                ProjectStatusEnum[] values = ProjectStatusEnum.values();
//                for (ProjectStatusEnum projectStatusEnum:values) {
//                    if (proStatus == projectStatusEnum.getStatus()){
//                        queryProjectVO.setProStatusContent(projectStatusEnum.getStatusContent());
//                    }
//                }
//                return queryProjectVO;
//            }).collect(Collectors.toList());
//            return ResultResponse.ok(queryProjectVOS);
//        }
//
//
//        // 非管理员通道
//        User user = userService.query().eq("u_id", userId).one();
//
//        // 合并后集合
//        List<Project> projects = new ArrayList<>();
//
//        Integer userCouId = user.getCouId();
//        Integer userDepId = user.getDepId();
//        Page<Project> page = query()
//                .ne("pro_status",DELETED_STATUS)
//                .eq(userCouId!=null,"cou_id",userCouId)
//                .eq(userDepId!=null,"cou_id",userDepId)
//                .eq("u_id",userId)
//                .page(new Page<>(pageNum, DEFAULT_PAGE_SIZE));
//
//
//
//
//
//
//        List<QueryProjectVO> queryProjectVOS = page.getRecords().stream().map(project -> {
//            QueryProjectVO queryProjectVO = new QueryProjectVO();
//            BeanUtil.copyProperties(project, queryProjectVO);
//            Department department = departmentService.query().eq("dep_id", project.getDepId()).one();
//            County county = countyService.query().eq("cou_id", project.getCouId()).one();
//            Town town = townService.query().eq("town_id", project.getTownId()).one();
//            ProjectCategory projectCategory = projectCategoryService.query().eq("prc_id", project.getPrcId()).one();
//            IndustryField industryField = industryFieldService.query().eq("inf_id", project.getInfId()).one();
//            if (department != null){
//                queryProjectVO.setDepartmentName(department.getDepName());
//            }
//            queryProjectVO.setArea(county.getCouName() + "-" + town.getTownName());
//            queryProjectVO.setPrcName(projectCategory.getPrcName());
//            queryProjectVO.setInfName(industryField.getInfName());
//            Integer proStatus = project.getProStatus();
//            ProjectStatusEnum[] values = ProjectStatusEnum.values();
//            for (ProjectStatusEnum projectStatusEnum:values) {
//                if (proStatus == projectStatusEnum.getStatus()){
//                    queryProjectVO.setProStatusContent(projectStatusEnum.getStatusContent());
//                }
//            }
//            return queryProjectVO;
//        }).collect(Collectors.toList());
//        return ResultResponse.ok(queryProjectVOS);
//    }
//
//    @Override
//    public ResultResponse getVagueProject(Integer pageNum, String proName,
//                                          Date beginTime, Date endTime,
//                                          Integer couId, Integer townId,
//                                          Integer prcId, Integer infId,
//                                          Integer proStatus, Integer projectStage) {
//
//        // 先拿到用户
//        UserDTO userDTO = UserHolder.getUser();
//        Integer userId = userDTO.getUId();
//        if (userId == 1){
//            // 管理员特殊通道
//            Page<Project> page = query()
//                    .like(proName!=null,"pro_name",proName)
//                    .eq(couId!=null,"cou_id",couId)
//                    .eq(townId!=null,"town_id",townId)
//                    .eq(prcId!=null,"prc_id",prcId)
//                    .eq(infId!=null,"inf_id",infId)
//                    .eq(proStatus!=null,"pro_status",proStatus)
//                    .page(new Page<>(pageNum, DEFAULT_PAGE_SIZE));
//            List<QueryProjectVO> queryProjectVOS = page.getRecords().stream().filter(project -> {
//                if (beginTime != null) {
//                    return project.getProDate().after(beginTime);
//                }
//                return true;
//            }).collect(Collectors.toList()).stream().filter(project -> {
//                if (endTime != null) {
//                    return project.getProDate().before(endTime);
//                }
//                return true;
//            }).collect(Collectors.toList()).stream().map(project -> {
//                QueryProjectVO queryProjectVO = new QueryProjectVO();
//                BeanUtil.copyProperties(project, queryProjectVO);
//                Department department = departmentService.query().eq("dep_id", project.getDepId()).one();
//                County county = countyService.query().eq("cou_id", project.getCouId()).one();
//                Town town = townService.query().eq("town_id", project.getTownId()).one();
//                ProjectCategory projectCategory = projectCategoryService.query().eq("prc_id", project.getPrcId()).one();
//                IndustryField industryField = industryFieldService.query().eq("inf_id", project.getInfId()).one();
//                queryProjectVO.setDepartmentName(department.getDepName());
//                queryProjectVO.setArea(county.getCouName() + "-" + town.getTownName());
//                queryProjectVO.setPrcName(projectCategory.getPrcName());
//                queryProjectVO.setInfName(industryField.getInfName());
//                Integer ps = project.getProStatus();
//                ProjectStatusEnum[] values = ProjectStatusEnum.values();
//                for (ProjectStatusEnum projectStatusEnum : values) {
//                    if (ps == projectStatusEnum.getStatus()) {
//                        queryProjectVO.setProStatusContent(projectStatusEnum.getStatusContent());
//                    }
//                }
//                return queryProjectVO;
//            }).collect(Collectors.toList());
//
//            //当前时间
//            Date date = new Date();
//            List<QueryProjectVO> projectVOS = queryProjectVOS.stream().filter(queryProjectVO -> {
//                if (projectStage != null) {
//                    switch (projectStage) {
//                        //1:前期阶段 2:在建 3:已建成
//                        case 1:
//                            return queryProjectVO.getProDisStart().after(date);
//                        case 2:
//                            return queryProjectVO.getProDisStart().before(date) && queryProjectVO.getProDisComplete().after(date);
//                        case 3:
//                            return queryProjectVO.getProDisComplete().before(date);
//                    }
//                }
//                return true;
//            }).collect(Collectors.toList());
//
//            return ResultResponse.ok(projectVOS);
//        }
//
//        // 非管理员通道
//        User user = userService.query().eq("u_id", userId).one();
//
//        Integer userCouId = user.getCouId();
//        Integer userDepId = user.getDepId();
//
//        Page<Project> page = query()
//                .like(proName!=null,"pro_name",proName)
//                .eq(userCouId!=null,"cou_id",userCouId)
//                .eq(userDepId!=null,"cou_id",userDepId)
//                .eq("u_id",userId)
//                .eq(townId!=null,"town_id",townId)
//                .eq(prcId!=null,"prc_id",prcId)
//                .eq(infId!=null,"inf_id",infId)
//                .eq(proStatus!=null,"pro_status",proStatus)
//                .page(new Page<>(pageNum, DEFAULT_PAGE_SIZE));
//        List<QueryProjectVO> queryProjectVOS = page.getRecords().stream().filter(project -> {
//            if (beginTime != null) {
//                return project.getProDate().after(beginTime);
//            }
//            return true;
//        }).collect(Collectors.toList()).stream().filter(project -> {
//            if (endTime != null) {
//                return project.getProDate().before(endTime);
//            }
//            return true;
//        }).collect(Collectors.toList()).stream().map(project -> {
//            QueryProjectVO queryProjectVO = new QueryProjectVO();
//            BeanUtil.copyProperties(project, queryProjectVO);
//            Department department = departmentService.query().eq("dep_id", project.getDepId()).one();
//            County county = countyService.query().eq("cou_id", project.getCouId()).one();
//            Town town = townService.query().eq("town_id", project.getTownId()).one();
//            ProjectCategory projectCategory = projectCategoryService.query().eq("prc_id", project.getPrcId()).one();
//            IndustryField industryField = industryFieldService.query().eq("inf_id", project.getInfId()).one();
//            queryProjectVO.setDepartmentName(department.getDepName());
//            queryProjectVO.setArea(county.getCouName() + "-" + town.getTownName());
//            queryProjectVO.setPrcName(projectCategory.getPrcName());
//            queryProjectVO.setInfName(industryField.getInfName());
//            Integer ps = project.getProStatus();
//            ProjectStatusEnum[] values = ProjectStatusEnum.values();
//            for (ProjectStatusEnum projectStatusEnum : values) {
//                if (ps == projectStatusEnum.getStatus()) {
//                    queryProjectVO.setProStatusContent(projectStatusEnum.getStatusContent());
//                }
//            }
//            return queryProjectVO;
//        }).collect(Collectors.toList());
//
//        // 当前时间
//        Date date = new Date();
//        List<QueryProjectVO> projectVOS = queryProjectVOS.stream().filter(queryProjectVO -> {
//            if (projectStage != null) {
//                switch (projectStage) {
//                    //1:前期阶段 2:在建 3:已建成
//                    case 1:
//                        return queryProjectVO.getProDisStart().after(date);
//                    case 2:
//                        return queryProjectVO.getProDisStart().before(date) && queryProjectVO.getProDisComplete().after(date);
//                    case 3:
//                        return queryProjectVO.getProDisComplete().before(date);
//                }
//            }
//            return true;
//        }).collect(Collectors.toList());
//
//        return ResultResponse.ok(projectVOS);
//    }
//
//    @Override
//    public ResultResponse addProject(AddProjectVO addProjectVO) {
//        Project project = new Project();
//        BeanUtil.copyProperties(addProjectVO,project);
//        UserDTO user = UserHolder.getUser();
//        project.setUId(user.getUId());
//        save(project);
//        return ResultResponse.ok();
//    }
//
//    @Override
//    public ResultResponse updateProject(UpdateProjectVO updateProjectVO) {
//        Project project = query().eq("pro_id", updateProjectVO.getProId()).one();
//        BeanUtil.copyProperties(updateProjectVO,project);
//        updateById(project);
//        return ResultResponse.ok();
//    }
//
//    @Override
//    public ResultResponse deleteProject(Integer proId) {
//        // TODO 逻辑删除中间表数据不该删除
//        Project project = query().eq("pro_id", proId).one();
//        project.setProStatus(DELETED_STATUS);
//        updateById(project);
//        return ResultResponse.ok();
//    }
//}
