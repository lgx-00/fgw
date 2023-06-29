package com.pxxy.service;

import com.pxxy.pojo.Stage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.StageVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xrw
 * @date 2023-06-14
 */
public interface StageService extends IService<Stage> {

    /**
     * @Description: 添加工程进展
     * @Author: xrw 
     * @Date: 2023/6/25 21:40
     * @Param: [stageVO]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse addStage(StageVO stageVO);

    /**
     * @Description: 修改工程进展
     * @Author: xrw
     * @Date: 2023/6/25 21:41
     * @Param: [stageId, stageVO]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse updateStage(Integer stageId, StageVO stageVO);

    /**
     * @Description: 查询所有工程进展
     * @Author: xrw
     * @Date: 2023/6/25 21:42
     * @Param: []
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse selectStage();

    /**
     * @Description: 删除工程进展
     * @Author: xrw
     * @Date: 2023/6/25 21:43
     * @Param: [stageId]
     * @return: com.pxxy.utils.ResultResponse
     **/
    ResultResponse deleteStage(Integer stageId);

}
