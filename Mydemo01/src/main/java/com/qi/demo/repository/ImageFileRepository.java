package com.qi.demo.repository;


import com.qi.demo.dataobject.AlgorithmParameters;
import com.qi.demo.dataobject.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageFileRepository extends JpaRepository<ImageFile, String> {

    //根据目录查找文件
    ImageFile findByDocumentsId(String documentsId);

    //根据文件id查找文件
    ImageFile findByPictureId(String pictureId);

    //根据文件id目录删除文件
    Integer deleteByDocumentsId(String documentsId);

}
