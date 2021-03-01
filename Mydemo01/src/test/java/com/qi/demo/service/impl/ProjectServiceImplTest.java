package com.qi.demo.service.impl;

import com.qi.demo.DTO.ProjectDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
class ProjectServiceImplTest {
    @Autowired
    ProjectServiceImpl projectService;

    @Test
    void create() throws Exception {
        ProjectDTO projectDTO = projectService.create("Test04");
        Assert.assertNotNull(projectDTO);
    }

    @Test
    void findOneById() {
        ProjectDTO projectDTO = projectService.findOneById("1605444696825487762");
        Assert.assertNotNull(projectDTO);
    }

    @Test
    void findOneByName() {
        Assert.assertNotNull(projectService.findOneByName("Test03"));
    }

    @Test
    void updateProjectUpdateTime() {
        Assert.assertNotNull(projectService.updateProjectUpdateTime("1605444696825487762"));
    }

    @Test
    void findAllPorjects() {
        Assert.assertNotNull(projectService.findAllPorjects());
    }

//    @Test
//    void deleteProject() {
//        Assert.assertEquals(true, projectService.deleteProject("1605444251891780605"));
//    }
}