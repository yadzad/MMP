package com.qi.demo.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qi.demo.utils.serializer.Date2LongSerializer;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
public class ProjectDTO {
    private String projectId;

    private String projectName;

//    @JsonSerialize(using = Date2LongSerializer.class)
    private Date projectCreationTime;

//    @JsonSerialize(using = Date2LongSerializer.class)
    private Date projectModificationTime;
}
