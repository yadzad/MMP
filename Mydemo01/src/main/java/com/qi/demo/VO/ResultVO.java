package com.qi.demo.VO;

import lombok.Data;

/**
 * http请求返回的最外层对象
 * Created by qi
 * 2020/4/2
 */
@Data
public class ResultVO<T> {

    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 记录处理时间*/
    private long time;

    /** 具体内容. */
    private T data;
}
