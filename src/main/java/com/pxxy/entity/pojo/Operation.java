package com.pxxy.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 操作实体类
 *
 * Create time: 2023/7/26 16:02
 *
 * @author xw
 * @version 1.0
 */
@Data
public class Operation {

    @TableId(type = IdType.AUTO)
    private Long operId;

    private Date operTime;

    private Long operIp;

    private Integer operStatus;

    private String operMethod;

    private String operPath;

    private String operInfo;

    private Integer uId;

}
