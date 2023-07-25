package com.pxxy.service.impl;

import com.pxxy.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
class DepartmentServiceImplTest {

    @Resource
    DepartmentService depService;

    @Test
    void getAllDepartment() {
        depService.getAllDepartment().getData().forEach(System.out::println);
    }

}