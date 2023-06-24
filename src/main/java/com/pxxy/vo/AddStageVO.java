package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: hesen
 * @Date: 2023-06-24-15:17
 * @Description:
 */
@Data
@ApiModel(value = "新增工程进展阶段参数")
public class AddStageVO {
    @ApiModelProperty(value = "工程进展阶段名称")
    @NotBlank(message = "工程进展阶段名称不能为空！")
    private String stageName;
}
