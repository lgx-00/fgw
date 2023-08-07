package com.pxxy.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hesen
 * @since 2023-06-19-13:34
 * @Description:
 */
@Data
@ApiModel("返回给前端的用户信息")
public class QueryUserVO {

    @ApiModelProperty("用户ID")
    private Integer uId;

    @ApiModelProperty("用户名")
    private String uName;

    @ApiModelProperty("所属科室")
    private String depName;

    @ApiModelProperty("辖区")
    private String couName;

    @ApiModelProperty("角色")
    private List<String> roleList = new ArrayList<>();
}
