package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@ApiModel(value = "修改项目请求模型")
public class UpdateProjectVO {


    @ApiModelProperty(value = "项目ID")
    private Integer proId;

    /**
     * 项目名称 非空
     */
    @ApiModelProperty(value = "项目名称")
    @NotBlank(message = "项目名称不能为空！")
    private String proName;

    /**
     * 日期 非空
     */
    @ApiModelProperty(value = "日期")
    @NotNull(message = "项目日期不能为空！")
    private Date proDate;


    /**
     * 建设地点 非空
     */
    @ApiModelProperty(value = "建设地点")
    @NotBlank(message = "建设地点不能为空！")
    private String proLocation;

    /**
     * 辖区 外键，非空
     */
    @ApiModelProperty(value = "辖区")
    @NotNull(message = "辖区不能为空！")
    private Integer couId;

    /**
     * 乡镇 ID
     */
    @ApiModelProperty(value = "乡镇")
    @NotNull(message = "乡镇不能为空！")
    private Integer townId;


    /**
     * 行业领域 外键，非空
     */
    @ApiModelProperty(value = "行业领域")
    @NotNull(message = "行业领域不能为空！")
    private Integer infId;


    /**
     * 项目类别
     */
    @ApiModelProperty(value = "项目类别")
    @NotNull(message = "项目类别不能为空！")
    private Integer prcId;

    /**
     * 项目法人或责任单位
     */
    @ApiModelProperty(value = "项目法人或责任单位")
    private String proLegalPerson;

    /**
     * 日常监管直接责任单位
     */
    @ApiModelProperty(value = "日常监管直接责任单位")
    private String proGuarantee;

    /**
     * 项目内容
     */
    @ApiModelProperty(value = "项目内容")
    private String proContent;

    /**
     * 项目代码
     */
    @ApiModelProperty(value = "项目代码")
    private String proCode;

    /**
     * 入库入统代码
     */
    @ApiModelProperty(value = "入库入统代码")
    private String proInCode;

    /**
     * 是否当年度新开工项目	0：不是，1：是
     */
    @ApiModelProperty(value = "是否当年度新开工项目")
    private Integer proIsNew;

    /**
     * 是否省大中型项目	0：不是，1：是
     */
    @ApiModelProperty(value = "是否省大中型项目")
    private Integer proIsProvincial;

    /**
     * 部门和地方采取的资金安排方式
     */
    @ApiModelProperty(value = "部门和地方采取的资金安排方式")
    private String proArrange;

    /**
     * 计划总投资
     */
    @ApiModelProperty(value = "计划总投资")
    private Integer proPlan;

    /**
     * 年计划完成投资
     */
    @ApiModelProperty(value = "年计划完成投资")
    private Integer proPlanYear;

    /**
     * 投资类别
     */
    @ApiModelProperty(value = "投资类别")
    private String proType;

    /**
     * 计划开工时间
     */
    @ApiModelProperty(value = "计划开工时间")
    private Date proStart;

    /**
     * 计划竣工时间
     */
    @ApiModelProperty(value = "计划竣工时间")
    private Date proComplete;

    /**
     * 实际开工日期（留空表示未开工）
     */
    @ApiModelProperty(value = "实际开工日期")
    private Date proDisStart;

    /**
     * 实际竣工日期（留空表示未竣工）
     */
    @ApiModelProperty(value = "实际竣工日期")
    private Date proDisComplete;


    /**
     * 下次调度的日期 如果是空的则永不提醒该项目的调度
     */
    @ApiModelProperty(value = "下次调度的日期")
    private Date proNextUpdate;


    /**
     * 标记
     */
    @ApiModelProperty(value = "标记")
    private String proMark;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String proRemark;


}
