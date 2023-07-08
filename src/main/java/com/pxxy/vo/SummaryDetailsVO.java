package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @Author: XRW
 * @CreateTime: 2023-07-06  11:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "统计汇总详情")
@Accessors(chain = true)
public class SummaryDetailsVO {

    private static final long serialVersionUID = 1L;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String proName;

    /*
     * 项目个数
     **/
    @ApiModelProperty(value = "项目个数")
    private Integer projectNum;

    /**
     * 建设地点 非空
     */
    @ApiModelProperty(value = "建设地点")
    private String proLocation;

    /**
     * 项目内容
     */
    @ApiModelProperty(value = "项目内容")
    private String proContent;

    /**
     * 开工年月
     */
    @ApiModelProperty(value = "开工年月")
    private Date proStartDate;

    /**
     * 完工年月
     */
    @ApiModelProperty(value = "完工年月")
    private Date proCompleteDate;

    /**
     * 总投资
     */
    @ApiModelProperty(value = "总投资")
    private Integer proAllPlan;

    /**
     * 年计划完成投资
     */
    @ApiModelProperty(value = "年计划完成投资")
    private Integer proPlanYear;

    /**
     * 这月之前完成投资
     */
    @ApiModelProperty(value = "这月之前完成投资")
    private Integer proPlanMonths;

    /**
     * 本月完成投资
     */
    @ApiModelProperty(value = "本月完成投资")
    private Integer proPlanMonth;

    /**
     * 今年已完成投资
     */
    @ApiModelProperty(value = "今年已完成投资")
    private Integer proYear;

    /**
     * 年计划完成投资比例% 百分比值的 100 倍
     */
    @ApiModelProperty(value = "年计划完成投资比例")
    private String proPlanCompletionPercent;

    //存放子菜单目录
    private List<SummaryDetailsVO> children;
}