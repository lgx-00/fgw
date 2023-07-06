package com.pxxy.utils;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: hesen
 * @Date: 2023-07-06-14:30
 * @Description:
 */
public class PageUtil {


    public static <T, VO> PageInfo<VO> selectPage(int pageNum, int pageSize, ISelect select, Function<T, VO> mapper) {
        PageInfo<T> page = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(select);
        PageInfo<VO> pageInfo = new PageInfo<>();
        BeanUtil.copyProperties(page, pageInfo);
        List<VO> newList = page.getList().stream().map(mapper).collect(Collectors.toList());
        pageInfo.setList(newList);
        return pageInfo;
    }

}
