package com.pxxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.mapper.IndustryFieldMapper;
import com.pxxy.pojo.IndustryField;
import com.pxxy.service.IndustryFieldService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddIndustryFieldVO;
import com.pxxy.vo.UpdateIndustryFieldVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.pxxy.constant.SystemConstant.DELETED_STATUS;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xrw
 * @since 2023-06-14
 */
@Service
public class IndustryFieldServiceImpl extends ServiceImpl<IndustryFieldMapper, IndustryField> implements IndustryFieldService {

    @Resource
    private IndustryFieldMapper IndustryFieldMapper;

    @Override
    public ResultResponse addIndustryField(AddIndustryFieldVO addIndustryFieldVO) {
        IndustryField industryField = new IndustryField()
                .setInfName(addIndustryFieldVO.getInfName())
                .setInfRemark(addIndustryFieldVO.getInfRemark());
        IndustryFieldMapper.insert(industryField);
        return ResultResponse.ok(industryField);
    }

    @Override
    public ResultResponse updateIndustryField(UpdateIndustryFieldVO updateIndustryFieldVO) {
        IndustryField industryField = query().eq("inf_id", updateIndustryFieldVO.getInfId()).one();
        if (industryField == null) {
            return ResultResponse.fail("非法操作");
        }
        industryField.setInfName(updateIndustryFieldVO.getInfName());
        IndustryFieldMapper.updateById(industryField);
        return ResultResponse.ok(industryField);
    }

    @Override
    public ResultResponse selectIndustryField() {
        List<IndustryField> industryFields = IndustryFieldMapper.selectList(null);
        return ResultResponse.ok(industryFields);
    }

    @Override
    public ResultResponse deleteIndustryField(Integer infId) {
        IndustryField industryField = query().eq("inf_id", infId).one();
        if (industryField != null) {
            removeById(infId);
            return ResultResponse.ok();
        }
        return ResultResponse.fail("非法操作");
    }
}

