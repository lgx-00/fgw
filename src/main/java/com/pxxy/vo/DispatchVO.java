package com.pxxy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Class name: AddDispatchVO
 *
 * Create time: 2023/7/12 15:11
 *
 * @author xw
 * @version 1.0
 */
@Data
public class DispatchVO implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String disAppendix;

}
