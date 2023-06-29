package com.pxxy.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author: XRW
 * @CreateTime: 2023-06-24  20:34
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProjectCategoryVO {

    private static final long serialVersionUID=1L;

    private String prcName;

    /**
     * 项目允许调度的日期范围，一个4位数，前两位是起始日期，后两位是最终日期。如果是0表示总是允许调度，没有时间限制。
     */
    private Integer prcPeriod;

}