package com.pxxy.service.impl;

import com.pxxy.advice.annotations.Cached;
import com.pxxy.entity.pojo.Permission;
import com.pxxy.mapper.PermissionMapper;
import com.pxxy.service.BaseService;
import com.pxxy.service.PermissionService;
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
    public List<Permission> getPerm() {
        return query().ne("p_status", DELETED_STATUS).list();
    }

}
