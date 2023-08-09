package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.stream.SimpleCollector;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.pxxy.entity.dto.PermissionDTO;
import com.pxxy.entity.dto.UserDTO;
import com.pxxy.entity.pojo.*;
import com.pxxy.entity.vo.*;
import com.pxxy.exceptions.UserPermissionException;
import com.pxxy.mapper.UserMapper;
import com.pxxy.service.*;
import com.pxxy.utils.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.hutool.core.stream.CollectorUtil.CH_ID;
import static com.pxxy.constant.ResponseMessage.CANNOT_DELETE_ADMINISTRATOR;
import static com.pxxy.constant.ResponseMessage.ILLEGAL_OPERATE;
import static com.pxxy.constant.SystemConstant.*;

/**
 * <p>
 * 服务实现类
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

    @Lazy
    @Resource
    private ProjectService projectService;

    private final Function<User, QueryUserVO> mapUserToVO = user -> {
        QueryUserVO queryUserVO = new QueryUserVO();
        BeanUtil.copyProperties(user, queryUserVO);
        // 根据科室id获取科室名
        Department department = departmentService.query().eq("dep_id", user.getDepId()).one();
        if (department != null) {
            queryUserVO.setDepName(department.getDepName());
        }
        // 根据辖区id获取辖区名
        County county = countyService.query().eq("cou_id", user.getCouId()).one();
        if (county != null) {
            queryUserVO.setCouName(county.getCouName());
        }
        // 根据用户id查询用户角色
        List<UserRole> userRoleList = userRoleService.query().eq("u_id", user.getUId()).list();
        // 查询角色名
        List<String> roleNames = userRoleList.stream().map(userRole -> {
            Role role = roleService.query().eq("r_id", userRole.getRId()).one();
            return role == null ? null : role.getRName();
        }).filter(Objects::nonNull).collect(Collectors.toList());

        queryUserVO.setRoleList(roleNames);
        return queryUserVO;
    };

    // 用来存储被禁用用户的禁用时间
    static Map<String, Date> disableTimeMap = new HashMap<>();

    static Map<String, Integer> mistakeTimes = new ConcurrentHashMap<>();

    @Override
    public ResultResponse<String> login(LoginVO loginVO) {

        // 根据用户名查询用户
        String uName = loginVO.getUName();

        User user = query().eq("u_name", uName).ne("u_status", DELETED_STATUS).one();

        if (user == null) {
            return ResultResponse.fail("用户名或密码错误！");
        }

        // 用户提交的密码给加密
        String password = Md5Util.code(loginVO.getUPassword());

        // 判断用户是否处于禁用时间中
        Date date = disableTimeMap.get(USER_DISABLED_TIME_KEY + user.getUId());
        Date now = new Date();
        if (date != null) {
            // 当前时间减去当时禁用时间
            long time = now.getTime() - date.getTime();
            if (time / 1000 / 60 >= USER_DEFAULT_DISABLE_TIME) {
                disableTimeMap.remove(USER_DISABLED_TIME_KEY + user.getUId());
                user.setUStatus(DEFAULT_STATUS);
                updateById(user);
            } else {
                return ResultResponse.fail(String.format("当前用户已被禁用，请%d分钟后再试", USER_DEFAULT_DISABLE_TIME));
            }
        }

        // 先判断用户状态
        switch (user.getUStatus()) {
            // 是否被禁用
            case USER_DISABLED_STATUS:
                return ResultResponse.fail("用户已被禁用，请联系管理员解除!");
            // 状态正常
            case DEFAULT_STATUS:
                if (!password.equals(user.getUPassword())) {
                    // 密码错误
                    // 错误次数+1
                    // 用户提交错误登录信息次数
                    int aMistakeTimes = mistakeTimes.getOrDefault(USER_MISTAKE_TIMES_KEY + user.getUId(),
                            USER_MISTAKE_DEFAULT_TIMES) + 1;
                    mistakeTimes.put(USER_MISTAKE_TIMES_KEY + user.getUId(), aMistakeTimes);
                    if (aMistakeTimes > USER_MISPASS_TIMES) {
                        // 将用户状态设置为禁用
                        user.setUStatus(USER_DISABLED_STATUS);
                        updateById(user);
                        // 将最后一次输入错误的时间存入Map
                        disableTimeMap.put(USER_DISABLED_TIME_KEY + user.getUId(), now);
                        // 错误次数置零
                        mistakeTimes.put(USER_MISTAKE_TIMES_KEY + user.getUId(), USER_MISTAKE_DEFAULT_TIMES);
                    }
                    return ResultResponse.fail("用户名或密码错误！");
                }

                // 检查是否过年了
                checkIfNewYear(now);
                // 将待调度的项目的状态更新为待调度
                projectService.updateDispatchStatus();

                // *查询用户权限
                UserDTO dto = getPerms(user);
                // 将用户信息存储起来供操作记录切面使用
                UserHolder.saveUser(dto);
                TokenUtil.Token xToken = TokenUtil.generate(dto);

                // 登录成功，更新用户登录时间
                user.setULoginTime(now);
                updateById(user);
                return ResultResponse.ok(xToken.token);
            default:
                return ResultResponse.fail("用户状态异常！");
        }
    }

    /**
     * 获取用户列表的权限
     *
     * @param userIdSet 用户编号列表
     * @return 得到权限信息的用户
     */
    @Transactional
    public Map<Integer, UserDTO> getPerms(Set<Integer> userIdSet) {
        if (Objects.isNull(userIdSet)) return null;
        if (userIdSet.isEmpty()) return Collections.emptyMap();

        List<UserRole> allUserRoles = userRoleService.query().in("u_id", userIdSet).list();
        Map<Integer, List<Integer>> userIdMapRoleIdList = allUserRoles.stream().collect(Collectors.groupingBy(
                UserRole::getUId, new SimpleCollector<UserRole,List<Integer>,List<Integer>>
                        (ArrayList::new, (l, r) -> l.add(r.getRId()), null, CH_ID)));

        Set<Integer> allRolesIdSet = allUserRoles.stream().map(UserRole::getRId).collect(Collectors.toSet());
        Set<Integer> roleIdSet = roleService.query().select("r_id").in("r_id", allRolesIdSet)
                .ne("r_status", DELETED_STATUS).list().stream().map(Role::getRId).collect(Collectors.toSet());

        if (roleIdSet.isEmpty()) {
            return Collections.emptyMap();
        }

        List<RolePermission> allRPList = rolePermissionService.query().in("r_id", roleIdSet).list();
        Set<Integer> allPermIdSet = allRPList.stream().map(RolePermission::getPId).collect(Collectors.toSet());
        Map<Integer, Permission> allPermIdMapPerm = permissionService.query().in("p_id", allPermIdSet)
                .ne("p_status", DELETED_STATUS).list().stream().collect(Collectors.toMap(Permission::getPId, p -> p));

        Map<Integer, List<RolePermission>> userIdMapRPList = new HashMap<>(userIdMapRoleIdList.size());
        userIdMapRoleIdList.forEach((userId, roleId) -> userIdMapRPList.put(userId, allRPList.stream()
                .filter(rp -> roleId.contains(rp.getRId())).collect(Collectors.toList())));

        Map<Integer, UserDTO> ret = new HashMap<>();
        Map<Integer, User> userIdMapUser = query().in("u_id", userIdSet).ne("u_status", DELETED_STATUS).list()
                .stream().collect(Collectors.toMap(User::getUId, u -> u));
        userIdMapRPList.forEach((userId, rpList) -> {
            if (Objects.isNull(rpList) || rpList.isEmpty()) return;
            UserDTO userDTO = new UserDTO(userIdMapUser.get(userId));
            Map<Integer, List<RolePermission>> permIdMapRPList =
                    rpList.stream().collect(Collectors.groupingBy(RolePermission::getPId));
            Map<String, PermissionDTO> perms = new HashMap<>((int) (permIdMapRPList.size() / 0.75) + 1);
            permIdMapRPList.forEach((permId, rps) -> {
                PermissionDTO dto = new PermissionDTO(allPermIdMapPerm.get(permId));
                dto.setRpDetail(rps.stream().reduce(0, (l, r) -> l | r.getRpDetail(), (a, b) -> null));
                perms.put(dto.getPPath(), dto);
            });
            userDTO.setPermission(perms);
            ret.put(userId, userDTO);
        });
        return ret;
    }

    /**
     * 获取单个用户的权限
     *
     * @param user 没有权限信息的用户列表
     * @return 得到权限信息的用户列表
     */
    @Transactional
    public UserDTO getPerms(User user) {
        if (Objects.isNull(user)) return null;
        // 先判断用户是否拥有角色
        List<UserRole> userRoles = userRoleService.query().eq("u_id", user.getUId()).list();
        // 角色id集合
        List<Integer> roleIds = userRoles.stream().map(UserRole::getRId).collect(Collectors.toList());
        long roleCount = roleService.query().in("r_id", roleIds).ne("r_status", DELETED_STATUS).count();

        if (roleCount == 0) {
            throw new UserPermissionException("用户所属的角色已被删除，请联系管理员！");
        }

        List<RolePermission> rolePermissions = rolePermissionService.query().in("r_id", roleIds).list();
        Set<Integer> pIds = rolePermissions.stream().map(RolePermission::getPId).collect(Collectors.toSet());

        Map<String, PermissionDTO> permissions = permissionService.query().in("p_id", pIds).list().stream()
                .map(p -> {
                    PermissionDTO dto = new PermissionDTO(p.getPPath(), p.getPName());
                    dto.setRpDetail(rolePermissions.stream().filter(rp -> rp.getPId().equals(p.getPId()))
                            .reduce(0, (a, b) -> a | b.getRpDetail(), (a, b) -> a | b));
                    return dto;
                }).collect(Collectors.toMap(PermissionDTO::getPPath, dto -> dto));
        return new UserDTO(user.getUId(), user.getUName(), user.getDepId(), permissions);
    }

    private void checkIfNewYear(Date now) {
        // TODO: 2023/7/17 未测试
        User one = query().select("max(u_login_time) u_login_time").one();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(one.getULoginTime());
        int lastLoginYear = calendar.get(Calendar.YEAR);
        calendar.setTime(now);
        int thisYear = calendar.get(Calendar.YEAR);
        if (thisYear > lastLoginYear) {
            projectService.clearDispatch();
        }
    }

    @Override
    @Transactional
    public ResultResponse<?> addUser(AddUserVO addUserVO) {
        User user = new User();
        // 对象属性拷贝
        BeanUtil.copyProperties(addUserVO, user);

        // MD5加密密码
        user.setUPassword(Md5Util.code(user.getUPassword()));

        try {
            save(user);
        } catch (DuplicateKeyException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("user.user_u_name__uindex")) {
                return ResultResponse.fail("用户名已被占用！");
            } else {
                log.error("用户新增失败！", e);
                return ResultResponse.fail("操作失败，未知原因！");
            }
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                return ResultResponse.fail("非法操作！");
            } else {
                log.error("用户新增失败！", e);
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
    public ResultResponse<?> deleteUser(Integer userId) {

        if (userId == 1) {
            return ResultResponse.fail(CANNOT_DELETE_ADMINISTRATOR);
        }

        User user = query().eq("u_id", userId).one();
        user.setUStatus(DELETED_STATUS);
        updateById(user);

        // 强制下线删除的用户
        TokenUtil.invalidate(new UserDTO(userId));

        return ResultResponse.ok();
    }

    @Override
    @Transactional
    public ResultResponse<?> updateUser(UpdateUserVO updateUserVO) {

        if (updateUserVO.getRoleList() == null || updateUserVO.getRoleList().size() == 0) {
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }

        Integer uid = updateUserVO.getUId();
        User user = query().eq("u_id", uid).one();
        user.setUName(updateUserVO.getUName())
                .setUPassword(Md5Util.code(updateUserVO.getUPassword()))
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

        // 强制下线修改的用户
        TokenUtil.invalidate(new UserDTO(uid));

        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<PageInfo<QueryUserVO>> getAllUser(Page page) {
        //  根据类型分页查询
        PageInfo<QueryUserVO> pageInfo = PageUtil.selectPage(page,
                () -> query().ne("u_status",DELETED_STATUS).orderByDesc("u_id").list(), mapUserToVO);
        return ResultResponse.ok(pageInfo);
    }

    @Override
    public ResultResponse<PageInfo<QueryUserVO>> getVagueUser(Page page, String uName) {
        //  根据类型分页查询
        PageInfo<QueryUserVO> pageInfo = PageUtil.selectPage(page,
                () -> query().like("u_name", uName)
                        .ne("u_status", DELETED_STATUS).orderByDesc("u_id").list(), mapUserToVO);
        return ResultResponse.ok(pageInfo);
    }

}
