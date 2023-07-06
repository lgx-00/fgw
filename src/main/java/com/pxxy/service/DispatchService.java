package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.pojo.Dispatch;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.QueryDispatchVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
public interface DispatchService extends IService<Dispatch> {

    ResultResponse<List<QueryDispatchVO>> getAllDispatch(Integer pageNum, Integer proId);
}
