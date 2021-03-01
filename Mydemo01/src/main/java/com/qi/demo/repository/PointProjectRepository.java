package com.qi.demo.repository;


import com.qi.demo.dataobject.PointProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointProjectRepository extends JpaRepository<PointProject, Integer> {
    List<PointProject> findByProjectId(String projectId);

    PointProject findByProjectIdAndAndPointCloudId(String projectId, String pointCloudID);

}
