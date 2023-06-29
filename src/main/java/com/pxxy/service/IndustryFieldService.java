package com.pxxy.service;

import com.pxxy.pojo.IndustryField;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddIndustryFieldVO;
import com.pxxy.vo.UpdateIndustryFieldVO;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author: xrw
 * @date:  12:32
 */
@Validated
public interface IndustryFieldService extends IService<IndustryField> {

    /**
     * @description: 创建行业领域
     * @author: xrw
     * @date:  10:52
     * @param: [AddIndustryFieldVO]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse addIndustryField(@Validated AddIndustryFieldVO addIndustryFieldVO);

    /**
     * @description: 修改行业
     * @author: xrw
     * @date:  10:58
     * @param: [UpdateIndustryFieldVO]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse updateIndustryField(@Validated UpdateIndustryFieldVO updateIndustryFieldVO);

    /**
     * @description: 查询所有行业
     * @author: xrw
     * @date:  11:00
     * @param: []
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse selectIndustryField();

    /**
     * @description: 删除行业领域
     * @author: xrw
     * @date:  11:02
     * @param: []
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse deleteIndustryField(Integer infId);


}
