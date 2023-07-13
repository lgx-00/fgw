package com.pxxy.enums;

import static com.pxxy.constant.SystemConstant.DEFAULT_STATUS;
import static com.pxxy.constant.SystemConstant.DELETED_STATUS;

/**
 * Enum: DispatchStatusEnum
 *
 * Create time: 2023/7/13 16:15
 *
 * @author xw
 * @version 1.0
 */
public enum DispatchStatusEnum {

    NORMAL(DEFAULT_STATUS, "正常"),
    LOCKED(1, "已锁定"),
    DELETED(DELETED_STATUS, "已删除");

    public final int val;
    public final String name;

    DispatchStatusEnum(int val, String name) {
        this.val = val;
        this.name = name;
    }

}
