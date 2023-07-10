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
@ApiModel(value = "项目导入Excel")
public class ProjectExcelVO {

    @ExcelProperty(value = "日期")
    @DateTimeFormat("yyyy-MM-dd")
    private Date proDate;

    @ExcelProperty(value = "项目名称")
    private String proName;

    @ExcelProperty(value = "项目内容")
    private String proContent;


    @ExcelProperty(value = "建设地点")
    private String proLocation;

    @ExcelProperty(value = "审核单位名称")
    private String departmentName;

    @ExcelProperty(value = "辖区")
    private String couName;

    @ExcelProperty(value = "二级辖区")
    private String townName;

    @ExcelProperty(value = "项目类型名称")
    private String prcName;

    @ExcelProperty(value = "行业领域名称")
    private String infName;

    @ExcelProperty(value = "项目代码")
    private String proCode;

    @ExcelProperty(value = "入库入统代码")
    private String proInCode;

    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "计划开工时间")
    private Date proStart;

    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "计划竣工时间")
    private Date proComplete;

    @ExcelProperty(value = "计划总投资")
    private Integer proPlan;

    @ExcelProperty(value = "年计划完成投资")
    private Integer proPlanYear;

    @ExcelProperty(value = "项目法人或责任单位")
    private String proLegalPerson;

    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "实际开工日期")
    private Date proDisStart;

    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "实际竣工日期")
    private Date proDisComplete;

    @ExcelProperty(value = "是否当年度新开工项目")
    private String proIsNew;

    @ExcelProperty(value = "是否省大中型项目")
    private String proIsProvincial;

    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "下次调度的日期")
    private Date proNextUpdate;

    @ExcelProperty(value = "投资类别")
    private String proType;

    @ExcelProperty(value = "部门和地方采取的资金安排方式")
    private String proArrange;

    @ExcelProperty(value = "日常监管直接责任单位")
    private String proGuarantee;

    @ExcelProperty(value = "累计完成投资")
    private Integer proDisTotal;

    @ExcelProperty(value = "今年已完成投资")
    private Integer proDisYear;

    @ExcelProperty(value = "累积投资完成率% 百分比值的 100 倍")
    private Integer proDisTotalPercent;

    @ExcelProperty(value = "今年投资完成率% 百分比值的 100 倍")
    private Integer proDisYearPercent;

    @ExcelProperty(value = "主要形象进度")
    private String proDisProgress;

    @ExcelProperty(value = "上次调度的日期")
    private Date proLastDis;

    @ExcelProperty(value = "标记")
    private String proMark;

    @ExcelProperty(value = "备注")
    private String proRemark;
}
