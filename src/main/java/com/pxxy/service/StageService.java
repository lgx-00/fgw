package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.pojo.Stage;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddStageVO;
import com.pxxy.vo.QueryStageVO;
import com.pxxy.vo.UpdateStageVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
public interface StageService extends IService<Stage> {

    ResultResponse<?> addStage(AddStageVO addStageVO);

    ResultResponse<?> updateStage(UpdateStageVO updateStageVO);

    ResultResponse<List<QueryStageVO>> getAllStage();

    ResultResponse<?> deleteStage(Integer stageId);
}
