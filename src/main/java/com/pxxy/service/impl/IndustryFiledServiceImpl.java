package com.pxxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.mapper.IndustryFiledMapper;
import com.pxxy.pojo.IndustryFiled;
import com.pxxy.service.IndustryFiledService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddIndustryFiledVO;
import com.pxxy.vo.UpdateIndustryFiledVO;
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
public class IndustryFiledServiceImpl extends ServiceImpl<IndustryFiledMapper, IndustryFiled> implements IndustryFiledService {

    @Resource
    private IndustryFiledMapper industryFiledMapper;

    @Override
    public ResultResponse addIndustryFiled(AddIndustryFiledVO addIndustryFiledVO) {
        IndustryFiled industryFiled = new IndustryFiled()
                .setInfName(addIndustryFiledVO.getInfName())
                .setInfRemark(addIndustryFiledVO.getInfRemark());
        industryFiledMapper.insert(industryFiled);
        return ResultResponse.ok(industryFiled);
    }

    @Override
    public ResultResponse updateIndustryFiled(Integer infId,UpdateIndustryFiledVO updateIndustryFiledVO) {
        IndustryFiled industryFiled = query().eq("inf_id", infId).ne("inf_status", DELETED_STATUS).one();
        if (industryFiled == null) {
            return ResultResponse.fail("非法操作");
        }
        industryFiled.setInfName(updateIndustryFiledVO.getInfName());
        industryFiledMapper.updateById(industryFiled);
        return ResultResponse.ok(industryFiled);
    }

    @Override
    public ResultResponse selectIndustryFiled() {
        QueryWrapper<IndustryFiled> queryWrapper = new QueryWrapper<IndustryFiled>();
        queryWrapper.ne("inf_status", DELETED_STATUS);
        List<IndustryFiled> industryFileds = industryFiledMapper.selectList(queryWrapper);
        return ResultResponse.ok(industryFileds);
    }

    @Override
    public ResultResponse deleteIndustryFiled(Integer infId) {
        IndustryFiled industryFiled = query().eq("inf_id", infId).ne("inf_status", DELETED_STATUS).one();
        if (industryFiled != null) {
            industryFiled.setInfStatus(DELETED_STATUS);
            updateById(industryFiled);
            return ResultResponse.ok();
        }
        return ResultResponse.fail("非法操作");
    }
}
