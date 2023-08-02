package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.pxxy.mapper.OperationMapper;
import com.pxxy.pojo.Operation;
import com.pxxy.pojo.User;
import com.pxxy.service.BaseService;
import com.pxxy.service.OperationService;
import com.pxxy.service.UserService;
import com.pxxy.utils.PageUtil;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.OperationVO;
import com.pxxy.vo.Page;
import com.pxxy.vo.QueryOperationVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class name: OperationServiceImpl
 *
 * Create time: 2023/7/26 16:19
 *
 * @author xw
 * @version 1.0
 */
@Service
public class OperationServiceImpl extends BaseService<OperationMapper, Operation> implements OperationService {

    @Resource
    private UserService userService;

    private Map<Integer, String> userMap;

    private final Function<Operation, OperationVO> mapOperationToVO = o -> {
        OperationVO operationVO = new OperationVO();
        BeanUtil.copyProperties(o, operationVO);
        operationVO.setUserName(userMap.get(o.getUId()));
        operationVO.setOperStatus(o.getOperStatus());
        operationVO.setOperIp(o.getOperIp());
        operationVO.setOperTime(o.getOperTime());
        return operationVO;
    };

    @Override
    public ResultResponse<PageInfo<OperationVO>> all(Page page) {
        userMap = userService.list().stream().collect(Collectors.toMap(User::getUId, User::getUName));
        return ResultResponse.ok(PageUtil.selectPage(page,
                () -> query().orderByDesc("oper_id").list(), mapOperationToVO));
    }

    @Override
    public ResultResponse<PageInfo<OperationVO>> vague(Page page, QueryOperationVO vo) {
        userMap = userService.list().stream().collect(Collectors.toMap(User::getUId, User::getUName));
        Collection<Integer> userIds = vo.getUName() == null
                ? userMap.keySet()
                : userMap.entrySet().stream()
                .filter(e -> StrUtil.containsIgnoreCase(e.getValue(), vo.getUName()))
                .map(Map.Entry::getKey).collect(Collectors.toList());

        if (userIds.isEmpty()) {
            PageInfo<OperationVO> emptyPageInfo = PageInfo.emptyPageInfo();
            emptyPageInfo.setPageNum(page.getPageNum());
            emptyPageInfo.setPageSize(page.getPageSize());
            return ResultResponse.ok(emptyPageInfo);
        }

        long ipv4;
        try {
            ipv4 = vo.getOperIp() == null ? 0L : Ipv4Util.ipv4ToLong(vo.getOperIp());
        } catch (Exception e) {
            ipv4 = 0L;
        }

        long finalIpv = ipv4;
        return ResultResponse.ok(PageUtil.selectPage(page,
                () -> query().in(userIds.size() < userMap.size(), "u_id", userIds)
                        .eq(Objects.nonNull(vo.getOperMethod()), "oper_method", vo.getOperMethod())
                        .between("oper_time", vo.getBeginTime(), vo.getEndTime())
                        .like(Objects.nonNull(vo.getOperPath()), "oper_path", vo.getOperPath())
                        .eq(Objects.nonNull(vo.getOperIp()), "oper_ip", finalIpv)
                        .eq(Objects.nonNull(vo.getOperStatus()), "oper_status", vo.getOperStatus())
                        .orderByDesc("oper_id")
                        .list(),
                mapOperationToVO));
    }

}
