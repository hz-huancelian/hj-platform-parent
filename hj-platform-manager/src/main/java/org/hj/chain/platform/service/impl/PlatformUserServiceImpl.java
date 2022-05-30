package org.hj.chain.platform.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hj.chain.platform.BPwdEncoderUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.mapper.PlatformUserMapper;
import org.hj.chain.platform.model.PlatformUser;
import org.hj.chain.platform.service.IPlatformUserService;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 平台用户服务
 * @Iteration : 1.0
 * @Date : 2021/5/8  9:07 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/08    create
 */
@Service
public class PlatformUserServiceImpl implements IPlatformUserService {
    @Autowired
    private PlatformUserMapper platformUserMapper;

    @Override
    public Result<Object> doLogin(String username, String password) {
        PlatformUser dbUser = platformUserMapper.selectOne(Wrappers.<PlatformUser>lambdaQuery()
                .select(PlatformUser::getId,
                        PlatformUser::getEmpName,
                        PlatformUser::getPhone,
                        PlatformUser::getUserType,
                        PlatformUser::getPassword)
                .eq(PlatformUser::getUsername, username));

        if (dbUser != null) {
            if (!BPwdEncoderUtil.matches(password, dbUser.getPassword().replace(BusiConstants.ENCRYPT_PREFIX, ""))) {
                return ResultUtil.validateError("用户名或密码不正确！");
            }

            LoginOutputVo loginOutputVo = new LoginOutputVo();
            loginOutputVo.setEmpName(dbUser.getEmpName())
                    .setUsername(username)
                    .setPhone(dbUser.getPhone())
                    .setUserType(dbUser.getUserType());
            StpUtil.setLoginId(dbUser.getId(), new SaLoginModel()
                    .setDevice("PC")
                    .setIsLastingCookie(true));
            // 获取token
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            // 获取session
            SaSession session = StpUtil.getSession();
            // 设置用户信息
            session.setAttribute(BusiConstants.SESSION_USER_KEY, loginOutputVo);
            return ResultUtil.data(tokenInfo.getTokenValue());
        } else {
            return ResultUtil.busiError("用户名在库中不存在！");
        }
    }
}