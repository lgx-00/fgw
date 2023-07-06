package com.pxxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

import static com.pxxy.constant.SystemConstant.DEFAULT_STATUS;
import static com.pxxy.constant.SystemConstant.DELETED_STATUS;

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
public class Stage implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键	主键，自增
     */
    @TableId(value = "stage_id", type = IdType.AUTO)
    private Integer stageId;

    /**
     * 建设阶段名称 非空
     */
    private String stageName;

    /**
     * 当前状态 非空，状态有：0正常、5已删除
     */
    @TableLogic(value = DEFAULT_STATUS + "",delval = DELETED_STATUS + "")
    private Integer stageStatus;

    /**
     * 备注
     */
    private String stageRemark;


}
