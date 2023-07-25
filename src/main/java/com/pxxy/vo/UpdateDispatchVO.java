package com.pxxy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static com.pxxy.constant.ResponseMessage.UPDATE_FAILED;

/**
 * Class name: UpdateDispatchVO
 *
 * Create time: 2023/7/12 15:18
 *
 * @author xw
 * @version 1.0
 */
@Data
@ApiModel("更新调度数据模型")
@EqualsAndHashCode(callSuper = true)
public class UpdateDispatchVO extends AddDispatchVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号 主键，自增
     */
    @ApiModelProperty("调度编号")
    @NotNull(message = UPDATE_FAILED + "，缺少必要参数")
    private Integer disId;

    @NotNull(message = UPDATE_FAILED + "，缺少必要参数")
    @ApiModelProperty("调度所属项目的编号")
    private Integer proId;

    UpdateDispatchVO() {
        super.setProId(0);
    }

}
