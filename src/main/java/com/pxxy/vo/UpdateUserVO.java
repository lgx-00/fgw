package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hesen
 * @Date: 2023-06-15-16:42
 * @Description:
 */
@Data
@ApiModel(value = "修改用户请求参数")
public class UpdateUserVO {
    @ApiModelProperty(value = "用户名")
    private String uName;

    @ApiModelProperty(value = "密码")
    private String uPassword;

    @ApiModelProperty(value = "科室id")
    private Integer depId;

    @ApiModelProperty(value = "辖区id")
    private Integer couId;

    @ApiModelProperty(value = "勾选的角色，可多选")
    private List<RoleVO> roleList = new ArrayList<>();
}
