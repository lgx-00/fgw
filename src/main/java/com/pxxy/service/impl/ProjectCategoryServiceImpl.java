package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.pxxy.advice.annotations.Cached;
import com.pxxy.mapper.ProjectCategoryMapper;
import com.pxxy.entity.pojo.ProjectCategory;
import com.pxxy.service.BaseService;
import com.pxxy.service.ProjectCategoryService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddProjectCategoryVO;
import com.pxxy.entity.vo.QueryProjectCategoryVO;
import com.pxxy.entity.vo.UpdateProjectCategoryVO;
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
public class ProjectCategoryServiceImpl extends BaseService<ProjectCategoryMapper, ProjectCategory> implements ProjectCategoryService {

    @Override
    public ResultResponse<?> addProjectCategory(AddProjectCategoryVO addProjectCategoryVO) {
        ProjectCategory projectCategory = new ProjectCategory();
        BeanUtil.copyProperties(addProjectCategoryVO,projectCategory);
        save(projectCategory);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<?> updateProjectCategory(UpdateProjectCategoryVO updateProjectCategoryVO) {
        ProjectCategory projectCategory = query().eq("prc_id", updateProjectCategoryVO.getPrcId()).one();
        if (projectCategory == null){
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }

        projectCategory.setPrcName(updateProjectCategoryVO.getPrcName())
                .setPrcPeriod(updateProjectCategoryVO.getPrcPeriod());

        updateById(projectCategory);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<List<QueryProjectCategoryVO>> getAllProjectCategory() {
        List<QueryProjectCategoryVO> queryProjectCategoryVOS = query().orderByDesc("prc_id")
                .list().stream().map(projectCategory -> {
            QueryProjectCategoryVO queryProjectCategoryVO = new QueryProjectCategoryVO();
            BeanUtil.copyProperties(projectCategory, queryProjectCategoryVO);
            return queryProjectCategoryVO;
        }).collect(Collectors.toList());
        return ResultResponse.ok(queryProjectCategoryVOS);
    }

    @Override
    public ResultResponse<?> deleteProjectCategory(Integer prcId) {
        ProjectCategory projectCategory = query().eq("prc_id", prcId).one();
        if (projectCategory == null){
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }
        removeById(prcId);
        return ResultResponse.ok();
    }

    @Override
    public List<ProjectCategory> all() {
        return query().list();
    }
}
