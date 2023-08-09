package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.stream.SimpleCollector;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pxxy.advice.annotations.Cached;
import com.pxxy.mapper.CountyMapper;
import com.pxxy.entity.pojo.County;
import com.pxxy.entity.pojo.Town;
import com.pxxy.service.BaseService;
import com.pxxy.service.CountyService;
import com.pxxy.service.TownService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddCountyVO;
import com.pxxy.entity.vo.QueryCountyVO;
import com.pxxy.entity.vo.QueryTownVO;
import com.pxxy.entity.vo.UpdateCountyVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.hutool.core.stream.CollectorUtil.CH_ID;
import static com.pxxy.constant.ResponseMessage.FAIL_MSG;
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
@Cached(parent = TownServiceImpl.class)
public class CountyServiceImpl extends BaseService<CountyMapper, County> implements CountyService {

    @Resource
    private TownService townService;

    private static final Function<Town, QueryTownVO> mapTownToVO =
            t -> new QueryTownVO(t.getTownId(), t.getTownName());

    @Override
    @Transactional
    public ResultResponse<?> addCounty(AddCountyVO addCountyVO) {
        County county = new County();
        county.setCouName(addCountyVO.getCouName());
        save(county);

        List<Town> towns = addCountyVO.getTownNames().stream()
                .map(name -> new Town(county.getCouId(), name)).collect(Collectors.toList());
        townService.saveBatch(towns);
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
        townService.remove(new QueryWrapper<Town>().eq("cou_id", couId));

        // 再加入
        List<Town> towns = updateCountyVO.getTownNames().stream()
                .map(name -> new Town().setCouId(couId).setTownName(name)).collect(Collectors.toList());
        townService.saveBatch(towns);

        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<List<QueryCountyVO>> getAllCounties() {
        List<County> countyList = query().orderByDesc("cou_id").list();

        List<Integer> couIds = countyList.stream().map(County::getCouId).collect(Collectors.toList());
        Map<Integer, List<QueryTownVO>> couIdMapTowns = townService.query().in("cou_id", couIds).list()
                .stream().collect(Collectors.groupingBy(Town::getCouId, new SimpleCollector<>(
                        ArrayList::new, (list, town) -> list.add(mapTownToVO.apply(town)), null, CH_ID)));

        List<QueryCountyVO> counties = countyList.stream()
                .map(c -> new QueryCountyVO(c.getCouId(), c.getCouName(),
                        Optional.ofNullable(couIdMapTowns.get(c.getCouId())).orElse(Collections.emptyList())))
                .collect(Collectors.toList());
        return ResultResponse.ok(counties);
    }

    @Override
    public ResultResponse<?> deleteCounty(Integer couId) {
        County county = query().eq("cou_id", couId).one();
        if (county == null) {
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }
        removeById(couId);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<QueryCountyVO> getCounty(Integer couId) {
        County county = getById(couId);
        if (county == null) {
            return ResultResponse.fail(FAIL_MSG);
        }
        List<QueryTownVO> towns = townService.query().eq("cou_id", couId)
                .ne("town_status", DELETED_STATUS).list().stream()
                .map(mapTownToVO).collect(Collectors.toList());
        QueryCountyVO vo = new QueryCountyVO();
        BeanUtil.copyProperties(county, vo);
        vo.setTowns(towns);
        return ResultResponse.ok(vo);
    }

    @Override
    public List<County> all() {
        return query().list();
    }

}
