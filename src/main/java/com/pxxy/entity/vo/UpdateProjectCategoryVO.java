package com.pxxy.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author hesen
 * @since 2023-06-24-14:52
 * @Description:
 */
@Data
@ApiModel("修改项目类型请求模型")
public class UpdateProjectCategoryVO {

    @ApiModelProperty("项目类型编号")
    private Integer prcId;

    @ApiModelProperty("项目类型名称")
    @Length(min = 1, message = "项目类型名称不能为空")
    @Length(max = 64, message = "项目类型名称不能超过 64 个字符")
    private String prcName;

    @ApiModelProperty("允许调度的时间范围")
    @Max(value = 3131, message = "允许调度的时间范围不合法")
    @Min(value = 101, message = "允许调度的时间范围不合法")
    private Integer prcPeriod;
}
