package com.pxxy.constant;

public class SystemConstant {
    //用户默认状态
    public static final int USER_DEFUALT_STATUS = 0;
    //用户被删除状态
    public static final int USER_DELETED_STATUS = 5;
    //用户被禁用的状态
    public static final int USER_DISABLED_STATUS = 4;
    //用户登录最大错误次数
    public static final int USER_MISPASS_TIMES = 3;
    //用户禁用时间key
    public static final String USER_DISABLED_TIME_KEY = "disabled:";
    //用户错误次数key
    public static final String USER_MISTAKE_TIMES_KEY = "mistake:";
    //用户错误次数初始值
    public static final int USER_MISTAKE_DEFUALT_TIMES = 0;
    //角色默认状态
    public static final int ROLE_DEFUALT_STATUS = 0;
    //默认用户禁用时间(单位：分钟)
    public static final long USER_DEFUALT_DISABLE_TIME = 1;
    //默认分页大小
    public static final int DEFAULT_PAGE_SIZE = 10;
    //科室被删除状态
    public static final int DEPARTMENT_DELETED_STATUS = 5;
    //辖区被删除状态
    public static final int COUNTY_DELETED_STATUS = 5;
}
