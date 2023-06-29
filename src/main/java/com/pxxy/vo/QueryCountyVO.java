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
@ApiModel(value = "返回给前端的乡镇信息")
public class QueryCountyVO {

    @ApiModelProperty(value = "乡镇ID")
    private Integer couId;

    @ApiModelProperty(value = "乡镇名称")
    private String couName;

    @ApiModelProperty(value = "下辖的村社")
    private List<String> townNames = new ArrayList<>();
}
