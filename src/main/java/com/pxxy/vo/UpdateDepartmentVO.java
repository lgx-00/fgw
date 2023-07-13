package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: hesen
 * @Date: 2023-06-23-16:02
 * @Description:
 */
@Data
@ApiModel("修改科室请求模型")
public class UpdateDepartmentVO {

    @ApiModelProperty("科室ID")
    private Integer depId;

    @ApiModelProperty("科室名")
    @NotBlank(message = "修改科室名称不能为空！")
    private String depName;

    @ApiModelProperty("项目类别ID")
    private List<Integer> projectCategory;

    public void setProjectCategory(Integer[] projectCategory) {
        this.projectCategory = Arrays.asList(projectCategory);
    }
}
