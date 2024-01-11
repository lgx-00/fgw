package com.pxxy.controller;


import com.pxxy.service.StageService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddStageVO;
import com.pxxy.entity.vo.QueryStageVO;
import com.pxxy.entity.vo.UpdateStageVO;
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
@Api(tags = "阶段")
@CrossOrigin
@RestController
@RequestMapping("/basedata/stage")
public class StageController {
    
    @Resource
    private StageService stageService;

    @PostMapping
    @ApiOperation("新增阶段")
    public ResultResponse<?> addStage(@RequestBody @Validated AddStageVO addStageVO) {
        return stageService.addStage(addStageVO) ? ok() : fail(ADD_FAILED);
    }

    @PutMapping
    @ApiOperation("修改阶段")
    public ResultResponse<?> updateStage(@RequestBody @Validated UpdateStageVO updateStageVO) {
        return stageService.updateStage(updateStageVO) ? ok() : fail(UPDATE_FAILED);
    }

    @GetMapping
    @ApiOperation("查询所有阶段")
    public ResultResponse<List<QueryStageVO>> getAllStage() {
        return ok(stageService.getAllStage());
    }

    @DeleteMapping
    @ApiOperation("删除阶段")
    public ResultResponse<?> deleteStage(@RequestParam Integer stageId) {
        return stageService.deleteStage(stageId) ? ok() : fail(DELETE_FAILED);
    }

}

