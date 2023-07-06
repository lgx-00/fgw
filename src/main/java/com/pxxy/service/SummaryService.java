package com.pxxy.service;

import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.SummaryVO;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @Author: XRW
 * @CreateTime: 2023-07-01  14:40
 */
public interface SummaryService {

    /**
     * @Description: 统计汇总所有项目
     * @Author: xrw
     * @Date: 2023/7/1 14:43
     * @Param: [statisticalType, beginTime, endTime, prcId, infId]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse<?> getSummary(Date beginTime, Date endTime, Integer prcId, Integer infId);

    /**
     * @Description: 导出excel
     * @Author: xrw
     * @Date: 2023/7/3 17:25
     * @Param: [response]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse<?> exportSummaryExcel(HttpServletResponse response, List<SummaryVO> summaryVOList);
}