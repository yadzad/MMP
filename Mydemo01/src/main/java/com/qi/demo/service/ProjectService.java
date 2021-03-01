package com.qi.demo.service;

import com.qi.demo.DTO.ProjectDTO;

import java.util.List;

public interface ProjectService {
    ProjectDTO create(String name) throws Exception;

    ProjectDTO findOneById(String id);

    ProjectDTO findOneByName(String name);

    ProjectDTO updateProjectUpdateTime(String projectId);

    List<ProjectDTO> findAllPorjects();

    Boolean deleteProject(String id);

    
}
