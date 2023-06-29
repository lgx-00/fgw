package com.pxxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
public class County implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "cou_id", type = IdType.AUTO)
    private Integer couId;

    private String couName;

    @TableLogic(value = "0",delval = "5")
    private Integer couStatus;

    private String couRemark;


}
