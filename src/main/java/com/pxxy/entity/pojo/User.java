package com.pxxy.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author hs
 * @since 2023-06-13
 */
@Data
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
     * 用户所属辖区编号
     */
    private Integer couId;

    /**
     * 上次登录时间 非空
     */
    private Date uLoginTime;

    /**
     * 用户状态 非空，状态有：正常0、已禁用4、已删除5
     */
    private Integer uStatus;


}
