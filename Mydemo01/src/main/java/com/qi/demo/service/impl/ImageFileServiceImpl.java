package com.qi.demo.service.impl;

import com.qi.demo.DTO.ImageFileDTO;
import com.qi.demo.dataobject.ImageFile;
import com.qi.demo.dataobject.ImageProject;
import com.qi.demo.enums.ResultEnum;
import com.qi.demo.exception.NrException;
import com.qi.demo.repository.ImageFileRepository;
import com.qi.demo.repository.ImageProjectRepository;
import com.qi.demo.service.ImageFileService;
import com.qi.demo.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageFileServiceImpl implements ImageFileService {
    @Autowired
    ImageFileRepository imageFileRepository;

    @Autowired
    ImageProjectRepository imageProjectRepository;

    @Override
    public ImageFileDTO saveImage(String documentsId, String projectId) {
        ImageFile checkImageFIleResult = imageFileRepository.findByDocumentsId(documentsId);
        if (checkImageFIleResult != null){
            ImageProject checkImageProjectResult = imageProjectRepository.findImageProjectByProjectIdAndPictureId(projectId, checkImageFIleResult.getPictureId());
            if (checkImageProjectResult != null){
                ImageFileDTO imageFileDTO = new ImageFileDTO();
                BeanUtils.copyProperties(checkImageFIleResult, imageFileDTO);
                BeanUtils.copyProperties(checkImageProjectResult,imageFileDTO);
                return imageFileDTO;
            }
            else {
                //写ImageProject表
                ImageProject imageProject = new ImageProject();
                imageProject.setPictureId(checkImageFIleResult.getPictureId());
                imageProject.setProjectId(projectId);
                ImageProject saveResult1 = imageProjectRepository.save(imageProject);
                ImageFileDTO imageFileDTO = new ImageFileDTO();
                BeanUtils.copyProperties(checkImageFIleResult, imageFileDTO);
                BeanUtils.copyProperties(saveResult1,imageFileDTO);
                return imageFileDTO;
            }
        }
        else {
            //写ImageFile表
            ImageFile imageFile = new ImageFile();
            String pictureId = KeyUtil.genUniqueKey();
            imageFile.setPictureId(pictureId);
            imageFile.setDocumentsId(documentsId);
            ImageFile saveResult = imageFileRepository.save(imageFile);

            //写ImageProject表
            ImageProject imageProject = new ImageProject();
            imageProject.setPictureId(pictureId);
            imageProject.setProjectId(projectId);
            ImageProject saveResult1 = imageProjectRepository.save(imageProject);

            ImageFileDTO imageFileDTO = new ImageFileDTO();
            BeanUtils.copyProperties(saveResult, imageFileDTO);
            BeanUtils.copyProperties(saveResult1, imageFileDTO);
            return imageFileDTO;
        }
    }

    @Override
    public String findImage(String pictureId) {
        ImageFile imageFile = imageFileRepository.findByPictureId(pictureId);
        if (imageFile == null) {
            return null;
        }
        String  documentsId = imageFile.getDocumentsId();
        return documentsId;
    }

    @Override
    public ImageFileDTO findImageByDocId(String documentId) {
        ImageFile imageFile = imageFileRepository.findByDocumentsId(documentId);
        if (imageFile == null)
            return null;
        ImageFileDTO imageFileDTO = new ImageFileDTO();
        BeanUtils.copyProperties(imageFile,imageFileDTO);
        return imageFileDTO;
    }

    @Override
    @Transactional
    public Boolean delete(String pictureId) throws Exception {
        try {
            //查找该图片的documentsId;
            ImageFile imageFile = imageFileRepository.findByPictureId(pictureId);
            if (imageFile == null) {
                throw new NrException(ResultEnum.FILE_NOT_FOUND);
            }

            //删除本地文件
            File file = new File(imageFile.getDocumentsId());
            if (file.exists()){
                file.delete();
            }

            //判断是否删除成功
            if (file.exists()){
                throw new NrException(ResultEnum.DELETE_LOCAL_FILE_ERROR);
            }

            //删除数据库中的记录(删除image_project表中记录即可剩余靠触发器解决)
            if(imageProjectRepository.deleteByPictureId(imageFile.getPictureId()) == 0){
                throw new NrException(ResultEnum.DELETE_DATABASE_RECORD_ERROR);
            }
        }
        catch (NrException nrException){
            return false;
        }
        return true;
    }

    @Override
    public List<ImageFileDTO> findAllImage(String projectId) {
        List<ImageProject> list = imageProjectRepository.findByProjectIdOrderByPictureAffiliationId(projectId);

        //新建一个List存储ImageDTO
        List<ImageFileDTO> imageFileDTOList = new ArrayList<>();
        //复制
        for (ImageProject imageProject: list){
            ImageFileDTO imageFileDTO = new ImageFileDTO();
            BeanUtils.copyProperties(imageProject,imageFileDTO);
            imageFileDTOList.add(imageFileDTO);
        }
        //返回这个List

        return imageFileDTOList;
    }
}
