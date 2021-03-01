package com.qi.demo.service.impl;

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
class ImageFileServiceImplTest {
    @Autowired ImageFileServiceImpl imageFileService;

    @Test
    void saveImage() {
        imageFileService.saveImage("E:\\NERCMS相关\\软件后端\\Mydemo01\\picture\\test.txt","1603878885167648890");
    }

    @Test
    void openImage() {
        Assert.assertEquals(imageFileService.findImage("1606025160339688237"),"/picture/test.txt");
    }

    @Test
    void delete() throws Exception {
        Assert.assertEquals(true,imageFileService.delete("1606030524238101200"));
    }
    @Test
    void findList(){
        Assert.assertNotNull(imageFileService.findAllImage("1603878885167648890"));
    }
    @Test
    void findImage(){
        Assert.assertNull(imageFileService.findImageByDocId("E:\\NERCMS相关\\软件后端\\Mydemo01\\picture\\test05.txt"));
    }
}