package com.qi.demo.repository;

import com.qi.demo.dataobject.ModelFile;
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
class ModelFileRepositoryTest {
    @Autowired
    ModelFileRepository modelFileRepository;

    @Test
    void save(){
        ModelFile modelFile = new ModelFile();
        modelFile.setModelId(KeyUtil.genUniqueKey());
        modelFile.setDocumentsId("XXX/XXX/xxxxx.ply");
        Assert.assertNotNull(modelFileRepository.save(modelFile));
    }
    @Test
    void findByModelId() {
        Assert.assertNotNull("1604839661556397532");
    }
}