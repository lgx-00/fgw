package com.pxxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
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
 * @since 2023-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键 主键，自增
     */
    @TableId(value = "u_id", type = IdType.AUTO)
    private Integer uId;

    /**
     * 用户名 非空
     */
    private String uName;

    /**
     * 密码 非空，已加密
     */
    private String uPassword;

    /**
     * 所属科室 外键，NULL 表示所有权限
     */
    private Integer depId;

    /**
     * 用户所属县区id
     */
    private Integer couId;

    /**
     * 上次登录时间 非空
     */
    private Date uLoginTime;

    /**
     * 用户状态 非空，状态有：正常、已禁用、已删除
     */
    private Integer uStatus;


}
