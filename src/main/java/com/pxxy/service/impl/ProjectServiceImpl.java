package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.exception.ExcelCommonException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageInfo;
import com.pxxy.dto.ProjectDTO;
import com.pxxy.dto.UserDTO;
import com.pxxy.enums.ProjectStageEnum;
import com.pxxy.enums.ProjectStatusEnum;
import com.pxxy.enums.YesOrNoEnum;
import com.pxxy.mapper.ProjectMapper;
import com.pxxy.pojo.*;
import com.pxxy.service.*;
import com.pxxy.utils.PageUtil;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import com.pxxy.vo.AddProjectVO;
import com.pxxy.vo.ProjectExcelVO;
import com.pxxy.vo.QueryProjectVO;
import com.pxxy.vo.UpdateProjectVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.pxxy.constant.ResponseMessage.*;
import static com.pxxy.constant.SystemConstant.*;
import static com.pxxy.enums.ProjectStatusEnum.*;

/**
 * <p>
 * 服务实现类
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

    private static Map<Integer, Department> departments;
    private static Map<Integer, County> counties;
    private static Map<Integer, Town> towns;
    private static Map<Integer, ProjectCategory> projectCategories;
    private static Map<Integer, IndustryField> industryFields;

    private static final Function<Project, QueryProjectVO> mapProjectToVO = project -> {
        QueryProjectVO queryProjectVO = new QueryProjectVO();
        BeanUtil.copyProperties(project, queryProjectVO);
        Town town = towns.get(project.getTownId());
        County county = counties.get(project.getCouId());
        Department department = departments.get(project.getDepId());
        IndustryField industryField = industryFields.get(project.getInfId());
        ProjectCategory projectCategory = projectCategories.get(project.getPrcId());
        if (department != null) {
            queryProjectVO.setDepName(department.getDepName());
        }
        if (county != null) {
            queryProjectVO.setArea(town != null
                    ? county.getCouName() + " - " + town.getTownName()
                    : county.getCouName());
        }
        if (projectCategory != null) {
            queryProjectVO.setPrcName(projectCategory.getPrcName());
        }

        if (industryField != null) {
            queryProjectVO.setInfName(industryField.getInfName());
        }
        Integer proStatus = project.getProStatus();
        ProjectStatusEnum[] values = ProjectStatusEnum.values();
        for (ProjectStatusEnum projectStatusEnum : values) {
            if (proStatus == projectStatusEnum.val) {
                queryProjectVO.setProStatusContent(projectStatusEnum.name);
            }
        }
        String[] s = project.getProMark().split("᛭", 3);
        queryProjectVO.setProMark1(s[0]);
        queryProjectVO.setProMark2(s[1]);
        queryProjectVO.setProMark3(s[2]);
        return queryProjectVO;
    };

    private void updateBaseData() {
        departments = departmentService.query().list()
                .stream().collect(Collectors.toMap(Department::getDepId, d -> d));
        counties = countyService.query().list()
                .stream().collect(Collectors.toMap(County::getCouId, c -> c));
        towns = townService.query().list()
                .stream().collect(Collectors.toMap(Town::getTownId, t -> t));
        projectCategories = projectCategoryService.query().list()
                .stream().collect(Collectors.toMap(ProjectCategory::getPrcId, p -> p));
        industryFields = industryFieldService.query().list()
                .stream().collect(Collectors.toMap(IndustryField::getInfId, i -> i));
    }

    @Override
    @Transactional
    public ResultResponse<PageInfo<QueryProjectVO>> getAllProject(Integer pageNum) {
        //先拿到用户信息
        UserDTO user = UserHolder.getUser();
        Integer uId = user.getUId();

        updateBaseData();

        //管理员特殊通道
        if (uId == 1) return ResultResponse.ok(PageUtil.selectPage(pageNum, DEFAULT_PAGE_SIZE,
                () -> this.query().ne("pro_status", DELETED_STATUS).list(), mapProjectToVO));

        // 非管理员通道
        User u = userService.query().eq("u_id", uId).one();
        Integer depId = u.getDepId();
        Integer couId = u.getCouId();

        PageInfo<QueryProjectVO> pageInfo = PageUtil.selectPage(pageNum, DEFAULT_PAGE_SIZE,
                () -> projectMapper.getAllProjectByUser(depId, couId, uId), mapProjectToVO);
        return ResultResponse.ok(pageInfo);
    }

    @Override
    @Transactional
    public ResultResponse<PageInfo<QueryProjectVO>> getVagueProject(Integer pageNum, ProjectDTO dto) {
        //先拿到用户信息
        UserDTO user = UserHolder.getUser();
        Integer uId = user.getUId();

        updateBaseData();

        // 管理员特殊通道
        if (uId == 1) return ResultResponse.ok(PageUtil
                .selectPage(pageNum, DEFAULT_PAGE_SIZE, getLambda(dto), mapProjectToVO));

        // 非管理员通道
        User u = userService.query().eq("u_id", uId).one();
        dto.setCouId(u.getCouId());
        dto.setDepId(u.getDepId());
        return ResultResponse.ok(PageUtil.selectPage(pageNum, DEFAULT_PAGE_SIZE, () ->
                projectMapper.getVagueProjectByUser(dto), mapProjectToVO));
    }

    // 抽取查询方法为一个 Lambda 表达式
    private ISelect getLambda(ProjectDTO dto) {
        return () -> ProjectStageEnum.values()[dto.getProjectStage()].list(query()
                .like(nn(dto.getProName()), "pro_name", dto.getProName())
                .like(nn(dto.getProMark1()), "pro_mark", "%" + dto.getProMark1() + "%᛭%᛭%")
                .like(nn(dto.getProMark2()), "pro_mark", "%᛭%" + dto.getProMark2() + "%᛭%")
                .like(nn(dto.getProMark3()), "pro_mark", "%᛭%᛭%" + dto.getProMark3() + "%")
                .eq(nn(dto.getCouId()), "cou_id", dto.getCouId())
                .eq(nn(dto.getTownId()), "town_id", dto.getTownId())
                .eq(nn(dto.getPrcId()), "prc_id", dto.getPrcId())
                .eq(nn(dto.getInfId()), "inf_id", dto.getInfId())
                .eq(nn(dto.getProStatus()), "pro_status", dto.getProStatus())
                .between(nn(dto.getBeginTime()) || nn(dto.getEndTime()), "pro_date",
                        Optional.ofNullable(dto.getBeginTime()).orElse(ZERO_DATE),
                        Optional.ofNullable(dto.getEndTime()).orElse(INFINITY_DATE)));
    }

    private static boolean nn(String s) {
        return !"".equals(s) && Objects.nonNull(s);
    }

    private static boolean nn(Object o) {
        return Objects.nonNull(o);
    }

    private boolean checkCountyAndTownMatch(Integer couId, Integer townId) {
        return nn(townId) && townService.query().eq("cou_id", couId).list()
                .stream().noneMatch(town -> town.getTownId().equals(townId));
    }

    @Override
    public ResultResponse<?> addProject(AddProjectVO vo) {

        // TODO 检查 项目类型 和 行业领域 的编号是否存在

        if (checkCountyAndTownMatch(vo.getCouId(), vo.getTownId())) {
            return ResultResponse.fail(COUNTY_AND_TOWN_NOT_MATCH);
        }
        Project project = new Project();
        BeanUtil.copyProperties(vo, project);
        project.setProMark(vo.getProMark());
        UserDTO user = UserHolder.getUser();
        project.setUId(user.getUId());
        save(project);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<?> updateProject(UpdateProjectVO vo) {

        // TODO 检查 项目类型 和 行业领域 的编号是否存在

        if (checkCountyAndTownMatch(vo.getCouId(), vo.getTownId())) {
            return ResultResponse.fail(COUNTY_AND_TOWN_NOT_MATCH);
        }
        Project project = query().eq("pro_id", vo.getProId()).one();
        if (project == null) {
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }
        BeanUtil.copyProperties(vo, project);
        project.setProMark(vo.getProMark());
        updateById(project);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<?> deleteProject(Integer proId) {
        if (query().eq("pro_id", proId).ne("pro_status", DELETED_STATUS).one() == null) {
            return ResultResponse.fail(DELETE_FAILED);
        }
        removeById(proId);
        return ResultResponse.ok();
    }

    private static List<Project> filterNE(List<Project> list, ProjectStatusEnum status) {
        return list.stream().filter(p ->
                !p.getProStatus().equals(status.val)).collect(Collectors.toList());
    }

    @Override
    public ResultResponse<?> reportProject(List<Integer> proIds, Integer depId) {
        List<Project> projects = query().in("pro_id", proIds).list();
        List<Project> projectList = filterNE(projects, FAILURE_TO_REPORT);
        int size = projectList.size();
        if (size > 0) {
            // 说明项目已上报
            return ResultResponse.fail(genMessage(projectList, size)
                    .append("已上报，不能再次上报！").toString());
        }
        projects.forEach(p -> p.setDepId(depId).setProStatus(PENDING_REVIEW.val));
        updateBatchById(projects);
        return ResultResponse.ok();
    }

    private StringBuilder genMessage(List<Project> projectList, int size) {
        StringBuilder sb = new StringBuilder("项目“");
        StringJoiner sj = new StringJoiner("”、“");
        projectList.subList(0, Math.min(size, 5)).forEach(p -> sj.add(p.getProName()));
        sb.append(sj).append("”");
        if (size > 5) {
            sb.append("等 ").append(size).append(" 个项目");
        }
        return sb;
    }

    @Override
    @Transactional
    public ResultResponse<?> importExcel(MultipartFile file) {

        int uid = UserHolder.getUser().getUId();
        // 错误信息集合
        List<String> errorMsgList = new ArrayList<>();

        try {
            List<ProjectExcelVO> projectExcelVOList = EasyExcel.read(file.getInputStream())
                    .head(ProjectExcelVO.class)
                    .sheet()
                    .doReadSync();
            List<Project> projectList = new ArrayList<>();
            for (ProjectExcelVO projectExcelVO : projectExcelVOList) {
                if (projectExcelVO.getProDate() == null) {
                    errorMsgList.add("项目日期不能为空；");
                }

                if (projectExcelVO.getProName() == null) {
                    errorMsgList.add("项目名称不能为空；");
                }

                if (projectExcelVO.getProLocation() == null) {
                    errorMsgList.add("建设地点不能为空；");
                }

                if (projectExcelVO.getCouName() == null) {
                    errorMsgList.add("辖区不能为空；");
                }

                if (projectExcelVO.getTownName() == null) {
                    errorMsgList.add("二级辖区不能为空；");
                }

                if (projectExcelVO.getPrcName() == null) {
                    errorMsgList.add("项目类型名称不能为空；");
                }

                if (projectExcelVO.getInfName() == null) {
                    errorMsgList.add("行业领域名称不能为空；");
                }
            }

            if (errorMsgList.size() != 0) {
                return ResultResponse.fail(errorMsgList.stream().distinct().collect(Collectors.toList()).toString());
            }


            for (ProjectExcelVO projectExcelVO : projectExcelVOList) {
                Project project = new Project();
                BeanUtil.copyProperties(projectExcelVO, project);
                String departmentName = projectExcelVO.getDepartmentName();
                if (departmentName != null) {
                    Department department = departmentService.query().eq("dep_name", departmentName).one();
                    if (department != null) {
                        project.setDepId(department.getDepId());
                    } else {
                        //若为空
                        errorMsgList.add("不存在科室名（" + departmentName + "）；");
                    }

                }

                String couName = projectExcelVO.getCouName();
                County county = countyService.query().eq("cou_name", couName).one();
                if (county != null) {
                    project.setCouId(county.getCouId());
                    String townName = projectExcelVO.getTownName();
                    Town town = townService.query().eq("town_name", townName).one();
                    if (town != null) {
                        if (town.getCouId().equals(county.getCouId())) {
                            project.setTownId(town.getTownId());
                        } else {
                            errorMsgList.add("【辖区】" + county.getCouName() + "不存在" + "【二级辖区】" + townName + "；");
                        }
                    } else {
                        //若为空
                        errorMsgList.add("不存在二级辖区名（" + townName + "）；");
                    }
                } else {
                    //若为空
                    errorMsgList.add("不存在辖区名（" + couName + "）；");
                }


                String prcName = projectExcelVO.getPrcName();
                ProjectCategory projectCategory = projectCategoryService.query().eq("prc_name", prcName).one();
                if (projectCategory != null) {
                    project.setPrcId(projectCategory.getPrcId());
                } else {
                    //若为空
                    errorMsgList.add("不存在项目类型名称（" + prcName + "）；");
                }

                String infName = projectExcelVO.getInfName();
                IndustryField industryField = industryFieldService.query().eq("inf_name", infName).one();
                if (industryField != null) {
                    project.setInfId(industryField.getInfId());
                } else {
                    //若为空
                    errorMsgList.add("不存在行业领域名称（" + infName + "）；");

                }

                // 是否 0 1 转换
                YesOrNoEnum[] yesOrNoEnums = YesOrNoEnum.values();
                String proIsNew = projectExcelVO.getProIsNew();
                if (proIsNew != null) {
                    for (YesOrNoEnum i : yesOrNoEnums) {
                        if (proIsNew.equals(i.name)) {
                            project.setProIsNew(i.val);
                        }
                        //是否当年度新开工项目
                        errorMsgList.add("是否当年度新开工项目【列】只能填是或否；");
                    }
                }

                String proIsProvincial = projectExcelVO.getProIsProvincial();
                if (proIsProvincial != null) {
                    for (YesOrNoEnum i : yesOrNoEnums) {
                        if (proIsProvincial.equals(i.name)) {
                            project.setProIsProvincial(i.val);
                        }
                        //是否省大中型项目
                        errorMsgList.add("是否省大中型项目【列】只能填是或否；");
                    }
                }

                // 把用户ID存进去才知道是谁导入的
                project.setUId(uid);

                projectList.add(project);
            }
            // 错误信息
            if (errorMsgList.size() != 0) {
                String s = errorMsgList.stream().distinct().collect(Collectors.toList()).toString();
                return ResultResponse.fail(s);
            }

            int total = projectList.size();

            // 插入的项目 项目代码以及入统入库代码必须唯一
            int s1 = (int) projectList.stream().map(Project::getProCode).distinct().count();
            if (s1 < total) {
                return ResultResponse.fail("项目代码必须保证唯一！");
            }
            int s2 = (int) projectList.stream().map(Project::getProInCode).distinct().count();
            if (s2 < total) {
                return ResultResponse.fail("入统入库代码必须保证唯一！");
            }

            saveBatch(projectList);
            return ResultResponse.ok("成功插入" + total + "条数据！");

        } catch (IOException e) {
            return ResultResponse.fail("导入失败！");
        } catch (ExcelCommonException e) {
            return ResultResponse.fail("请选择.xlsx或.xls文件！");
        }
    }

    private ResultResponse<?> checkPendingReview(List<Integer> proIds, String oper) {
        List<Project> projects = query().in("pro_id", proIds).list();
        List<Project> illegalList = filterNE(projects, PENDING_REVIEW);
        int size = illegalList.size();
        return size > 0 ? ResultResponse.fail(genMessage(illegalList, size)
                .append(String.format("不是待审核状态，不能执行%s操作！", oper)).toString()) : null;
    }

    @Override
    public ResultResponse<?> accept(List<Integer> proIds) {
        ResultResponse<?> sb = checkPendingReview(proIds, "批准");
        if (sb != null) return sb;
        // TODO 批准要发送 WebSocket 通知
        // TODO 批准要修改下次调度提醒时间
        return update().in("pro_id", proIds)
                .eq("pro_status", PENDING_REVIEW.val)
                .set("pro_status", NORMAL.val).update()
                ? ResultResponse.ok()
                : ResultResponse.fail(FAIL_MSG);
    }

    @Override
    public ResultResponse<?> reject(List<Integer> proIds) {
        ResultResponse<?> sb = checkPendingReview(proIds, "驳回");
        if (sb != null) return sb;
        // TODO 驳回要发送 WebSocket 通知
        return update().in("pro_id", proIds)
                .eq("pro_status", PENDING_REVIEW.val)
                .set("pro_status", FAILURE_TO_REPORT.val)
                .set("dep_id", null).update()
                ? ResultResponse.ok()
                : ResultResponse.fail(FAIL_MSG);
    }

    private static final List<Integer> status = Arrays.asList(NORMAL.val,
            UNLOCKED.val, TO_BE_SCHEDULED.val);

    @Override
    public ResultResponse<?> markAsComplete(List<Integer> proIds) {
        return update().isNull("pro_dis_complete")
                .in( "pro_id",     proIds)
                .in( "pro_status", status)
                .set("pro_status", COMPLETE.val)
                .set("pro_dis_complete", new Date())
                .set("pro_next_update",  null).update()
                ? ResultResponse.ok()
                : ResultResponse.fail(FAIL_MSG);
    }

    private static final List<Integer> status1 = Arrays.asList(NORMAL.val, UNLOCKED.val);

    @Override
    public void updateDispatchStatus() {
        update().in("pro_status", status1).le("pro_next_update", new Date())
                .set("pro_status", TO_BE_SCHEDULED.val).update();
    }

    @Override
    @Transactional
    public ResultResponse<Integer> getDispatchingCount() {
        User user = userService.getById(UserHolder.getUser().getUId());
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setUId(user.getUId());
        projectDTO.setDepId(user.getDepId());
        projectDTO.setCouId(user.getCouId());
        projectDTO.setProStatus(TO_BE_SCHEDULED);
        return ResultResponse.ok(projectMapper.getDispatchingCount(projectDTO));
    }

    @Override
    @Transactional
    public ResultResponse<QueryProjectVO> getProject(Integer proId) {
        updateBaseData();
        return ResultResponse.ok(mapProjectToVO.apply(getById(proId)));
    }

}
