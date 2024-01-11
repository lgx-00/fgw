package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.pxxy.advice.annotations.Cached;
import com.pxxy.entity.pojo.ProjectCategory;
import com.pxxy.entity.vo.AddProjectCategoryVO;
import com.pxxy.entity.vo.QueryProjectCategoryVO;
import com.pxxy.entity.vo.UpdateProjectCategoryVO;
import com.pxxy.exceptions.ForbiddenException;
import com.pxxy.mapper.ProjectCategoryMapper;
import com.pxxy.service.BaseService;
import com.pxxy.service.ProjectCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean addProjectCategory(AddProjectCategoryVO addProjectCategoryVO) {
        ProjectCategory projectCategory = new ProjectCategory();
        BeanUtil.copyProperties(addProjectCategoryVO,projectCategory);
        return save(projectCategory);
    }

    @Override
    @Transactional
    public boolean updateProjectCategory(UpdateProjectCategoryVO updateProjectCategoryVO) throws ForbiddenException {
        ProjectCategory projectCategory = query().eq("prc_id", updateProjectCategoryVO.getPrcId()).one();
        if (projectCategory == null) {
            throw new ForbiddenException(ILLEGAL_OPERATE);
        }

        projectCategory.setPrcName(updateProjectCategoryVO.getPrcName())
                .setPrcPeriod(updateProjectCategoryVO.getPrcPeriod());

        return updateById(projectCategory);
    }

    @Override
    public List<QueryProjectCategoryVO> getAllProjectCategory() {
        return query().orderByDesc("prc_id")
                .list().stream().map(projectCategory -> {
            QueryProjectCategoryVO queryProjectCategoryVO = new QueryProjectCategoryVO();
            BeanUtil.copyProperties(projectCategory, queryProjectCategoryVO);
            return queryProjectCategoryVO;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteProjectCategory(Integer prcId) throws ForbiddenException {
        ProjectCategory projectCategory = query().eq("prc_id", prcId).one();
        if (projectCategory == null){
            throw new ForbiddenException(ILLEGAL_OPERATE);
        }
        return removeById(prcId);
    }

    @Override
    public List<ProjectCategory> all() {
        return query().list();
    }
}
