package com.pxxy.pojo;

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
public class RolePermission implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键 主键，自增
     */
    @TableId(value = "rp_id", type = IdType.AUTO)
    private Integer rpId;

    /**
     * 权限ID 外键，非空
     */
    private Integer pId;

    /**
     * 角色ID 外键，非空
     */
    private Integer rId;

    /**
     * 	权限细分，个十百千位表示删改增查	非空，1111表示所有权限，1100表示增加和查询权限
     */
    private Integer rpDetail;

}
