package com.example.system.hander;

import com.example.system.constant.MessageConstant;
import com.example.system.exception.BaseException;
import com.example.system.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
//异常处理类的注解，将返回结果写入响应体
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理SQL异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex)
    {
        String message = ex.getMessage();
        if(message.contains("Duplicate entry"))
        {
            String[] s = message.split(" ");
            String name = s[2];
            String msg = name+ MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }
        else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}

