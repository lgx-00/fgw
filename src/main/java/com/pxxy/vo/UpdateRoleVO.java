package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @Author: hesen
 * @Date: 2023-06-20-16:31
 * @Description:
 */
@Data
@ApiModel(value = "修改角色请求参数")
public class UpdateRoleVO {

    @ApiModelProperty(value = "角色ID")
    private Integer rId;

    @ApiModelProperty(value = "角色名")
    @NotBlank(message = "修改角色名不能为空！")
    private String rName;

    @ApiModelProperty(value = "权限 key是权限ID value是rp_detail")
    private Map<Integer, Integer> permissionMapper;
}
