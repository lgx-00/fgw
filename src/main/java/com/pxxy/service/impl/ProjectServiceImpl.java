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
import com.pxxy.exceptions.ReportException;
import com.pxxy.mapper.ProjectMapper;
import com.pxxy.pojo.*;
import com.pxxy.service.*;
import com.pxxy.utils.PageUtil;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import com.pxxy.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private DepartmentService depService;

    @Resource
    private DepPrcService depPrcService;

    @Resource
    private CountyService countyService;

    @Resource
    private TownService townService;

    @Resource
    private ProjectCategoryService prcService;

    @Resource
    private IndustryFieldService infService;

    @Resource
    private UserService userService;

    private Map<Integer, Department> departments;
    private Map<Integer, County> counties;
    private Map<Integer, Town> towns;
    private Map<Integer, ProjectCategory> projectCategories;
    private Map<Integer, IndustryField> industryFields;

    private final Function<Project, QueryProjectVO> mapProjectToVO = project -> {
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
        departments = depService.all().stream().collect(Collectors.toMap(Department::getDepId, d -> d));
        counties = countyService.all().stream().collect(Collectors.toMap(County::getCouId, c -> c));
        towns = townService.all().stream().collect(Collectors.toMap(Town::getTownId, t -> t));
        projectCategories = prcService.all().stream().collect(Collectors.toMap(ProjectCategory::getPrcId, p -> p));
        industryFields = infService.all().stream().collect(Collectors.toMap(IndustryField::getInfId, i -> i));
    }

    private void updateBaseData(String what, boolean findDeleted) {
        switch (what) {
            case "dep":
                Stream<Department> departments = depService.all().stream();
                this.departments = (findDeleted ? departments : departments
                        .filter(d -> DELETED_STATUS != d.getDepStatus()))
                        .collect(Collectors.toMap(Department::getDepId, d -> d));
                return;
            case "cou":
                Stream<County> counties = countyService.all().stream();
                this.counties = (findDeleted ? counties : counties
                        .filter(c -> DELETED_STATUS != c.getCouStatus()))
                        .collect(Collectors.toMap(County::getCouId, c -> c));
                return;
            case "town":
                Stream<Town> towns = townService.all().stream();
                this.towns = (findDeleted ? towns : towns
                        .filter(t -> DELETED_STATUS != t.getTownStatus()))
                        .collect(Collectors.toMap(Town::getTownId, t -> t));
                return;
            case "prc":
                Stream<ProjectCategory> pcs = prcService.all().stream();
                projectCategories = (findDeleted ? pcs : pcs
                        .filter(p -> DELETED_STATUS != p.getPrcStatus()))
                        .collect(Collectors.toMap(ProjectCategory::getPrcId, p -> p));
                return;
            case "inf":
                Stream<IndustryField> ifs = infService.all().stream();
                industryFields = (findDeleted ? ifs : ifs
                        .filter(i -> DELETED_STATUS != i.getInfStatus()))
                        .collect(Collectors.toMap(IndustryField::getInfId, i -> i));
        }
    }

    @Override
    @Transactional
    public ResultResponse<PageInfo<QueryProjectVO>> getAllProject(Page page) {
        // 先拿到用户信息
        UserDTO user = UserHolder.getUser();
        Integer uId = user.getUId();

        updateBaseData();

        // 管理员特殊通道
        if (uId == 1) return ResultResponse.ok(PageUtil.selectPage(page, () ->
                this.query().ne("pro_status", DELETED_STATUS).orderByDesc("pro_id").list(), mapProjectToVO));

        // 非管理员通道
        User u = userService.query().eq("u_id", uId).one();
        Integer depId = u.getDepId();
        Integer couId = u.getCouId();

        PageInfo<QueryProjectVO> pageInfo = PageUtil.selectPage(page, () ->
                baseMapper.getAllProjectByUser(depId, couId, uId), mapProjectToVO);
        return ResultResponse.ok(pageInfo);
    }

    @Override
    public ResultResponse<PageInfo<QueryProjectVO>> getAllDispatchProject(Page page) {
        // 先拿到用户信息
        UserDTO user = UserHolder.getUser();
        Integer uId = user.getUId();

        updateBaseData();

        // 管理员特殊通道
        if (uId == 1) return ResultResponse.ok(PageUtil.selectPage(page, () ->
                query().in("pro_status", Arrays.asList(1, 3, 4)).list(), mapProjectToVO));

        // 非管理员通道
        User u = userService.query().eq("u_id", uId).one();
        Integer depId = u.getDepId();
        Integer couId = u.getCouId();

        return ResultResponse.ok(PageUtil.selectPage(page,
                () -> baseMapper.getAllDispatchProjectByUser(depId, couId, uId), mapProjectToVO));
    }

    @Override
    @Transactional
    public ResultResponse<PageInfo<QueryProjectVO>> getVagueProject(Page page, ProjectDTO dto) {
        //先拿到用户信息
        UserDTO user = UserHolder.getUser();
        Integer uId = user.getUId();

        updateBaseData();

        // 管理员特殊通道
        if (uId == 1) return ResultResponse.ok(PageUtil
                .selectPage(page, getLambda(dto), mapProjectToVO));

        // 非管理员通道
        User u = userService.query().eq("u_id", uId).one();
        dto.setCouId(u.getCouId());
        dto.setDepId(u.getDepId());
        return ResultResponse.ok(PageUtil.selectPage(page, () ->
                baseMapper.getVagueProjectByUser(dto), mapProjectToVO));
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

        ResultResponse<?> ret = checkPrcAndInf(vo.getPrcId(), vo.getInfId(), false);
        if (ret != null) return ret;

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

        ResultResponse<?> ret = checkPrcAndInf(vo.getPrcId(), vo.getInfId());
        if (ret != null) return ret;

        if (checkCountyAndTownMatch(vo.getCouId(), vo.getTownId())) {
            return ResultResponse.fail(COUNTY_AND_TOWN_NOT_MATCH);
        }
        Project project = query().eq("pro_id", vo.getProId()).one();
        if (project == null) {
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }

        if (!project.getProStatus().equals(FAILURE_TO_REPORT.val)) {
            return ResultResponse.fail(ILLEGAL_UPDATING_PROJECT);
        }

        BeanUtil.copyProperties(vo, project);
        project.setProMark(vo.getProMark());
        updateById(project);
        return ResultResponse.ok();
    }

    private ResultResponse<?> checkPrcAndInf(Integer prcId, Integer infId) {
        return checkPrcAndInf(prcId, infId, true);
    }

    private ResultResponse<?> checkPrcAndInf(Integer prcId, Integer infId, boolean findDeleted) {
        if (prcId != null && projectCategories != null && projectCategories.get(prcId) == null) {
            updateBaseData("prc", findDeleted);
            if (projectCategories.get(prcId) == null) return ResultResponse.fail(INVALID_PROJECT_CATEGORY_ID);
        }
        if (infId != null && industryFields != null && industryFields.get(infId) == null) {
            updateBaseData("inf", findDeleted);
            if (industryFields.get(infId) == null) return ResultResponse.fail(INVALID_INDUSTRY_FIELD_ID);
        }
        return null;
    }

    @Override
    public ResultResponse<?> deleteProject(Integer proId) {
        if (query().eq("pro_id", proId).eq("pro_status", FAILURE_TO_REPORT.val).one() == null) {
            return ResultResponse.fail(CANNOT_DELETE_REPORTED_PROJECT);
        }
        if (update().eq("pro_id", proId).set("pro_status", DELETED.val).update()) {
            return ResultResponse.ok();
        }
        return ResultResponse.fail(DELETE_FAILED);
    }

    private static List<Project> filterNE(List<Project> list, ProjectStatusEnum status) {
        return list.stream().filter(p ->
                !p.getProStatus().equals(status.val)).collect(Collectors.toList());
    }

    @Override
    public ResultResponse<?> reportProject(List<Integer> proIds, Integer depId) {
        List<Project> projects = getProjects(proIds);
        if (projects.size() == 0) {
             return ResultResponse.fail(FAIL_MSG);
        }
        List<Integer> prcIds = depPrcService.query().eq("dep_id", depId)
                .list().stream().map(DepPrc::getPrcId).collect(Collectors.toList());
        if (projects.stream().allMatch(p -> prcIds.contains(p.getPrcId()))) {
            projects.forEach(p -> p.setDepId(depId).setProStatus(PENDING_REVIEW.val));
            updateBatchById(projects);
            return ResultResponse.ok();
        }
        return ResultResponse.fail(REPORT_FAILED);
    }

    @Override
    public ResultResponse<?> reportProject(List<Integer> proIds, List<Integer> depIds) {
        if (proIds.size() != depIds.size()) return ResultResponse.fail(FAIL_MSG);
        List<Project> projects = getProjects(proIds);
        if (projects.size() == 0) {
            return ResultResponse.fail(FAIL_MSG);
        }

        ResultResponse<?> resp = testPerm(proIds, depIds, projects);
        if (resp != null) return resp;
        updateBatchById(projects);
        return ResultResponse.ok();
    }

    private List<Project> getProjects(List<Integer> proIds) throws ReportException {
        List<Project> projects = query().select("pro_id", "prc_id",
                "pro_name", "pro_status").in("pro_id", proIds).list();
        // 检查上报的科室是否具有管理该类型的项目的权限
        List<Project> illegalList = filterNE(projects, FAILURE_TO_REPORT);
        int size = illegalList.size();
        if (size > 0) {
            // 说明项目已上报
            StringBuilder msg = genMessage(illegalList, size).append("已上报，不能再次上报！");
            throw new ReportException(msg.toString());
        }
        return projects;
    }

    private ResultResponse<?> testPerm(List<Integer> proIds, List<Integer> depIds, List<Project> projects) {
        // 检查上报的科室是否具有管理该类型的项目的权限
        Map<Integer, List<Integer>> depIdMapPrcIds = depIds.stream().distinct()
                .collect(Collectors.toMap(id -> id, id -> depPrcService.query().eq("dep_id", id)
                        .list().stream().map(DepPrc::getPrcId).collect(Collectors.toList())));
        // 逐个项目进行检查，不通过直接返回失败消息
        for (int i = proIds.size() - 1; i >= 0; i--) {
            int depId = depIds.get(i);
            List<Integer> prcIds = depIdMapPrcIds.get(depId);
            Project project = projects.get(i);
            if (!prcIds.contains(project.getPrcId())) {
                // 检查不通过
                return ResultResponse.fail(REPORT_FAILED);
            }
            project.setDepId(depId);
        }
        return null;
    }

    private StringBuilder genMessage(List<Project> projectList, int size) {
        StringBuilder sb = new StringBuilder("项目“");
        if (size == 1) {
            return sb.append(projectList.get(0).getProName()).append("”");
        }
        StringJoiner sj = new StringJoiner("”、“");
        projectList.subList(0, Math.min(size - 1, 4)).forEach(p -> sj.add(p.getProName()));
        sb.append(sj).append("”");
        if (size > 1) {
            sb.append("和“").append(projectList.get(Math.min(size - 1, 4)).getProName()).append("”");
        }
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
                if (projectExcelVO.getProDate()     == null) errorMsgList.add("项目日期不能为空；");
                if (projectExcelVO.getProName()     == null) errorMsgList.add("项目名称不能为空；");
                if (projectExcelVO.getProLocation() == null) errorMsgList.add("建设地点不能为空；");
                if (projectExcelVO.getCouName()     == null) errorMsgList.add("辖区不能为空；");
                if (projectExcelVO.getTownName()    == null) errorMsgList.add("二级辖区不能为空；");
                if (projectExcelVO.getPrcName()     == null) errorMsgList.add("项目类型名称不能为空；");
                if (projectExcelVO.getInfName()     == null) errorMsgList.add("行业领域名称不能为空；");
            }

            if (errorMsgList.size() != 0)
                return ResultResponse.fail(errorMsgList.stream().distinct().collect(Collectors.toList()).toString());

            for (ProjectExcelVO projectExcelVO : projectExcelVOList) {
                Project project = new Project();
                BeanUtil.copyProperties(projectExcelVO, project);
                String departmentName = projectExcelVO.getDepartmentName();
                if (departmentName != null) {
                    Department department = depService.query().eq("dep_name", departmentName).one();
                    if (department != null) {
                        project.setDepId(department.getDepId());
                    } else {
                        //若为空
                        errorMsgList.add("不存在科室名“" + departmentName + "”；");
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
                            errorMsgList.add("辖区“" + county.getCouName() + "”与二级辖区“" + townName + "”不匹配；");
                        }
                    } else {
                        //若为空
                        errorMsgList.add("不存在名称为“" + townName + "”的二级辖区；");
                    }
                } else {
                    //若为空
                    errorMsgList.add("不存在名称为“" + couName + "”的辖区；");
                }


                String prcName = projectExcelVO.getPrcName();
                ProjectCategory projectCategory = prcService.query().eq("prc_name", prcName).one();
                if (projectCategory != null) {
                    project.setPrcId(projectCategory.getPrcId());
                } else {
                    //若为空
                    errorMsgList.add("不存在名称为“" + prcName + "”的项目类型；");
                }

                String infName = projectExcelVO.getInfName();
                IndustryField industryField = infService.query().eq("inf_name", infName).one();
                if (industryField != null) {
                    project.setInfId(industryField.getInfId());
                } else {
                    //若为空
                    errorMsgList.add("不存在名称为“" + infName + "”的行业领域；");

                }

                // 是否 0 1 转换
                String proIsNew = projectExcelVO.getProIsNew();
                if (proIsNew != null) {
                    try {
                        project.setProIsNew(YesOrNoEnum.from(proIsNew));
                    } catch (IllegalArgumentException e) {
                        // 是否当年度新开工项目
                        errorMsgList.add("是否当年度新开工项目【列】只能填是或否；");
                    }
                }

                String proIsProvincial = projectExcelVO.getProIsProvincial();
                if (proIsProvincial != null) {
                    try {
                        project.setProIsNew(YesOrNoEnum.from(proIsProvincial));
                    } catch (IllegalArgumentException e) {
                        // 是否省大中型项目
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
        List<Project> projects = query().select("pro_id", "pro_name", "pro_status")
                .in("pro_id", proIds).list();
        if (projects.size() < proIds.size()) {
            return ResultResponse.fail(FAIL_MSG);
        }
        List<Project> illegalList = filterNE(projects, PENDING_REVIEW);
        int size = illegalList.size();
        return size > 0 ? ResultResponse.fail(genMessage(illegalList, size)
                .append(String.format("不是待审核状态，不能执行%s操作！", oper)).toString()) : null;
    }

    @Override
    @Transactional
    public ResultResponse<?> accept(List<Integer> proIds) {
        ResultResponse<?> sb = checkPendingReview(proIds, "批准");
        if (sb != null) return sb;
        // TODO 批准要发送 WebSocket 通知
        updateBaseData("prc", true);
        List<Project> projects = query().in("pro_id", proIds)
                .eq("pro_status", PENDING_REVIEW.val).list();
        updateNextUpdateTime(projects);
        projects.forEach(p -> p.setProStatus(NORMAL.val));
        return updateBatchById(projects)
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

    private void updateNextUpdateTime(List<Project> projects) {
        Calendar nextMonthCalendar = Calendar.getInstance();
        nextMonthCalendar.add(Calendar.MONTH, 1);
        nextMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        Date nextMonth = nextMonthCalendar.getTime();
        Date now = new Date();

        Calendar thisMonthCalendar = Calendar.getInstance();
        int max1 = thisMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int max2 = nextMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (Project project : projects) {
            Date date = project.getProNextUpdate();
            if (date == null || date.before(now)) {
                Integer period = projectCategories.get(project.getPrcId()).getPrcPeriod();
                if (period == null || period == 0) {
                    project.setProNextUpdate(nextMonth);
                    continue;
                }
                int min = Math.min(period / 100, period % 100);
                thisMonthCalendar.set(Calendar.DAY_OF_MONTH, Math.min(min, max1));
                Date time = thisMonthCalendar.getTime();
                if (time.before(now)) {
                    nextMonthCalendar.set(Calendar.DAY_OF_MONTH, Math.min(min, max2));
                    project.setProNextUpdate(nextMonthCalendar.getTime());
                    continue;
                }
                project.setProNextUpdate(time);
            }
        }
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
        return ResultResponse.ok(baseMapper.getDispatchingCount(projectDTO));
    }

    @Override
    @Transactional
    public ResultResponse<QueryProjectVO> getProject(Integer proId) {
        Project project = getById(proId);
        if (project == null) {
            return ResultResponse.fail(FAIL_MSG);
        }
        updateBaseData();
        return ResultResponse.ok(mapProjectToVO.apply(project));
    }

    @Override
    public void clearDispatch() {
        update().set("pro_dis_year", 0).set("pro_dis_year_percent", 0).update();
    }

}
