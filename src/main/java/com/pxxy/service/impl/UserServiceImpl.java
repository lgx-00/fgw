package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.constant.SystemConstant;
import com.pxxy.dto.LoginFormDTO;
import com.pxxy.dto.PermissionDTO;
import com.pxxy.dto.UserDTO;
import com.pxxy.mapper.UserMapper;
import com.pxxy.pojo.*;
import com.pxxy.service.*;
import com.pxxy.utils.Md5Utils;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddUserVO;
import com.pxxy.vo.QueryUserVO;
import com.pxxy.vo.RoleVO;
import com.pxxy.vo.UpdateUserVO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.pxxy.constant.SystemConstant.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hs
 * @since 2023-06-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RoleService roleService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private CountyService countyService;

    @Resource
    private PermissionService permissionService;

    @Resource
    private RolePermissionService rolePermissionService;

    // 用来存储被禁用用户的禁用时间
    static Map<String, Date> disableTimeMap = new HashMap<>();

    static Map<String, Integer> mistakeTimes = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public ResultResponse login(LoginFormDTO loginForm, HttpSession session) {

        // 根据用户名查询用户
        String uName = loginForm.getUName();

        User user = query().eq("u_name", uName).one();

        if (user == null){
            return ResultResponse.fail("用户不存在！");
        }

        // 用户提交的密码给加密
        String password = Md5Utils.code(loginForm.getUPassword());

        // 判断用户是否处于禁用时间中
        Date date = disableTimeMap.get(USER_DISABLED_TIME_KEY + user.getUId());
        if (date != null) {
            // 当前时间减去当时禁用时间
            long time = new Date().getTime() - date.getTime();
            if (time / 1000 / 60 >= USER_DEFAULT_DISABLE_TIME){
                disableTimeMap.remove(USER_DISABLED_TIME_KEY + user.getUId());
                user.setUStatus(DEFAULT_STATUS);
                updateById(user);
            }else {
                return ResultResponse.fail(String.format("当前用户已被禁用，请%d分钟后再试", USER_DEFAULT_DISABLE_TIME));
            }
        }

        // 先判断用户状态
        switch (user.getUStatus()) {
            // 是否被删除
            case DELETED_STATUS:
                return ResultResponse.fail("用户名或密码错误!");
            // 是否被禁用
            case USER_DISABLED_STATUS:
                return ResultResponse.fail("用户已被禁用，请联系管理员解除!");
            // 状态正常
            case DEFAULT_STATUS:
                if (!password.equals(user.getUPassword())) {
                    // 密码错误
                    // 错误次数+1
                    // 用户提交错误登录信息次数
                    int aMistakeTimes = mistakeTimes.getOrDefault(USER_MISTAKE_TIMES_KEY + user.getUId(), USER_MISTAKE_DEFAULT_TIMES) + 1;
                    mistakeTimes.put(USER_MISTAKE_TIMES_KEY + user.getUId(), aMistakeTimes) ;
                    if (aMistakeTimes > USER_MISPASS_TIMES) {
                        // 将用户状态设置为禁用
                        user.setUStatus(USER_DISABLED_STATUS);
                        updateById(user);
                        // 将最后一次输入错误的时间存入Map
                        disableTimeMap.put(USER_DISABLED_TIME_KEY + user.getUId(), new Date());
                        // 错误次数置零
                        mistakeTimes.put(USER_MISTAKE_TIMES_KEY + user.getUId(), USER_MISTAKE_DEFAULT_TIMES);
                    }
                    return ResultResponse.fail("用户名或密码错误！");
                }else {
                    // 登录成功，更新用户登录时间，并将用户信息存入session
                    user.setULoginTime(new Date());
                    updateById(user);

                    // *查询用户权限
                    Map<String, Integer> permission = new HashMap<>();
                    List<UserRole> userRoleList = userRoleService.query().eq("u_id", user.getUId()).list();

                    // 角色id集合
                    List<Integer> roleIds = userRoleList.stream().map(UserRole::getRId).collect(Collectors.toList());

                    for (Integer roleId : roleIds) {
                        List<RolePermission> rolePermissionList = rolePermissionService.query().eq("r_id", roleId).list();
                        for (RolePermission rolePermission : rolePermissionList) {
                            PermissionDTO permissionDTO = new PermissionDTO();
                            BeanUtil.copyProperties(rolePermission, permissionDTO);
                            permissionDTO.setPPath(permissionService.query().eq("p_id",rolePermission.getPId()).one().getPPath());

                            int perm = permission.getOrDefault(permissionDTO.getPPath(),0);
                            permission.put(permissionDTO.getPPath(), perm | permissionDTO.getRpDetail());
                        }
                    }

                    session.setAttribute("user",new UserDTO(user.getUId(),user.getUName(), permission));
                    return ResultResponse.ok();
                }
            default:
                return ResultResponse.fail("用户状态异常！");
        }
    }

    @Override
    public ResultResponse logout(HttpSession session) {
        if (session != null) {
            // 退出登录，如果session存在吗，则销毁
            session.invalidate();
            return ResultResponse.ok("销毁session成功！");
        }
        return ResultResponse.fail("销毁session失败！");
    }

    @Override
    @Transactional
    public ResultResponse addUser(AddUserVO addUserVO) {
        User user = new User();
        // 对象属性拷贝
        BeanUtil.copyProperties(addUserVO,user);

        // MD5加密密码
        user.setUPassword(Md5Utils.code(user.getUPassword()));

        try {
            save(user);
        } catch (DuplicateKeyException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("user.user_u_name__uindex")) {
                return ResultResponse.fail("用户名重复！");
            } else {
                return ResultResponse.fail("操作失败，未知原因！");
            }
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                return ResultResponse.fail("非法操作！");
            } else {
                log.error("用户插入失败！", e);
                return ResultResponse.fail("操作失败，未知原因！");
            }
        }

        List<RoleVO> roleList = addUserVO.getRoleList();

        List<UserRole> userRoleList = roleList.stream().map(role -> {
            UserRole userRole = new UserRole();
            userRole.setRId(role.getRId());
            userRole.setUId(user.getUId());
            return userRole;
        }).collect(Collectors.toList());

        if (!userRoleService.saveBatch(userRoleList)) {
            return ResultResponse.fail("新增用户失败！");
        }
        return ResultResponse.ok();

    }

    @Override
    public ResultResponse deleteUser(Integer userId) {
        User user = query().eq("u_id", userId).one();
        user.setUStatus(DELETED_STATUS);
        updateById(user);
        return ResultResponse.ok();
    }

    @Override
    @Transactional
    public ResultResponse modifyUser(UpdateUserVO updateUserVO) {

        if (updateUserVO.getRoleList() == null || updateUserVO.getRoleList().size() == 0) {
            return ResultResponse.fail("非法操作！");
        }

        Integer uid = updateUserVO.getUId();
        User user = query().eq("u_id", uid).one();
        user.setUName(updateUserVO.getUName())
                .setUPassword(Md5Utils.code(updateUserVO.getUPassword()))
                .setDepId(updateUserVO.getDepId())
                .setCouId(updateUserVO.getCouId());
        // 修改用户信息
        updateById(user);
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getUId, uid);
        // 先删除
        userRoleService.remove(userRoleLambdaQueryWrapper);
        // 再加入
        List<RoleVO> roleList = updateUserVO.getRoleList();
        List<UserRole> userRoleList = roleList.stream().map(role -> {
            UserRole userRole = new UserRole();
            userRole.setUId(uid);
            userRole.setRId(role.getRId());
            return userRole;
        }).collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
        return ResultResponse.ok();
    }

    @Override
    @Transactional
    public ResultResponse getAllUser(Integer pageNum) {
        //  根据类型分页查询
        Page<User> page = query().page(new Page<>(pageNum, DEFAULT_PAGE_SIZE));

        // 过滤掉超级管理员以及被删除的用户
        List<User> userList = page.getRecords().stream().filter(user ->
                !user.getUName().equals("123") && user.getUStatus() != DELETED_STATUS)
                .collect(Collectors.toList());

        List<QueryUserVO> queryUserVOS = userList.stream().map(user -> {
            QueryUserVO queryUserVO = new QueryUserVO();
            BeanUtil.copyProperties(user, queryUserVO);
            // 根据科室id获取科室名
            Department department = departmentService.query().eq("dep_id", user.getDepId()).one();
            if (department.getDepStatus() != SystemConstant.DELETED_STATUS) {
                queryUserVO.setDepName(department.getDepName());
            }
            // 根据辖区id获取辖区名
            County county = countyService.query().eq("cou_id", user.getCouId()).one();
            if (county.getCouStatus() != SystemConstant.DELETED_STATUS) {
                queryUserVO.setCouName(county.getCouName());
            }
            // 根据用户id查询用户角色
            List<UserRole> userRoleList = userRoleService.query().eq("u_id", user.getUId()).list();
            // 查询角色名
            List<String> roleNames = userRoleList.stream().map(userRole -> {
                Role role = roleService.query().eq("r_id", userRole.getRId()).one();
                return role.getRName();
            }).collect(Collectors.toList());

            queryUserVO.setRoleList(roleNames);

            return queryUserVO;
        }).collect(Collectors.toList());

        //  返回数据
        return ResultResponse.ok(queryUserVOS);
    }

    @Override
    public ResultResponse getVagueUser(int pageNum, String uName) {
        //  根据类型分页查询
        Page<User> page = query().like("u_name",uName).page(new Page<>(pageNum, DEFAULT_PAGE_SIZE));

        // 过滤掉超级管理员以及被删除的用户
        List<User> userList = page.getRecords().stream().filter(user ->
                !user.getUName().equals("123") && user.getUStatus() != DELETED_STATUS)
                .collect(Collectors.toList());

        List<QueryUserVO> queryUserVOS = userList.stream().map(user -> {
            QueryUserVO queryUserVO = new QueryUserVO();
            BeanUtil.copyProperties(user, queryUserVO);
            // 根据科室id获取科室名
            Department department = departmentService.query().eq("dep_id", user.getDepId()).one();
            if (department.getDepStatus() != SystemConstant.DELETED_STATUS) {
                queryUserVO.setDepName(department.getDepName());
            }
            // 根据辖区id获取辖区名
            County county = countyService.query().eq("cou_id", user.getCouId()).one();
            if (county.getCouStatus() != SystemConstant.DELETED_STATUS) {
                queryUserVO.setCouName(county.getCouName());
            }
            // 根据用户id查询用户角色
            List<UserRole> userRoleList = userRoleService.query().eq("u_id", user.getUId()).list();
            // 查询角色名
            List<String> roleNames = userRoleList.stream().map(userRole -> {
                Role role = roleService.query().eq("r_id", userRole.getRId()).one();
                return role.getRName();
            }).collect(Collectors.toList());

            queryUserVO.setRoleList(roleNames);

            return queryUserVO;
        }).collect(Collectors.toList());

        // 返回数据
        return ResultResponse.ok(queryUserVOS);
    }

}
