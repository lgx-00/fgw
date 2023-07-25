package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.pojo.Permission;
import com.pxxy.utils.ResultResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
public interface PermissionService extends IService<Permission> {

    ResultResponse<List<Permission>> getPerm();

}
