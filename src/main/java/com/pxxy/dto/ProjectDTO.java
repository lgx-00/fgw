package com.pxxy.dto;

import com.pxxy.enums.ProjectStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.Optional;

import static com.pxxy.constant.SystemConstant.INFINITY_DATE;
import static com.pxxy.constant.SystemConstant.ZERO_DATE;

/**
 * @author hesen
 * @since 2023-07-07-12:49
 */

@Data
@ApiModel(value = "查询项目模型")
public class ProjectDTO {

    @ApiModelProperty(value = "科室编号")
    private Integer depId;

    @ApiModelProperty(value = "辖区编号")
    private Integer couId;

    @ApiModelProperty(value = "用户编号")
    private Integer uId;

    @ApiModelProperty(value = "项目名称")
    private String proName;

    @ApiModelProperty(value = "二级辖区编号")
    private Integer townId;

    @ApiModelProperty(value = "项目类别编号")
    private Integer prcId;

    @ApiModelProperty(value = "行业领域编号")
    private Integer infId;

    @ApiModelProperty(value = "项目状态")
    private Integer proStatus;

    @ApiModelProperty(value = "从")
    private Date beginTime;

    @ApiModelProperty(value = "至")
    private Date endTime;

    @ApiModelProperty(value = "标记1")
    private String proMark1;

    @ApiModelProperty(value = "标记2")
    private String proMark2;

    @ApiModelProperty(value = "标记3")
    private String proMark3;

    @Max(value = 3, message = "项目阶段为非法值！")
    @Min(value = 0, message = "项目阶段为非法值！")
    private Integer projectStage;

    public void setProName(String proName) {
        if (proName != null && proName.length() == 0) {
            this.proName = null;
            return;
        }
        this.proName = proName;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = Optional.ofNullable(beginTime).orElse(ZERO_DATE);
    }

    public void setEndTime(Date endTime) {
        this.endTime = Optional.ofNullable(endTime).orElse(INFINITY_DATE);
    }

    public void setProjectStage(Integer projectStage) {
        this.projectStage = Optional.ofNullable(projectStage).orElse(0);
    }

    public void setProStatus(Integer proStatus) {
        this.proStatus = proStatus;
    }

    public void setProStatus(ProjectStatusEnum status) {
        this.proStatus = status.val;
    }
}