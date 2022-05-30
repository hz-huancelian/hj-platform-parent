package org.hj.chain.platform.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hj.chain.platform.*;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.mapper.OrganMapper;
import org.hj.chain.platform.mapper.SysUserAppRoleMapper;
import org.hj.chain.platform.model.Organ;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.model.SysUserAppRole;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.MobileLoginByUsernameTdo;
import org.hj.chain.platform.tdo.MobileLoginTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project : hj-platform-parent
 * @Description : TODO 小程序登录控制
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-04-27  8:59 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh     2021/04/27    create
 */
@RestController
@RequestMapping("/login")
public class MobileLoginController {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private OrganMapper organMapper;
    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.secret}")
    private String secret;
    @Value("${weixin.access-token-base-url}")
    private String wxAccessTokenBaseUrl;
    @Autowired
    private SysUserAppRoleMapper sysUserAppRoleMapper;

    /**
     * TODO 小程序授权
     *
     * @param code 临时登录凭证code
     * @Author chh
     * @Date 2021-04-30 10:56
     * @Iteration 1.0
     */
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public Result<Object> loginAppkey(@RequestParam String code) {
        if (StrUtil.isBlank(code)) {
            return ResultUtil.error(ResultCode.VALIDATE_FAILED.getCode(), "code不能为空");
        }
        RestTemplate restTemplate = new RestTemplate();
        String url = wxAccessTokenBaseUrl + "appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        ResponseEntity forEntity1 = restTemplate.getForEntity(url, String.class);
        JSONObject parse = JSONObject.parseObject(forEntity1.getBody().toString());
        System.out.println("微信接口返回：" + parse);
        // parse解析出errcode说明code已经过期
        if (StrUtil.isNotBlank(parse.getString("errcode"))) {
            return ResultUtil.error(ResultCode.FAILED.getCode(), "临时code无效！");
        }
        return ResultUtil.data(parse);
    }

    /**
     * TODO 快捷登录
     *
     * @param dto
     * @Author chh
     * @Date 2021-05-05 1:46
     * @Iteration 1.0
     */
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public Result<Object> doLogin(@RequestBody @Valid MobileLoginTdo dto) throws IOException {
        String openId = dto.getOpenId();
        String sessionKey = dto.getSessionKey();
        String encryptedData = dto.getEncryptedData();
        String iv = dto.getIv();
        JSONObject resp = WechatDecryptDataUtil.getPhoneNumber(sessionKey, encryptedData, iv);
        String phoneNumber = resp.getString("phoneNumber");
        UserVo userVo = sysUserService.selectUserByPhoneNumber(phoneNumber);
        if (userVo == null) {
            return ResultUtil.validateError("手机号未注册！");
        }
        if(userVo.getStatus().equals("1")) {
            return ResultUtil.busiError("账号被停用！");
        }
        if(userVo.getIsAppLogin().equals("0")) {
            return ResultUtil.busiError("未被授权登录小程序！");
        }
        LoginOutputVo loginOutputVo = buildLoginOutputVoFromUserInfo(userVo);
        List<SysUserAppRole> appRoles = sysUserAppRoleMapper.selectList(Wrappers.<SysUserAppRole>lambdaQuery().select(SysUserAppRole::getAppRole).eq(SysUserAppRole::getUserId, userVo.getUserId()));
        if (appRoles != null && !appRoles.isEmpty()) {
            List<String> appRoleList = appRoles.stream().map(item -> item.getAppRole()).collect(Collectors.toList());
            loginOutputVo.setAppRoles(appRoleList);
        }
        List<String> resources = sysUserService.selectUserPermsGroup(userVo.getUserId());
        if(resources != null && !resources.isEmpty()) {
            loginOutputVo.setResources(new HashSet<>(resources));
        }
        Organ dbOrgan = organMapper.selectOne(Wrappers.<Organ>lambdaQuery()
                .select(Organ::getOrganName)
                .eq(Organ::getOrganId, userVo.getOrganId()));
        if (dbOrgan != null) {
            loginOutputVo.setOrganName(dbOrgan.getOrganName());
        }
        sysUserService.update(Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getOpenId, openId)
                .set(SysUser::getSessionKey, sessionKey)
                .eq(SysUser::getPhonenumber, phoneNumber));
        StpUtil.setLoginId(userVo.getUserId(), new SaLoginModel()
                .setDevice("APP")
                .setIsLastingCookie(true)
                .setTimeout(-1));

        // 获取token
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 获取session
        SaSession session = StpUtil.getSession();
        // 设置用户信息
        session.setAttribute(BusiConstants.SESSION_USER_KEY, loginOutputVo);
        return ResultUtil.data(tokenInfo.getTokenValue());
    }

    /**
     * TODO 加载用户信息
     *
     * @Author chh
     * @Date 2021-05-05 2:05
     * @Iteration 1.0
     */
    @RequestMapping(value = "/loadUserInfo", method = RequestMethod.GET)
    public Result<LoginOutputVo> loadUserInfo() {
        // 获取session
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        return ResultUtil.data(loginOutputVo);
    }

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
     * TODO 退出登录
     *
     * @Author chh
     * @Date 2021-04-30 13:05
     * @Iteration 1.0
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result<Object> logout() {
        String userId = (String) StpUtil.getLoginId();
        sysUserService.update(Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getOpenId, null)
                .set(SysUser::getSessionKey, null)
                .eq(SysUser::getUserId, userId));
        StpUtil.logoutByLoginId(userId, "APP");
        return ResultUtil.success("登出成功！");
    }

    /**
     * TODO 静默登录
     *
     * @param code 临时登录凭证
     * @Author chh
     * @Date 2021-05-06 19:36
     * @Iteration 1.0
     */
    @RequestMapping(value = "/doSlientLogin", method = RequestMethod.POST)
    public Result<Object> slientLogin(@RequestBody String code) {
        if (StrUtil.isBlank(code)) {
            return ResultUtil.busiError("临时登录凭证code不能为空！");
        }
        JSONObject json = JSON.parseObject(code);
        RestTemplate restTemplate = new RestTemplate();
        String url = wxAccessTokenBaseUrl + "appid=" + appid + "&secret=" + secret + "&js_code=" + json.getString("code") + "&grant_type=authorization_code";
        ResponseEntity forEntity1 = restTemplate.getForEntity(url, String.class);
        JSONObject parse = JSONObject.parseObject(forEntity1.getBody().toString());
        System.out.println("微信接口返回：" + parse);
        // parse解析出errcode说明code已经过期
        if (StrUtil.isNotBlank(parse.getString("errcode"))) {
            return ResultUtil.error(ResultCode.FAILED.getCode(), "临时登录凭证code无效！");
        }
        String openId = parse.getString("openid");
        SysUser user = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getOpenId, openId));
        JSONObject resp = new JSONObject();
        if (user != null && user.getStatus().equals("0") && user.getIsAppLogin().equals("1")) {
            //先登出
            StpUtil.logoutByLoginId(user.getUserId());
            //登入
            StpUtil.setLoginId(user.getUserId(), new SaLoginModel()
                    .setDevice("APP")
                    .setIsLastingCookie(true)
                    .setTimeout(-1));
            resp.put("Authorization", StpUtil.getTokenInfo().getTokenValue());
            resp.put("sign", "0");
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            LoginOutputVo loginOutputVo = buildLoginOutputVoFromUserInfo(userVo);

            List<SysUserAppRole> appRoles = sysUserAppRoleMapper.selectList(Wrappers.<SysUserAppRole>lambdaQuery().select(SysUserAppRole::getAppRole).eq(SysUserAppRole::getUserId, userVo.getUserId()));
            if (appRoles != null && !appRoles.isEmpty()) {
                List<String> appRoleList = appRoles.stream().map(item -> item.getAppRole()).collect(Collectors.toList());
                loginOutputVo.setAppRoles(appRoleList);
            }
            List<String> resources = sysUserService.selectUserPermsGroup(userVo.getUserId());
            if(resources != null && !resources.isEmpty()) {
                loginOutputVo.setResources(new HashSet<>(resources));
            }
            Organ dbOrgan = organMapper.selectOne(Wrappers.<Organ>lambdaQuery()
                    .select(Organ::getOrganName)
                    .eq(Organ::getOrganId, userVo.getOrganId()));
            if (dbOrgan != null) {
                loginOutputVo.setOrganName(dbOrgan.getOrganName());
            }
            // 获取session
            SaSession session = StpUtil.getSession();
            // 设置用户信息
            session.setAttribute(BusiConstants.SESSION_USER_KEY, loginOutputVo);
            return ResultUtil.data(resp);
        } else {
            resp.put("Authorization", null);
            resp.put("sign", "1");
            return ResultUtil.data(resp);
        }
    }

    /**
     * TODO 小程序账号&密码登录
     *
     * @param tdo
     * @Author chh
     * @Date 2021-05-10 9:50
     * @Iteration 1.0
     */
    @RequestMapping(value = "/doLoginByUsername", method = RequestMethod.POST)
    public Result<Object> doLoginByUsername(@Validated @RequestBody MobileLoginByUsernameTdo tdo) {
        UserVo userVo = sysUserService.selectUserByLoginName(tdo.getUsername());
        if (userVo == null) {
            return ResultUtil.validateError("用户名在库中不存在！");
        }
        if(userVo.getStatus().equals("1")) {
            return ResultUtil.busiError("账号被停用！");
        }
        if(userVo.getIsAppLogin().equals("0")) {
            return ResultUtil.busiError("未被授权登录小程序！");
        }
        if (!BPwdEncoderUtil.matches(tdo.getPassword(), userVo.getPassword().replace(BusiConstants.ENCRYPT_PREFIX, ""))) {
            return ResultUtil.validateError("用户名或密码不正确！");
        }

        LoginOutputVo loginOutputVo = buildLoginOutputVoFromUserInfo(userVo);

        List<SysUserAppRole> appRoles = sysUserAppRoleMapper.selectList(Wrappers.<SysUserAppRole>lambdaQuery().select(SysUserAppRole::getAppRole).eq(SysUserAppRole::getUserId, userVo.getUserId()));
        if (appRoles != null && !appRoles.isEmpty()) {
            List<String> appRoleList = appRoles.stream().map(item -> item.getAppRole()).collect(Collectors.toList());
            loginOutputVo.setAppRoles(appRoleList);
        }
        List<String> resources = sysUserService.selectUserPermsGroup(userVo.getUserId());
        if(resources != null && !resources.isEmpty()) {
            loginOutputVo.setResources(new HashSet<>(resources));
        }

        Organ dbOrgan = organMapper.selectOne(Wrappers.<Organ>lambdaQuery()
                .select(Organ::getOrganName)
                .eq(Organ::getOrganId, userVo.getOrganId()));
        if (dbOrgan != null) {
            loginOutputVo.setOrganName(dbOrgan.getOrganName());
        }
        StpUtil.setLoginId(userVo.getUserId(), new SaLoginModel()
                .setDevice("APP")
                .setIsLastingCookie(true));
        // 获取token
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 获取session
        SaSession session = StpUtil.getSession();
        // 设置用户信息
        session.setAttribute(BusiConstants.SESSION_USER_KEY, loginOutputVo);
        return ResultUtil.data(tokenInfo.getTokenValue());
    }
}
