package com.pxxy.mapper;

import com.pxxy.entity.dto.SimpleUserDataDTO;
import com.pxxy.entity.vo.DashboardVO;

import java.util.List;
import java.util.Map;

/**
 * <p>统计汇总的查询抽象接口</p>
 *
 * <p>Create time: 2023/8/8 09:43</p>
 *
 * @author xw
 * @version 1.0
 */
public interface SummaryMapper {

    Map<String, Object> getMonthly(SimpleUserDataDTO data);

    Map<String, Object> getTotal(SimpleUserDataDTO data);

    List<DashboardVO.ProjectVO> lastDispatchingProject(SimpleUserDataDTO data);

    List<DashboardVO.ProjectVO> waitingForDispatching(SimpleUserDataDTO data);

    List<DashboardVO.LineVO> line(SimpleUserDataDTO data);

    List<DashboardVO.BarVO> bar(SimpleUserDataDTO data);

    DashboardVO.PieVO pie(SimpleUserDataDTO data);

}
