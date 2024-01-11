package com.pxxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pxxy.entity.pojo.Department;
import com.pxxy.utils.ResultResponse;
import com.pxxy.entity.vo.AddDepartmentVO;
import com.pxxy.entity.vo.QueryDepartmentVO;
import com.pxxy.entity.vo.UpdateDepartmentVO;

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

    boolean addDepartment(AddDepartmentVO addDepartmentVO);

    boolean updateDepartment(UpdateDepartmentVO updateDepartmentVO);

    List<QueryDepartmentVO> getAllDepartment();

    boolean deleteDepartment(Integer depId);

    List<Department> all();
}
