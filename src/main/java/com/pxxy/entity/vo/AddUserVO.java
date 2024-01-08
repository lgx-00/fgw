package com.pxxy.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hesen
 * @since 2023-06-15-16:42
 * @Description:
 */
@Data
@ApiModel("新增用户请求模型")
public class AddUserVO {
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String uName;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String uPassword;

    @ApiModelProperty("科室id")
    private Integer depId;

    @ApiModelProperty("辖区id")
    private Integer couId;

    @ApiModelProperty("勾选的角色，可多选")
    private List<RoleVO> roleList = new ArrayList<>();
}
