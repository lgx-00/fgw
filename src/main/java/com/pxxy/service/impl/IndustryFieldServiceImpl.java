package com.pxxy.service.impl;

import com.pxxy.advice.annotations.Cached;
import com.pxxy.mapper.IndustryFieldMapper;
import com.pxxy.pojo.IndustryField;
import com.pxxy.service.BaseService;
import com.pxxy.service.IndustryFieldService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddIndustryFieldVO;
import com.pxxy.vo.UpdateIndustryFieldVO;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pxxy.constant.ResponseMessage.ILLEGAL_OPERATE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xrw
 * @since 2023-06-14
 */
@Cached
@Service
public class IndustryFieldServiceImpl extends BaseService<IndustryFieldMapper, IndustryField> implements IndustryFieldService {

    @Override
    public ResultResponse<?> addIndustryField(AddIndustryFieldVO addIndustryFieldVO) {
        IndustryField industryField = new IndustryField()
                .setInfName(addIndustryFieldVO.getInfName())
                .setInfRemark(addIndustryFieldVO.getInfRemark());
        baseMapper.insert(industryField);
        return ResultResponse.ok(industryField);
    }

    @Override
    public ResultResponse<?> updateIndustryField(UpdateIndustryFieldVO updateIndustryFieldVO) {
        IndustryField industryField = query().eq("inf_id", updateIndustryFieldVO.getInfId()).one();
        if (industryField == null) {
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }
        industryField.setInfName(updateIndustryFieldVO.getInfName())
                .setInfRemark(updateIndustryFieldVO.getInfRemark());
        baseMapper.updateById(industryField);
        return ResultResponse.ok(industryField);
    }

    @Override
    public ResultResponse<List<IndustryField>> getAll() {
        List<IndustryField> industryFields = baseMapper.selectList(null);
        return ResultResponse.ok(industryFields);
    }

    @Override
    public ResultResponse<?> deleteIndustryField(Integer infId) {
        IndustryField industryField = query().eq("inf_id", infId).one();
        if (industryField != null) {
            removeById(infId);
            return ResultResponse.ok();
        }
        return ResultResponse.fail(ILLEGAL_OPERATE);
    }

    @Override
    public List<IndustryField> all() {
        return query().list();
    }
}

