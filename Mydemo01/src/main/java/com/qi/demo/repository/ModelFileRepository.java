package com.qi.demo.repository;


import com.qi.demo.dataobject.ImageProject;
import com.qi.demo.dataobject.ModelFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModelFileRepository extends JpaRepository<ModelFile, String> {
    //根据文件id查找模型文件
    ModelFile findByModelId(String modelId);

    //根据目录位置查找模型
    ModelFile findByDocumentsId(String documentsId);

    //根据目录位置删除模型文件
    Integer deleteByDocumentsId(String documentsId);
}
