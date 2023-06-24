package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.pojo.County;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddCountyVO;
import com.pxxy.vo.UpdateCountyVO;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Validated
public interface CountyService extends IService<County> {

    ResultResponse addCounty(@Validated AddCountyVO addCountyVO);

    ResultResponse updateCounty(@Validated UpdateCountyVO updateCountyVO);

    ResultResponse getAllCounty();

    ResultResponse deleteCounty(Integer couId);
}
