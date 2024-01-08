package com.pxxy.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author hesen
 * @since 2023-06-20-11:01
 * @Description:
 */
@Data
@ApiModel("新增角色请求模型")
public class AddRoleVO {
    @ApiModelProperty("角色名")
    @NotBlank(message = "角色名不能为空")
    private String rName;

    @ApiModelProperty("权限 key是权限ID value是rp_detail")
    private Map<Integer, Integer> permissionMapper;
}
