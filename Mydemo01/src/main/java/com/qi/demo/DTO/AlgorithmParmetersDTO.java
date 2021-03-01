package com.qi.demo.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qi.demo.utils.serializer.Date2LongSerializer;
import lombok.Data;

import java.util.Date;
@Data
public class AlgorithmParmetersDTO {

    private String projectId;

    private String algorithmId;

    private Integer algorithmParameter1;

    private Integer algorithmParameter2;

    private Integer algorithmParameter3;

    private Integer algorithmParameter4;

    private Integer algorithmParameter5;

    private Integer algorithmParameter6;

    @JsonSerialize(using = Date2LongSerializer.class)
    private Date recordTime;

}
