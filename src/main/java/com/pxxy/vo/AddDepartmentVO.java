package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: hesen
 * @Date: 2023-06-23-16:02
 * @Description:
 */
@Data
@ApiModel("新增科室请求模型")
public class AddDepartmentVO {

    @ApiModelProperty("科室名")
    @NotBlank(message = "科室名称不能为空！")
    private String depName;

    @ApiModelProperty("项目类别ID")
    @NotEmpty(message = "项目类别不能为空！")
    private List<Integer> projectCategory;

    public void setProjectCategory(Integer[] projectCategory) {
        this.projectCategory = Arrays.asList(projectCategory);
    }

}
