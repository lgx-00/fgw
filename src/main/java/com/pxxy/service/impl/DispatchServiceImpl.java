package com.pxxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.mapper.DispatchMapper;
import com.pxxy.pojo.Dispatch;
import com.pxxy.service.DispatchService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.QueryDispatchVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Service
public class DispatchServiceImpl extends ServiceImpl<DispatchMapper, Dispatch> implements DispatchService {

    @Override
    public ResultResponse<List<QueryDispatchVO>> getAllDispatch(Integer pageNum, Integer proId) {
        return null;
    }
}
