package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pxxy.mapper.DepartmentMapper;
import com.pxxy.pojo.DepPrc;
import com.pxxy.pojo.Department;
import com.pxxy.service.DepPrcService;
import com.pxxy.service.DepartmentService;
import com.pxxy.service.ProjectCategoryService;
import com.pxxy.utils.ResultResponse;
import com.pxxy.vo.AddDepartmentVO;
import com.pxxy.vo.QueryDepartmentVO;
import com.pxxy.vo.UpdateDepartmentVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.pxxy.constant.ResponseMessage.ILLEGAL_OPERATE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hs
 * @since 2023-06-14
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Resource
    private DepPrcService depPrcService;

    @Resource
    private ProjectCategoryService projectCategoryService;

    @Override
    @Transactional
    public ResultResponse<?> addDepartment(AddDepartmentVO addDepartmentVO) {
        Department department = new Department();
        department.setDepName(addDepartmentVO.getDepName());
        save(department);
        Integer depId = department.getDepId();

        //分类Id集合
        List<Integer> prcIdList = addDepartmentVO.getProjectCategory();
        List<DepPrc> depPrcList = prcIdList.stream().map(prcId ->
                new DepPrc().setDepId(depId).setPrcId(prcId)).collect(Collectors.toList());

        depPrcService.saveBatch(depPrcList);
        return ResultResponse.ok();
    }

    @Override
    @Transactional
    public ResultResponse<?> updateDepartment(UpdateDepartmentVO updateDepartmentVO) {
        Integer depId = updateDepartmentVO.getDepId();
        Department department = query().eq("dep_id", depId).one();
        if (department == null){
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }
        department.setDepName(updateDepartmentVO.getDepName());
        updateById(department);

        LambdaQueryWrapper<DepPrc> depPrcLambdaQueryWrapper = new LambdaQueryWrapper<>();
        depPrcLambdaQueryWrapper.eq(DepPrc::getDepId,depId);

        //先删除
        depPrcService.remove(depPrcLambdaQueryWrapper);

        //再添加
        //分类Id集合
        List<Integer> prcIdList = updateDepartmentVO.getProjectCategory();
        List<DepPrc> depPrcList = prcIdList.stream().map(prcId ->
                new DepPrc().setDepId(depId).setPrcId(prcId)).collect(Collectors.toList());
        depPrcService.saveBatch(depPrcList);

        return ResultResponse.ok();
    }

    @Override
    public ResultResponse<List<QueryDepartmentVO>> getAllDepartment() {
        List<Department> departmentList = query().list();
        List<QueryDepartmentVO> queryDepartmentVOS = departmentList.stream().map(department -> {
            QueryDepartmentVO queryDepartmentVO = new QueryDepartmentVO();
            BeanUtil.copyProperties(department,queryDepartmentVO);
            List<DepPrc> depPrcList = depPrcService.query().eq("dep_id", department.getDepId()).list();
            List<String> projectCategoryName = depPrcList.stream().map(depPrc ->
                    projectCategoryService.query().eq("prc_id", depPrc.getPrcId())
                            .one().getPrcName()).collect(Collectors.toList());
            queryDepartmentVO.setProjectCategoryName(projectCategoryName);
            return queryDepartmentVO;
        }).collect(Collectors.toList());

        return ResultResponse.ok(queryDepartmentVOS);
    }

    @Override
    public ResultResponse<?> deleteDepartment(Integer depId) {
        Department department = query().eq("dep_id", depId).one();
        if (department == null){
            return ResultResponse.fail(ILLEGAL_OPERATE);
        }
        removeById(depId);
        return ResultResponse.ok();
    }
}
