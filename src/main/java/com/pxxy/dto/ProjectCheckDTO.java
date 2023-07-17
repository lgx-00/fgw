package com.pxxy.dto;

import lombok.Data;

/**
 * Class name: ProjectCheckDTO
 *
 * Create time: 2023/7/16 12:01
 *
 * @author xw
 * @version 1.0
 */
@Data
public class ProjectCheckDTO {

    private Integer proId;

    private Integer disId;

    private Integer depId;

    private Integer uId;

    private Integer prcPeriod;

    private Integer proStatus;

    /**
     * 已下达投资
     */
    private Integer disInvest;

    /**
     * 计划总投资
     */
    private Integer proPlan;

    /**
     * 年计划完成投资
     */
    private Integer proPlanYear;

    /**
     * 上次调度的累计完成投资
     */
    private Integer proDisTotal;

    /**
     * 上次调度的今年已完成投资
     */
    private Integer proDisYear;

    /**
     * 本次调度的年计划投资
     */
    private Integer disPlanYear;

    /**
     * 本次调度的已完成投资
     */
    private Integer disYear;

    /**
     * 累计完成投资
     */
    private Integer disTotal;

    /**
     * 累积投资完成率% 百分比值的 100 倍
     */
    private Integer disTotalPercent;

    /**
     * 今年投资完成率% 百分比值的 100 倍
     */
    private Integer disYearPercent;

}
