package com.qi.demo.service.pointCloudHandle;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
class PointCloudAlgorithmTest {

    PointCloudAlgorithm pointCloudAlgorithm = new PointCloudAlgorithm();

    @Test
    void voxelGridFilterService() {
    }

    @Test
    void radiusFilterService() {
    }

    @Test
    void gaussStatisticalFilterService() {
    }

    @Test
    void conditionalFilterService() {

    }

    @Test
    void samplingRadiusFilterService() {
    }

    @Test
    void colorMappingService() {
    }

    @Test
    void colorMultiMappingService() {
    }

    @Test
    void textureMappingService() {
    }

    @Test
    void textureMultiMappingService() {
    }

    @Test
    void greedyProjectionService()throws Exception {
        String path = "1";
//        path = pointCloudAlgorithm.greedyProjectionService("home/nsy/necrms/软件后端/shader_services_test/data/livox/1_filter.pcd", (float) 0.5, 2, false, (float) 0.05, (float) 0.1, (float) 0.04, (float) 0.02);
        System.out.println(path);
    }

    @Test
    void testGreedyProjectionService() {
    }
}