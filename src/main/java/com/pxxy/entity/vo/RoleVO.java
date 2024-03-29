package com.pxxy.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hesen
 * @since 2023-06-15-19:25
 * @Description:
 */
@Data
@ApiModel("选择角色")
public class RoleVO {
    @ApiModelProperty("角色ID")
    private Integer rId;
}
