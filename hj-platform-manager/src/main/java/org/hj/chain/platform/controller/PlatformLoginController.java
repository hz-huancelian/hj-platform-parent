package org.hj.chain.platform.controller;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.service.IPlatformUserService;
import org.hj.chain.platform.tdo.LoginUserTdo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 平台用户登录
 * @Iteration : 1.0
 * @Date : 2021/5/8  8:10 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/08    create
 */
@RestController
@RequestMapping("platform/user")
public class PlatformLoginController {

    @Autowired
    private IPlatformUserService platformUserService;

    /**
     * TODO 平台用户登录
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 8:15 下午
     */
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public Result<Object> doLogin(@RequestBody LoginUserTdo tdo) {

        return platformUserService.doLogin(tdo.getUsername(), tdo.getPassword());
    }

}