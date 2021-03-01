package com.qi.demo.service.impl;

import com.qi.demo.DTO.PointCloudFileDTO;
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

class PointCloudFileServiceImplTest {
    @Autowired
    PointCloudFileServiceImpl pointCloudFileService;

    @Test
    void savePointCloud() {
        Assert.assertNotNull(pointCloudFileService.savePointCloud("C:XXXX/XXXX/XXX/xxx.pcd","1605444696825487762"));
    }

    @Test
    void openPointCloud() throws Exception {
        Assert.assertNotNull(pointCloudFileService.findPointCloud("1606651080071866570"));
    }

    @Test
    void delete() {
        Assert.assertEquals(true,pointCloudFileService.delete("1606651080071866570"));
    }

    @Test
    void findAll(){
        Assert.assertNotNull(pointCloudFileService.findAllPoinCloud("1603878885167648890"));
    }
}