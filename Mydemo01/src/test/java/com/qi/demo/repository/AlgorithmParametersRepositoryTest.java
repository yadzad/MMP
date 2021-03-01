package com.qi.demo.repository;

import com.qi.demo.dataobject.AlgorithmParameters;
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
class AlgorithmParametersRepositoryTest {
    @Autowired
    AlgorithmParametersRepository algorithmParametersRepository;

//    @Test
//    void save(){
//        AlgorithmParameters algorithmParameters = new AlgorithmParameters();
//        algorithmParameters.setAlgorithmId(KeyUtil.genUniqueKey());
//        algorithmParameters.setProjectId("1603878885167648890");
//        algorithmParameters.setAlgorithmParameter1(128);
//        Assert.assertNotNull(algorithmParametersRepository.save(algorithmParameters));
//    }
    @Test
    void findByProjectId() {
        Assert.assertNotNull(algorithmParametersRepository.findByProjectId("1603878885167648890"));
    }

    @Test
    void findByAId() {
      Assert.assertNotNull(algorithmParametersRepository.findByAlgorithmIntegerId(1));
    }

    @Test
    void findByAlgorithmId() {
        Assert.assertNotNull(algorithmParametersRepository.findByAlgorithmId("1604046361345589628"));
    }
}