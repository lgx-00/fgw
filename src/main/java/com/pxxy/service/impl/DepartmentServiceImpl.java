package com.pxxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.stream.SimpleCollector;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pxxy.advice.annotations.Cached;
import com.pxxy.entity.pojo.DepPrc;
import com.pxxy.entity.pojo.Department;
import com.pxxy.entity.pojo.ProjectCategory;
import com.pxxy.entity.vo.AddDepartmentVO;
import com.pxxy.entity.vo.QueryDepartmentVO;
import com.pxxy.entity.vo.UpdateDepartmentVO;
import com.pxxy.exceptions.ForbiddenException;
import com.pxxy.mapper.DepartmentMapper;
import com.pxxy.service.BaseService;
import com.pxxy.service.DepPrcService;
import com.pxxy.service.DepartmentService;
import com.pxxy.service.ProjectCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.hutool.core.stream.CollectorUtil.CH_ID;
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
@Cached(parent = ProjectCategoryServiceImpl.class)
public class DepartmentServiceImpl extends BaseService<DepartmentMapper, Department> implements DepartmentService {

    @Resource
    private DepPrcService depPrcService;

    @Resource
    private ProjectCategoryService prcService;

    @Override
    @Transactional
    public boolean addDepartment(AddDepartmentVO addDepartmentVO) {
        Department department = new Department();
        department.setDepName(addDepartmentVO.getDepName());
        save(department);
        Integer depId = department.getDepId();

        //分类Id集合
        List<Integer> prcIdList = addDepartmentVO.getProjectCategory();
        List<DepPrc> depPrcList = prcIdList.stream().map(prcId ->
                new DepPrc().setDepId(depId).setPrcId(prcId)).collect(Collectors.toList());

        depPrcService.saveBatch(depPrcList);
        return true;
    }

    @Override
    @Transactional
    public boolean updateDepartment(UpdateDepartmentVO updateDepartmentVO) throws ForbiddenException {
        Integer depId = updateDepartmentVO.getDepId();
        Department department = query().eq("dep_id", depId).one();
        if (department == null) {
            throw new ForbiddenException(ILLEGAL_OPERATE);
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

        return true;
    }

    @Override
    public List<QueryDepartmentVO> getAllDepartment() {

        // get all departments and the middle entities for the following operation
        List<Department> departmentList = query().orderByDesc("dep_id").list();
        List<Integer> depIds = departmentList.stream().map(Department::getDepId).collect(Collectors.toList());
        List<DepPrc> depPrcList = depPrcService.query().in("dep_id", depIds).list();

        // get project category mapper ( id -> name )
        List<Integer> prcIds = depPrcList.stream().map(DepPrc::getPrcId).distinct().collect(Collectors.toList());
        Map<Integer, String> prcMapper = prcService.query().in("prc_id", prcIds).list().stream()
                .collect(Collectors.toMap(ProjectCategory::getPrcId, ProjectCategory::getPrcName));

        // get department id and project category mapper ( depId -> list of prcName )
        Map<Integer, List<String>> depIdMapPrcNames = depPrcList.stream()
                .collect(Collectors.groupingBy(DepPrc::getDepId, new SimpleCollector<>(ArrayList::new,
                        (list, dp) -> list.add(prcMapper.get(dp.getPrcId())), null, CH_ID)));

        return departmentList.stream().map(d -> {
            QueryDepartmentVO queryDepartmentVO = new QueryDepartmentVO();
            BeanUtil.copyProperties(d, queryDepartmentVO);
            queryDepartmentVO.setProjectCategoryName(depIdMapPrcNames.get(d.getDepId()));
            return queryDepartmentVO;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean deleteDepartment(Integer depId) throws ForbiddenException {
        Department department = query().eq("dep_id", depId).one();
        if (department == null) {
            throw new ForbiddenException(ILLEGAL_OPERATE);
        }
        removeById(depId);
        return true;
    }

    @Override
    public List<Department> all() {
        return query().list();
    }
}
