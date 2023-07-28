package com.pxxy.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * Class name: BaseService
 *
 * Create time: 2023/7/25 20:58
 *
 * @author xw
 * @version 1.0
 */
public abstract class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    @Override
    public QueryChainWrapper<T> query() {
        return super.query();
    }
}
