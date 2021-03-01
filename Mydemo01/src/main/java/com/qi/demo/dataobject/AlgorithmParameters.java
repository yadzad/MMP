package com.qi.demo.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
public class AlgorithmParameters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer algorithmIntegerId;

    private String projectId;

    private String algorithmId;

    private Integer algorithmParameter1;

    private Integer algorithmParameter2;

    private Integer algorithmParameter3;

    private Integer algorithmParameter4;

    private Integer algorithmParameter5;

    private Integer algorithmParameter6;

    private Date recordTime;

}
