package com.qi.demo.repository;

import com.qi.demo.DTO.ImageFileDTO;
import com.qi.demo.dataobject.PcdFile;
import com.qi.demo.utils.KeyUtil;
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
class PcdFileRepositoryTest {
    @Autowired
    PcdFileRepository pcdFileRepository;

    @Test
    void save(){
        PcdFile pcdFile = new PcdFile();
        pcdFile.setDocumentsId("xxxx/xxx.xxxx");
        pcdFile.setPointCloudId(KeyUtil.genUniqueKey());
        Assert.assertNotNull(pcdFileRepository.save(pcdFile));
    }
    @Test
    void findByPointCloudId() {
        Assert.assertNotNull(pcdFileRepository.findByPointCloudId("1605331624751640252"));
    }
}