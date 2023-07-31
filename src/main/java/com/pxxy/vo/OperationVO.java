package com.pxxy.vo;

import cn.hutool.core.net.Ipv4Util;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Class name: OperationVO
 *
 * Create time: 2023/7/26 17:18
 *
 * @author xw
 * @version 1.0
 */
@Data
@ApiModel("操作日志模型")
public class OperationVO {

    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operTime;

    @ApiModelProperty("操作用户")
    private String userName;

    @ApiModelProperty("IP 地址")
    private String operIp;

    @ApiModelProperty("操作状态 0操作成功 1操作失败 2未知")
    private String operStatus;

    @ApiModelProperty("接口路径")
    private String operPath;

    @ApiModelProperty("操作方法")
    private String operMethod;

    @ApiModelProperty("操作详情")
    private String operInfo;

    public void setOperStatus(Integer operStatus) {
        this.operStatus = operStatus == 0 ? "操作成功" : operStatus == 1 ? "操作失败" : "未知";
    }

    public void setOperIp(Long operIp) {
        this.operIp = Ipv4Util.longToIpv4(operIp);
    }

}
