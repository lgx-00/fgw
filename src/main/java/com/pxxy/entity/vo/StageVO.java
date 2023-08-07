package com.pxxy.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author XRW
 * @CreateTime: 2023-06-25  21:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StageVO {

    private static final long serialVersionUID=1L;

    /**
     * 建设阶段名称 非空
     */
    private String stageName;

    /**
     * 备注
     */
    private String stageRemark;

}