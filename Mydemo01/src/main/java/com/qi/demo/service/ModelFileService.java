package com.qi.demo.service;

import com.qi.demo.DTO.ImageFileDTO;
import com.qi.demo.DTO.ModelFileDTO;
import org.springframework.ui.Model;

import java.util.List;

public interface ModelFileService {
    ModelFileDTO saveModel(String documentsId, String projectId);
    String findModel(String modelId);
    Boolean delete(String modelId) throws Exception;
    List<ModelFileDTO> findAllModel(String projectId);
    ModelFileDTO findModelByDocId(String documentId);
}
