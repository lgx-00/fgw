package com.pxxy.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author: XRW
 * @CreateTime: 2023-07-01  14:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SummaryVO {

    private static final long serialVersionUID = 1L;

    /**
     * 乡镇名称
     */
    @ExcelProperty("乡镇名称")
    @ColumnWidth(10)
    private String townName;

    /**
     * 项目个数
     */
    @ExcelProperty("项目个数")
    @ColumnWidth(10)
    private Integer projectNum;

    /**
     * 开工个数
     */
    @ExcelProperty("开工个数")
    @ColumnWidth(10)
    private Integer projectWorkNum;

    /**
     * 开工率
     */
    @ExcelProperty("开工率")
    @ColumnWidth(10)
    private String ratio;

    /**
     * 总投资
     */
    @ExcelProperty("总投资")
    @ColumnWidth(10)
    private Integer proAllPlan;

    /**
     * 年计划完成投资
     */
    @ExcelProperty("年计划投资")
    @ColumnWidth(10)
    private Integer proPlanYear;

    /**
     * 1-7月完成投资
     */
    @ExcelProperty("1-7月完成投资")
    @ColumnWidth(10)
    private Integer proPlanMonths;

    /**
     * 完成比例% 百分比值的 100 倍
     */
    @ExcelProperty("完成比例")
    @ColumnWidth(10)
    private String proCompletionPercent;


}