package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.mapper.ProjectCategoryMapper;
import com.pxxy.pojo.ProjectCategory;
import com.pxxy.service.ProjectCategoryService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddProjectCategoryVO;
import com.pxxy.vo.QueryProjectCategoryVO;
import com.pxxy.vo.UpdateProjectCategoryVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.pxxy.constant.SystemConstant.DELETED_STATUS;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Service
public class ProjectCategoryServiceImpl extends ServiceImpl<ProjectCategoryMapper, ProjectCategory> implements ProjectCategoryService {

    @Override
    public ResultResponse addProjectCategory(AddProjectCategoryVO addProjectCategoryVO) {
        ProjectCategory projectCategory = new ProjectCategory();
        BeanUtil.copyProperties(addProjectCategoryVO,projectCategory);
        save(projectCategory);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse updateProjectCategory(UpdateProjectCategoryVO updateProjectCategoryVO) {
        ProjectCategory projectCategory = query().eq("prc_id", updateProjectCategoryVO.getPrcId()).one();
        if (projectCategory == null){
            return ResultResponse.fail("非法操作");
        }

        projectCategory.setPrcName(updateProjectCategoryVO.getPrcName())
                .setPrcPeriod(updateProjectCategoryVO.getPrcPeriod());

        updateById(projectCategory);
        return ResultResponse.ok();
    }

    @Override
    public ResultResponse getAllProjectCategory() {
        List<QueryProjectCategoryVO> queryProjectCategoryVOS = query().list().stream().map(projectCategory -> {
            QueryProjectCategoryVO queryProjectCategoryVO = new QueryProjectCategoryVO();
            BeanUtil.copyProperties(projectCategory, queryProjectCategoryVO);
            return queryProjectCategoryVO;
        }).collect(Collectors.toList());
        return ResultResponse.ok(queryProjectCategoryVOS);
    }

    @Override
    public ResultResponse deleteProjectCategory(Integer prcId) {
        ProjectCategory projectCategory = query().eq("prc_id", prcId).one();
        if (projectCategory == null){
            return ResultResponse.fail("非法操作");
        }
        removeById(prcId);
        return ResultResponse.ok();
    }
}
