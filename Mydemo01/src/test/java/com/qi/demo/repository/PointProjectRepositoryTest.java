package com.qi.demo.repository;

import com.qi.demo.dataobject.PointProject;
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
class PointProjectRepositoryTest {
    @Autowired
    PointProjectRepository pointProjectRepository;

    @Test
    void save(){
        PointProject pointProject = new PointProject();
        pointProject.setPointCloudId("1605331624751640252");
        pointProject.setProjectId("1603878885167648890");
        Assert.assertNotNull(pointProjectRepository.save(pointProject));
    }
    @Test
    void findByProjectId() {
        Assert.assertNotNull(pointProjectRepository.findByProjectId("1603878885167648890"));

    }
}