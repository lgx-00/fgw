package com.pxxy.mapper;

import com.pxxy.pojo.Dispatch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
     * @Param: [date1, date2]
     * @return: java.util.List<com.pxxy.pojo.Dispatch>
     *
     **/
    List<Dispatch> getDispatch(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

}
