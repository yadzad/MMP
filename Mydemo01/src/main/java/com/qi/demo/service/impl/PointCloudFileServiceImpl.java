package com.qi.demo.service.impl;

import com.qi.demo.DTO.ImageFileDTO;
import com.qi.demo.DTO.ModelFileDTO;
import com.qi.demo.DTO.PointCloudFileDTO;
import com.qi.demo.dataobject.ImageProject;
import com.qi.demo.dataobject.ModelProject;
import com.qi.demo.dataobject.PcdFile;
import com.qi.demo.dataobject.PointProject;
import com.qi.demo.enums.ResultEnum;
import com.qi.demo.exception.NrException;
import com.qi.demo.repository.PcdFileRepository;
import com.qi.demo.repository.PointProjectRepository;
import com.qi.demo.service.PointCloudFileService;
import com.qi.demo.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PointCloudFileServiceImpl implements PointCloudFileService {
    @Autowired
    PcdFileRepository pcdFileRepository;

    @Autowired
    PointProjectRepository pointProjectRepository;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * function: 创建、更新点云
     * input: 点云存储path, project Id
     * output: 点云文件DTO
     * */
    @Override
    public PointCloudFileDTO savePointCloud(String documentsId, String projectId) {
        /**
         * 更新策略：先更新缓存、后更新数据库
         */

        //查询缓存是否存在该path
        ValueOperations<String, PcdFile> operations = redisTemplate.opsForValue();
        ValueOperations<String, PointProject> operations1 = redisTemplate.opsForValue();
        String pcdKey = "PcdFile_documentsId_" + documentsId;
        boolean hasKey = redisTemplate.hasKey(pcdKey); //判断缓存中是否有该KEY
        if (hasKey) {
            //判断缓存中是否存在docId-Project记录
            String pointCloudID = operations.get(pcdKey).getPointCloudId();
            String pointProjectKey = "PointProject_pointCloudId_" + pointCloudID;
            boolean hasKey1 = redisTemplate.hasKey(pointProjectKey);
            if (hasKey1) {
                //如果均存在则代表不用更改数据库
                PointCloudFileDTO pointCloudFileDTO = new PointCloudFileDTO();
                pointCloudFileDTO.setPointCloudId(pointCloudID);
                pointCloudFileDTO.setPointCloudAffiliationId(operations1.get(pointProjectKey).getPointCloudAffiliationId());
                pointCloudFileDTO.setProjectId(projectId);
                pointCloudFileDTO.setDocumentsId(documentsId);
                return pointCloudFileDTO;
            } else {
                //如果缓存中不存在docId-Project记录则在数据库中查找是否存在该记录
                PointProject pointProject = pointProjectRepository.findByProjectIdAndAndPointCloudId(projectId, pointCloudID);
                if (pointProject != null) {
                    //如果数据库中存在该记录更新缓存
                    operations1.set(pointProjectKey, pointProject, 3, TimeUnit.HOURS);    //过期时间3小时
                    PointCloudFileDTO pointCloudFileDTO = new PointCloudFileDTO();
                    BeanUtils.copyProperties(pointProject, pointCloudFileDTO);
                    pointCloudFileDTO.setDocumentsId(documentsId);
                    return pointCloudFileDTO;
                } else {
                    //如果数据库中也不存在则更新数据库后更新缓存
                    //写PointProject Table
                    PointProject pointProject1 = new PointProject();
                    pointProject1.setPointCloudId(pointCloudID);
                    pointProject1.setProjectId(projectId);
                    pointProjectRepository.save(pointProject1);
                    //更新缓存
                    operations1.set(pointProjectKey, pointProject, 3, TimeUnit.HOURS);    //过期时间3小时
                    //返回DTO
                    PointCloudFileDTO pointCloudFileDTO = new PointCloudFileDTO();
                    BeanUtils.copyProperties(pointProject1, pointCloudFileDTO);
                    pointCloudFileDTO.setDocumentsId(documentsId);
                    return pointCloudFileDTO;
                }
            }
        } else {
            //缓存中不存在该doc记录
            PointProject returnPointProject;
            PcdFile returnPcdFile;

            //在pcdFile表中查找
            PcdFile checkPointResult = pcdFileRepository.findByDocumentsId(documentsId);

            //存在，在pointProject表中查找
            if(checkPointResult != null){
                PointProject checkPointProjectResult = pointProjectRepository.findByProjectIdAndAndPointCloudId(projectId, checkPointResult.getPointCloudId());
                //如果路径存在切关联，则为更新点云操作则不用更改数据库。
                //存在 do nothing
                if (checkPointProjectResult != null){
                    returnPointProject = checkPointProjectResult;
                    returnPcdFile = checkPointResult;
                }
                //不存在，更新pointProject表
                else{
                    //写PointProject Table
                    PointProject pointProject = new PointProject();
                    pointProject.setPointCloudId(checkPointResult.getPointCloudId());
                    pointProject.setProjectId(projectId);
                    returnPointProject = pointProjectRepository.save(pointProject);
                    returnPcdFile = checkPointResult;
                }
            }
            //不存在，更新pcdFile表、pointProject表
            else {
                PcdFile pcdFile = new PcdFile();
                String pointId = KeyUtil.genUniqueKey();
                pcdFile.setPointCloudId(pointId);
                pcdFile.setDocumentsId(documentsId);
                returnPcdFile = pcdFileRepository.save(pcdFile);

                PointProject pointProject = new PointProject();
                pointProject.setPointCloudId(pointId);
                pointProject.setProjectId(projectId);
                returnPointProject = pointProjectRepository.save(pointProject);

            }
            //更新缓存，返回DTO
            operations.set(pcdKey, returnPcdFile, 3, TimeUnit.HOURS);
            String pointProjectKey = "PointProject_pointCloudId_" + returnPointProject.getPointCloudId();
            String pcdPCIdKeyPCId = "PcdFile_pointCloudId_" + returnPointProject.getPointCloudId();
            operations.set(pcdPCIdKeyPCId,returnPcdFile, 3, TimeUnit.HOURS);

            operations1.set(pointProjectKey, returnPointProject,3,TimeUnit.HOURS);

            PointCloudFileDTO pointCloudFileDTO = new PointCloudFileDTO();
            BeanUtils.copyProperties(returnPcdFile,pointCloudFileDTO);
            BeanUtils.copyProperties(returnPointProject,pointCloudFileDTO);
            return pointCloudFileDTO;
        }


    }

    /***
     * function: 根据点云ID查找点云
     * @param pointCloudId:点云ID
     * @return 点云的服务器存储路径
     */
    @Override
    public String findPointCloud(String pointCloudId){
        //查询缓存是否存在该点云
        ValueOperations<String, PcdFile> operations = redisTemplate.opsForValue();
        String pcdKey = "PcdFile_pointCloudId_" + pointCloudId;
        if (redisTemplate.hasKey(pcdKey)) {
            return operations.get(pcdKey).getDocumentsId();

        }
        //调用持久层查询点云文件
        PcdFile pcdFile = pcdFileRepository.findByPointCloudId(pointCloudId);
            if (pcdFile != null){
                return pcdFile.getDocumentsId();
            }
            else{
                return null;
            }

    }

    /***
     * function:根据点云Id删除点云
     * @param pointCloudId
     * @return
     */
    @Override
    @Transactional
    public Boolean delete(String pointCloudId) {
        try {
            //查找点云的documentsId;
            String pcdKey = "PcdFile_pointCloudId_" + pointCloudId;
            String pcdDocKey = null;
            String pointProjectKey = "PointProject_pointCloudId_"+ pointCloudId;
            ValueOperations<String, PcdFile> operations = redisTemplate.opsForValue();
            boolean cacheIsExisted = redisTemplate.hasKey(pcdKey);
            String documentsId;
            //查找缓存
            if (!cacheIsExisted) {
                //缓存没有，查数据库，删本地文件，删数据库
                PcdFile pcdFile = pcdFileRepository.findByPointCloudId(pointCloudId);
                if (pcdFile == null) {
                    throw new NrException(ResultEnum.FILE_NOT_FOUND);
                }
                documentsId = pcdFile.getDocumentsId();
            } else {
                //缓存有，删本地文件，删数据库记录，删缓存
                documentsId = operations.get(pcdKey).getDocumentsId();
                pcdDocKey = "PcdFile_documentsId_" + documentsId;
            }

            //删除本地文件
            File file = new File(documentsId);
            if (file.exists()){
                file.delete();
            }

            //判断是否删除成功
            if (file.exists()){
                throw new NrException(ResultEnum.DELETE_LOCAL_FILE_ERROR);
            }

            //删除数据库中的记录
            if(pcdFileRepository.deleteByPointCloudId(pointCloudId) == 0){
                throw new NrException(ResultEnum.DELETE_DATABASE_RECORD_ERROR);
            }
            if (cacheIsExisted){
                redisTemplate.delete(pcdKey);
                redisTemplate.delete(pcdDocKey);
                redisTemplate.delete(pointProjectKey);
            }
        }
        catch (NrException nrException){
            return false;
        }
        return true;
    }

    /***
     * function: 查找一个工程下所有的点云文件
     * @param projectId：项目ID
     * @return 点云文件LIST
     */
    @Override
    public List<PointCloudFileDTO> findAllPoinCloud(String projectId) {
        List<PointProject> pointProjects = pointProjectRepository.findByProjectId(projectId);

        //新建一个List存储PointDTO
        List<PointCloudFileDTO> pointCloudFileDTOS = new ArrayList<>();
        //复制
        for (PointProject pointProject: pointProjects){
            PointCloudFileDTO pointCloudFileDTO = new PointCloudFileDTO();
            BeanUtils.copyProperties(pointProject,pointCloudFileDTO);
            pointCloudFileDTOS.add(pointCloudFileDTO);
        }
        //返回这个List
        return pointCloudFileDTOS;
    }

    /**
     * funciton: 通过路径查找点云
     * @param documentsId 点云存储路径
     * @return 点云文件
     */
    @Override
    public PointCloudFileDTO findPointCloudByDocId(String documentsId) {
        //查询缓存是否存在该点云
        ValueOperations<String, PcdFile> operations = redisTemplate.opsForValue();
        String pcdKey = "PcdFile_documentsId_" + documentsId;
        if (redisTemplate.hasKey(pcdKey)) {
            PointCloudFileDTO pointCloudFileDTO = new PointCloudFileDTO();
            PcdFile pcdFile = operations.get(pcdKey);
            BeanUtils.copyProperties(pcdFile, pointCloudFileDTO);
            return pointCloudFileDTO;
        }

        PcdFile pcdFile = pcdFileRepository.findByDocumentsId(documentsId);
        if (pcdFile == null) {
            return null;
        }
        PointCloudFileDTO pointCloudFileDTO = new PointCloudFileDTO();
        BeanUtils.copyProperties(pcdFile, pointCloudFileDTO);
        return pointCloudFileDTO;
    }


}
