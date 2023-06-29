package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.pojo.Role;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddRoleVO;
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

    ResultResponse getAllRole(Integer pageNum);

    ResultResponse getVagueRole(Integer pageNum, String rName);

    ResultResponse addRole(@Validated AddRoleVO roleVO);

    ResultResponse deleteRole(Integer roleId);

    ResultResponse updateRole(@Validated UpdateRoleVO roleVO);
}
