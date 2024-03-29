package com.pxxy.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class DepPrc implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键	主键，自增
     */
    @TableId(value = "dp_id", type = IdType.AUTO)
    private Integer dpId;

    /**
     * 科室ID 科室的外键，非空
     */
    private Integer depId;

    /**
     * 项目类别ID 项目类别的外键，非空
     */
    private Integer prcId;


}
