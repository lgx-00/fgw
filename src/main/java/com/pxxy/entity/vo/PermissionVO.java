package com.pxxy.entity.vo;

import com.pxxy.entity.pojo.Permission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Objects;

/**
 * Class name: PermissionVO
 *
 * Create time: 2023/8/1 11:00
 *
 * @author xw
 * @version 1.0
 */
@Getter
@ApiModel("权限模型")
public class PermissionVO {

    @ApiModelProperty("权限编号")
    private Integer pId;

    @ApiModelProperty("权限细节，查询 增加 修改 删除")
    private final boolean[] rpDetail = new boolean[4];

    @ApiModelProperty("权限标题")
    private String permTitle;

    @ApiModelProperty("权限名称")
    private String permName;

    public PermissionVO(int rpDetail, Permission p) {
        if (Objects.nonNull(p)) {
            this.permName = p.getName();
            this.permTitle = p.getTitle();
            this.pId = p.getPId();
        }
        this.rpDetail[0] = (rpDetail >> 3 & 1) > 0;
        this.rpDetail[1] = (rpDetail >> 2 & 1) > 0;
        this.rpDetail[2] = (rpDetail >> 1 & 1) > 0;
        this.rpDetail[3] = (rpDetail & 1) > 0;
    }

    public boolean[] getRpDetail() {
        return rpDetail;
    }
}
