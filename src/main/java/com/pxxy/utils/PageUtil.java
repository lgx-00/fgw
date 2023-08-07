package com.pxxy.utils;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxxy.entity.vo.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <h2>分页查询工具类</h2>
 * <p>提供一个静态方法，按分页信息查询。</p>
 *
 * @author xw
 * @since 1.0
 */
public class PageUtil {

    /**
     * 按照分页信息，执行查询方法并分页，然后使用映射方法将查询结果集映射成其他数据类型的结果集。
     * @param p 分页信息
     * @param select 查询的方法
     * @param mapper 映射方法
     * @param <T> 查询得到的结果集的数据类型
     * @param <VO> 希望映射成的数据类型
     * @return 数据集合
     */
    public static <T, VO> PageInfo<VO> selectPage(Page p, ISelect select, Function<T, VO> mapper) {

        PageInfo<T> page = PageHelper.startPage(p.getPageNum(), p.getPageSize()).doSelectPageInfo(select);

        PageInfo<VO> pageInfo = new PageInfo<>();
        BeanUtil.copyProperties(page, pageInfo);

        List<VO> newList = page.getList().stream().map(mapper).collect(Collectors.toList());
        pageInfo.setList(newList);

        return pageInfo;
    }

}
