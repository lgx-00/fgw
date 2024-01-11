package com.pxxy.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static com.pxxy.constant.SystemConstant.SECOND_LEVEL_JURISDICTION;

/**
 * Class name: TownVO
 * Create time: 2023/12/4 18:39
 *
 * @author xw
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(SECOND_LEVEL_JURISDICTION + "模型")
public class TownVO {

    @ApiModelProperty(SECOND_LEVEL_JURISDICTION + "名称")
    @Length(max = 20, message = SECOND_LEVEL_JURISDICTION + "名称不能超过 20 个字符")
    private String townName;

}
