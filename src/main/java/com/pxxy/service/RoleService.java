package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.pxxy.entity.pojo.Role;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddRoleVO;
import com.pxxy.entity.vo.Page;
import com.pxxy.entity.vo.QueryRoleVO;
import com.pxxy.entity.vo.UpdateRoleVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
public interface RoleService extends IService<Role> {

    ResultResponse<PageInfo<QueryRoleVO>> getAllRole(Page page);

    ResultResponse<PageInfo<QueryRoleVO>> getVagueRole(Page page, String rName);

    ResultResponse<?> addRole(AddRoleVO roleVO);

    ResultResponse<?> deleteRole(Integer roleId);

    ResultResponse<?> updateRole(UpdateRoleVO roleVO);

}
