package com.pxxy.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "用户登录请求参数")
public class LoginFormDTO {
    @NotNull
    @ApiModelProperty(value = "用户名")
    private String uName;

    @NotNull
    @ApiModelProperty(value = "密码")
    private String uPassword;
}