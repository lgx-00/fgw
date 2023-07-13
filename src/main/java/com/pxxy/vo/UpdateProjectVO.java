package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
@ApiModel("修改项目请求模型")
public class UpdateProjectVO {

    @ApiModelProperty("项目ID")
    private Integer proId;

    /**
     * 项目名称 非空
     */
    @ApiModelProperty("项目名称")
    @NotBlank(message = "项目名称不能为空！")
    @Length(max = 128, message = "项目名称不能超过 128 个字符！")
    private String proName;

    /**
     * 日期 非空
     */
    @ApiModelProperty("日期")
    private Date proDate;

    /**
     * 建设地点 非空
     */
    @ApiModelProperty("建设地点")
    @NotBlank(message = "建设地点不能为空！")
    @Length(max = 32, message = "建设地点不能超过 32 个字符！")
    private String proLocation;

    /**
     * 辖区 外键，非空
     */
    @ApiModelProperty("辖区")
    @NotNull(message = "辖区不能为空！")
    private Integer couId;

    /**
     * 二级辖区 ID
     */
    @ApiModelProperty("二级辖区")
    private Integer townId;


    /**
     * 行业领域 外键，非空
     */
    @ApiModelProperty("行业领域")
    @NotNull(message = "行业领域不能为空！")
    private Integer infId;


    /**
     * 项目类别
     */
    @ApiModelProperty("项目类别")
    @NotNull(message = "项目类别不能为空！")
    private Integer prcId;

    /**
     * 项目法人或责任单位
     */
    @ApiModelProperty("项目法人或责任单位")
    @Length(max = 128, message = "项目法人或责任单位不能超过 128 个字符！")
    private String proLegalPerson;

    /**
     * 日常监管直接责任单位
     */
    @ApiModelProperty("日常监管直接责任单位")
    @Length(max = 64, message = "日常监管直接责任单位不能超过 64 个字符！")
    private String proGuarantee;

    /**
     * 项目内容
     */
    @ApiModelProperty("项目内容")
    @Length(max = 255, message = "项目内容不能超过 255 个字符！")
    private String proContent;

    /**
     * 项目代码
     */
    @ApiModelProperty("项目代码")
    @Length(max = 64, message = "项目代码不能超过 64 个字符！")
    private String proCode;

    /**
     * 入库入统代码
     */
    @ApiModelProperty("入库入统代码")
    @Length(max = 64, message = "入库入统代码不能超过 64 个字符！")
    private String proInCode;

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
     * 部门和地方采取的资金安排方式
     */
    @ApiModelProperty("部门和地方采取的资金安排方式")
    @Length(max = 64, message = "部门和地方采取的资金安排方式不能超过 64 个字符！")
    private String proArrange;

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
    @Length(max = 64, message = "投资类别不能超过 64 个字符！")
    private String proType;

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

    @ApiModelProperty(hidden = true)
    public String getProMark() {
        return (proMark1 == null ? "" : proMark1) + "᛭"
                + (proMark2 == null ? "" : proMark2) + "᛭"
                + (proMark3 == null ? "" : proMark3);
    }
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String proRemark;


}
