package com.qi.demo.service.impl;

import com.qi.demo.DTO.ImageFileDTO;
import com.qi.demo.DTO.ModelFileDTO;
import com.qi.demo.dataobject.ImageFile;
import com.qi.demo.dataobject.ImageProject;
import com.qi.demo.dataobject.ModelFile;
import com.qi.demo.dataobject.ModelProject;
import com.qi.demo.enums.ResultEnum;
import com.qi.demo.exception.NrException;
import com.qi.demo.repository.ModelFileRepository;
import com.qi.demo.repository.ModelProjectRepository;
import com.qi.demo.repository.ProjectRepository;
import com.qi.demo.service.ModelFileService;
import com.qi.demo.utils.KeyUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
@Service
public class ModelFileServiceImpl implements ModelFileService {
    @Autowired
    ModelFileRepository modelFileRepository;

    @Autowired
    ModelProjectRepository modelProjectRepository;

    @Override
    public ModelFileDTO saveModel(String documentsId, String projectId) {
        ModelFile checkModelResult = modelFileRepository.findByDocumentsId(documentsId);
        if (checkModelResult != null){
            ModelProject checkModelProjectResult = modelProjectRepository.findByProjectIdAndModelId(projectId, checkModelResult.getModelId());
            if (checkModelProjectResult != null){
                //复制到DTO
                ModelFileDTO modelFileDTO = new ModelFileDTO();
                BeanUtils.copyProperties(checkModelResult, modelFileDTO);
                BeanUtils.copyProperties(checkModelProjectResult, modelFileDTO);
                return modelFileDTO;
            }
            else {
                //写ModelProject表
                ModelProject modelProject = new ModelProject();
                modelProject.setModelId(checkModelResult.getModelId());
                modelProject.setProjectId(projectId);
                ModelProject saveResult1 = modelProjectRepository.save(modelProject);

                //复制到DTO
                ModelFileDTO modelFileDTO = new ModelFileDTO();
                BeanUtils.copyProperties(saveResult1, modelFileDTO);
                BeanUtils.copyProperties(checkModelProjectResult, modelFileDTO);
                return modelFileDTO;
            }
        }
        else {
            //新建一个Model对象
            ModelFile modelFile = new ModelFile();
            modelFile.setDocumentsId(documentsId);
            //生成主键
            String modelId = KeyUtil.genUniqueKey();
            modelFile.setModelId(modelId);

            //新建一个ModelProject对象
            ModelProject modelProject = new ModelProject();
            modelProject.setProjectId(projectId);
            modelProject.setModelId(modelId);

            //调用DAO层写入数据库
            modelFileRepository.save(modelFile);
            modelProjectRepository.save(modelProject);

            //复制到DTO
            ModelFileDTO modelFileDTO = new ModelFileDTO();
            BeanUtils.copyProperties(modelFile, modelFileDTO);
            BeanUtils.copyProperties(modelProject, modelFileDTO);
            return modelFileDTO;
        }
    }

    @Override
    public String findModel(String modelId) {
        //调用DAO查找Model
        ModelFile modelFile = modelFileRepository.findByModelId(modelId);
        if (modelFile == null) {
            return null;
        }
        //返回目录地址
        return modelFile.getDocumentsId();
    }

    @Override
    @Transactional
    public Boolean delete(String modelId) throws Exception {
        //查找这个Model，没有就抛异常
        ModelFile modelFile = modelFileRepository.findByModelId(modelId);

        //删除本地文件
        try {
            File file = new File(modelFile.getDocumentsId());
            if (file.exists()) {
                file.delete();
            }

            //判断是否删除成功
            if (file.exists()) {
                throw new NrException(ResultEnum.DELETE_LOCAL_FILE_ERROR);
            }
            //删除数据库记录
            if (modelProjectRepository.deleteByModelId(modelFile.getModelId()) == 0) {
                throw new NrException(ResultEnum.DELETE_DATABASE_RECORD_ERROR);
            }
        }

        catch (NrException nrException){
            return false;
        }
        return true;
    }

    @Override
    public List<ModelFileDTO> findAllModel(String projectId) {
        List<ModelProject> list = modelProjectRepository.findByProjectId(projectId);

        //新建一个List存储ImageDTO
        List<ModelFileDTO> modelFileDTOList = new ArrayList<>();
        //复制
        for (ModelProject modelProject: list){
            ModelFileDTO modelFileDTO = new ModelFileDTO();
            BeanUtils.copyProperties(modelProject,modelFileDTO);
            modelFileDTOList.add(modelFileDTO);
        }
        //返回这个List
        return modelFileDTOList;
    }

    @Override
    public ModelFileDTO findModelByDocId(String documentId) {
        ModelFile modelFile = modelFileRepository.findByDocumentsId(documentId);
        if (modelFile == null) {
            return null;
        }
        ModelFileDTO modelFileDTO = new ModelFileDTO();
        BeanUtils.copyProperties(modelFile, modelFileDTO);
        return modelFileDTO;
    }
}
