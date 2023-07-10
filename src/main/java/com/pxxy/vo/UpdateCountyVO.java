package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hesen
 * @Date: 2023-06-23-19:04
 * @Description:
 */
@Data
@ApiModel(value = "修改辖区请求模型")
public class UpdateCountyVO {

    @ApiModelProperty(value = "辖区ID")
    private Integer couId;

    @ApiModelProperty(value = "辖区名称")
    @NotBlank(message = "修改辖区名称不能为空！")
    private String couName;

    @ApiModelProperty(value = "下辖的二级辖区")
    private List<String> townNames = new ArrayList<>();

}
