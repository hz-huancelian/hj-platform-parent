package org.hj.chain.platform;


import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 前后端交互数据标准
 * @Param:
 * @return:
 * @Author: lijinku
 * @Iteration : 1.0
 * @Date: 2019/7/25 10:55 PM
 */
@Data
public class Result<T> implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 失败消息
     */
    private String message;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 结果对象
     */
    private T result;


    public Result() {
    }

    public Result(ResultCode resultCode, T data) {
        this.success = ResultCode.SUCCESS.getCode() == resultCode.getCode();
        this.message = resultCode.getMessage();
        this.code = resultCode.getCode();
        this.result = data;
    }

    public Result(T data) {
        this(ResultCode.SUCCESS, data);
    }
}
