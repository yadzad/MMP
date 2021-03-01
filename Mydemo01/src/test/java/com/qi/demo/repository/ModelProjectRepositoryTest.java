package com.qi.demo.repository;

import com.qi.demo.dataobject.ModelProject;
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
class ModelProjectRepositoryTest {
    @Autowired
    ModelProjectRepository modelProjectRepository;

    @Test
    void save(){
        ModelProject modelProject = new ModelProject();
        modelProject.setModelId("1604839661556397532");
        modelProject.setProjectId("1603878885167648890");
        Assert.assertNotNull(modelProjectRepository.save(modelProject));
    }

    @Test
    void findByProjectId() {
        Assert.assertNotNull(modelProjectRepository.findByProjectId("1603878885167648890"));
    }
}