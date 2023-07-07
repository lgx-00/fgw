package com.pxxy.dto;

import lombok.Data;

import java.util.Date;
import java.util.Optional;

/**
 * @Author: hesen
 * @Date: 2023-07-07-12:49
 * @Description:
 */

@Data
public class GetVagueProjectDTO {

    private Integer depId;

    private Integer couId;

    private Integer uId;

    private String proName;

    private Integer townId;

    private Integer prcId;

    private Integer infId;

    private Integer proStatus;

    private Date beginTime;

    private Date endTime;

    private int projectStage;

    public GetVagueProjectDTO() {
    }

    public GetVagueProjectDTO(Integer depId, Integer couId, Integer uId, String proName, Integer townId, Integer prcId, Integer infId, Integer proStatus, Date beginTime, Date endTime, int projectStage) {
        this.depId = depId;
        this.couId = couId;
        this.uId = uId;
        this.proName = proName;
        this.townId = townId;
        this.prcId = prcId;
        this.infId = infId;
        this.proStatus = proStatus;
        this.beginTime = Optional.ofNullable(beginTime).orElse(new Date(0));
        this.endTime = Optional.ofNullable(endTime).orElse(new Date(7985664000000L));
        this.projectStage = projectStage;
    }
}
