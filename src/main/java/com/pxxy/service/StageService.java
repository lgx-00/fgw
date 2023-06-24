package com.pxxy.service;

import com.pxxy.pojo.Stage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddStageVO;
import com.pxxy.vo.UpdateStageVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
public interface StageService extends IService<Stage> {

    ResultResponse addStage(AddStageVO addStageVO);

    ResultResponse updateStage(UpdateStageVO updateStageVO);

    ResultResponse getAllStage();

    ResultResponse deleteStage(Integer stageId);
}
