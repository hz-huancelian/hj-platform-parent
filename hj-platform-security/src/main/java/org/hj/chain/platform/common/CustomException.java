package org.hj.chain.platform.common;

import lombok.Data;

/**
 * @Project : nmerp-master
 * @Description : TODO 自定义异常
 * @Author : lijinku
 * @Iteration : 1.0
 * @Date : 2020/5/19  4:23 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2020/05/19    create
 */
@Data
public class CustomException extends RuntimeException {
    //错误码
    private int code;

    //错误信息
    private String msg;

    public CustomException() {
        this(10001, "接口异常！");
    }

    public CustomException(String msg) {
        this(10001, msg);
    }

    public CustomException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}