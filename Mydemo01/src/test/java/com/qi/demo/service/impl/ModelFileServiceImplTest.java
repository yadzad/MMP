package com.qi.demo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.ws.soap.MTOM;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
class ModelFileServiceImplTest {
    @Autowired
    ModelFileServiceImpl modelFileService;

    @Test
    void saveModel() {
        Assert.assertNotNull(modelFileService.saveModel("E:\\NERCMS相关\\软件后端\\Mydemo01\\model\\testFile.txt","1605444784485735205"));
    }

    @Test
    void openModel() {
        Assert.assertNotNull(modelFileService.findModel("1604839661556397532"));
    }

    @Test
    void delete() throws Exception {
        Assert.assertEquals(true,modelFileService.delete("1606894680855373063"));
    }

    @Test
    void findAllModel() {
        Assert.assertNotNull(modelFileService.findAllModel("1605444784485735205"));
    }
}