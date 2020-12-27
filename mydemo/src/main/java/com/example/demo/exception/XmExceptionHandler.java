package com.example.demo.exception;

import com.example.demo.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 16:45
 * @Description:
 */
@ControllerAdvice
@ResponseBody
public class XmExceptionHandler {

    @Autowired
    private ResultMessage resultMessage;

    @ExceptionHandler(XmException.class)
    public ResultMessage handleException(XmException e){
        ExceptionEnum em = e.getExceptionEnum();
        resultMessage.fail(em.getCode() + "", em.getMsg());
        return resultMessage;
    }
}
