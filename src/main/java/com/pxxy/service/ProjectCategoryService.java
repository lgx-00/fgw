package com.pxxy.service;

import com.pxxy.pojo.ProjectCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddProjectCategoryVO;
import com.pxxy.vo.UpdateProjectCategoryVO;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Validated
public interface ProjectCategoryService extends IService<ProjectCategory> {

    ResultResponse addProjectCategory(@Validated AddProjectCategoryVO addProjectCategoryVO);

    ResultResponse updateProjectCategory(@Validated UpdateProjectCategoryVO updateProjectCategoryVO);

    ResultResponse getAllProjectCategory();

    ResultResponse deleteProjectCategory(Integer prcId);
}
