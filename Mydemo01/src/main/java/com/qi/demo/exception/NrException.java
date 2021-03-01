package com.qi.demo.exception;


import com.qi.demo.enums.ResultEnum;

public class NrException extends RuntimeException{

    private Integer code;

    public NrException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public NrException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
