package com.qi.demo.repository;


import com.qi.demo.dataobject.PointProject;
import com.qi.demo.dataobject.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String> {
    Project findByProjectId(String projectId);
    Project findByProjectName(String projectName);
    List<Project> findByOrderByProjectModificationTime();
    Integer deleteProjectByProjectId(String projeectId);

}
