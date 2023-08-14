package com.pxxy.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 仪表板上的数据
 *
 * Create time: 2023/8/3 09:34
 *
 * @author xw
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("仪表板数据模型")
public class DashboardVO {

    @ApiModelProperty("本月调度额（万元）")
    private Object monthlyInvest;

    @ApiModelProperty("本月调度次数")
    private Object monthlyDispatchingCount;

    @ApiModelProperty("已入库项目数")
    private Object projectQuantity;

    @ApiModelProperty("已完工项目数")
    private Object completeQuantity;

    @ApiModelProperty("最近调度过的项目")
    private List<ProjectVO> lastDispatchingProject;

    @ApiModelProperty("长期未调度的项目")
    private List<ProjectVO> waitingForDispatching;

    @ApiModelProperty("折线图数据")
    private List<LineVO> lineChart;

    @ApiModelProperty("柱状图数据")
    private List<BarVO> barChart;

    @ApiModelProperty("饼图数据")
    private PieVO pieChart;

    @Data
    @ApiModel("仪表板项目列表数据模型")
    public static class ProjectVO {

        @ApiModelProperty("上次调度的日期")
        private Date lastDispatchingDate;

        @ApiModelProperty("项目名称")
        private String proName;

        @ApiModelProperty("调度额")
        private Integer invest;

    }

    @Data
    @ApiModel("仪表板折线图数据模型")
    public static class LineVO {

        @ApiModelProperty("月份")
        private String month;

        @ApiModelProperty("调度额（万元）")
        private Integer invests = 0;

        @ApiModelProperty("调度次数")
        private Integer counts;

    }

    @Data
    @ApiModel("仪表板柱状图数据模型")
    public static class BarVO {

        @ApiModelProperty("月份")
        private String month;

        @ApiModelProperty("未开工的项目数")
        private Integer notStartedQuantities;

        @ApiModelProperty("推进中的项目数")
        private Integer dispatchingQuantities;

        @ApiModelProperty("已完工项目数")
        private Integer completedQuantities;

    }

    @Data
    @ApiModel("仪表板饼图数据模型")
    public static class PieVO {

        @ApiModelProperty("未上报的项目数")
        private Integer preReport;

        @ApiModelProperty("审核中的项目数")
        private Integer review;

        @ApiModelProperty("正常项目数")
        private Integer normal;

        @ApiModelProperty("待调度的项目数")
        private Integer dispatch;

        @ApiModelProperty("未锁定项目数")
        private Integer unlocked;

        @ApiModelProperty("已完工项目数")
        private Integer complete;

    }

}
