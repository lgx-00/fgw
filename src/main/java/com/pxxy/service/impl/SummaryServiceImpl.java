package com.pxxy.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pxxy.dto.UserDTO;
import com.pxxy.enums.ProjectStatusEnum;
import com.pxxy.mapper.ProjectMapper;
import com.pxxy.mapper.TownMapper;
import com.pxxy.pojo.*;
import com.pxxy.service.*;
import com.pxxy.utils.ResultResponse;
import com.pxxy.utils.UserHolder;
import com.pxxy.vo.SummaryVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * @Author: XRW
 * @CreateTime: 2023-07-01  14:40
 */

@Service
public class SummaryServiceImpl implements SummaryService {

    @Resource
    private UserService userService;


    @Resource
    private TownMapper townMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public ResultResponse getSummary(Integer summaryType, Date beginTime, Date endTime, Integer prcId, Integer infId) {
        //先拿到用户信息
        UserDTO user = UserHolder.getUser();
        Integer uId = user.getUId();
        //管理员特殊通道   区本级也可以访问
        if (uId == 1) {
            QueryWrapper<Project> wrapper = new QueryWrapper<>();
            wrapper.eq("pro_status", ProjectStatusEnum.NORMAL.getStatusContent())    //只查询项目状态为正常的 0
                    .eq(prcId != null, "prc_id", prcId)
                    .eq(infId != null, "inf_id", infId)
                    .ge(beginTime != null, "pro_date", beginTime)
                    .le(endTime != null, "pro_date", endTime);
            List<Project> projects = projectMapper.selectList(wrapper);
            Integer total = projectMapper.selectCount(wrapper);
            if (projects == null || total == null) {
                return ResultResponse.fail("无符合查询的数据！");
            }
            //对同一乡镇的项目进行分组
            Map<Integer, List<Project>> listMap = projects.stream().collect(Collectors.groupingBy(Project::getTownId));

            List<SummaryVO> summaryVOList = listMap.keySet().stream().map(key -> {
                SummaryVO summaryVO = new SummaryVO();
                summaryVO.setTownName(townMapper.selectById(key).getTownName());
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

                String ratio = String.format("%.2f%%", calculatePercentage(count.get(), projectsList.size()));
                summaryVO.setRatio(ratio);

                int proAllPlan = projectsList.stream().mapToInt(Project::getProPlan).sum();
                summaryVO.setProAllPlan(proAllPlan);

                int proPlanYear = projectsList.stream().mapToInt(Project::getProPlanYear).sum();
                summaryVO.setProPlanYear(proPlanYear);
                return summaryVO;
            }).collect(Collectors.toList());

            //全区
            SummaryVO summaryVO = new SummaryVO();
            summaryVO.setTownName("湘东区");
            summaryVO.setProjectNum(total);
            int projectWorkNum = summaryVOList.stream().mapToInt(SummaryVO::getProjectWorkNum).sum();
            summaryVO.setProjectWorkNum(projectWorkNum);
            String ratio = String.format("%.2f%%", calculatePercentage(projectWorkNum, total));
            summaryVO.setRatio(ratio);
            int projectAllPlan = summaryVOList.stream().mapToInt(SummaryVO::getProAllPlan).sum();
            summaryVO.setProAllPlan(projectAllPlan);
            int proPlanYear = summaryVOList.stream().mapToInt(SummaryVO::getProPlanYear).sum();
            summaryVO.setProPlanYear(proPlanYear);
            summaryVOList.add(summaryVO);
            return ResultResponse.ok(summaryVOList);
        }

        // 非管理员通道
        User user1 = userService.query().eq("u_id", uId).one();
        Integer depId = user1.getDepId();  //科室
        Integer couId = user1.getCouId();   //辖区
        List<Project> projectList = projectMapper.getProjectByUser(depId, couId, uId);
        // 记录总条数
        Integer total = projectList.size();
        if (projectList == null || total == null) {
            return ResultResponse.fail("无符合查询的数据！");
        }
        //对同一乡镇的项目进行分组
        Map<Integer, List<Project>> listMap = projectList.stream().collect(Collectors.groupingBy(Project::getTownId));

        List<SummaryVO> summaryVOList = listMap.keySet().stream().map(key -> {
            SummaryVO summaryVO = new SummaryVO();
            summaryVO.setTownName(townMapper.selectById(key).getTownName());
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

            String ratio = String.format("%.2f%%", calculatePercentage(count.get(), projectsList.size()));
            summaryVO.setRatio(ratio);

            int proAllPlan = projectsList.stream().mapToInt(Project::getProPlan).sum();
            summaryVO.setProAllPlan(proAllPlan);

            int proPlanYear = projectsList.stream().mapToInt(Project::getProPlanYear).sum();
            summaryVO.setProPlanYear(proPlanYear);
            return summaryVO;
        }).collect(Collectors.toList());

        return ResultResponse.ok(summaryVOList);
    }

    @Override
    public ResultResponse exportSummaryExcel(HttpServletResponse response, List<SummaryVO> summaryVOList) {
        try {
            this.setExcelResponseProp(response, "统计汇总");
            EasyExcel.write(response.getOutputStream())
                    .head(SummaryVO.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("统计汇总")
                    .doWrite(summaryVOList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResultResponse.ok();
    }

    //用于计算百分比
    public static double calculatePercentage(double numerator, double denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("分母不能为零");
        }

        return (numerator / denominator) * 100;
    }


    private void setExcelResponseProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }


}