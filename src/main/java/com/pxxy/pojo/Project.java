package com.pxxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Project implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 序号 主键，自增
     */
    @TableId(value = "pro_id", type = IdType.AUTO)
    private Integer proId;

    /**
     * 日期 非空
     */
    private Date proDate;

    /**
     * 所属科室 外键，非空
     */
    private Integer depId;

    /**
     * 辖区 外键，非空
     */
    private Integer couId;

    /**
     * 乡镇 ID
     */
    private Integer townId;

    /**
     * 项目类别
     */
    private Integer prcId;

    /**
     * 行业领域 外键，非空
     */
    private Integer infId;

    /**
     * 项目代码
     */
    private String proCode;

    /**
     * 入库入统代码
     */
    private String proInCode;

    /**
     * 项目名称 非空
     */
    private String proName;

    /**
     * 项目内容
     */
    private String proContent;

    /**
     * 建设地点 非空
     */
    private String proLocation;

    /**
     * 计划开工时间
     */
    private Date proStart;

    /**
     * 计划竣工时间
     */
    private Date proComplete;

    /**
     * 计划总投资
     */
    private Integer proPlan;

    /**
     * 年计划完成投资
     */
    private Integer proPlanYear;

    /**
     * 项目法人或责任单位
     */
    private String proLegalPerson;

    /**
     * 实际开工日期（留空表示未开工）
     */
    private Date proDisStart;

    /**
     * 实际竣工日期（留空表示未竣工）
     */
    private Date proDisComplete;

    /**
     * 是否当年度新开工项目	0：不是，1：是
     */
    private Integer proIsNew;

    /**
     * 是否省大中型项目	0：不是，1：是
     */
    private Integer proIsProvincial;

    /**
     * 下次调度的日期 如果是空的则永不提醒该项目的调度
     */
    private Date proNextUpdate;

    /**
     * 项目允许调度的日期范围，一个4位数，前两位是起始日期，后两位是最终日期。如果是0表示总是允许调度，没有时间限制。
     */
    private Integer proDisDateRange;

    /**
     * 投资类别
     */
    private String proType;

    /**
     * 部门和地方采取的资金安排方式
     */
    private String proArrange;

    /**
     * 日常监管直接责任单位
     */
    private String proGuarantee;

    /**
     * 累计完成投资	非空，这下面五项用来存储最新的调度信息
     */
    private Integer proDisTotal;

    /**
     * 今年已完成投资 非空
     */
    private Integer proDisYear;

    /**
     * 累积投资完成率% 百分比值的 100 倍
     */
    private Integer proDisTotalPercent;

    /**
     * 今年投资完成率% 百分比值的 100 倍
     */
    private Integer proDisYearPercent;

    /**
     * 主要形象进度
     */
    private String proDisProgress;

    /**
     * 添加项目时的系统时间  非空
     */
    private Date proAddTime;

    /**
     * 添加项目的用户的 ID   外键，非空
     */
    private Integer uId;

    private Date proLastDis;

    /**
     * 当前状态 非空，状态有：0正常、4待调度、5已删除
     */
    private Integer proStatus;

    /**
     * 负责调度该项目的科室
     */
    private Integer proCheckDep;

    /**
     * 标记
     */
    private String proMark;

    /**
     * 备注
     */
    private String proRemark;


}
