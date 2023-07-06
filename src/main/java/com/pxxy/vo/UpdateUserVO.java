package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hesen
 * @Date: 2023-06-15-16:42
 * @Description:
 */
@Data
@ApiModel(value = "修改用户请求模型")
public class UpdateUserVO {

    @NotBlank(message = "修改用户名不能为空！")
    @ApiModelProperty(value = "用户名")
    private String uName;

    @NotBlank(message = "修改密码不能为空！")
    @ApiModelProperty(value = "密码")
    private String uPassword;

    @ApiModelProperty(value = "用户 ID")
    private Integer uId;

    @ApiModelProperty(value = "科室 ID")
    private Integer depId;

    @ApiModelProperty(value = "辖区 ID")
    private Integer couId;

    @ApiModelProperty(value = "勾选的角色，可多选")
    private List<RoleVO> roleList = new ArrayList<>();
}
