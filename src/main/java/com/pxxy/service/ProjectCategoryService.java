package com.pxxy.service;

import com.pxxy.pojo.ProjectCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddProjectCategoryVO;
import com.pxxy.vo.UpdateProjectCategoryVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
public interface ProjectCategoryService extends IService<ProjectCategory> {

    ResultResponse addProjectCategory(AddProjectCategoryVO addProjectCategoryVO);

    ResultResponse updateProjectCategory(UpdateProjectCategoryVO updateProjectCategoryVO);

    ResultResponse getAllProjectCategory();

    ResultResponse deleteProjectCategory(Integer prcId);
}
