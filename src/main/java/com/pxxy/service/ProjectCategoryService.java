package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.pojo.ProjectCategory;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddProjectCategoryVO;
import com.pxxy.vo.QueryProjectCategoryVO;
import com.pxxy.vo.UpdateProjectCategoryVO;
import org.springframework.validation.annotation.Validated;

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
}
