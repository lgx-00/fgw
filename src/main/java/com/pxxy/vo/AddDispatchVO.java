package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: hesen
 * @Date: 2023-07-05-9:31
 * @Description:
 */
@Data
@ApiModel(value = "新增调度请求模型")
public class AddDispatchVO {

    @ApiModelProperty(value = "总累计已完成的投资额")
    private Integer accumulateCompletedInvestment;

    @ApiModelProperty(value = "今年累计已完成的投资额")
    private Integer yearAccumulateCompletedInvestment;
}
