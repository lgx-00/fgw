package com.pxxy.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: XRW
 * @CreateTime: 2023-06-20  10:55
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpdateIndustryFiledVO implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "inf_id", type = IdType.AUTO)
    private Integer infId;

    @NotNull(message = "不可为空")
    private String infName;

}