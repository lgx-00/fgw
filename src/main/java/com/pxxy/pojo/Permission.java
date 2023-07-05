package com.pxxy.pojo;

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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
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
    // 一级标题
    @TableField(exist = false)
    private String title;
    // 二级标题
    @TableField(exist = false)
    private String name;

    public void setPName(String pName) {
        this.pName = pName;
        if (pName != null && pName.contains("，")) {
            String[] strings = pName.split("，");
            title = strings[0];
            name = strings[1];
        }
    }


}
