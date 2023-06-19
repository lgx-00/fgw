package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hesen
 * @Date: 2023-06-19-13:34
 * @Description:
 */
@Data
@ApiModel(value = "返回给前端的用户信息")
public class GetAllUserVO {

    @ApiModelProperty(value = "用户ID")
    private Integer uId;

    @ApiModelProperty(value = "用户名")
    private String uName;

    @ApiModelProperty(value = "所属科室")
    private String depName;

    @ApiModelProperty(value = "辖区")
    private String couName;

    @ApiModelProperty(value = "角色")
    private List<String> roleList = new ArrayList<>();
}
