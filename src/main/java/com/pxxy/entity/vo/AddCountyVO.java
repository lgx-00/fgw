package com.pxxy.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

import static com.pxxy.constant.SystemConstant.FIRST_LEVEL_JURISDICTION;
import static com.pxxy.constant.SystemConstant.SECOND_LEVEL_JURISDICTION;

/**
 * @author hesen
 * @since 2023-06-23-19:04
 * @Description:
 */
@Data
@ApiModel("新增" + FIRST_LEVEL_JURISDICTION + "请求模型")
public class AddCountyVO {

    @ApiModelProperty(FIRST_LEVEL_JURISDICTION + "名称")
    @NotBlank(message = FIRST_LEVEL_JURISDICTION + "名称不能为空")
    @Length(max = 16, message = FIRST_LEVEL_JURISDICTION + "名称不能超过 16 个字符")
    private String couName;

    @ApiModelProperty(SECOND_LEVEL_JURISDICTION)
    private List<TownVO> towns = new ArrayList<>();
}
