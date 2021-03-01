package com.qi.demo.repository;

import com.qi.demo.dataobject.ImageFile;
import com.qi.demo.utils.KeyUtil;
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
class ImageFileRepositoryTest {
    @Autowired
    ImageFileRepository imageFileRepository;

    @Test
    void save(){
        String dId = KeyUtil.genUniqueKey();
        String pId = KeyUtil.genUniqueKey();
        ImageFile imageFile = new ImageFile();
        imageFile.setDocumentsId(dId);
        imageFile.setPictureId(pId);
        Assert.assertNotNull(imageFileRepository.save(imageFile));
    }

    @Test
    void findByDocumentsId() {
        Assert.assertNotNull(imageFileRepository.findByDocumentsId("1604047051062406593"));
    }

    @Test
    void findByPictureId() {
        Assert.assertNotNull(imageFileRepository.findByPictureId("1604047051062533199"));
    }
}