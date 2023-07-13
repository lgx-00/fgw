package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: hesen
 * @Date: 2023-06-24-14:52
 * @Description:
 */
@Data
@ApiModel("返回给前端的项目类型信息")
public class QueryProjectCategoryVO {

    @ApiModelProperty("项目类型ID")
    private Integer prcId;

    @ApiModelProperty("项目类型名称")
    private String prcName;

    @ApiModelProperty("允许调度时间")
    private Integer prcPeriod;
}
