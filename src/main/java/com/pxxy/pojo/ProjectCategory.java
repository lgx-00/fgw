package com.pxxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProjectCategory implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "prc_id", type = IdType.AUTO)
    private Integer prcId;

    private String prcName;

    /**
     * 项目允许调度的日期范围，一个4位数，前两位是起始日期，后两位是最终日期。如果是0表示总是允许调度，没有时间限制。
     */
    private Integer prcPeriod;

    private Integer prcStatus;

    private String prcRemark;


}
