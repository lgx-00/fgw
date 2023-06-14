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
 * 乡镇
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Town implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 乡镇编号
     */
    @TableId(value = "town_id", type = IdType.AUTO)
    private Integer townId;

    /**
     * 所属县区
     */
    private Integer couId;

    /**
     * 乡镇名称
     */
    private String townName;

    /**
     * 0 正常    5 已删除
     */
    private Integer townStatus;


}
