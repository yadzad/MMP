package com.qi.demo.repository;

import com.qi.demo.dataobject.ImageProject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
class ImageProjectRepositoryTest {
    @Autowired
    ImageProjectRepository imageProjectRepository;

    @Test
    void save(){
        ImageProject imageProject = new ImageProject();
        imageProject.setProjectId("1603878885167648890");
        imageProject.setPictureId("1604047051062533199");
        Assert.assertNotNull(imageProjectRepository.save(imageProject));
    }

    @Test
    void findByProjectIdOrderByPictureAffiliationId(){
        Assert.assertNotNull(imageProjectRepository.findByProjectIdOrderByPictureAffiliationId("1603878885167648890"));
    }
}