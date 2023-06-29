package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.pojo.Department;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddDepartmentVO;
import com.pxxy.vo.UpdateDepartmentVO;
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
public interface DepartmentService extends IService<Department> {

    ResultResponse addDepartment(@Validated AddDepartmentVO addDepartmentVO);

    ResultResponse updateDepartment(@Validated UpdateDepartmentVO updateDepartmentVO);

    ResultResponse getAllDepartment();

    ResultResponse deleteDepartment(Integer depId);
}
