package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @Author: hesen
 * @Date: 2023-06-20-11:01
 * @Description:
 */
@Data
@ApiModel(value = "新增角色请求参数")
public class AddRoleVO {
    @ApiModelProperty(value = "角色名")
    @NotBlank(message = "角色名不能为空！")
    private String rName;

    @ApiModelProperty(value = "权限 key是权限ID value是rp_detail")
    private Map<Integer, Integer> permissionMapper;
}
