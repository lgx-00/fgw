package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.pojo.Town;

import java.util.List;

/**
 * <p>
 * 二级辖区 服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
public interface TownService extends IService<Town> {

    List<Town> all();

}
