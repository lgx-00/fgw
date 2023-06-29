package com.pxxy.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: XRW
 * @CreateTime: 2023-06-20  10:48
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AddIndustryFieldVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "行业领域名称不能为空！")
    private String infName;

    private String infRemark;

}