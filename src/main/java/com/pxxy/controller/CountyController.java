package com.pxxy.controller;


import com.pxxy.service.CountyService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddCountyVO;
import com.pxxy.vo.QueryCountyVO;
import com.pxxy.vo.UpdateCountyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Validated
@CrossOrigin
@RestController
@Api(tags = "乡镇")
@RequestMapping("/basedata/county")
public class CountyController {
    
    @Resource
    private CountyService countyService;
    
    @PostMapping
    @ApiOperation("新增乡镇")
    public ResultResponse<?> addCounty(@RequestBody @Validated AddCountyVO addCountyVO) {
        return countyService.addCounty(addCountyVO);
    }

    @PutMapping
    @ApiOperation("修改乡镇")
    public ResultResponse<?> updateCounty(@RequestBody @Validated UpdateCountyVO updateCountyVO) {
        return countyService.updateCounty(updateCountyVO);
    }

    @GetMapping
    @ApiOperation("查询所有乡镇")
    public ResultResponse<List<QueryCountyVO>> getAllCounty(){
        return countyService.getAllCounty();
    }

    @DeleteMapping
    @ApiOperation("删除乡镇")
    public ResultResponse<?> deleteCounty(@RequestParam Integer couId) {
        return countyService.deleteCounty(couId);
    }


}

