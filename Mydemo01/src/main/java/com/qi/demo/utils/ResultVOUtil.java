package com.qi.demo.utils;


import com.qi.demo.VO.ResultVO;
import com.qi.demo.enums.ResultEnum;

public class ResultVOUtil {
    
    public static ResultVO successWithRunTIme(Object object, long runTime){
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setTime(runTime);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }

    public static ResultVO error(ResultEnum projectNameExisted) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(projectNameExisted.getCode());
        resultVO.setMsg(projectNameExisted.getMessage());
        return resultVO;
    }
}
