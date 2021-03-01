package com.qi.demo.enums;

import lombok.Getter;


@Getter
public enum ResultEnum {

    SUCCESS(0, "成功"),

    PARAM_ERROR(1, "参数不正确"),

    FILE_NOT_FOUND(2,"未查询到该文件"),

    DELETE_LOCAL_FILE_ERROR(3,"删除本地文件失败"),

    DELETE_DATABASE_RECORD_ERROR(4,"删除数据库记录失败"),

    PROJECT_NAME_EXISTED(5, "该工程名已经存在"),

    PROJECT_NAME_NOT_EXISTED(6,"未查询到该工程名"),

    PROJECT_ID_NOT_EXISTED(7,"未查询到该工程ID"),

    IMAGE_NOT_FOUND(8,"未找到该图像"),

    IMAGE_DELETE_FAILED(9,"删除图像失败"),

    IMAGE_UPLOAD_FAILED(10,"图像上传失败"),

    FILE_NOT_EXIST_IN_SERVICE(11,"未在服务器上找到该文件"),

    UPLOAD_PATH_ILLEGAL(12, "上传路径非法"),

    FILE_NAME_NOT_MATCH_WITH_PATH(13,"文件名与documentId中的文件名不一致"),

    POINT_CLOUD_FILE_NOT_FOUND(14,"点云文件未找到"),

    MODEL_FILE_NOT_FOUND(15, "模型文件未找到")

    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
