package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.entity.pojo.Stage;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddStageVO;
import com.pxxy.entity.vo.QueryStageVO;
import com.pxxy.entity.vo.UpdateStageVO;

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
