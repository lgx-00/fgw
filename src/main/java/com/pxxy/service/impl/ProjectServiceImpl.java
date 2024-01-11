package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.exception.ExcelCommonException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageInfo;
import com.pxxy.entity.dto.ProjectDTO;
import com.pxxy.entity.dto.UserDTO;
import com.pxxy.entity.pojo.*;
import com.pxxy.entity.vo.*;
import com.pxxy.enums.ProjectStageEnum;
import com.pxxy.enums.ProjectStatusEnum;
import com.pxxy.enums.YesOrNoEnum;
import com.pxxy.exceptions.ForbiddenException;
import com.pxxy.exceptions.NotFoundException;
import com.pxxy.mapper.ProjectMapper;
import com.pxxy.service.*;
import com.pxxy.utils.PageUtil;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

    @Resource
    private ResourceLoader resourceLoader;

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

    private PageInfo<QueryProjectVO> page(Page page, ISelect select) {
        return PageUtil.selectPage(page, select, mapProjectToVO);
    }

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
    public PageInfo<QueryProjectVO> getAllProject(Page page) {
        // 先拿到用户信息
        UserDTO user = UserHolder.getUser();
        Integer uId = user.getUId();

        updateBaseData();

        // 管理员特殊通道
        if (uId == 1) return page(page, () -> this.query().ne("pro_status", DELETED_STATUS).orderByDesc("pro_id").list());

        // 非管理员通道
        User u = userService.query().eq("u_id", uId).one();
        Integer depId = u.getDepId();
        Integer couId = u.getCouId();

        return page(page, () -> baseMapper.getAllProjectByUser(depId, couId, uId));
    }

    @Override
    public PageInfo<QueryProjectVO> getAllDispatchProject(Page page) {
        // 先拿到用户信息
        UserDTO user = UserHolder.getUser();
        Integer uId = user.getUId();

        updateBaseData();

        // 管理员特殊通道
        if (uId == 1) return page(page, () ->
                query().in("pro_status", Arrays.asList(1, 3, 4)).orderByDesc("pro_id").list());

        // 非管理员通道
        User u = userService.query().eq("u_id", uId).one();
        Integer depId = u.getDepId();
        Integer couId = u.getCouId();

        return page(page, () -> baseMapper.getAllDispatchProjectByUser(depId, couId, uId));
    }

    @Override
    @Transactional
    public PageInfo<QueryProjectVO> getVagueProject(Page page, ProjectDTO dto) {
        //先拿到用户信息
        UserDTO user = UserHolder.getUser();
        Integer uId = user.getUId();

        updateBaseData();

        // 管理员特殊通道
        if (uId == 1) return page(page, getLambda(dto));

        // 非管理员通道
        User u = userService.query().eq("u_id", uId).one();
        dto.setCouId(u.getCouId());
        dto.setDepId(u.getDepId());
        return page(page, () -> baseMapper.getVagueProjectByUser(dto));
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
                        Optional.ofNullable(dto.getEndTime()).orElse(INFINITY_DATE))
                .orderByDesc("pro_id"));
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
    public boolean addProject(AddProjectVO vo) throws ForbiddenException {

        checkPrcAndInf(vo.getPrcId(), vo.getInfId(), false);

        if (checkCountyAndTownMatch(vo.getCouId(), vo.getTownId())) {
            throw new ForbiddenException(COUNTY_AND_TOWN_NOT_MATCH);
        }
        Project project = new Project();
        BeanUtil.copyProperties(vo, project);
        project.setProMark(vo.getProMark());
        UserDTO user = UserHolder.getUser();
        project.setUId(user.getUId());
        return save(project);
    }

    @Override
    public boolean updateProject(UpdateProjectVO vo) throws ForbiddenException {

        checkPrcAndInf(vo.getPrcId(), vo.getInfId());

        if (checkCountyAndTownMatch(vo.getCouId(), vo.getTownId())) {
            throw new ForbiddenException(COUNTY_AND_TOWN_NOT_MATCH);
        }
        Project project = query().eq("pro_id", vo.getProId()).one();
        if (project == null) {
            throw new ForbiddenException(ILLEGAL_OPERATE);
        }

        if (!project.getProStatus().equals(FAILURE_TO_REPORT.val)) {
            throw new ForbiddenException(ILLEGAL_UPDATING_PROJECT);
        }

        BeanUtil.copyProperties(vo, project);
        project.setProMark(vo.getProMark());
        return updateById(project);
    }

    private void checkPrcAndInf(Integer prcId, Integer infId) {
        checkPrcAndInf(prcId, infId, true);
    }

    private void checkPrcAndInf(Integer prcId, Integer infId, boolean findDeleted) throws NotFoundException {
        if (prcId != null && projectCategories != null && projectCategories.get(prcId) == null) {
            updateBaseData("prc", findDeleted);
            if (projectCategories.get(prcId) == null) throw new NotFoundException(INVALID_PROJECT_CATEGORY_ID);
        }
        if (infId != null && industryFields != null && industryFields.get(infId) == null) {
            updateBaseData("inf", findDeleted);
            if (industryFields.get(infId) == null) throw new NotFoundException(INVALID_INDUSTRY_FIELD_ID);
        }
    }

    @Override
    public boolean deleteProject(Integer proId) throws ForbiddenException {
        if (query().eq("pro_id", proId).eq("pro_status", FAILURE_TO_REPORT.val).one() == null) {
            throw new ForbiddenException(CANNOT_DELETE_REPORTED_PROJECT);
        }
        return update().eq("pro_id", proId).set("pro_status", DELETED.val).update();
    }

    private static List<Project> filterNE(List<Project> list, ProjectStatusEnum status) {
        return list.stream().filter(p ->
                !p.getProStatus().equals(status.val)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean reportProject(List<Integer> proIds, Integer depId) throws ForbiddenException {
        List<Project> projects = getProjectsForReport(proIds);
        if (projects.isEmpty()) return false;
        List<Integer> prcIds = depPrcService.query().eq("dep_id", depId)
                .list().stream().map(DepPrc::getPrcId).collect(Collectors.toList());
        if (projects.stream().allMatch(p -> prcIds.contains(p.getPrcId()))) {
            projects.forEach(p -> p.setDepId(depId).setProStatus(PENDING_REVIEW.val));
            updateBatchById(projects);
            return true;
        }
        throw new ForbiddenException(REPORT_FAILED);
    }

    @Override
    @Transactional
    public boolean reportProject(List<Integer> proIds, List<Integer> depIds) {
        if (proIds.size() != depIds.size()) return false;
        List<Project> projects = getProjectsForReport(proIds);
        if (projects.isEmpty()) return false;

        testPerm(proIds, depIds, projects);
        updateBatchById(projects);
        return true;
    }

    private List<Project> getProjectsForReport(List<Integer> proIds) throws ForbiddenException {
        List<Project> projects = query().select("pro_id", "prc_id",
                "pro_name", "pro_status").in("pro_id", proIds).list();
        // 检查上报的科室是否具有管理该类型的项目的权限
        List<Project> illegalList = filterNE(projects, FAILURE_TO_REPORT);
        int size = illegalList.size();
        if (size > 0) {
            // 说明项目已上报
            StringBuilder msg = genMessage(illegalList, size).append("已上报，不能再次上报");
            throw new ForbiddenException(msg.toString());
        }
        return projects;
    }

    private void testPerm(List<Integer> proIds, List<Integer> depIds, List<Project> projects) throws ForbiddenException {
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
                throw new ForbiddenException(REPORT_FAILED);
            }
            project.setDepId(depId);
            project.setProStatus(PENDING_REVIEW.val);
        }
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
    public Object importExcel(MultipartFile file) {

        int uid = UserHolder.getUser().getUId();

        try {
            List<ProjectExcelVO> projectExcelVOList = EasyExcel.read(file.getInputStream())
                    .head(ProjectExcelVO.class)
                    .sheet()
                    .doReadSync();
            Date now = new Date();

            // 错误信息集合
            List<String> errorMsgList = new ArrayList<>();
            for (ProjectExcelVO projectExcelVO : projectExcelVOList) {
                if (projectExcelVO.getProDate()     == null) projectExcelVO.setProDate(now);
                if (projectExcelVO.getCouName()     == null) errorMsgList.add("辖区不能为空；");
                if (projectExcelVO.getProName()     == null) errorMsgList.add("项目名称不能为空；");
                if (projectExcelVO.getProLocation() == null) errorMsgList.add("建设地点不能为空；");
                if (projectExcelVO.getPrcName()     == null) errorMsgList.add("项目类型名称不能为空；");
                if (projectExcelVO.getInfName()     == null) errorMsgList.add("行业领域名称不能为空；");
            }
            if (!errorMsgList.isEmpty())
                return (errorMsgList.stream().distinct().collect(Collectors.toList()).toString());

            Map<String, Integer> depNameMapId = depService.all().stream().collect(Collectors.toMap(Department::getDepName, Department::getDepId));
            Map<String, Integer> couNameMapId = countyService.all().stream().collect(Collectors.toMap(County::getCouName, County::getCouId));
            Map<String, Town> townNameMapTown = townService.all().stream().collect(Collectors.toMap(Town::getTownName, t -> t));
            Map<String, Integer> prcNameMapId = prcService.all().stream().collect(Collectors.toMap(ProjectCategory::getPrcName, ProjectCategory::getPrcId));
            Map<String, Integer> infNameMapId = infService.all().stream().collect(Collectors.toMap(IndustryField::getInfName, IndustryField::getInfId));

            List<Project> projectList = new ArrayList<>();
            for (ProjectExcelVO projectExcelVO : projectExcelVOList) {
                Project project = new Project();
                BeanUtil.copyProperties(projectExcelVO, project);
                String departmentName = projectExcelVO.getDepartmentName();
                if (departmentName != null) {
                    Integer depId = depNameMapId.get(departmentName);
                    if (Objects.nonNull(depId)) {
                        project.setDepId(depId);
                    } else {  // 若为空
                        errorMsgList.add("不存在科室名“" + departmentName + "”；");
                    }
                }

                Integer couId = couNameMapId.get(projectExcelVO.getCouName());
                if (Objects.nonNull(couId)) {
                    project.setCouId(couId);
                    String townName = projectExcelVO.getTownName();
                    if (Objects.nonNull(townName)) {
                        Town town = townNameMapTown.get(townName);
                        if (Objects.nonNull(town)) {
                            if (town.getCouId().equals(couId)) {
                                project.setTownId(town.getTownId());
                            } else {
                                errorMsgList.add("辖区“" + projectExcelVO.getCouName()
                                        + "”与二级辖区“" + townName + "”不匹配；");
                            }
                        } else {  // 若为空
                            errorMsgList.add("不存在名称为“" + townName + "”的二级辖区；");
                        }
                    }
                } else {  // 若为空
                    errorMsgList.add("不存在名称为“" + projectExcelVO.getCouName() + "”的辖区；");
                }

                Integer prcId = prcNameMapId.get(projectExcelVO.getPrcName());
                if (Objects.nonNull(prcId)) {
                    project.setPrcId(prcId);
                } else {  // 若为空
                    errorMsgList.add("不存在名称为“" + projectExcelVO.getPrcName() + "”的项目类型；");
                }

                Integer infId = infNameMapId.get(projectExcelVO.getInfName());
                if (Objects.nonNull(infId)) {
                    project.setInfId(infId);
                } else {  // 若为空
                    errorMsgList.add("不存在名称为“" + projectExcelVO.getInfName() + "”的行业领域；");

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
                project.setProMark(
                        StrUtil.nullToEmpty(projectExcelVO.getProMark1()) + "᛭" +
                        StrUtil.nullToEmpty(projectExcelVO.getProMark2()) + "᛭" +
                        StrUtil.nullToEmpty(projectExcelVO.getProMark3())
                );

                projectList.add(project);
            }
            // 错误信息
            if (!errorMsgList.isEmpty()) {
                return errorMsgList.stream().distinct().collect(Collectors.toList()).toString();
            }

            int total = projectList.size();

            // 插入的项目 项目代码以及入统入库代码必须唯一
            for (int i = 0; i < projectList.size(); i++) {
                String proCode = projectList.get(i).getProCode();
                if (Objects.isNull(proCode)) continue;
                for (int j = i + 1; j < projectList.size(); j++) {
                    String proCode1 = projectList.get(j).getProCode();
                    if (Objects.isNull(proCode1)) continue;
                    if (proCode.equals(proCode1)) return "项目代码必须保证唯一";
                }
            }
            for (int i = 0; i < projectList.size(); i++) {
                String proCode = projectList.get(i).getProInCode();
                if (Objects.isNull(proCode)) continue;
                for (int j = i + 1; j < projectList.size(); j++) {
                    String proCode1 = projectList.get(j).getProInCode();
                    if (Objects.isNull(proCode1)) continue;
                    if (proCode.equals(proCode1)) return "入统入库代码必须保证唯一";
                }
            }

            saveBatch(projectList);
            return total;

        } catch (IOException e) {
            return "导入失败";
        } catch (ExcelCommonException e) {
            return "请选择.xlsx或.xls文件";
        }
    }

    private void checkPendingReview(List<Integer> proIds, String oper) {
        List<Project> projects = query().select("pro_id", "pro_name", "pro_status")
                .in("pro_id", proIds).list();
        if (projects.size() < proIds.size()) {
            throw new ForbiddenException(FAIL_MSG);
        }
        List<Project> illegalList = filterNE(projects, PENDING_REVIEW);
        int size = illegalList.size();
        throw new ForbiddenException(genMessage(illegalList, size)
                .append(String.format("不是待审核状态，不能执行%s操作", oper)).toString());
    }

    @Override
    @Transactional
    public boolean accept(List<Integer> proIds) {
        checkPendingReview(proIds, "批准");
        // TODO 批准要发送 WebSocket 通知
        updateBaseData("prc", true);
        List<Project> projects = query().in("pro_id", proIds)
                .eq("pro_status", PENDING_REVIEW.val).list();
        updateNextUpdateTime(projects);
        projects.forEach(p -> p.setProStatus(NORMAL.val));
        return updateBatchById(projects);
    }

    @Override
    public boolean reject(List<Integer> proIds) {
        checkPendingReview(proIds, "驳回");
        // TODO 驳回要发送 WebSocket 通知
        return update().in("pro_id", proIds)
                .eq("pro_status", PENDING_REVIEW.val)
                .set("pro_status", FAILURE_TO_REPORT.val)
                .set("dep_id", null).update();
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
    public boolean markAsComplete(List<Integer> proIds) {
        return update().isNull("pro_dis_complete")
                .in( "pro_id",     proIds)
                .in( "pro_status", status)
                .set("pro_status", COMPLETE.val)
                .set("pro_dis_complete", new Date())
                .set("pro_next_update",  null).update();
    }

    private static final List<Integer> status1 = Arrays.asList(NORMAL.val, UNLOCKED.val);

    @Override
    public void updateDispatchStatus() {
        update().in("pro_status", status1).le("pro_next_update", new Date())
                .set("pro_status", TO_BE_SCHEDULED.val).update();
    }

    @Override
    @Transactional
    public Integer getDispatchingCount() {
        User user = userService.getById(UserHolder.getUser().getUId());
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setUId(user.getUId());
        projectDTO.setDepId(user.getDepId());
        projectDTO.setCouId(user.getCouId());
        projectDTO.setProStatus(TO_BE_SCHEDULED);
        return baseMapper.getDispatchingCount(projectDTO);
    }

    @Override
    @Transactional
    public QueryProjectVO getProject(Integer proId) {
        Project project = getById(proId);
        if (project == null) return null;
        updateBaseData();
        return mapProjectToVO.apply(project);
    }

    @Override
    public void clearDispatch() {
        update().set("pro_dis_year", 0).set("pro_dis_year_percent", 0).update();
    }

    @Override
    public PageInfo<QueryProjectVO> getVagueDispatchProject(Page page, ProjectDTO projectDTO) throws ForbiddenException {
        int status = Optional.ofNullable(projectDTO.getProStatus()).orElse(1);
        if (status != 3 && status != 4 && status != 1) {
            throw new ForbiddenException(ILLEGAL_PROJECT_STATUS);
        }
        projectDTO.setUId(UserHolder.getUser().getUId());
        updateBaseData();
        return page(page, () -> baseMapper.getVagueDispatchProjectByUser(projectDTO));
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadTemplate() {
        try {
            File file = resourceLoader.getResource("classpath:template.xlsx").getFile();

            return ResponseEntity.ok().headers(h -> {
                h.add(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=%E5%AF%BC%E5%85%A5%E9%A1%B9%E7%9B%AE%E6%A8%A1%E6%9D%BF.xlsx");
                h.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
                h.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
                h.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            }).body(new InputStreamResource(Files.newInputStream(file.toPath())));
        } catch (IOException e) {
            log.error("获取导入模板文件失败。", e);
            byte[] bytes = JSONUtil.toJsonStr(ResultResponse.fail(LOAD_TEMPLATE_FAILED)).getBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InputStreamResource(bais));
        }
    }

}
