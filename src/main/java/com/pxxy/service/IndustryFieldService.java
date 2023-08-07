package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.entity.pojo.IndustryField;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddIndustryFieldVO;
import com.pxxy.entity.vo.UpdateIndustryFieldVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xrw
 * @since  12:32
 */
public interface IndustryFieldService extends IService<IndustryField> {

    ResultResponse<?> addIndustryField(AddIndustryFieldVO addIndustryFieldVO);

    ResultResponse<?> updateIndustryField(UpdateIndustryFieldVO updateIndustryFieldVO);

    ResultResponse<List<IndustryField>> getAll();

    ResultResponse<?> deleteIndustryField(Integer infId);

    List<IndustryField> all();

}
