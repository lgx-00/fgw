package com.pxxy.service;

import com.pxxy.pojo.ProjectCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.ProjectCategoryVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xrw
 * @since 2023-06-14
 */
public interface ProjectCategoryService extends IService<ProjectCategory> {

    /**
     * @Description: 添加项目类型
     * @Author: xrw
     * @Date: 2023/6/24 21:36
     * @Param: [projectCategoryVO]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse addProjectCategory(ProjectCategoryVO projectCategoryVO);

    /**
     * @Description: 修改项目类型
     * @Author: xrw
     * @Date: 2023/6/24 21:35
     * @Param: [prcId, projectCategoryVO]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse updateProjectCategory(Integer prcId, ProjectCategoryVO projectCategoryVO);

    /**
     * @Description: 查询所有项目类型
     * @Author: xrw
     * @Date: 2023/6/24 21:35
     * @Param: []
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse selectProjectCategory();

    /**
     * @Description: 删除项目类型
     * @Author: xrw 
     * @Date: 2023/6/24 21:35
     * @Param: [prcId]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse deleteProjectCategory(Integer prcId);

}
