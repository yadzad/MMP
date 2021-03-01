package com.qi.demo.repository;

import com.qi.demo.dataobject.Project;
import com.qi.demo.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
class ProjectRepositoryTest {
    @Autowired
    ProjectRepository projectRepository;
    String Key = KeyUtil.genUniqueKey();
    @Test
    void save(){
        Project project = new Project();
        project.setProjectName("测试工程05");
        project.setProjectId(Key);
        Project result = projectRepository.save(project);
        Assert.assertNotNull(result);
    }
    @Test
    void findByProjectId() {
        Project result = projectRepository.findByProjectId("1603878885167648890");
        Assert.assertNotNull(result);
    }

    @Test
    void findByPorjectName(){
        Assert.assertNotNull(projectRepository.findByProjectName("Test1"));
    }

    @Test
    void findByOrderByProjectModificationTime(){
        Assert.assertNotNull(projectRepository.findByOrderByProjectModificationTime());
    }
    @Test
    @Transactional
    @Rollback(true)
    void deleteByProjectId(){
        Integer i  = 1;
        Assert.assertEquals(i,projectRepository.deleteProjectByProjectId("1604819821726945659"));
    }

}