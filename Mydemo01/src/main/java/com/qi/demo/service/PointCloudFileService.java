package com.qi.demo.service;


import com.qi.demo.DTO.PointCloudFileDTO;

import java.util.List;

public interface PointCloudFileService {
    PointCloudFileDTO savePointCloud(String documentsId, String projectId);
    String findPointCloud(String pointCloudId) throws Exception;
    Boolean delete(String pointCloudId);
    List<PointCloudFileDTO> findAllPoinCloud(String project);
    PointCloudFileDTO findPointCloudByDocId(String documentId);
}
