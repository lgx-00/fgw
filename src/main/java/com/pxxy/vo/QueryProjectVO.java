package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: hesen
 * @Date: 2023-06-26-9:37
 * @Description:
 */
@Data
@ApiModel("返回给前端的项目信息")
public class QueryProjectVO {

    @ApiModelProperty("项目ID")
    private Integer proId;

    @ApiModelProperty("项目名称")
    private String proName;

    @ApiModelProperty("日期")
    private Date proDate;

    @ApiModelProperty("审核单位名称")
    private String depName;

    @ApiModelProperty("辖区（辖区-二级辖区）")
    private String area;

    @ApiModelProperty("项目类型名称")
    private String prcName;

    @ApiModelProperty("行业领域名称")
    private String infName;

    /**
     * 项目代码
     */
    @ApiModelProperty("项目代码")
    private String proCode;

    /**
     * 入库入统代码
     */
    @ApiModelProperty("入库入统代码")
    private String proInCode;

    @ApiModelProperty("项目内容")
    private String proContent;

    @ApiModelProperty("建设地点")
    private String proLocation;

    /**
     * 计划开工时间
     */
    @ApiModelProperty("计划开工时间")
    private Date proStart;

    /**
     * 计划竣工时间
     */
    @ApiModelProperty("计划竣工时间")
    private Date proComplete;

    /**
     * 计划总投资
     */
    @ApiModelProperty("计划总投资")
    private Integer proPlan;

    /**
     * 年计划完成投资
     */
    @ApiModelProperty("年计划完成投资")
    private Integer proPlanYear;

    /**
     * 投资类别
     */
    @ApiModelProperty("投资类别")
    private String proType;

    /**
     * 部门和地方采取的资金安排方式
     */
    @ApiModelProperty("部门和地方采取的资金安排方式")
    private String proArrange;

    /**
     * 日常监管直接责任单位
     */
    @ApiModelProperty("日常监管直接责任单位")
    private String proGuarantee;

    /**
     * 累计完成投资	非空，这下面五项用来存储最新的调度信息
     */
    @ApiModelProperty("累计完成投资")
    private Integer proDisTotal;

    /**
     * 今年已完成投资 非空
     */
    @ApiModelProperty("今年已完成投资")
    private Integer proDisYear;

    /**
     * 累积投资完成率% 百分比值的 100 倍
     */
    @ApiModelProperty("累积投资完成率%")
    private Integer proDisTotalPercent = 0;

    public String getProDisTotalPercent() {
        return "" + proDisTotalPercent / 100 + "." + proDisTotalPercent % 100 + "%";
    }

    /**
     * 今年投资完成率% 百分比值的 100 倍
     */
    @ApiModelProperty("今年投资完成率%")
    private Integer proDisYearPercent = 0;

    public String getProDisYearPercent() {
        return "" + proDisYearPercent / 100 + "." + proDisYearPercent % 100 + "%";
    }

    /**
     * 主要形象进度
     */
    @ApiModelProperty("主要形象进度")
    private String proDisProgress;


    /**
     * 项目法人或责任单位
     */
    @ApiModelProperty("项目法人或责任单位")
    private String proLegalPerson;


    /**
     * 实际开工日期（留空表示未开工）
     */
    @ApiModelProperty("实际开工日期")
    private Date proDisStart;

    /**
     * 实际竣工日期（留空表示未竣工）
     */
    @ApiModelProperty("实际竣工日期")
    private Date proDisComplete;

    /**
     * 是否当年度新开工项目	0：不是，1：是
     */
    @ApiModelProperty("是否当年度新开工项目")
    private Integer proIsNew;

    /**
     * 是否省大中型项目	0：不是，1：是
     */
    @ApiModelProperty("是否省大中型项目")
    private Integer proIsProvincial;

    /**
     * 下次调度的日期 如果是空的则永不提醒该项目的调度
     */
    @ApiModelProperty("下次调度的日期")
    private Date proNextUpdate;

    /**
     * 标记1
     */
    @ApiModelProperty("标记1")
    private String proMark1;
    /**
     * 标记2
     */
    @ApiModelProperty("标记2")
    private String proMark2;
    /**
     * 标记3
     */
    @ApiModelProperty("标记3")
    private String proMark3;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String proRemark;

    /**
     * 当前状态 非空，状态有：0正常、1未上报、2待审核、4待调度、5已删除
     */
    @ApiModelProperty("当前状态, 0未上报 1正常 2待审核 3未锁定 4待调度 5已删除 6已完工")
    private String proStatusContent;


}
