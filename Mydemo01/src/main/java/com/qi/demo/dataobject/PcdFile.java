package com.qi.demo.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
@Data
@DynamicUpdate
@DynamicInsert
public  class PcdFile {
    @Id
    private String pointCloudId;

    private String documentsId;
}