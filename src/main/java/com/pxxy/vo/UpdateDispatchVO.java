package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static com.pxxy.constant.ResponseMessage.ADD_FAILED;
import static com.pxxy.constant.ResponseMessage.UPDATE_FAILED;

/**
 * Class name: UpdateDispatchVO
 *
 * Create time: 2023/7/12 15:18
 *
 * @author xw
 * @version 1.0
 */
@Data
@ApiModel("更新调度数据模型")
@EqualsAndHashCode(callSuper = true)
public class UpdateDispatchVO extends DispatchVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号 主键，自增
     */
    @ApiModelProperty("调度编号")
    @NotNull(message = UPDATE_FAILED)
    private Integer disId;

    @NotNull(message = UPDATE_FAILED)
    @ApiModelProperty("调度所属项目的编号")
    private Integer proId;


    /**
     * 累计完成投资 非空
     */
    @ApiModelProperty("累计完成投资")
    @NotNull(message = "累计完成投资不能为空！")
    private Integer disTotal;

    /**
     * 今年计划投资 非空
     */
    @ApiModelProperty("今年计划投资")
    @NotNull(message = "今年计划投资不能为空！")
    private Integer disPlanYear;

    /**
     * 今年已完成投资 非空
     */
    @ApiModelProperty("今年已完成投资")
    @NotNull(message = "今年已完成投资不能为空！")
    private Integer disYear;

    /**
     * 累积投资完成率% 百分比值的 100 倍
     */
    @ApiModelProperty("累积投资完成率%")
    private Integer disTotalPercent;

    /**
     * 今年投资完成率% 百分比值的 100 倍
     */
    @ApiModelProperty("今年投资完成率%")
    private Integer disYearPercent;

    /**
     * 主要形象进度
     */
    @ApiModelProperty("主要形象进度")
    @NotNull(message = "主要形象进度不能为空！")
    @NotBlank(message = "主要形象进度不能为空！")
    @Length(max = 255, message = "主要形象进度不能超过 255 个字符！")
    private String disProgress;

    /**
     * 建设阶段
     */
    @ApiModelProperty("建设阶段")
    private Integer stageId;

    /**
     * 已下达投资
     */
    @ApiModelProperty("已下达投资")
    private Integer disInvest;

    /**
     * 本次申请投资
     */
    @ApiModelProperty("本次申请投资")
    private Integer disApply;

    /**
     * 工程进展状况
     */
    @ApiModelProperty("工程进展状况")
    @Length(max = 255, message = "工程进展状况不能超过 255 个字符！")
    private String disSituation;

    /**
     * 报送单位
     */
    @ApiModelProperty("报送单位")
    @Length(max = 32, message = "报送单位不能超过 32 个字符！")
    private String disToDep;

    /**
     * 项目来源
     */
    @ApiModelProperty("项目来源")
    @Length(max = 32, message = "项目来源不能超过 32 个字符！")
    private String disSource;

    /**
     * 包保责任领导
     */
    @ApiModelProperty("包保责任领导")
    @Length(max = 32, message = "包保责任领导不能超过 32 个字符！")
    private String disGuarantee;

    /**
     * 所属行业领域
     */
    @ApiModelProperty("所属行业领域")
    @Length(max = 64, message = "所属行业领域不能超过 64 个字符！")
    private String disFiled;

    /**
     * 存在问题
     */
    @ApiModelProperty("存在问题")
    @Length(max = 255, message = "存在问题不能超过 255 个字符！")
    private String disIssue;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @Length(max = 255, message = "备注不能超过 255 个字符！")
    private String disRemark;

}
