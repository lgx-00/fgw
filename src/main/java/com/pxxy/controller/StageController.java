package com.pxxy.controller;


import com.pxxy.service.StageService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.StageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xrw
 * @since 2023-06-14
 */
@RestController
@RequestMapping("/stage")
@Api(tags = "工程进展阶段管理")
public class StageController {

    @Autowired
    private StageService stageService;

    @PostMapping("/add")
    @ApiOperation("新增工程进展阶段")
    public ResultResponse addStage(@RequestBody StageVO stageVO ){
        return stageService.addStage(stageVO);
    }

    @PostMapping("/update/{stageId}")
    @ApiOperation("修改工程进展阶段")
    public ResultResponse updateStage(@PathVariable Integer stageId, @RequestBody StageVO stageVO ){
        return stageService.updateStage(stageId,stageVO);
    }

    @GetMapping("/selectAll")
    @ApiOperation("查询所有工程进展阶段")
    public ResultResponse selectStage(){
        return stageService.selectStage();
    }

    @PostMapping("/delete/{stageId}")
    @ApiOperation("删除工程进展阶段")
    public ResultResponse deleteStage(@PathVariable Integer stageId){
        return stageService.deleteStage(stageId);
    }

}

