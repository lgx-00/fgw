package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.pxxy.entity.pojo.Operation;
import com.pxxy.entity.vo.OperationVO;
import com.pxxy.entity.vo.Page;
import com.pxxy.entity.vo.QueryOperationVO;

/**
 * Interface name: OperationService
 *
 * Create time: 2023/7/26 16:17
 *
 * @author xw
 * @version 1.0
 */
public interface OperationService extends IService<Operation> {

    /**
     * 分页查询所有操作记录
     *
     * @param page 分页详情
     * @return 一页操作记录
     */
    PageInfo<OperationVO> all(Page page);

    /**
     * 分页模糊查询操作记录
     *
     * @param page 分页详情
     * @param queryOperationVO 模糊查询的条件
     * @return 一页操作记录
     */
    PageInfo<OperationVO> vague(Page page, QueryOperationVO queryOperationVO);

}
