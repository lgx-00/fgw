package com.pxxy.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Class name: UpdatePasswordVO
 *
 * Create time: 2023/8/30 21:53
 *
 * @author xw
 * @version 1.0
 */
@Data
@ApiModel("修改密码的数据模型")
public class UpdatePasswordVO {

    @ApiModelProperty("旧密码")
    @Length(max = 64, message = "密码长度不能超过 64 位")
    private String old;

    @ApiModelProperty("新密码")
    @Length(max = 64, message = "密码长度不能超过 64 位")
    private String passwd;

}
