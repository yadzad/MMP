package com.qi.demo.service;

import com.qi.demo.DTO.ImageFileDTO;

import java.util.List;

public interface ImageFileService {
    ImageFileDTO saveImage(String documentsId, String projectId);
    String findImage(String pictureId);
    ImageFileDTO findImageByDocId(String documentId);
    Boolean delete(String pictureId) throws Exception;
    List<ImageFileDTO> findAllImage(String projectId);

}
