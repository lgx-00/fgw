package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.stream.SimpleCollector;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pxxy.advice.annotations.Cached;
import com.pxxy.entity.pojo.County;
import com.pxxy.entity.pojo.Town;
import com.pxxy.entity.vo.AddCountyVO;
import com.pxxy.entity.vo.QueryCountyVO;
import com.pxxy.entity.vo.QueryTownVO;
import com.pxxy.entity.vo.UpdateCountyVO;
import com.pxxy.exceptions.DBException;
import com.pxxy.exceptions.ForbiddenException;
import com.pxxy.mapper.CountyMapper;
import com.pxxy.service.BaseService;
import com.pxxy.service.CountyService;
import com.pxxy.service.TownService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.hutool.core.stream.CollectorUtil.CH_ID;
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
    public boolean addCounty(AddCountyVO addCountyVO) {
        County county = new County();
        county.setCouName(addCountyVO.getCouName());
        if (!save(county)) {
            throw new DBException("Saving county error! ");
        }

        List<Town> towns = addCountyVO.getTowns().stream()
                .map(townVO -> new Town(county.getCouId(), townVO.getTownName())).collect(Collectors.toList());
        return townService.saveBatch(towns);
    }

    @Override
    @Transactional
    public boolean updateCounty(UpdateCountyVO updateCountyVO) {

        Integer couId = updateCountyVO.getCouId();
        County county = query().eq("cou_id",couId).one();
        if (county == null) {
            throw new ForbiddenException(ILLEGAL_OPERATE);
        }

        county.setCouName(updateCountyVO.getCouName());
        updateById(county);

        // 先删除
        townService.remove(new QueryWrapper<Town>().eq("cou_id", couId));

        // 再加入
        List<Town> towns = updateCountyVO.getTowns().stream()
                .map(townVO -> new Town(couId, townVO.getTownName())).collect(Collectors.toList());
        townService.saveBatch(towns);

        return true;
    }

    @Override
    public List<QueryCountyVO> getAllCounties() {
        List<County> countyList = query().orderByDesc("cou_id").list();

        List<Integer> couIds = countyList.stream().map(County::getCouId).collect(Collectors.toList());
        Map<Integer, List<QueryTownVO>> couIdMapTowns = townService.query().in("cou_id", couIds).list()
                .stream().collect(Collectors.groupingBy(Town::getCouId, new SimpleCollector<>(
                        ArrayList::new, (list, town) -> list.add(mapTownToVO.apply(town)), null, CH_ID)));

        return countyList.stream()
                .map(c -> new QueryCountyVO(c.getCouId(), c.getCouName(),
                        Optional.ofNullable(couIdMapTowns.get(c.getCouId())).orElse(Collections.emptyList())))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteCounty(Integer couId) {
        County county = query().eq("cou_id", couId).one();
        if (county == null) throw new ForbiddenException(ILLEGAL_OPERATE);
        removeById(couId);
        return true;
    }

    @Override
    public QueryCountyVO getCounty(Integer couId) {
        County county = getById(couId);
        if (county == null) return null;
        List<QueryTownVO> towns = townService.query().eq("cou_id", couId)
                .ne("town_status", DELETED_STATUS).list().stream()
                .map(mapTownToVO).collect(Collectors.toList());
        QueryCountyVO vo = new QueryCountyVO();
        BeanUtil.copyProperties(county, vo);
        vo.setTowns(towns);
        return vo;
    }

    @Override
    public List<County> all() {
        return query().list();
    }

}
