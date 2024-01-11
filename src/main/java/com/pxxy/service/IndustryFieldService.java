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

    boolean addIndustryField(AddIndustryFieldVO addIndustryFieldVO);

    boolean updateIndustryField(UpdateIndustryFieldVO updateIndustryFieldVO);

    List<IndustryField> getAll();

    boolean deleteIndustryField(Integer infId);

    List<IndustryField> all();

}
