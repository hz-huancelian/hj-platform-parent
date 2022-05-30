package org.hj.chain.platform.service;

import org.hj.chain.platform.Result;

public interface IPlatformUserService {


    /**
     * TODO 登录
     *
     * @param username
     * @param password
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 9:05 下午
     */
    Result<Object> doLogin(String username, String password);
}
