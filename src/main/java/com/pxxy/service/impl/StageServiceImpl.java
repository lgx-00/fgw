package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.advice.annotations.Cached;
import com.pxxy.mapper.StageMapper;
import com.pxxy.entity.pojo.Stage;
import com.pxxy.service.StageService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddStageVO;
import com.pxxy.entity.vo.QueryStageVO;
import com.pxxy.entity.vo.UpdateStageVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.pxxy.constant.ResponseMessage.ILLEGAL_OPERATE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Cached
@Service
public class StageServiceImpl extends ServiceImpl<StageMapper, Stage> implements StageService {

    @Override
    public ResultResponse<?> addStage(AddStageVO addStageVO) {
        save(new Stage().setStageName(addStageVO.getStageName()));
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<?> updateStage(UpdateStageVO updateStageVO) {
        Stage stage = query().eq("stage_id", updateStageVO.getStageId()).one();
        if (stage == null) {
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }
        stage.setStageName(updateStageVO.getStageName());
        updateById(stage);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<List<QueryStageVO>> getAllStage() {
        List<QueryStageVO> queryStageVOS = query().list().stream().map(stage -> {
            QueryStageVO queryStageVO = new QueryStageVO();
            BeanUtil.copyProperties(stage, queryStageVO);
            return queryStageVO;
        }).collect(Collectors.toList());
        return ResultResponse.ok(queryStageVOS);
    }

    @Override
    public ResultResponse<?> deleteStage(Integer stageId) {
        Stage stage = query().eq("stage_id", stageId).one();
        if (stage == null) {
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }
        removeById(stageId);
        return ResultResponse.ok();
    }
}
