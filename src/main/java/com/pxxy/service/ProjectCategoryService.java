package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.entity.pojo.ProjectCategory;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddProjectCategoryVO;
import com.pxxy.entity.vo.QueryProjectCategoryVO;
import com.pxxy.entity.vo.UpdateProjectCategoryVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
public interface ProjectCategoryService extends IService<ProjectCategory> {

    ResultResponse<?> addProjectCategory(AddProjectCategoryVO addProjectCategoryVO);

    ResultResponse<?> updateProjectCategory(UpdateProjectCategoryVO updateProjectCategoryVO);

    ResultResponse<List<QueryProjectCategoryVO>> getAllProjectCategory();

    ResultResponse<?> deleteProjectCategory(Integer prcId);

    List<ProjectCategory> all();
}
