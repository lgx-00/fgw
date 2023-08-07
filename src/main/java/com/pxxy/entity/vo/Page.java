package com.pxxy.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static com.pxxy.constant.SystemConstant.DEFAULT_PAGE_NUM;
import static com.pxxy.constant.SystemConstant.DEFAULT_PAGE_SIZE;

/**
 * Class name: Page
 *
 * Create time: 2023/7/11 15:58
 *
 * @author xw
 * @version 1.0
 */
@Data
@ApiModel("分页数据模型")
public class Page {

    @ApiModelProperty("页码")
    @Min(value = 1, message = "页码必须为正整数！")
    private int pageNum = DEFAULT_PAGE_NUM;

    @ApiModelProperty("单个页面的记录的数量")
    @Max(value = 300, message = "单个页面数据记录数不能超过 300！")
    private int pageSize = DEFAULT_PAGE_SIZE;

}
