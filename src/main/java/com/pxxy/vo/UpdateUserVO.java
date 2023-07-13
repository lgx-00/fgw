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
@ApiModel("修改用户请求模型")
public class UpdateUserVO {

    @NotBlank(message = "修改用户名不能为空！")
    @ApiModelProperty("用户名")
    private String uName;

    @NotBlank(message = "修改密码不能为空！")
    @ApiModelProperty("密码")
    private String uPassword;

    @ApiModelProperty("用户 ID")
    private Integer uId;

    @ApiModelProperty("科室 ID")
    private Integer depId;

    @ApiModelProperty("辖区 ID")
    private Integer couId;

    @ApiModelProperty("勾选的角色，可多选")
    private List<RoleVO> roleList = new ArrayList<>();
}
