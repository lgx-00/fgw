package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.pojo.Department;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddDepartmentVO;
import com.pxxy.vo.QueryDepartmentVO;
import com.pxxy.vo.UpdateDepartmentVO;
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
public interface DepartmentService extends IService<Department> {

    ResultResponse<?> addDepartment(AddDepartmentVO addDepartmentVO);

    ResultResponse<?> updateDepartment(UpdateDepartmentVO updateDepartmentVO);

    ResultResponse<List<QueryDepartmentVO>> getAllDepartment();

    ResultResponse<?> deleteDepartment(Integer depId);
}
