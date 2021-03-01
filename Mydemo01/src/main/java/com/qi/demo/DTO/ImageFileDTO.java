package com.qi.demo.DTO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
public class ImageFileDTO {
    private String pictureId;

    private String documentsId;

    private Integer pictureAffiliationId;

    private String projectId;

}
