package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.pxxy.pojo.Role;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddRoleVO;
import com.pxxy.vo.QueryRoleVO;
import com.pxxy.vo.UpdateRoleVO;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Validated
public interface RoleService extends IService<Role> {

    ResultResponse<PageInfo<QueryRoleVO>> getAllRole(Integer pageNum);

    ResultResponse<PageInfo<QueryRoleVO>> getVagueRole(Integer pageNum, String rName);

    ResultResponse<?> addRole(@Validated AddRoleVO roleVO);

    ResultResponse<?> deleteRole(Integer roleId);

    ResultResponse<?> updateRole(@Validated UpdateRoleVO roleVO);
}
