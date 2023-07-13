package com.pxxy.constant;

/**
 * @author: xrw
 * @date:  15:58
 **/
public class ResponseMessage {

    public static final int OK_CODE = 200;
    public static final int FAIL_CODE = 400;
    public static final int INVALID_TOKEN = 401;
    public static final int PERMISSION_NOT_ENOUGH = 402;

    public static final String OK_MSG = "操作成功";
    public static final String FAIL_MSG = "操作失败";
    public static final String ILLEGAL_OPERATE = "非法操作";

    public static final String ADD_FAILED = "新增失败";
    public static final String DELETE_FAILED = "删除失败";
    public static final String UPDATE_FAILED = "更新失败";
    public static final String UPLOAD_FAILED = "上传文件失败";

    // 项目相关的错误信息
    public static final String COUNTY_AND_TOWN_NOT_MATCH = "辖区和二级辖区不匹配";
    public static final String ILLEGAL_UPDATING_PROJECT = "不能更新已上报的项目";
    public static final String INVALID_PROJECT_CATEGORY_ID = "无效的项目类别编号";
    public static final String INVALID_INDUSTRY_FIELD_ID = "无效的行业领域编号";

    // 调度相关的错误信息
    public static final String LOCK_FAILED = "锁定失败";
    public static final String UNLOCK_FAILED = "解锁失败";
    public static final String CANNOT_MAKE_DIR = "无法创建文件";
    public static final String NO_APPENDIX = "该调度无附件可下载";
    public static final String APPENDIX_NOT_AVAILABLE = "该附件已失效";

}
