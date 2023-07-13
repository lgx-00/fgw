package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.pojo.County;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddCountyVO;
import com.pxxy.vo.QueryCountyVO;
import com.pxxy.vo.UpdateCountyVO;
import org.springframework.validation.annotation.Validated;

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

    ResultResponse<?> addCounty(AddCountyVO addCountyVO);

    ResultResponse<?> updateCounty(UpdateCountyVO updateCountyVO);

    ResultResponse<List<QueryCountyVO>> getAllCounties();

    ResultResponse<?> deleteCounty(Integer couId);

    ResultResponse<QueryCountyVO> getCounty(Integer couId);
}
