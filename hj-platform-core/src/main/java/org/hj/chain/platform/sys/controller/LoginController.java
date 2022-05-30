package org.hj.chain.platform.sys.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.BPwdEncoderUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.mapper.OrganMapper;
import org.hj.chain.platform.model.Organ;
import org.hj.chain.platform.service.ISysMenuService;
import org.hj.chain.platform.service.ISysPostService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.sys.tdo.LoginByPhoneTdo;
import org.hj.chain.platform.sys.tdo.LoginByUsernameTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.RouterVo;
import org.hj.chain.platform.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 登录控制
 * @Iteration : 1.0
 * @Date : 2021/4/28  3:10 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/04/28    create
 */
@Slf4j
@RequestMapping("/sys/login")
@RestController
public class LoginController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private OrganMapper organMapper;
    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private ISysPostService sysPostService;

    /**
     * TODO 用户名登录接口
     *
     * @param tdo 登录表单实体
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/28 3:45 下午
     */
    @RequestMapping(value = "/doLoginByUsername", method = RequestMethod.POST)
    public Result<Object> doLoginByUsername(@RequestBody LoginByUsernameTdo tdo) {
        UserVo userVo = sysUserService.selectUserByLoginName(tdo.getUsername());
        if (userVo == null) {
            return ResultUtil.validateError("用户名在库中不存在！");
        }
        if(userVo.getStatus().equals("1")) {
            return ResultUtil.busiError("账号被停用！");
        }
        if (!BPwdEncoderUtil.matches(tdo.getPassword(), userVo.getPassword().replace(BusiConstants.ENCRYPT_PREFIX, ""))) {
            return ResultUtil.validateError("用户名或密码不正确！");
        }
        LoginOutputVo loginOutputVo = buildLoginOutputVoFromUserInfo(userVo);
        //用户关联的角色
        if (!userVo.getUserType().equals("0")) {
            List<String> roles = sysUserService.selectUserRoleGroup(userVo.getUserId());
            loginOutputVo.setRoles(roles);
            List<String> permsGroup = sysUserService.selectUserPermsGroup(userVo.getUserId());
            loginOutputVo.setResources(new HashSet<>(permsGroup));
        } else {
            List<String> roleVos = new ArrayList<>();
            roleVos.add("admin");
            loginOutputVo.setRoles(roleVos);
        }

        List<String> postCodes = sysPostService.selectUserPostGroup(userVo.getUserId());
        loginOutputVo.setPostCodes(postCodes);

        Organ dbOrgan = organMapper.selectOne(Wrappers.<Organ>lambdaQuery()
                .select(Organ::getOrganName)
                .eq(Organ::getOrganId, userVo.getOrganId()));
        if (dbOrgan != null) {
            loginOutputVo.setOrganName(dbOrgan.getOrganName());
        }
        // sa-token 登录
//        StpUtil.setLoginId(userVo.getId());
        StpUtil.setLoginId(userVo.getUserId(), new SaLoginModel()
                .setDevice("PC")
                .setIsLastingCookie(true));
        // 获取token
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 获取session
        SaSession session = StpUtil.getSession();
        // 设置用户信息
        session.setAttribute(BusiConstants.SESSION_USER_KEY, loginOutputVo);
        return ResultUtil.data(tokenInfo.getTokenValue());
    }

    /**
     * 根据用户信息构建登录信息
     *
     * @param userVo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/28 4:36 下午
     */
    private LoginOutputVo buildLoginOutputVoFromUserInfo(UserVo userVo) {
        LoginOutputVo loginOutputVo = new LoginOutputVo();
        loginOutputVo.setUsername(userVo.getUsername())
                .setPhone(userVo.getPhonenumber())
                .setEmpName(userVo.getEmpName())
                .setAvatarPath(userVo.getAvatarPath())
                .setOrganId(userVo.getOrganId())
                .setDeptId(userVo.getDeptId())
                .setUserStatus(userVo.getStatus())
                .setUserType(userVo.getUserType())
                .setUserStatus(userVo.getStatus())
                .setUserId(userVo.getUserId());
        return loginOutputVo;
    }

    /**
     * TODO 手机号登录接口：返回token
     *
     * @param tdo 手机登录表单实体
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/28 4:00 下午
     */
    @RequestMapping(value = "/doLoginByPhone", method = RequestMethod.POST)
    public Result<Object> doLoginByPhone(@RequestBody LoginByPhoneTdo tdo) {
        UserVo userVo = sysUserService.selectUserByPhoneNumber(tdo.getPhone());
        if (userVo == null) {
            return ResultUtil.validateError("手机号未注册！");
        }
        if(userVo.getStatus().equals("1")) {
            return ResultUtil.busiError("账号被停用！");
        }

        if (!BPwdEncoderUtil.matches(tdo.getPassword(), userVo.getPassword().replace(BusiConstants.ENCRYPT_PREFIX, ""))) {
            return ResultUtil.validateError("用户名或密码不正确！");
        }

        LoginOutputVo loginOutputVo = buildLoginOutputVoFromUserInfo(userVo);
        if (!userVo.getUserType().equals("0")) {
            List<String> roles = sysUserService.selectUserRoleGroup(userVo.getUserId());
            loginOutputVo.setRoles(roles);
            List<String> resources = sysUserService.selectUserPermsGroup(userVo.getUserId());
            loginOutputVo.setResources(new HashSet<>(resources));

        } else {
            List<String> roleVos = new ArrayList<>();
            roleVos.add("admin");
            loginOutputVo.setRoles(roleVos);
        }

        List<String> postCodes = sysPostService.selectUserPostGroup(userVo.getUserId());
        loginOutputVo.setPostCodes(postCodes);

        Organ dbOrgan = organMapper.selectOne(Wrappers.<Organ>lambdaQuery()
                .select(Organ::getOrganName)
                .eq(Organ::getOrganId, userVo.getOrganId()));
        if (dbOrgan != null) {
            loginOutputVo.setOrganName(dbOrgan.getOrganName());
        }
        // sa-token 登录
        StpUtil.setLoginId(userVo.getUserId(), new SaLoginModel()
                .setDevice("PC")
                .setIsLastingCookie(true));
        // 获取token
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 获取session
        SaSession session = StpUtil.getSession();
        // 设置用户信息
        session.setAttribute(BusiConstants.SESSION_USER_KEY, loginOutputVo);
        return ResultUtil.data(tokenInfo.getTokenValue());
    }


    /**
     * TODO 获取用户信息
     *
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/28 6:11 下午
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public Result<LoginOutputVo> getUserInfo() {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        return ResultUtil.data(loginOutputVo);
    }

    /**
     * TODO  注销
     *
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/28 6:07 下午
     */
    @RequestMapping(value = "/doLogout", method = RequestMethod.POST)
    public Result<Object> logout() {
        //判断是否登录
        if (StpUtil.isLogin()) {
//            StpUtil.logout();
            StpUtil.logoutByLoginId(StpUtil.getLoginId(), "PC");
        }
        return ResultUtil.success("注销成功！");
    }


    /**
     * TODO 构建前端路由
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 10:16 下午
     */
    @RequestMapping(value = "/getRouters", method = RequestMethod.GET)
    public Result<List<RouterVo>> getRouters() {

        List<RouterVo> routerVos = sysMenuService.getRouters();
        return ResultUtil.data(routerVos);
    }

}