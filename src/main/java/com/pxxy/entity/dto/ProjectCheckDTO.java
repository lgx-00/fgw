package com.pxxy.entity.dto;

import com.pxxy.entity.pojo.Dispatch;
import com.pxxy.entity.vo.AddDispatchVO;
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

    private Integer depId;

    private Integer uId;

    private Integer prcPeriod;

    private Integer proStatus;

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

    private boolean lastDis;

    private AddDispatchVO newDispatch;

    private Dispatch oldDispatch;
}
