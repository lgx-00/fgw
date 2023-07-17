package com.pxxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
public class Dispatch implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 序号 主键，自增
     */
    @TableId(value = "dis_id", type = IdType.AUTO)
    private Integer disId;

    /**
     * 调度项目 项目的外键，非空
     */
    private Integer proId;

    /**
     * 调度时间 非空
     */
    private Date disTime;

    /**
     * 添加该调度的用户的 ID  非空，外键
     */
    private Integer uId;

    /**
     * 累计完成投资 非空
     */
    private Integer disTotal;

    /**
     * 今年计划投资 非空
     */
    private Integer disPlanYear;

    /**
     * 今年已完成投资 非空
     */
    private Integer disYear;

    /**
     * 累积投资完成率% 百分比值的 100 倍
     */
    private Integer disTotalPercent;

    /**
     * 今年投资完成率% 百分比值的 100 倍
     */
    private Integer disYearPercent;

    /**
     * 主要形象进度
     */
    private String disProgress;

    /**
     * 建设阶段
     */
    private Integer stageId;

    /**
     * 已下达投资
     */
    private Integer disInvest;

    /**
     * 本次申请投资
     */
    private Integer disApply;

    /**
     * 工程进展状况
     */
    private String disSituation;

    /**
     * 报送单位
     */
    private String disToDep;

    /**
     * 项目来源
     */
    private String disSource;

    /**
     * 包保责任领导
     */
    private String disGuarantee;

    /**
     * 所属行业领域
     */
    private String disFiled;

    /**
     * 存在问题
     */
    private String disIssue;

    /**
     * 状态 非空，状态有：0 正常、5 已删除
     */
    private Integer disStatus;

    /**
     * 备注
     */
    private String disRemark;

    /**
     * 上传的附件的链接
     */
    private String disAppendix;

}
