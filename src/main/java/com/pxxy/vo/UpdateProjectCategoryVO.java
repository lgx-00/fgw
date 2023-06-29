package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: hesen
 * @Date: 2023-06-24-14:52
 * @Description:
 */
@Data
@ApiModel(value = "修改项目类型请求参数")
public class UpdateProjectCategoryVO {

    @ApiModelProperty(value = "项目类型ID")
    private Integer prcId;

    @ApiModelProperty(value = "项目类型名称")
    @NotBlank(message = "项目类型名称不能为空！")
    private String prcName;

    @ApiModelProperty(value = "允许调度时间")
    private Integer prcPeriod;
}
