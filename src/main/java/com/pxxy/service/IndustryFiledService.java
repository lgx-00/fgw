package com.pxxy.service;

import com.pxxy.pojo.IndustryFiled;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddIndustryFiledVO;
import com.pxxy.vo.UpdateIndustryFiledVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author: xrw
 * @date:  12:32
 */
public interface IndustryFiledService extends IService<IndustryFiled> {

    /**
     * @description: 创建行业领域
     * @author: xrw
     * @date:  10:52
     * @param: [AddIndustryFiledVO]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse addIndustryFiled(AddIndustryFiledVO addIndustryFiledVO);

    /**
     * @description: 修改行业
     * @author: xrw
     * @date:  10:58
     * @param: [UpdateIndustryFiledVO]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse updateIndustryFiled(Integer infId,UpdateIndustryFiledVO updateIndustryFiledVO);

    /**
     * @description: 查询所有行业
     * @author: xrw
     * @date:  11:00
     * @param: []
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse selectIndustryFiled();

    /**
     * @description: 删除行业领域
     * @author: xrw
     * @date:  11:02
     * @param: []
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse deleteIndustryFiled(Integer infId);


}
