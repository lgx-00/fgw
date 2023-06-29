package com.pxxy.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: XRW
 * @CreateTime: 2023-06-20  10:55
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpdateIndustryFieldVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer infId;

    @NotBlank(message = "修改行业领域名称不能为空！")
    private String infName;

}