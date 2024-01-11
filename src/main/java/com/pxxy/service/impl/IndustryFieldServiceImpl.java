package com.pxxy.service.impl;

import com.pxxy.advice.annotations.Cached;
import com.pxxy.entity.pojo.IndustryField;
import com.pxxy.entity.vo.AddIndustryFieldVO;
import com.pxxy.entity.vo.UpdateIndustryFieldVO;
import com.pxxy.exceptions.ForbiddenException;
import com.pxxy.mapper.IndustryFieldMapper;
import com.pxxy.service.BaseService;
import com.pxxy.service.IndustryFieldService;
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
    public boolean addIndustryField(AddIndustryFieldVO addIndustryFieldVO) {
        IndustryField industryField = new IndustryField()
                .setInfName(addIndustryFieldVO.getInfName())
                .setInfRemark(addIndustryFieldVO.getInfRemark());
        return save(industryField);
    }

    @Override
    public boolean updateIndustryField(UpdateIndustryFieldVO updateIndustryFieldVO) throws ForbiddenException {
        IndustryField industryField = query().eq("inf_id", updateIndustryFieldVO.getInfId()).one();
        if (industryField == null) {
            throw new ForbiddenException(ILLEGAL_OPERATE);
        }
        industryField.setInfName(updateIndustryFieldVO.getInfName())
                .setInfRemark(updateIndustryFieldVO.getInfRemark());
        return updateById(industryField);
    }

    @Override
    public List<IndustryField> getAll() {
        return query().orderByDesc("inf_id").list();
    }

    @Override
    public boolean deleteIndustryField(Integer infId) throws ForbiddenException {
        IndustryField industryField = query().eq("inf_id", infId).one();
        if (industryField != null) {
            return removeById(infId);
        }
        throw new ForbiddenException(ILLEGAL_OPERATE);
    }

    @Override
    public List<IndustryField> all() {
        return query().list();
    }
}

