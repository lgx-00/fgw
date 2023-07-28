package com.pxxy.service.impl;

import com.pxxy.advice.annotations.Cached;
import com.pxxy.mapper.PermissionMapper;
import com.pxxy.pojo.Permission;
import com.pxxy.service.BaseService;
import com.pxxy.service.PermissionService;
import com.pxxy.utils.ResultResponse;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pxxy.constant.SystemConstant.DELETED_STATUS;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Cached
@Service
public class PermissionServiceImpl extends BaseService<PermissionMapper, Permission> implements PermissionService {

    @Override
    public ResultResponse<List<Permission>> getPerm() {
        return ResultResponse.ok(query().ne("p_status", DELETED_STATUS).list());
    }

}
