package com.pxxy.entity.dto;

import com.pxxy.entity.pojo.Permission;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author hesen
 * @since 2023-06-21-12:28
 * @Description:
 */
@Data
public class PermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer rpDetail = 0;

    private String pPath;

    // 一级标题
    private String title;
    // 二级标题
    private String name;

    public PermissionDTO(String pPath, String pName) {
        this.pPath = pPath;
        if (pName != null && pName.contains("/")) {
            String[] strings = pName.split("/");
            title = strings[0];
            name = strings[1];
        }
    }

    public PermissionDTO(Permission perm) {
        this(perm.getPPath(), perm.getPName());
    }

    public PermissionDTO() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionDTO that = (PermissionDTO) o;
        return rpDetail.equals(that.rpDetail) && pPath.equals(that.pPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rpDetail, pPath);
    }

}
