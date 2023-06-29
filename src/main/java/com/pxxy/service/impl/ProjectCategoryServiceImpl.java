package com.pxxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pxxy.pojo.ProjectCategory;
import com.pxxy.mapper.ProjectCategoryMapper;
import com.pxxy.service.ProjectCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.ProjectCategoryVO;
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
public class ProjectCategoryServiceImpl extends ServiceImpl<ProjectCategoryMapper, ProjectCategory> implements ProjectCategoryService {

    @Resource
    private ProjectCategoryMapper projectCategoryMapper;

    @Override
    public ResultResponse addProjectCategory(ProjectCategoryVO projectCategoryVO) {
        ProjectCategory projectCategory = new ProjectCategory()
                .setPrcName(projectCategoryVO.getPrcName())
                .setPrcPeriod(projectCategoryVO.getPrcPeriod());
        projectCategoryMapper.insert(projectCategory);
        return ResultResponse.ok(projectCategory);
    }

    @Override
    public ResultResponse updateProjectCategory(Integer prcId, ProjectCategoryVO projectCategoryVO) {
        ProjectCategory projectCategory = query().eq("prc_id", prcId).ne("prc_status", DELETED_STATUS).one();
        if (projectCategory == null){
            return ResultResponse.fail("非法操作！");
        }
        projectCategory.setPrcName(projectCategoryVO.getPrcName()).setPrcPeriod(projectCategoryVO.getPrcPeriod());
        updateById(projectCategory);
        return ResultResponse.ok(projectCategory);
    }

    @Override
    public ResultResponse selectProjectCategory() {
        QueryWrapper<ProjectCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("prc_status", DELETED_STATUS);
        List<ProjectCategory> projectCategoryList = projectCategoryMapper.selectList(queryWrapper);
        return ResultResponse.ok(projectCategoryList);
    }

    @Override
    public ResultResponse deleteProjectCategory(Integer prcId) {
        ProjectCategory projectCategory = query().eq("prc_id", prcId).ne("prc_status", DELETED_STATUS).one();
        if (projectCategory == null){
            return ResultResponse.fail("非法操作！");
        }
        projectCategory.setPrcStatus(DELETED_STATUS);
        updateById(projectCategory);
        return ResultResponse.ok();
    }
}
