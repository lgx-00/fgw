package com.pxxy.mapper;

import com.pxxy.pojo.Dispatch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xrw
 * @since 2023-06-14
 */
public interface DispatchMapper extends BaseMapper<Dispatch> {

    /**
     * @Description: 查询当前年调度过的项目进行统计汇总    date1为所在年第一天；date2为所在月第一天；
     * @Author: xrw 
     * @Date: 2023/7/5 17:28
     **/
    List<Dispatch> getDispatch(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

    @Update("update dispatch set dis_status=1 where dis_id in (" +
              "select max_id from (" +
                "select max(dis_id) max_id " +
                "from dispatch " +
                "where dis_id<#{disId} " +
                  "and pro_id=#{proId} " +
                  "and dis_status <> 5" +
              ") a" +
            ")")
    void lockLastDispatch(@Param("disId") Integer disId, @Param("proId") Integer proId);

    @Select("select dis_id, pro_id, dis_invest, dis_total, dis_plan_year, dis_total_percent, dis_year, dis_year_percent " +
            "from dispatch where dis_id in (select max(dis_id) from dispatch where dis_status <> 5 and dis_id<#{di" +
            "sId} and pro_id=#{proId})")
    Dispatch getLastDispatch(@Param("disId") Integer disId, @Param("proId") Integer proId);

}
