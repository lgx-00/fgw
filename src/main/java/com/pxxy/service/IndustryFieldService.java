package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.pojo.IndustryField;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddIndustryFieldVO;
import com.pxxy.vo.UpdateIndustryFieldVO;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author: xrw
 * @date:  12:32
 */
public interface IndustryFieldService extends IService<IndustryField> {

    /**
     * @description: 创建行业领域
     * @author: xrw
     * @date:  10:52
     * @param: [AddIndustryFieldVO]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse<?> addIndustryField(AddIndustryFieldVO addIndustryFieldVO);

    /**
     * @description: 修改行业
     * @author: xrw
     * @date:  10:58
     * @param: [UpdateIndustryFieldVO]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse<?> updateIndustryField(UpdateIndustryFieldVO updateIndustryFieldVO);

    /**
     * @description: 查询所有行业
     * @author: xrw
     * @date:  11:00
     * @param: []
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse<List<IndustryField>> selectIndustryField();

    /**
     * @description: 删除行业领域
     * @author: xrw
     * @date:  11:02
     * @param: []
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse<?> deleteIndustryField(Integer infId);


}
