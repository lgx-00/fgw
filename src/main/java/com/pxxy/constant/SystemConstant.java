package com.pxxy.constant;

import java.util.Date;

public class SystemConstant {
    // 默认状态
    public static final int         DEFAULT_STATUS                  =                0;
    // 被删除状态
    public static final int         DELETED_STATUS                  =                5;

    // 用户被禁用的状态
    public static final int         USER_DISABLED_STATUS            =                4;
    // 用户登录最大错误次数
    public static final int         USER_MISPASS_TIMES              =               10;
    // 用户禁用时间key
    public static final String      USER_DISABLED_TIME_KEY          =      "disabled:";
    // 用户错误次数key
    public static final String      USER_MISTAKE_TIMES_KEY          =       "mistake:";
    // 用户错误次数初始值
    public static final int         USER_MISTAKE_DEFAULT_TIMES      =                0;
    // 默认用户禁用时间(单位：分钟)
    public static final long        USER_DEFAULT_DISABLE_TIME       =                5;

    // 默认分页大小
    public static final int         DEFAULT_PAGE_SIZE               =               20;
    // 默认页数大小
    public static final int         DEFAULT_PAGE_NUM                =                1;

    public static final Date        INFINITY_DATE                   = new Date(7985664000000L);
    public static final Date        ZERO_DATE                       = new Date(0);


    public static final String USER_DATA$UPLOAD_FILE_NAME = "upload-file-name";
    public static final String USER_DATA$UPLOAD_ORIGINAL_FILE_NAME = "upload-file-original-name";
    public static final String USER_DATA$LOGOUT_HANDLERS = "remove-handlers";

}
