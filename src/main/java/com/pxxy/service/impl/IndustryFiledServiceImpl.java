package com.pxxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pxxy.pojo.IndustryFiled;
import com.pxxy.mapper.IndustryFiledMapper;
import com.pxxy.service.IndustryFiledService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddIndustryFiledVO;
import com.pxxy.vo.UpdateIndustryFiledVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static com.pxxy.constant.SystemConstant.*;

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
                .setInfStatus(0)
                .setInfRemark(addIndustryFiledVO.getInfRemark());
        industryFiledMapper.insert(industryFiled);
        return ResultResponse.ok(industryFiled);
    }

    @Override
    public ResultResponse updateIndustryFiled(Integer infId,UpdateIndustryFiledVO updateIndustryFiledVO) {
        IndustryFiled industryFiled =query().eq("inf_id",infId).one();
        industryFiled.setInfName(updateIndustryFiledVO.getInfName());
        industryFiledMapper.updateById(industryFiled);
        return ResultResponse.ok(industryFiled);
    }

    @Override
    public ResultResponse selectIndustryFiled() {
        QueryWrapper<IndustryFiled> queryWrapper = new QueryWrapper<IndustryFiled>();
        queryWrapper.in("inf_status",USER_DEFUALT_STATUS);
        List<IndustryFiled> industryFileds = industryFiledMapper.selectList(queryWrapper);
        return ResultResponse.ok(industryFileds);
    }

    @Override
    public ResultResponse deleteIndustryFiled(Integer infId) {
        IndustryFiled industryFiled = query().eq("inf_id", infId).one();
        industryFiled.setInfStatus(USER_DELETED_STATUS);
        updateById(industryFiled);
        return ResultResponse.ok();
    }
}
