package com.qi.demo.repository;


import com.qi.demo.dataobject.AlgorithmParameters;
import com.qi.demo.dataobject.ImageProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageProjectRepository extends JpaRepository<ImageProject, Integer> {

    List<ImageProject> findByProjectIdOrderByPictureAffiliationId(String projectId);
    ImageProject findImageProjectByProjectIdAndPictureId(String projectId, String pictureId);
    Integer deleteByPictureId(String pictureId);

}
