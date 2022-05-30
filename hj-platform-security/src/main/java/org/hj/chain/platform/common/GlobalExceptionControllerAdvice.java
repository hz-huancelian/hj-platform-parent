package org.hj.chain.platform.common;

import cn.dev33.satoken.exception.NotLoginException;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultCode;
import org.hj.chain.platform.ResultUtil;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project : nmerp-master
 * @Description : TODO 全局异常统一处理
 * @Author : lijinku
 * @Iteration : 1.0
 * @Date : 2020/5/19  4:28 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2020/05/19    create
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionControllerAdvice {

    //参数校验全局异常处理
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        //获取错误对象
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
//        ObjectError objectError = allErrors.get(0);
        String data = allErrors.stream().map(item -> item.getDefaultMessage()).collect(Collectors.joining(";"));
        //返回错误信息
        log.error("检验异常：异常信息->", data);
        return ResultUtil.validateError(data);
    }

    //自定义异常全局处理
    @ExceptionHandler(CustomException.class)
    public Result<String> CustomExceptionHandler(CustomException e) {
        log.error("业务异常：{}", e.getMessage());
        return ResultUtil.error(e.getMsg());
    }

    //自定义异常全局处理
    @ExceptionHandler(NotLoginException.class)
    public Result<String> NotLoginException(NotLoginException e) {
        e.printStackTrace();
        log.error("业务异常：{}", e.getMessage());
        return new Result<>(ResultCode.UNAUTHORIZED, e.getMessage());
    }

    //自定义异常全局处理
    @ExceptionHandler(Exception.class)
    public Result<String> SystemExceptionHandler(Exception e) {
        e.printStackTrace();
        log.error("数据异常，请联系管理员：", e);
        return new Result<>(ResultCode.FAILED, e.getMessage());
    }
}