package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ApiModelProperty("密码")
    private String uPassword;

    public void setUPassword(String uPassword) throws BindException {
        if (uPassword != null) {
            int maxLength = 64;
            if (uPassword.length() > maxLength) {
                BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(this, "updateUserVO");
                bindingResult.addError(new FieldError("updateUserVO", "uPassword", "密码长度不能超过64"));
                throw new BindException(bindingResult);
            }
            if (uPassword.length() == 0) {
                this.uPassword = null;
            }
        }
    }

    @ApiModelProperty("用户 ID")
    private Integer uId;

    @ApiModelProperty("科室 ID")
    private Integer depId;

    @ApiModelProperty("辖区 ID")
    private Integer couId;

    @ApiModelProperty("勾选的角色，可多选")
    private List<RoleVO> roleList = new ArrayList<>();
}
