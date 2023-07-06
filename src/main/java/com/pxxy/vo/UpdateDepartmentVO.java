package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hesen
 * @Date: 2023-06-23-16:02
 * @Description:
 */
@Data
@ApiModel(value = "修改科室请求模型")
public class UpdateDepartmentVO {

    @ApiModelProperty(value = "科室ID")
    private Integer depId;

    @ApiModelProperty(value = "科室名")
    @NotBlank(message = "修改科室名称不能为空！")
    private String depName;

    @ApiModelProperty(value = "项目类别")
    List<ProjectCategoryVO> projectCategory = new ArrayList<>();
}
