package com.qi.demo.repository;


import com.qi.demo.dataobject.ModelFile;
import com.qi.demo.dataobject.ModelProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModelProjectRepository extends JpaRepository<ModelProject, String> {
    List<ModelProject> findByProjectId(String modelId);
    ModelProject findByProjectIdAndModelId(String projectID, String ModelId);
    Integer deleteByModelId(String modelId);

}
