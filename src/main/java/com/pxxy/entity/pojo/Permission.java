package com.pxxy.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Permission implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键 主键，自增
     */
    @TableId(value = "p_id", type = IdType.AUTO)
    private Integer pId;

    /**
     * 菜单路径
     */
    private String pPath;

    /**
     * 菜单名
     */
    private String pName;

    /**
     * 状态
     */
    private Integer pStatus;

    /**
     * 该页面允许的操作，8=查询 4=增加 2=修改 1=删除；
     * 以及默认的操作，128=查询 64=增加 32=修改 16=删除
     */
    private Integer pMod;

    // 一级标题
    @TableField(exist = false)
    private String title;

    // 二级标题
    @TableField(exist = false)
    private String name;

    @SuppressWarnings("unused")
    public void setPName(String pName) {
        this.pName = pName;
        if (pName != null && pName.contains("/")) {
            String[] strings = pName.split("/");
            title = strings[0];
            name = strings[1];
        }
    }


}
