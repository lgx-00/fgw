package com.pxxy.service;

import com.pxxy.pojo.Stage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddStageVO;
import com.pxxy.vo.UpdateStageVO;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Validated
public interface StageService extends IService<Stage> {

    ResultResponse addStage(@Validated AddStageVO addStageVO);

    ResultResponse updateStage(@Validated UpdateStageVO updateStageVO);

    ResultResponse getAllStage();

    ResultResponse deleteStage(Integer stageId);
}
