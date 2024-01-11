package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.advice.annotations.Cached;
import com.pxxy.entity.pojo.Stage;
import com.pxxy.entity.vo.AddStageVO;
import com.pxxy.entity.vo.QueryStageVO;
import com.pxxy.entity.vo.UpdateStageVO;
import com.pxxy.exceptions.ForbiddenException;
import com.pxxy.mapper.StageMapper;
import com.pxxy.service.StageService;
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
    public boolean addStage(AddStageVO addStageVO) {
        return save(new Stage().setStageName(addStageVO.getStageName()));
    }

    @Override
    public boolean updateStage(UpdateStageVO updateStageVO) throws ForbiddenException {
        Stage stage = query().eq("stage_id", updateStageVO.getStageId()).one();
        if (stage == null) {
            throw new ForbiddenException(ILLEGAL_OPERATE);
        }
        stage.setStageName(updateStageVO.getStageName());
        return updateById(stage);
    }

    @Override
    public List<QueryStageVO> getAllStage() {
        return query().orderByDesc("stage_id").list().stream().map(stage -> {
            QueryStageVO queryStageVO = new QueryStageVO();
            BeanUtil.copyProperties(stage, queryStageVO);
            return queryStageVO;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean deleteStage(Integer stageId) throws ForbiddenException {
        Stage stage = query().eq("stage_id", stageId).one();
        if (stage == null) throw new ForbiddenException(ILLEGAL_OPERATE);
        return removeById(stageId);
    }
}
