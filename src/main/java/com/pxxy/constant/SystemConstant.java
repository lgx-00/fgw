package com.pxxy.constant;

public class SystemConstant {
    // 默认状态
    public static final int         DEFAULT_STATUS                  =                0;
    // 被删除状态
    public static final int         DELETED_STATUS                  =                5;

    // 用户被禁用的状态
    public static final int         USER_DISABLED_STATUS            =                4;
    // 用户登录最大错误次数
    public static final int         USER_MISPASS_TIMES              =                10;
    // 用户禁用时间key
    public static final String      USER_DISABLED_TIME_KEY          =      "disabled:";
    // 用户错误次数key
    public static final String      USER_MISTAKE_TIMES_KEY          =       "mistake:";
    // 用户错误次数初始值
    public static final int         USER_MISTAKE_DEFAULT_TIMES      =                0;
    // 默认用户禁用时间(单位：分钟)
    public static final long        USER_DEFAULT_DISABLE_TIME       =                5;

    // 默认分页大小
    public static final int         DEFAULT_PAGE_SIZE               =               10;

}
