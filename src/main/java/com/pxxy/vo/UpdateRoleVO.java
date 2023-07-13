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
@ApiModel("修改角色请求模型")
public class UpdateRoleVO {

    @ApiModelProperty("角色ID")
    private Integer rId;

    @ApiModelProperty("角色名")
    @NotBlank(message = "修改角色名不能为空！")
    private String rName;

    @ApiModelProperty("权限 key是权限ID value是rp_detail")
    private Map<Integer, Integer> permissionMapper;
}
