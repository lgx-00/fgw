package com.pxxy.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: hesen
 * @Date: 2023-06-21-12:28
 * @Description:
 */
@Data
public class PermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer rpDetail;

    private String pPath;

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
