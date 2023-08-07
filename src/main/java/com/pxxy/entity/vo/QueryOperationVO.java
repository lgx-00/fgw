package com.pxxy.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Date;

import static com.pxxy.constant.SystemConstant.INFINITY_DATE;
import static com.pxxy.constant.SystemConstant.ZERO_DATE;

/**
 * Class name: QueryOperationVO
 *
 * Create time: 2023/7/26 16:52
 *
 * @author xw
 * @version 1.0
 */
@Data
@ApiModel("查询操作日志的模型")
public class QueryOperationVO {

    @ApiModelProperty("用户名模糊查询")
    private String uName;

    @ApiModelProperty("IP 地址")
    @Pattern(regexp = "^([0-2]\\d{0,2}\\.){3}[0-2]\\d{0,2}$", message = "IP 地址不合法")
    private String operIp;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime = ZERO_DATE;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime = INFINITY_DATE;

    @ApiModelProperty("操作路径模糊查询")
    private String operPath;

    @ApiModelProperty("操作方法")
    @Pattern(regexp = "^(GET)|(POST)|(PUT)$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "操作方法参数不合法")
    private String operMethod;

    @ApiModelProperty("操作状态，0操作成功 1操作失败 2未知")
    @Max(value = 2, message = "操作状态参数不合法")
    @Min(value = 0, message = "操作状态参数不合法")
    private Integer operStatus;

}
