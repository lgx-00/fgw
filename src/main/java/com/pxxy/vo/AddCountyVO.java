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
@ApiModel(value = "新增乡镇请求模型")
public class AddCountyVO {

    @ApiModelProperty(value = "乡镇名称")
    @NotBlank(message = "乡镇名称不能为空！")
    private String couName;

    @ApiModelProperty(value = "下辖的村社")
    private List<String> townNames = new ArrayList<>();
}
