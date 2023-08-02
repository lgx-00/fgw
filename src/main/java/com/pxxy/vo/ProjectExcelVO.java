package com.pxxy.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @Author: hesen
 * @Date: 2023-06-26-9:37
 * @Description:
 */
@Data
@ApiModel("项目导入Excel")
public class ProjectExcelVO {

    @ExcelProperty("日期")
    @DateTimeFormat("yyyy-MM-dd")
    private Date proDate;

    @ExcelProperty("项目名称")
    private String proName;

    @ExcelProperty("项目内容")
    private String proContent;


    @ExcelProperty("建设地点")
    private String proLocation;

    @ExcelProperty("审核单位名称")
    private String departmentName;

    @ExcelProperty("辖区")
    private String couName;

    @ExcelProperty("二级辖区")
    private String townName;

    @ExcelProperty("项目类型名称")
    private String prcName;

    @ExcelProperty("行业领域名称")
    private String infName;

    @ExcelProperty("项目代码")
    private String proCode;

    @ExcelProperty("入库入统代码")
    private String proInCode;

    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("计划开工时间")
    private Date proStart;

    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("计划竣工时间")
    private Date proComplete;

    @ExcelProperty("计划总投资")
    private Integer proPlan;

    @ExcelProperty("年计划完成投资")
    private Integer proPlanYear;

    @ExcelProperty("项目法人或责任单位")
    private String proLegalPerson;

    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("实际开工日期")
    private Date proDisStart;

    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("实际竣工日期")
    private Date proDisComplete;

    @ExcelProperty("是否当年度新开工项目")
    private String proIsNew;

    @ExcelProperty("是否省大中型项目")
    private String proIsProvincial;

    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty("下次调度的日期")
    private Date proNextUpdate;

    @ExcelProperty("投资类别")
    private String proType;

    @ExcelProperty("部门和地方采取的资金安排方式")
    private String proArrange;

    @ExcelProperty("日常监管直接责任单位")
    private String proGuarantee;

    @ExcelProperty("累计完成投资")
    private Integer proDisTotal;

    @ExcelProperty("今年已完成投资")
    private Integer proDisYear;

    @ExcelProperty("累积投资完成率% 百分比值的 100 倍")
    private Integer proDisTotalPercent;

    @ExcelProperty("今年投资完成率% 百分比值的 100 倍")
    private Integer proDisYearPercent;

    @ExcelProperty("主要形象进度")
    private String proDisProgress;

    @ExcelProperty("上次调度的日期")
    private Date proLastDis;

    @ExcelProperty("标记1")
    private String proMark1;

    @ExcelProperty("标记2")
    private String proMark2;

    @ExcelProperty("标记3")
    private String proMark3;

    @ExcelProperty("备注")
    private String proRemark;
}
