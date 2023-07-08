package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.exception.ExcelCommonException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageInfo;
import com.pxxy.dto.GetVagueProjectDTO;
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

import static com.pxxy.constant.SystemConstant.*;

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

    private final Function<Project, QueryProjectVO> mapProjectToVO = project -> {
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
        if (county != null) {
            queryProjectVO.setArea(town != null
                    ? county.getCouName() + "-" + town.getTownName()
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
        return queryProjectVO;
    };

    @Override
    public ResultResponse<PageInfo<QueryProjectVO>> getAllProject(Integer pageNum) {
        //先拿到用户信息
        UserDTO user = UserHolder.getUser();
        Integer uId = user.getUId();
        //管理员特殊通道
        if (uId == 1) return ResultResponse.ok(PageUtil.selectPage(pageNum,
                DEFAULT_PAGE_SIZE, () -> this.query().list(), mapProjectToVO));

        // 非管理员通道
        User u = userService.query().eq("u_id", uId).one();
        Integer depId = u.getDepId();
        Integer couId = u.getCouId();

        PageInfo<QueryProjectVO> pageInfo = PageUtil.selectPage(pageNum, DEFAULT_PAGE_SIZE,
                () -> projectMapper.getAllProjectByUser(depId, couId, uId), mapProjectToVO);
        return ResultResponse.ok(pageInfo);
    }

    @Override
    public ResultResponse<PageInfo<QueryProjectVO>> getVagueProject(
            Integer pageNum, String proName, Date beginTime, Date endTime,
            Integer couId, Integer townId, Integer prcId, Integer infId,
            Integer proStatus, Integer projectStage
    ) {
        //先拿到用户信息
        UserDTO user = UserHolder.getUser();
        Integer uId = user.getUId();

        // 抽取查询为一个 Lambda 表达式
        ISelect select = () -> ProjectStageEnum.values()[projectStage].list(query()
                    .like(Objects.nonNull(proName), "pro_name", proName)
                    .eq(Objects.nonNull(couId), "cou_id", couId)
                    .eq(Objects.nonNull(townId), "town_id", townId)
                    .eq(Objects.nonNull(prcId), "prc_id", prcId)
                    .eq(Objects.nonNull(infId), "inf_id", infId)
                    .eq(Objects.nonNull(proStatus), "pro_status", proStatus)
                    .between(Objects.nonNull(beginTime) || Objects.nonNull(endTime), "pro_date",
                            Optional.ofNullable(beginTime).orElse(ZERO_DATE),
                            Optional.ofNullable(endTime).orElse(INFINITY_DATE)));

        // 管理员特殊通道, 使用上边的查询
        if (uId == 1) return ResultResponse.ok(PageUtil
                .selectPage(pageNum, DEFAULT_PAGE_SIZE, select, mapProjectToVO));

        // 非管理员通道
        User u = userService.query().eq("u_id", uId).one();
        Integer uCouId = u.getCouId();
        Integer uDepId = u.getDepId();
        GetVagueProjectDTO dto = new GetVagueProjectDTO(uDepId, uCouId, uId, proName, townId,
                prcId, infId, proStatus, beginTime, endTime, projectStage);
        return ResultResponse.ok(PageUtil.selectPage(pageNum, DEFAULT_PAGE_SIZE, () ->
                projectMapper.getVagueProjectByUser(dto), mapProjectToVO));
    }

    @Override
    public ResultResponse<?> addProject(AddProjectVO addProjectVO) {
        Project project = new Project();
        BeanUtil.copyProperties(addProjectVO, project);
        UserDTO user = UserHolder.getUser();
        project.setUId(user.getUId());
        save(project);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<?> updateProject(UpdateProjectVO updateProjectVO) {
        Project project = query().eq("pro_id", updateProjectVO.getProId()).one();
        if (project == null) {
            return ResultResponse.fail("非法操作！");
        }
        BeanUtil.copyProperties(updateProjectVO, project);
        updateById(project);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<?> deleteProject(Integer proId) {
        Project project = query().eq("pro_id", proId).ne("pro_status", DELETED_STATUS).one();
        if (project == null) {
            return ResultResponse.fail("项目已被删除，请勿重复操作！");
        }
        removeById(proId);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<?> reportProject(Integer proId, Integer depId) {
        Project project = query().eq("pro_id", proId).one();
        //未上报状态
        ProjectStatusEnum report = ProjectStatusEnum.FAILURE_TO_REPORT;
        //待审核状态
        ProjectStatusEnum pendingReview = ProjectStatusEnum.PENDING_REVIEW;

        if (project.getProStatus() != report.val) {
            //说明项目已上报
            return ResultResponse.fail("已上报项目不允许再上报！");
        }
        project.setDepId(depId);
        project.setProStatus(pendingReview.val);
        updateById(project);
        return ResultResponse.ok();
    }

    @Override
    @Transactional
    public ResultResponse<?> importExcel(MultipartFile file) {
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
                    errorMsgList.add("乡镇不能为空；");
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
                } else {
                    //若为空
                    errorMsgList.add("不存在辖区名（" + couName + "）；");
                }

                String townName = projectExcelVO.getTownName();
                Town town = townService.query().eq("town_name", townName).one();
                if (town != null) {
                    if (town.getCouId().equals(county.getCouId())) {
                        project.setTownId(town.getTownId());
                    } else {
                        errorMsgList.add("【辖区】" + county.getCouName() + "不存在" + "【乡镇】" + townName + "；");
                    }
                } else {
                    //若为空
                    errorMsgList.add("不存在乡镇名（" + townName + "）；");
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
                project.setUId(UserHolder.getUser().getUId());

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

    @Override
    public ResultResponse<PageInfo<QueryProjectVO>> getExamineProject(
            Integer pageNum, String proName, Date beginTime, Date endTime,
            Integer couId, Integer townId, Integer prcId, Integer infId, Integer projectStage
    ) {
        //获取用户信息
        UserDTO user = UserHolder.getUser();
        Integer depId = userService.query().eq("u_id", user.getUId()).one().getDepId();
        return ResultResponse.ok(
                PageUtil.selectPage(pageNum, DEFAULT_PAGE_SIZE, () ->
                        projectMapper.getExamineProjectByUser(depId, proName, townId, prcId, infId), mapProjectToVO)
        );
    }

    @Override
    public ResultResponse<PageInfo<QueryProjectVO>> getDispatchProject(
            Integer pageNum, String proName, Date beginTime, Date endTime,
            Integer couId, Integer townId, Integer prcId, Integer infId, Integer projectStage
    ) {
        //获取用户信息
        UserDTO user = UserHolder.getUser();
        User u = userService.query().eq("u_id", user.getUId()).one();
        Integer depId = u.getDepId();
        List<Project> projectList = projectMapper.getDispatchProjectByUser(depId, proName, townId, prcId, infId);
        List<QueryProjectVO> queryProjectVOS = this.packProjectVO(projectList, beginTime, endTime);
        //当前时间
        Date date = new Date();
        List<QueryProjectVO> projectVOS = this.calProjectStage(queryProjectVOS, projectStage, date);

        // FIXME
        List<QueryProjectVO> projectVOList = projectVOS.stream().skip((pageNum - 1) * 10L).limit(DEFAULT_PAGE_SIZE).collect(Collectors.toList());
        return ResultResponse.ok(PageInfo.of(projectVOList));
    }

    // 用于计算项目阶段
    private List<QueryProjectVO> calProjectStage(List<QueryProjectVO> queryProjectVOS, Integer projectStage, Date date) {
        return queryProjectVOS.stream().filter(queryProjectVO -> {
            if (projectStage != null && queryProjectVO.getProDisStart() != null) {
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

    // project -> QueryProjectVO(其实就是为了减少代码冗余，并无实际意义)
    private List<QueryProjectVO> packProjectVO(List<Project> projectList, Date beginTime, Date endTime) {
        return projectList.stream().filter(project -> {
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
                if (ps == projectStatusEnum.val) {
                    queryProjectVO.setProStatusContent(projectStatusEnum.name);
                }
            }
            return queryProjectVO;
        }).collect(Collectors.toList());
    }

}
