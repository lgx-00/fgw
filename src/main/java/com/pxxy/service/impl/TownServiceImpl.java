package com.pxxy.service.impl;

import com.pxxy.advice.annotations.Cached;
import com.pxxy.mapper.TownMapper;
import com.pxxy.pojo.Town;
import com.pxxy.service.BaseService;
import com.pxxy.service.TownService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 二级辖区 服务实现类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Cached
@Service
public class TownServiceImpl extends BaseService<TownMapper, Town> implements TownService {

    @Override
    public List<Town> all() {
        return query().list();
    }
}
