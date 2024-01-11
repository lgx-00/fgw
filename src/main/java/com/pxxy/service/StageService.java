package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.entity.pojo.Stage;
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

    boolean addStage(AddStageVO addStageVO);

    boolean updateStage(UpdateStageVO updateStageVO);

    List<QueryStageVO> getAllStage();

    boolean deleteStage(Integer stageId);
}
