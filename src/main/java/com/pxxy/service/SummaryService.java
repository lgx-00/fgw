package com.pxxy.service;

import com.pxxy.entity.vo.DashboardVO;
import com.pxxy.entity.vo.SummaryDetailsVO;
import com.pxxy.entity.vo.SummaryVO;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author XRW
 * @since 2023-07-01  14:40
 */
public interface SummaryService {

    /**
     * 统计汇总所有项目
     * @author xrw
     * @since 2023/7/7 14:35
     **/
    List<SummaryVO> getSummary(Date beginTime, Date endTime, Integer prcId, Integer infId);

    /**
     * 统计汇总详情
     * @author xrw
     * @since 2023/7/7 14:35
     **/
    List<SummaryDetailsVO> detailsSummary(Date beginTime, Date endTime, Integer prcId, Integer infId);

    /**
     * 导出excel
     * @author xrw
     * @since 2023/7/3 17:25
     **/
    boolean exportSummaryExcel(HttpServletResponse response, List<SummaryVO> summaryVOList);

    /**
     * 获取仪表板上的数据
     * @return 仪表板上的数据
     */
    DashboardVO dashboard();

}
