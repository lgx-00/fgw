package com.pxxy.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: XRW
 * @CreateTime: 2023-07-01  14:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "统计汇总")
@Accessors(chain = true)
public class SummaryVO {

    private static final long serialVersionUID = 1L;

    /**
     * 二级辖区名称
     */
    @ExcelProperty("二级辖区名称")
    @ColumnWidth(10)
    @ApiModelProperty(value = "二级辖区名称")
    private String townName;

    /**
     * 项目个数
     */
    @ExcelProperty("项目个数")
    @ColumnWidth(10)
    @ApiModelProperty(value = "项目个数")
    private Integer projectNum;

    /**
     * 开工个数
     */
    @ExcelProperty("开工个数")
    @ColumnWidth(10)
    @ApiModelProperty(value = "开工个数")
    private Integer projectWorkNum;

    /**
     * 开工率
     */
    @ExcelProperty("开工率")
    @ColumnWidth(10)
    @ApiModelProperty(value = "开工率")
    private String ratio;

    /**
     * 总投资
     */
    @ExcelProperty("总投资")
    @ColumnWidth(10)
    @ApiModelProperty(value = "总投资")
    private Integer proAllPlan;

    /**
     * 年计划完成投资
     */
    @ExcelProperty("年计划投资")
    @ColumnWidth(10)
    @ApiModelProperty(value = "年计划投资")
    private Integer proPlanYear;

    /**
     * 今年已完成投资
     */
    @ExcelProperty("今年完成投资")
    @ColumnWidth(10)
    @ApiModelProperty(value = "今年完成投资")
    private Integer proPlanMonths;

    /**
     * 完成比例% 百分比值的 100 倍
     */
    @ExcelProperty("完成比例")
    @ColumnWidth(10)
    @ApiModelProperty(value = "完成比例")
    private String proCompletionPercent;

    //存放子菜单目录
    private List<SummaryVO> children;


}