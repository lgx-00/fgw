package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: hesen
 * @Date: 2023-06-26-18:23
 * @Description:
 */
@Data
@ApiModel(value = "模糊查询项目请求参数")
public class QueryVagueProjectVO {

    @ApiModelProperty(value = "项目名称")
    private String proName;

    @ApiModelProperty(value = "从")
    private Date beginTime;

    @ApiModelProperty(value = "至")
    private Date endTime;

    @ApiModelProperty(value = "县区ID")
    private Integer couId;

    @ApiModelProperty(value = "乡镇ID")
    private Integer townId;

    @ApiModelProperty(value = "项目类别ID")
    private Integer prcId;

    @ApiModelProperty(value = "行业领域ID")
    private Integer infId;

    @ApiModelProperty(value = "项目状态")
    private Integer proStatus;

    @ApiModelProperty(value = "工程阶段")
    private Integer projectStage;


}
