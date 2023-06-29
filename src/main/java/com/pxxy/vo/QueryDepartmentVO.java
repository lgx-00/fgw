package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hesen
 * @Date: 2023-06-23-16:11
 * @Description:
 */
@Data
@ApiModel(value = "返回给前端的科室信息")
public class QueryDepartmentVO {

    @ApiModelProperty(value = "科室ID")
    private Integer depId;

    @ApiModelProperty(value = "科室名称")
    private String depName;

    @ApiModelProperty(value = "项目类型")
    private List<String> projectCategoryName = new ArrayList<>();
}
