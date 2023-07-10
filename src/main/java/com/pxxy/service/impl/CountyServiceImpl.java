package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.mapper.CountyMapper;
import com.pxxy.pojo.County;
import com.pxxy.pojo.Town;
import com.pxxy.service.CountyService;
import com.pxxy.service.TownService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddCountyVO;
import com.pxxy.vo.QueryCountyVO;
import com.pxxy.vo.QueryTownVO;
import com.pxxy.vo.UpdateCountyVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.pxxy.constant.ResponseMessage.ILLEGAL_OPERATE;
import static com.pxxy.constant.SystemConstant.DELETED_STATUS;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Service
public class CountyServiceImpl extends ServiceImpl<CountyMapper, County> implements CountyService {

    @Resource
    private TownService townService;

    private static final Function<Town, QueryTownVO> mapTownToVO = t -> {
        QueryTownVO vo = new QueryTownVO();
        vo.setTownId(t.getTownId());
        vo.setTownName(t.getTownName());
        return vo;
    };

    @Override
    @Transactional
    public ResultResponse<?> addCounty(AddCountyVO addCountyVO) {
        County county = new County();
        county.setCouName(addCountyVO.getCouName());
        save(county);

        List<String> townNames = addCountyVO.getTownNames();
        for (String s:townNames) {
            Town town = new Town().setCouId(county.getCouId()).setTownName(s);
            townService.save(town);
        }
        return ResultResponse.ok();
    }

    @Override
    @Transactional
    public ResultResponse<?> updateCounty(UpdateCountyVO updateCountyVO) {

        Integer couId = updateCountyVO.getCouId();
        County county = query().eq("cou_id",couId).one();
        if (county == null) {
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }

        county.setCouName(updateCountyVO.getCouName());
        updateById(county);

        // 先删除
        LambdaQueryWrapper<Town> townLambdaQueryWrapper = new LambdaQueryWrapper<>();
        townLambdaQueryWrapper.eq(Town::getCouId,couId);
        townService.remove(townLambdaQueryWrapper);

        // 再加入
        List<String> townNames = updateCountyVO.getTownNames();
        for (String s:townNames) {
            Town town = new Town().setCouId(couId).setTownName(s);
            townService.save(town);
        }

        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<List<QueryCountyVO>> getAllCounties() {
        List<County> countyList = query().list();
        List<QueryCountyVO> queryCountyVOS = countyList.stream().map(county -> {
            QueryCountyVO queryCountyVO = new QueryCountyVO();
            BeanUtil.copyProperties(county, queryCountyVO);
            List<QueryTownVO> towns = townService.query()
                    .eq("cou_id", county.getCouId())
                    .list()
                    .stream()
                    .map(mapTownToVO)
                    .collect(Collectors.toList());
            queryCountyVO.setTowns(towns);
            return queryCountyVO;
        }).collect(Collectors.toList());
        return ResultResponse.ok(queryCountyVOS);
    }

    @Override
    public ResultResponse<?> deleteCounty(Integer couId) {
        County county = query().eq("cou_id", couId).one();
        if (county == null){
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }
        removeById(couId);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<QueryCountyVO> getCounty(Integer couId) {
        County county = getById(couId);
        List<QueryTownVO> towns = townService.query().eq("cou_id", couId)
                .ne("town_status", DELETED_STATUS).list().stream()
                .map(mapTownToVO).collect(Collectors.toList());
        QueryCountyVO vo = new QueryCountyVO();
        BeanUtil.copyProperties(county, vo);
        vo.setTowns(towns);
        return ResultResponse.ok(vo);
    }

}
