package com.qi.demo.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
public class ImageProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pictureAffiliationId;

    private String projectId;

    private String pictureId;


}
