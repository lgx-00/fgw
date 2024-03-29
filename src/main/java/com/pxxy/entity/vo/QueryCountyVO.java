package com.pxxy.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hesen
 * @since 2023-06-23-18:58
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("返回给前端的辖区信息")
public class QueryCountyVO {

    @ApiModelProperty("辖区ID")
    private Integer couId;

    @ApiModelProperty("辖区名称")
    private String couName;

    @ApiModelProperty("下辖的二级辖区")
    private List<QueryTownVO> towns = new ArrayList<>();
}
