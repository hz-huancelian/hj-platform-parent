package org.hj.chain.platform.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.hj.chain.platform.BPwdEncoderUtil;
import org.hj.chain.platform.StringUtils;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.mapper.*;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.model.SysUserAppRole;
import org.hj.chain.platform.model.SysUserPost;
import org.hj.chain.platform.model.SysUserRole;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.SysUserTdo;
import org.hj.chain.platform.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/7/27  3:52 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/27    create
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysUserAppRoleMapper sysUserAppRoleMapper;
    @Autowired
    private SysUserPostMapper sysUserPostMapper;

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public IPage<UserVo> selectUserListByCondition(PageVo pageVo, UserQryVo qryVo) {
        qryVo.setStatus(StrUtil.trimToNull(qryVo.getStatus()))
                .setUsername(StrUtil.trimToNull(qryVo.getUsername()))
                .setEmpName(StrUtil.trimToNull(qryVo.getEmpName()))
                .setPhonenumber(StrUtil.trimToNull(qryVo.getPhonenumber()));

        Page<UserVo> page = PageUtil.initMpPage(pageVo);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        sysUserMapper.selectUserListByCondition(page, qryVo, loginOutputVo.getOrganId());
        return page;
    }

    @Override
    public UserVo selectUserByLoginName(String username) {
        UserVo userVo = sysUserMapper.selectUserByLoginName(username);
        return userVo;
    }

    @Override
    public UserVo selectUserByPhoneNumber(String phoneNumber) {
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        UserVo userVo = sysUserMapper.selectUserByPhoneNumber(phoneNumber);
        return userVo;
    }

    @Override
    public UserVo selectUserByEmail(String email) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        UserVo userVo = sysUserMapper.selectUserByEmail(email, loginOutputVo.getOrganId());
        return userVo;
    }

    @Override
    public UserVo findUserByUserId(String userId) {
        UserVo userVo = sysUserMapper.selectUserByUserId(userId);
        return userVo;
    }

    @Override
    public List<SysUserRole> selectUserRoleByUserId(String userId) {
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
        return userRoles;
    }

    @Override
    public int deleteUserById(String userId) {
        LocalDateTime now = LocalDateTime.now();
        int count = sysUserMapper.update(null, Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getStatus, "1")
                .set(SysUser::getUpdateTime, now)
                .eq(SysUser::getUserId, userId));
        return count;
    }


    @Transactional
    @Override
    public int deleteUserByIds(String ids) {
        LocalDateTime now = LocalDateTime.now();
        String[] idArrays = ids.split(",");
        List<Long> userIdList = Arrays.stream(idArrays).map(item -> Long.valueOf(item)).collect(Collectors.toList());
        int count = sysUserMapper.update(null, Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getStatus, "1")
                .set(SysUser::getUpdateTime, now)
                .in(SysUser::getUserId, userIdList));
        return count;
    }


    @Transactional
    @Override
    public boolean registerUser(SysUserTdo user, String username) {
        String userId = user.getUserId();
        String email = user.getEmail();
        String phonenumber = user.getPhonenumber();
//        int res = checkLoginNameUnique(userId, username);
//        if (res == 0) {
//            return false;
//        }

//        int res = checkEmailUnique(userId, email);
//        if (res == 0) {
//            return false;
//        }
        int res = checkPhoneUnique(userId, phonenumber);
        if (res == 0) {
            return false;
        }

        Integer serNo = 0;
        String organId = user.getOrganId();
        if (StrUtil.isBlank(username)) {
            SaSession session = StpUtil.getSession();
            LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
            organId = loginOutputVo.getOrganId();
//            serNo = sysUserMapper.selectCount(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getOrganId, organId));
            SysUser su = this.getOne(Wrappers.<SysUser>lambdaQuery()
                    .eq(SysUser::getOrganId, organId).orderByDesc(SysUser::getUserId), false);
            serNo = Integer.parseInt(su.getUserId().substring(organId.length()));
            username = StringUtils.getFixLengStr(organId, 7, serNo);

        }
        //username 是属于机构下有顺序的号

        userId = StringUtils.getFixLengStr(organId, 16, serNo);
        LocalDateTime now = LocalDateTime.now();
        SysUser po = new SysUser();
        po.setDeptId(user.getDeptId())
                .setOrganId(organId)
                .setUserType(user.getUserType())
                .setPassword(BusiConstants.ENCRYPT_PREFIX + passwordEncoder.encode("123456"))
                .setStatus("0")
                .setUsername(username)
                .setAvatarPath(user.getAvatar())
                .setEmail(email)
                .setPhonenumber(phonenumber)
                .setEmpCode(user.getEmpCode())
                .setEmpName(user.getEmpName())
                .setDelFlag("0")
                .setRemark(user.getRemark())
                .setSex(user.getSex())
                .setIsAppLogin(user.getIsAppLogin())
                .setUserId(userId)
                .setCreateTime(now);

        int count = sysUserMapper.insert(po);
        List<String> appRoles = user.getAppRoles();
        if (appRoles != null && !appRoles.isEmpty()) {
            addAppRole(userId, appRoles);
        }

        List<Long> roleIds = user.getRoleIds();
        if (roleIds != null && !roleIds.isEmpty()) {
            insertUserAuth(userId, roleIds);
        }
        List<Long> postIds = user.getPostIds();
        if (postIds != null && !postIds.isEmpty()) {
            insertUserPost(userId, postIds);
        }
        return count > 0;
    }

    @Transactional
    @Override
    public int updateUserByUserId(SysUserTdo user) {
        String userId = user.getUserId();
        String email = user.getEmail();
        String phonenumber = user.getPhonenumber();
//        int res = checkLoginNameUnique(userId, username);
//        if (res == 0) {
//            return 0;
//        }

//        int res = checkEmailUnique(userId, email);
//        if (res == 0) {
//            return 0;
//        }
        int res = checkPhoneUnique(userId, phonenumber);
        if (res == 0) {
            return 0;
        }

        LocalDateTime now = LocalDateTime.now();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        SysUser po = new SysUser();
        po.setDeptId(user.getDeptId())
                .setOrganId(loginOutputVo.getOrganId())
                .setAvatarPath(user.getAvatar())
                .setEmail(email)
                .setPhonenumber(phonenumber)
                .setEmpCode(user.getEmpCode())
                .setEmpName(user.getEmpName())
                .setRemark(user.getRemark())
                .setSex(user.getSex())
                .setIsAppLogin(user.getIsAppLogin())
                .setUserId(userId)
                .setUpdateTime(now);

        sysUserAppRoleMapper.delete(Wrappers.<SysUserAppRole>lambdaQuery().eq(SysUserAppRole::getUserId, userId));
        List<String> appRoles = user.getAppRoles();
        if (appRoles != null && !appRoles.isEmpty()) {
            addAppRole(userId, appRoles);
        }

        sysUserRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
        List<Long> roleIds = user.getRoleIds();
        if (roleIds != null && !roleIds.isEmpty()) {
            insertUserAuth(userId, roleIds);
        }
        sysUserPostMapper.delete(Wrappers.<SysUserPost>lambdaQuery().eq(SysUserPost::getUserId, userId));
        List<Long> postIds = user.getPostIds();
        if (postIds != null && !postIds.isEmpty()) {
            insertUserPost(userId, postIds);
        }

        int count = sysUserMapper.updateById(po);
        return count;
    }

    @Transactional
    @Override
    public void insertUserAuth(String userId, List<Long> roleIds) {
        List<SysUserRole> userRoles = roleIds.stream().map(item -> {
            SysUserRole po = new SysUserRole();
            po.setRoleId(item)
                    .setUserId(userId);
            return po;
        }).collect(Collectors.toList());

        SqlHelper.executeBatch(SysUserRole.class, this.log, userRoles, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
            String sqlStatement = SqlHelper.getSqlStatement(SysUserRoleMapper.class, SqlMethod.INSERT_ONE);
            sqlSession.insert(sqlStatement, entity);
        });
    }

    @Override
    public int resetUserPwd(UserPwdResetVo resetVo) {
        String userId = resetVo.getUserId();
        String oldPwd = resetVo.getOldPwd();
        String newPwd = resetVo.getNewPwd();

        SysUser dbPo = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                .select(SysUser::getPassword)
                .eq(SysUser::getUserId, userId));
        if (dbPo == null) {
            return -1;
        }

        if (!BPwdEncoderUtil.matches(oldPwd, dbPo.getPassword().replace(BusiConstants.ENCRYPT_PREFIX, ""))) {
            return -2;
        }

        LocalDateTime now = LocalDateTime.now();
        int count = sysUserMapper.update(null, Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getPassword, BusiConstants.ENCRYPT_PREFIX + passwordEncoder.encode(newPwd))
                .set(SysUser::getPwdUpdateDate, now)
                .set(SysUser::getUpdateTime, now)
                .eq(SysUser::getUserId, userId));
        return count;
    }


    @Transactional
    @Override
    public void insertUserPost(String userId, List<Long> postIds) {
        List<SysUserPost> userPosts = postIds.stream().map(item -> {
            SysUserPost po = new SysUserPost();
            po.setPostId(item)
                    .setUserId(userId);
            return po;
        }).collect(Collectors.toList());


        SqlHelper.executeBatch(SysUserPost.class, this.log, userPosts, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
            String sqlStatement = SqlHelper.getSqlStatement(SysUserPostMapper.class, SqlMethod.INSERT_ONE);
            sqlSession.insert(sqlStatement, entity);
        });
    }

//    @Override
//    public int checkLoginNameUnique(String userId, String loginName) {
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        userId = userId == null ? "" : userId;
//        SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
//                .select(SysUser::getUserId)
//                .eq(SysUser::getOrganId, loginOutputVo.getOrganId())
//                .eq(SysUser::getUsername, loginName));
//
//        if (sysUser != null && !sysUser.getUserId().equals(userId)) {
//            return 0;
//        }
//
//        return 1;
//    }

    @Override
    public int checkPhoneUnique(String userId, String phone) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        userId = userId == null ? "" : userId;
        SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                .select(SysUser::getUserId)
                .eq(SysUser::getOrganId, loginOutputVo.getOrganId())
                .eq(SysUser::getPhonenumber, phone));

        if (sysUser != null && !sysUser.getUserId().equals(userId)) {
            return 0;
        }

        return 1;
    }

    @Override
    public int checkEmailUnique(String userId, String email) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        userId = userId == null ? "" : userId;
        SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                .select(SysUser::getUserId)
                .eq(SysUser::getOrganId, loginOutputVo.getOrganId())
                .eq(SysUser::getEmail, email));

        if (sysUser != null && !sysUser.getUserId().equals(userId)) {
            return 0;
        }

        return 1;
    }

    @Override
    public List<String> selectUserRoleGroup(String userId) {
        List<String> permims = sysRoleMapper.selectRoleKeysByUserId(userId);
        return permims;
    }

    @Override
    public List<String> selectUserPermsGroup(String userId) {
        List<String> permims = sysMenuMapper.selectPermsByUserId(userId);
        return permims;
    }

    @Override
    public int changeStatus(String userId, String status) {
        LocalDateTime now = LocalDateTime.now();
        int count = sysUserMapper.update(null, Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getStatus, status)
                .set(SysUser::getUpdateTime, now)
                .eq(SysUser::getUserId, userId));
        return count;
    }

    @Override
    public int resetOriganPassword(String userId) {
        LocalDateTime now = LocalDateTime.now();
        int count = sysUserMapper.update(null, Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getPassword, BusiConstants.ENCRYPT_PREFIX + passwordEncoder.encode("123456"))
                .set(SysUser::getUpdateTime, now)
                .eq(SysUser::getUserId, userId));
        return count;
    }

    @Override
    public int invalidUserByUserId(String userId) {
        LocalDateTime now = LocalDateTime.now();
        int count = sysUserMapper.update(null, Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getStatus, "1")
                .set(SysUser::getUpdateTime, now)
                .eq(SysUser::getUserId, userId)
                .eq(SysUser::getStatus, "0"));
        return count;
    }

    @Override
    public List<String> selectAppRolesByUserId(String userId) {
        List<SysUserAppRole> appRoles = sysUserAppRoleMapper.selectList(Wrappers.<SysUserAppRole>lambdaQuery()
                .select(SysUserAppRole::getAppRole)
                .eq(SysUserAppRole::getUserId, userId));

        if (appRoles != null && !appRoles.isEmpty()) {
            List<String> apps = appRoles.stream().map(item -> item.getAppRole()).collect(Collectors.toList());
            return apps;
        }
        return null;
    }

    @Transactional
    @Override
    public int addAppRole(String userId, List<String> appRoles) {
        appRoles.stream().forEach(item -> {
            SysUserAppRole appRole = new SysUserAppRole();
            appRole.setUserId(userId)
                    .setAppRole(item);
            sysUserAppRoleMapper.insert(appRole);
        });
        return appRoles.size();
    }

    @Transactional
    @Override
    public int delAppRole(String userId, List<String> appRoles) {
        appRoles.stream().forEach(item -> {
            sysUserAppRoleMapper.delete(Wrappers.<SysUserAppRole>lambdaQuery()
                    .eq(SysUserAppRole::getUserId, userId)
                    .eq(SysUserAppRole::getAppRole, item));
        });
        return appRoles.size();
    }

    @Override
    public List<UserParamVo> selectUsersByDeptIds(List<Long> deptIds) {
        return sysUserMapper.findUserParamByDeptIds(deptIds);
    }

    @Override
    public List<UserParamVo> selectUsersByCondition(String empName, Long deptId, Long postId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        return sysUserMapper.selectUsersByCondition(empName, loginOutputVo.getOrganId(), deptId, postId);
    }

    @Override
    public List<UserParamVo> selectUsersByRoleKeyAndOrganId(String roleKey, String organId) {
        return sysUserMapper.selectUserParamByRoleKeyAndOrganId(roleKey, organId);
    }

    @Override
    public List<UserParamVo> selectUsersByAppRoleAndOranId(String appRole, String organId) {
        return sysUserMapper.selectUserParamByAppRoleAndOrganId(appRole, organId);
    }

    @Override
    public int enableUserByUserId(String userId) {
        LocalDateTime now = LocalDateTime.now();
        int count = sysUserMapper.update(null, Wrappers.<SysUser>lambdaUpdate()
                .set(SysUser::getStatus, "0")
                .set(SysUser::getUpdateTime, now)
                .eq(SysUser::getUserId, userId)
                .eq(SysUser::getStatus, "1"));
        return count;
    }
}