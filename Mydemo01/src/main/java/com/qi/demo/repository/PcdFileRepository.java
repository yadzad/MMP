package com.qi.demo.repository;


import com.qi.demo.dataobject.PcdFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PcdFileRepository extends JpaRepository<PcdFile, String> {
    PcdFile findByPointCloudId(String pointCloudId);

    PcdFile findByDocumentsId(String documentsId);

    Integer deleteByPointCloudId(String pointCloudId);
}
