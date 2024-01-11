package com.pxxy.controller;


import com.pxxy.service.CountyService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddCountyVO;
import com.pxxy.entity.vo.QueryCountyVO;
import com.pxxy.entity.vo.UpdateCountyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.pxxy.constant.ResponseMessage.*;
import static com.pxxy.utils.ResultResponse.fail;
import static com.pxxy.utils.ResultResponse.ok;

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
@Api(tags = "辖区")
@RequestMapping("/basedata/county")
public class CountyController {
    
    @Resource
    private CountyService countyService;
    
    @PostMapping
    @ApiOperation("新增辖区")
    public ResultResponse<?> addCounty(@RequestBody @Validated AddCountyVO addCountyVO) {
        return countyService.addCounty(addCountyVO) ? ok() : fail(ADD_FAILED);
    }

    @PutMapping
    @ApiOperation("修改辖区")
    public ResultResponse<?> updateCounty(@RequestBody @Validated UpdateCountyVO updateCountyVO) {
        return countyService.updateCounty(updateCountyVO) ? ok() : fail(UPDATE_FAILED);
    }

    @GetMapping("/all")
    @ApiOperation("查询所有辖区")
    public ResultResponse<List<QueryCountyVO>> getAllCounties(){
        return ok(countyService.getAllCounties());
    }

    @GetMapping
    @ApiOperation("查询一个辖区")
    public ResultResponse<QueryCountyVO> getCounty(@RequestParam Integer couId) {
        return ok(countyService.getCounty(couId));
    }

    @DeleteMapping
    @ApiOperation("删除辖区")
    public ResultResponse<?> deleteCounty(@RequestParam Integer couId) {
        return countyService.deleteCounty(couId) ? ok() : fail(DELETE_FAILED);
    }


}

