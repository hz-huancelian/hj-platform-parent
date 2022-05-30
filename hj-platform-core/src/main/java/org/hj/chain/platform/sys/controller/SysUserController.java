package org.hj.chain.platform.sys.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hj.chain.platform.FileUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.mapper.SysUserMapper;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.service.ISysPostService;
import org.hj.chain.platform.service.ISysRoleService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.sys.tdo.UserEditTdo;
import org.hj.chain.platform.tdo.SysUserTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserParamVo;
import org.hj.chain.platform.vo.UserQryVo;
import org.hj.chain.platform.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : hj-platform-parent
 * @Description : TODO 用户控制
 * @Author : lijinku
 * @Iteration : 1.0
 * @Date : 2021/4/27  8:59 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/04/27    create
 */
@RequestMapping("/sys/user")
@RestController
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysPostService sysPostService;

    @Value("${image.upload.avatar}")
    private String uploadAvatarPath;


    /**
     * TODO 新增用户
     *
     * @param tdo 用户新增信息
     * @return: org.hj.chain.platform.Result<java.lang.Object>
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/27 9:08 下午
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result<Object> save(@Validated @RequestBody SysUserTdo tdo) {
        tdo.setUserType("1");
        boolean res = sysUserService.registerUser(tdo, null);
        if (res) {
            return ResultUtil.success("注册成功！");
        }
        return ResultUtil.busiError("注册失败，邮件地址或手机号重复！");
    }


    /**
     * TODO 根据用户ID查询用户信息
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/28 2:26 下午
     */
    @RequestMapping(value = "/findUserByUserId/{userId}", method = RequestMethod.GET)
    public Result<Map<String, Object>> findUserByUserId(@PathVariable String userId) {
        UserVo userVo = sysUserService.findUserByUserId(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("user", userVo);
        List<Long> roleIds = sysRoleService.selectRoleIdsByUserId(userId);
        map.put("checkedRoleIds", roleIds);
        List<Long> postIds = sysPostService.selectPostIdsByUserId(userId);
        map.put("checkedPostIds", postIds);
        List<String> appRoles = sysUserService.selectAppRolesByUserId(userId);
        map.put("appRoles", appRoles);
        return ResultUtil.data(map);
    }


    /**
     * TODO 修改用户
     *
     * @param tdo
     * @return: org.hj.chain.platform.Result<java.lang.Object>
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/27 9:08 下午
     */
    @RequestMapping(value = "/modifyUserById", method = RequestMethod.POST)
    public Result<Object> modifyUserById(@Validated @RequestBody SysUserTdo tdo) {
        int count = sysUserService.updateUserByUserId(tdo);
        if (count > 0) {
            return ResultUtil.success("修改成功！");
        }
        return ResultUtil.busiError("修改失败，邮件地址或手机号重复！");
    }


    /**
     * TODO 用户列表查询
     *
     * @param qryVo  查询条件
     * @param pageVo 分页信息
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/28 2:24 下午
     */
    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    public Result<IPage<UserVo>> getByCondition(@ModelAttribute UserQryVo qryVo,
                                                @ModelAttribute PageVo pageVo) {
        IPage<UserVo> page = sysUserService.selectUserListByCondition(pageVo, qryVo);
        return ResultUtil.data(page);
    }


    /**
     * TODO 管理员重置其他密码
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/28 2:26 下午
     */
    @RequestMapping(value = "/resetOriganPassword/{userId}", method = RequestMethod.GET)
    public Result<Object> resetPassword(@PathVariable String userId) {

        if (StrUtil.isBlank(userId)) {
            return ResultUtil.validateError("用户ID不能为空！");
        }
        int count = sysUserService.resetOriganPassword(userId);
        if (count > 0) {
            return ResultUtil.success("重置成功！");
        }
        return ResultUtil.error("重置失败！");
    }


    /**
     * TODO 置用户无效
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/28 2:26 下午
     */
    @RequestMapping(value = "/invalidUserByUserId/{userId}", method = RequestMethod.GET)
    public Result<Object> invalidUserByUserId(@PathVariable String userId) {
        if (StrUtil.isBlank(userId)) {
            return ResultUtil.validateError("用户ID不能为空！");
        }
        int count = sysUserService.invalidUserByUserId(userId);
        if (count > 0) {
            return ResultUtil.success("设置成功！");
        }
        return ResultUtil.error("设置失败！");
    }


    /**
     * TODO 启用用户
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/11/4 3:06 下午
     */
    @RequestMapping(value = "/enableUserByUserId/{userId}", method = RequestMethod.GET)
    public Result<Object> enableUserByUserId(@PathVariable String userId) {
        if (StrUtil.isBlank(userId)) {
            return ResultUtil.validateError("用户ID不能为空！");
        }
        int count = sysUserService.enableUserByUserId(userId);
        if (count > 0) {
            return ResultUtil.success("设置成功！");
        }
        return ResultUtil.error("设置失败！");
    }

    /**
     * TODO 头像图片上传
     *
     * @param file
     * @Author chh
     * @Date 2021-05-10 23:04
     * @Iteration 1.0
     */
    @RequestMapping(value = "/uploadAvatarPath", method = RequestMethod.POST)
    public Result<Object> uploadAvatarPath(@RequestParam("file") MultipartFile file) {
        String fileName = FileUtil.fileUpload(file, uploadAvatarPath);
        if (fileName == null) {
            return ResultUtil.busiError("程序错误，请重新上传！");
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String acatarPath = "https://www.huancelian.com/avatar/" + fileName;
        sysUserMapper.update(null, Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getAvatarPath, acatarPath)
                .eq(SysUser::getUserId, loginOutputVo.getUserId()));
        loginOutputVo.setAvatarPath(acatarPath);
        session.setAttribute(BusiConstants.SESSION_USER_KEY, loginOutputVo);
        return ResultUtil.data(acatarPath, "用户头像保存成功！");
    }

    /**
     * 用户编辑个人信息
     *
     * @param tdo
     * @return
     */
    @RequestMapping(value = "/editUserById", method = RequestMethod.POST)
    public Result<Object> editUserById(@Validated @RequestBody UserEditTdo tdo) {
        String phonenumber = tdo.getPhone();
        SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getPhonenumber, phonenumber));
        if (sysUser != null) {
            return ResultUtil.busiError("更新失败，邮件或手机号重复！");
        }
        sysUserMapper.update(null, Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getPhonenumber, phonenumber)
                .eq(SysUser::getUserId, tdo.getUserId()));
        return ResultUtil.success("更新成功！");
    }


    /**
     * TODO 条件查询用户信息
     *
     * @param empName
     * @param deptId
     * @param postId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 6:06 下午
     */
    @RequestMapping(value = "/findUserParamVosByCondition", method = RequestMethod.GET)
    public Result<List<UserParamVo>> findUserParamVosByCondition(@RequestParam String empName,
                                                                 @RequestParam Long deptId,
                                                                 @RequestParam Long postId) {
        empName = StrUtil.trimToNull(empName);
        List<UserParamVo> paramVos = sysUserService.selectUsersByCondition(empName, deptId, postId);
        return ResultUtil.data(paramVos);
    }

}