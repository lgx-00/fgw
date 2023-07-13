package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hesen
 * @Date: 2023-06-20-10:37
 * @Description:
 */
@Data
@ApiModel("返回给前端的角色信息")
public class QueryRoleVO {

    @ApiModelProperty("角色ID")
    private Integer rId;

    @ApiModelProperty("角色名")
    private String rName;

    @ApiModelProperty("权限")
    private List<String> permission = new ArrayList<>();
}
