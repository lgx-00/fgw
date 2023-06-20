package com.pxxy.service;

import com.pxxy.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddRoleVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
public interface RoleService extends IService<Role> {

    ResultResponse getAllRole(Integer pageNum);

    ResultResponse getVagueRole(Integer pageNum, String rName);

    ResultResponse addRole(AddRoleVO roleVO);
}
