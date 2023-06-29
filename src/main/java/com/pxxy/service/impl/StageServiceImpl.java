package com.pxxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pxxy.pojo.Stage;
import com.pxxy.mapper.StageMapper;
import com.pxxy.service.StageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.StageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static com.pxxy.constant.SystemConstant.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xrw
 * @since 2023-06-14
 */
@Service
public class StageServiceImpl extends ServiceImpl<StageMapper, Stage> implements StageService {

    @Resource
    private StageMapper stageMapper;

    @Override
    public ResultResponse addStage(StageVO stageVO) {
        Stage stage = new Stage()
                .setStageName(stageVO.getStageName())
                .setStageRemark(stageVO.getStageRemark());
        stageMapper.insert(stage);
        return ResultResponse.ok(stage);
    }

    @Override
    public ResultResponse updateStage(Integer stageId, StageVO stageVO) {
        Stage stage = query().eq("stage_id", stageId).ne("stage_status", DELETED_STATUS).one();
        if (stage == null){
            return ResultResponse.fail("非法操作");
        }
        stage.setStageName(stageVO.getStageName());
        stageMapper.updateById(stage);
        return ResultResponse.ok(stage);
    }

    @Override
    public ResultResponse selectStage() {
        QueryWrapper<Stage> queryWrapper = new QueryWrapper<Stage>();
        queryWrapper.ne("stage_status", DELETED_STATUS);
        List<Stage> selectList = stageMapper.selectList(queryWrapper);
        return ResultResponse.ok(selectList);
    }

    @Override
    public ResultResponse deleteStage(Integer stageId) {
        Stage stage = query().eq("stage_id", stageId).ne("stage_status", DELETED_STATUS).one();
        if (stage == null){
            return ResultResponse.fail("非法操作");
        }
        stage.setStageStatus(DELETED_STATUS);
        updateById(stage);
        return ResultResponse.ok();
    }
}
