package com.pxxy.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 项目和项目类别的中间表
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProPrc implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "pp_id", type = IdType.AUTO)
    private Integer ppId;

    /**
     * 项目的外键
     */
    private Integer proId;

    /**
     * 项目类别的外键
     */
    private Integer prcId;

    /**
     * 项目名称的冗余字段，用于实现同类型项目名称不能重复。
     */
    private String proName;


}
