package com.pxxy.vo;

import com.pxxy.enums.DispatchStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.*;

/**
 * 查询调度的实体类
 *
 * @author xw
 * @version 1.0
 */
@Data
public class QueryDispatchVO {

    /**
     * 调度编号
     */
    @ApiModelProperty("调度编号")
    private Integer disId;

    /**
     * 项目编号
     */
    @ApiModelProperty("项目编号")
    private Integer proId;

    /**
     * 添加时间
     */
    @ApiModelProperty("添加时间")
    private Date disTime;

    /**
     * 本次申请投资
     */
    @ApiModelProperty("本次申请投资")
    private Integer disApply;

    /**
     * 已下达投资
     */
    @ApiModelProperty("已下达投资")
    private Integer disInvest;

    /**
     * 当年计划投资
     */
    @ApiModelProperty("当年计划投资")
    private Integer disPlanYear;

    /**
     * 当年已完成投资
     */
    @ApiModelProperty("当年已完成投资")
    private Integer disYear;

    /**
     * 累计完成投资
     */
    @ApiModelProperty("累计完成投资")
    private Integer disTotal;

    /**
     * 累积投资完成率%
     */
    @ApiModelProperty("累积投资完成率%")
    private Integer disTotalPercent;

    public String getDisTotalPercent() {
        return disTotalPercent / 100 + "." + disTotalPercent % 100 + "%";
    }

    /**
     * 当年投资完成率%
     */
    @ApiModelProperty("当年投资完成率%")
    private Integer disYearPercent;

    public String getDisYearPercent() {
        return disYearPercent / 100 + "." + disYearPercent % 100 + "%";
    }

    /**
     * 主要形象进度
     */
    @ApiModelProperty("主要形象进度")
    private String disProgress;

    /**
     * 建设阶段
     */
    @ApiModelProperty("建设阶段")
    private String stage;

    /**
     * 工程进展状况
     */
    @ApiModelProperty("工程进展状况")
    private String disSituation;

    /**
     * 报送单位
     */
    @ApiModelProperty("报送单位")
    private String disToDep;

    /**
     * 项目来源
     */
    @ApiModelProperty("项目来源")
    private String disSource;

    /**
     * 包保责任领导
     */
    @ApiModelProperty("包保责任领导")
    private String disGuarantee;

    /**
     * 所属行业领域
     */
    @ApiModelProperty("所属行业领域")
    private String disFiled;

    /**
     * 存在问题
     */
    @ApiModelProperty("存在问题")
    private String disIssue;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String disRemark;

    @ApiModelProperty("项目状态")
    private String disStatus;

    public void setDisStatus(Integer disStatus) {
        for (DispatchStatusEnum value : DispatchStatusEnum.values()) {
            if (Objects.equals(value.val, disStatus)) {
                this.disStatus = value.name;
                return;
            }
        }
        this.disStatus = null;
    }

    /**
     * 是否有附件
     */
    @ApiModelProperty("是否有附件")
    private boolean hasAppendix;

    public void setDisAppendix(String disAppendix) {
        this.hasAppendix = disAppendix != null && !"".equals(disAppendix);
    }

}
