package com.pxxy.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pxxy.mapper.DispatchMapper;
import com.pxxy.mapper.ProjectMapper;
import com.pxxy.entity.pojo.*;
import com.pxxy.service.*;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import com.pxxy.entity.vo.DashboardVO;
import com.pxxy.entity.vo.SummaryDetailsVO;
import com.pxxy.entity.vo.SummaryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.pxxy.enums.ProjectStatusEnum.*;


/**
 * @author XRW
 * @CreateTime: 2023-07-01  14:40
 */

@Service
public class SummaryServiceImpl implements SummaryService {

    @Resource
    private UserService userService;


    @Resource
    private TownService townService;

    @Resource
    private DispatchService dispatchService;

    @Resource
    private ProjectService projectService;

    @Resource
    private IndustryFieldService infService;

    @Resource
    private ProjectCategoryService prcService;

    private static Map<Integer, String> infMap;
    private static Map<Integer, String> prcMap;
    private static Map<Integer, String> townMap;

    @Override
    @Transactional
    public ResultResponse<List<SummaryVO>> getSummary(Date beginTime, Date endTime, Integer prcId, Integer infId) {
        //先拿到用户信息
        Integer uId = UserHolder.getUser().getUId();
        //管理员特殊通道   区本级也可以访问
        if (uId == 1) {
            QueryWrapper<Project> wrapper = new QueryWrapper<>();
            wrapper.in("pro_status", Arrays.asList(NORMAL.val, UNLOCKED.val, TO_BE_SCHEDULED.val))
                    .eq(Objects.nonNull(prcId), "prc_id", prcId)
                    .eq(Objects.nonNull(infId), "inf_id", infId)
                    .ge(Objects.nonNull(beginTime), "pro_date", beginTime)
                    .le(Objects.nonNull(endTime), "pro_date", endTime);
            List<Project> projects = projectService.list(wrapper);
            if (projects.size() == 0) {
                return ResultResponse.ok(Collections.emptyList());
            }
            List<SummaryVO> summaryVOList = groupSummary(projects);


            //全区
            SummaryVO summaryVO = new SummaryVO();
            summaryVO.setTownName("湘东区");
            summaryVO.setProjectNum(projects.size());
            int projectWorkNum = summaryVOList.stream().mapToInt(SummaryVO::getProjectWorkNum).sum();
            summaryVO.setProjectWorkNum(projectWorkNum);
            String ratio = calculatePercentage(projectWorkNum, projects.size());
            summaryVO.setRatio(ratio);
            summaryVO.setProAllPlan(summaryVOList.stream().mapToInt(SummaryVO::getProAllPlan).sum());
            int proPlanYear = summaryVOList.stream().mapToInt(SummaryVO::getProPlanYear).sum();
            summaryVO.setProPlanYear(proPlanYear);
            int proPlanMonths = summaryVOList.stream().mapToInt(SummaryVO::getProPlanMonths).sum();
            summaryVO.setProPlanMonths(proPlanMonths);
            if (proPlanMonths == 0) {
                summaryVO.setProCompletionPercent("0%");
            } else {
                String calculatePercentage = calculatePercentage(proPlanMonths, proPlanYear);
                summaryVO.setProCompletionPercent(calculatePercentage);
            }
            summaryVO.setChildren(summaryVOList);
            //返回值
            ArrayList<SummaryVO> summaryVOArrayList = new ArrayList<>();
            summaryVOArrayList.add(summaryVO);
            return ResultResponse.ok(summaryVOArrayList);
        }

        // 非管理员通道
        User user = userService.query().eq("u_id", uId).one();
        Integer depId = user.getDepId();  //科室
        Integer couId = user.getCouId();   //辖区
        List<Project> projectList = ((ProjectMapper) projectService.getBaseMapper()).getProjectByUser(depId, couId, uId);
        if (projectList.size() == 0) {
            return ResultResponse.ok(Collections.emptyList());
        }
        List<SummaryVO> summaryVOList = groupSummary(projectList);

        return ResultResponse.ok(summaryVOList);
    }

    @Override
    @Transactional
    public ResultResponse<List<SummaryDetailsVO>> detailsSummary(Date beginTime, Date endTime, Integer prcId, Integer infId) {
        //先拿到用户信息
        Integer uId = UserHolder.getUser().getUId();
        //管理员特殊通道   区本级也可以访问
        if (uId == 1) {
            QueryWrapper<Project> wrapper = new QueryWrapper<>();
            wrapper.in("pro_status", Arrays.asList(NORMAL.val, UNLOCKED.val, TO_BE_SCHEDULED.val))
                    .eq(Objects.nonNull(prcId), "prc_id", prcId)
                    .eq(Objects.nonNull(infId), "inf_id", infId)
                    .ge(Objects.nonNull(beginTime), "pro_date", beginTime)
                    .le(Objects.nonNull(endTime), "pro_date", endTime);
            List<Project> projects = projectService.list(wrapper);
            if (projects.size() == 0) {
                return ResultResponse.ok(Collections.emptyList());
            }
            LocalDate[] firstDay = getFirstDay();
            List<Dispatch> dispatchList = ((DispatchMapper) dispatchService.getBaseMapper()).getDispatch(firstDay[0], firstDay[1]);
            List<SummaryDetailsVO> listList = groupSummaryDetails(projects, dispatchList);

            //全区
            SummaryDetailsVO summaryDetailsVO = new SummaryDetailsVO()
                    .setProName("全区")
                    .setProjectNum(projects.size())
                    .setProAllPlan(listList.stream().mapToInt(SummaryDetailsVO::getProAllPlan).sum())
                    .setProPlanYear(listList.stream().mapToInt(SummaryDetailsVO::getProYear).sum())
                    .setProPlanMonths(listList.stream().mapToInt(SummaryDetailsVO::getProPlanMonths).sum())
                    .setProPlanMonth(listList.stream().mapToInt(SummaryDetailsVO::getProPlanMonth).sum())
                    .setProYear(listList.stream().mapToInt(SummaryDetailsVO::getProYear).sum());
            if (listList.stream().mapToInt(SummaryDetailsVO::getProYear).sum() != 0) {
                summaryDetailsVO.setProPlanCompletionPercent(calculatePercentage(listList.stream().mapToInt(SummaryDetailsVO::getProYear).sum(), listList.stream().mapToInt(SummaryDetailsVO::getProYear).sum()));
            } else {
                summaryDetailsVO.setProPlanCompletionPercent("0%");
            }
            summaryDetailsVO.setChildren(listList);
            //返回值
            ArrayList<SummaryDetailsVO> summaryDetailsVOArrayList = new ArrayList<>();
            summaryDetailsVOArrayList.add(summaryDetailsVO);
            return ResultResponse.ok(summaryDetailsVOArrayList);
        }

        // 非管理员通道
        User user = userService.query().eq("u_id", uId).one();
        Integer depId = user.getDepId();  //科室
        Integer couId = user.getCouId();   //辖区
        List<Project> projectList = ((ProjectMapper) projectService.getBaseMapper()).getProjectByUser(depId, couId, uId);
        if (projectList.size() == 0) {
            return ResultResponse.ok(Collections.emptyList());
        }
        LocalDate[] firstDay = getFirstDay();
        List<Dispatch> dispatchList = ((DispatchMapper) dispatchService.getBaseMapper()).getDispatch(firstDay[0], firstDay[1]);
        List<SummaryDetailsVO> listList = groupSummaryDetails(projectList, dispatchList);

        return ResultResponse.ok(listList);
    }

    @Override
    public ResultResponse<?> exportSummaryExcel(HttpServletResponse response, List<SummaryVO> summaryVOList) {
        try {
            // 设置文本类型
            response.setContentType("application/vnd.ms-excel");
            // 设置字符编码
            response.setCharacterEncoding("utf-8");
            // 设置响应头
            response.setHeader("Content-disposition", "attachment;filename=统计信息.xlsx");
            EasyExcel.write(response.getOutputStream(), SummaryVO.class).sheet("导出统计信息").doWrite(summaryVOList);
        } catch (IOException e) {
            return ResultResponse.fail("导出失败！");
        }
        return null;
    }

    private List<SummaryVO> groupSummary(List<Project> projects) {
        //对同一辖区的项目进行分组
        Map<Integer, List<Project>> listMap = projects.stream().collect(Collectors.groupingBy(Project::getCouId));
        townMap = townService.all().stream().collect(Collectors.toMap(Town::getTownId, Town::getTownName));
        return listMap.keySet().stream().map(key -> {
            SummaryVO summaryVO = new SummaryVO();
            summaryVO.setTownName(townMap.get(key));
            List<Project> projectsList = listMap.get(key);  //组内对象集合
            summaryVO.setProjectNum(projectsList.size()); // 项目个数
            AtomicInteger count = new AtomicInteger(0); // 初始化计数器为0
            projectsList.forEach(project -> {
                Date date = new Date();
                Date proStart = project.getProDisStart();//实际开工日期
                Date proComplete = project.getProDisComplete(); //实际竣工日期（留空表示未竣工）
                if (proStart != null && proComplete == null && date.after(proStart)) {
                    count.incrementAndGet();  // 满足条件时(开工中项目)，计数器加一
                }
            });
            summaryVO.setProjectWorkNum(count.get());

            String ratio = calculatePercentage(count.get(), projectsList.size());
            summaryVO.setRatio(ratio);

            int proAllPlan = projectsList.stream().mapToInt(Project::getProDisTotal).sum();  //每个辖区所有项目总投资
            summaryVO.setProAllPlan(proAllPlan);

            int proPlanYear = projectsList.stream().mapToInt(Project::getProPlanYear).sum();  //年计划完成投资
            summaryVO.setProPlanYear(proPlanYear);

            int proPlanMonths = projectsList.stream().mapToInt(Project::getProDisYear).sum();  //今年完成投资
            summaryVO.setProPlanMonths(proPlanMonths);
            if (proPlanMonths == 0) {
                summaryVO.setProCompletionPercent("0%");
            } else {
                String calculatePercentage = calculatePercentage(proPlanMonths, proPlanYear);
                summaryVO.setProCompletionPercent(calculatePercentage);
            }
            return summaryVO;
        }).collect(Collectors.toList());
    }

    private void updateBaseData() {
        infMap = infService.all().stream()
                .collect(Collectors.toMap(IndustryField::getInfId, IndustryField::getInfName));
        prcMap = prcService.all().stream()
                .collect(Collectors.toMap(ProjectCategory::getPrcId, ProjectCategory::getPrcName));
        townMap = townService.all().stream()
                .collect(Collectors.toMap(Town::getTownId, Town::getTownName));
    }

    private List<SummaryDetailsVO> groupSummaryDetails(List<Project> projects, List<Dispatch> dispatchList) {
        // TODO: 2023/7/14 根据辖区分组
        //对同一辖区的项目进行分组
        Map<Integer, List<Project>> couIdMapProjects = projects.stream().collect(Collectors.groupingBy(Project::getCouId));
        updateBaseData();

        return couIdMapProjects.entrySet().stream().map(e -> {
            ArrayList<SummaryDetailsVO> vos = new ArrayList<>();     //此辖区下的项目集合
            List<Project> projectList = e.getValue();
            for (Project project : projectList) {
                SummaryDetailsVO summaryDetailsVO = new SummaryDetailsVO()
                        .setProName(project.getProName())
                        .setProjectNum(1)
                        .setProLocation(project.getProLocation())
                        .setProContent(project.getProContent())
                        .setProStartDate(project.getProDisStart())
                        .setProCompleteDate(project.getProDisComplete())
                        .setProAllPlan(project.getProDisTotal())
                        .setProPlanYear(project.getProPlanYear())
                        .setProYear(project.getProDisYear())
                        .setInfName(infMap.get(project.getInfId()))
                        .setPrcName(prcMap.get(project.getPrcId()))
                        .setProPlanCompletionPercent(calculatePercentage(project.getProDisYear(),
                            project.getProPlanYear()));

                if (dispatchList != null) {
                    for (Dispatch dispatch : dispatchList) {
                        if (dispatch.getProId().equals(project.getProId())) {
                            summaryDetailsVO.setProPlanMonths(dispatch.getDisYear());
                            summaryDetailsVO.setProPlanMonth(project.getProDisYear() - dispatch.getDisYear());
                        } else {
                            summaryDetailsVO.setProPlanMonths(0);
                            summaryDetailsVO.setProPlanMonth(0);
                        }
                    }
                } else {
                    summaryDetailsVO.setProPlanMonths(0);
                    summaryDetailsVO.setProPlanMonth(0);
                }
                vos.add(summaryDetailsVO);
            }
            //对辖区下面的项目进行统计
            SummaryDetailsVO summaryDetailsVO = new SummaryDetailsVO()
                    .setProName(townMap.get(e.getKey()))
                    .setProjectNum(projectList.size())
                    .setProAllPlan(vos.stream().mapToInt(SummaryDetailsVO::getProAllPlan).sum())
                    .setProPlanYear(vos.stream().mapToInt(SummaryDetailsVO::getProYear).sum())
                    .setProPlanMonths(vos.stream().mapToInt(SummaryDetailsVO::getProPlanMonths).sum())
                    .setProPlanMonth(vos.stream().mapToInt(SummaryDetailsVO::getProPlanMonth).sum())
                    .setProYear(vos.stream().mapToInt(SummaryDetailsVO::getProYear).sum());
            if (vos.stream().mapToInt(SummaryDetailsVO::getProYear).sum() != 0) {
                summaryDetailsVO.setProPlanCompletionPercent(calculatePercentage(
                        vos.stream().mapToInt(SummaryDetailsVO::getProYear).sum(),
                        vos.stream().mapToInt(SummaryDetailsVO::getProYear).sum()));
            } else {
                summaryDetailsVO.setProPlanCompletionPercent("0%");
            }
            summaryDetailsVO.setChildren(vos);

            return summaryDetailsVO;
        }).collect(Collectors.toList());
    }

    // 用于计算百分比
    public static String calculatePercentage(double numerator, double denominator) {
        if (denominator == 0) {
            return "除数为 0 ，无法计算";
        }
        return String.format("%.2f%%", (numerator / denominator) * 100);
    }

    /**
     * 获取当前年第一天和上个月最后一天。
     *
     * @return 当前年第一天和上个月最后一天。
     */
    private static LocalDate[] getFirstDay() {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 当前年第一天
        LocalDate firstDayOfYear = LocalDate.of(currentDate.getYear(), Month.JANUARY, 1);
        // 当前年上个月最后一天
        LocalDate lastDayOfLastMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1).minusDays(1);

        // 存储日期到数组  并且返回日期数组
        return new LocalDate[]{firstDayOfYear, lastDayOfLastMonth};
    }

    @Override
    public ResultResponse<DashboardVO> dashboard() {
        DashboardVO vo = new DashboardVO();
        Integer uId = UserHolder.getUser().getUId();

        return null;
    }

}