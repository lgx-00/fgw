package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: hesen
 * @Date: 2023-06-23-16:12
 * @Description:
 */
@Data
@ApiModel(value = "选择分类")
public class ProjectCategoryVO {
    @ApiModelProperty(value = "项目类别ID")
    private Integer prcId;
}
