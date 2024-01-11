package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.entity.pojo.County;
import com.pxxy.entity.vo.AddCountyVO;
import com.pxxy.entity.vo.QueryCountyVO;
import com.pxxy.entity.vo.UpdateCountyVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
public interface CountyService extends IService<County> {

    boolean addCounty(AddCountyVO addCountyVO);

    boolean updateCounty(UpdateCountyVO updateCountyVO);

    List<QueryCountyVO> getAllCounties();

    boolean deleteCounty(Integer couId);

    QueryCountyVO getCounty(Integer couId);

    List<County> all();
}
