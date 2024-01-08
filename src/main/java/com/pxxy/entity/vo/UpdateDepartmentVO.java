package com.pxxy.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Arrays;
import java.util.List;

/**
 * @author hesen
 * @since 2023-06-23-16:02
 * @Description:
 */
@Data
@ApiModel("修改科室请求模型")
public class UpdateDepartmentVO {

    @ApiModelProperty("科室ID")
    private Integer depId;

    @ApiModelProperty("科室名")
    @Length(min = 1, message = "修改科室名称不能为空")
    private String depName;

    @ApiModelProperty("项目类别ID")
    private List<Integer> projectCategory;

    public void setProjectCategory(Integer[] projectCategory) {
        this.projectCategory = Arrays.asList(projectCategory);
    }
}
