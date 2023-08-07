package com.pxxy.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hesen
 * @since 2023-06-23-19:04
 * @Description:
 */
@Data
@ApiModel("新增辖区请求模型")
public class AddCountyVO {

    @ApiModelProperty("辖区名称")
    @NotBlank(message = "辖区名称不能为空！")
    private String couName;

    @ApiModelProperty("二级辖区")
    private List<String> townNames = new ArrayList<>();
}
