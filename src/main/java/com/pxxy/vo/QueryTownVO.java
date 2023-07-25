package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class name: QueryTownVO
 *
 * Create time: 2023/7/10 18:44
 *
 * @author xw
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("查询二级辖区数据模型")
public class QueryTownVO {

    @ApiModelProperty("二级辖区编号")
    private Integer townId;

    @ApiModelProperty("二级辖区名称")
    private String townName;

}
