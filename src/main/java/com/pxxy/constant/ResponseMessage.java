package com.pxxy.constant;

/**
 * 全局静态响应值常量
 **/
public class ResponseMessage {

    public static final int OK_CODE = 200;
    public static final int FAIL_CODE = 400;
    public static final int INVALID_TOKEN = 401;
    public static final int PERMISSION_NOT_ENOUGH = 402;

    public static final String OK_MSG = "操作成功";
    public static final String FAIL_MSG = "操作失败";
    public static final String ILLEGAL_OPERATE = "非法操作";
    public static final String CANNOT_DELETE_SUPER_USER = "禁止删除该用户";

    public static final String ADD_FAILED = "新增失败";
    public static final String DELETE_FAILED = "删除失败";
    public static final String UPDATE_FAILED = "更新失败";
    public static final String UPLOAD_FAILED = "上传文件失败";

    // 系统控制
    public static final String NO_PERMISSION_FOR_SYSTEM_CONTROL = "你没有执行此项操作的权限";

    // 项目相关的错误信息
    public static final String COUNTY_AND_TOWN_NOT_MATCH = "辖区和二级辖区不匹配";
    public static final String ILLEGAL_UPDATING_PROJECT = "不能更新已上报的项目";
    public static final String INVALID_PROJECT_CATEGORY_ID = "无效的项目类别编号";
    public static final String INVALID_INDUSTRY_FIELD_ID = "无效的行业领域编号";
    public static final String REPORT_FAILED = "上报的科室不具有管理所选项目的类型的权限";
    public static final String CANNOT_DELETE_REPORTED_PROJECT = "不能删除已上报的项目";

    // 调度相关的错误信息
    public static final String LOCK_FAILED = "锁定失败";
    public static final String UNLOCK_FAILED = "解锁失败";
    public static final String CANNOT_MAKE_DIR = "无法创建文件";
    public static final String NO_APPENDIX = "该调度无附件可下载";
    public static final String APPENDIX_NOT_AVAILABLE = "该附件已失效";
    public static final String CANNOT_DELETE_LOCKED_DISPATCH = "不能删除已锁定的调度";
    public static final String CANNOT_UPDATE_LOCKED_DISPATCH = "不能修改已锁定的调度";
    public static final String PARAMETER_CHECK_FAILED = "数值计算结果错误";
    public static final String ADD_OUT_OF_TIME = "当前不在允许添加调度的时间段";
    public static final String NO_PERMISSION = "你没有操作此项目的调度的权限";
    public static final String ONLY_UNLOCK_LAST_DISPATCH = "只能解锁最后一次调度";
    public static final String ILLEGAL_PROJECT_STATUS = "非法的项目状态";

}
