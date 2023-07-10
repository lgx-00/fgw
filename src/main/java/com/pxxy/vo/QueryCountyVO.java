package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hesen
 * @Date: 2023-06-23-18:58
 * @Description:
 */
@Data
@ApiModel(value = "返回给前端的辖区信息")
public class QueryCountyVO {

    @ApiModelProperty(value = "辖区ID")
    private Integer couId;

    @ApiModelProperty(value = "辖区名称")
    private String couName;

    @ApiModelProperty(value = "下辖的二级辖区")
    private List<QueryTownVO> towns = new ArrayList<>();
}
