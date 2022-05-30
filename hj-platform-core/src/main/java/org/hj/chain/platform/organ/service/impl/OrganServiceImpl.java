package org.hj.chain.platform.organ.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.StringUtils;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.factor.entity.FactorCheckStandard;
import org.hj.chain.platform.factor.mapper.FactorCheckStandardMapper;
import org.hj.chain.platform.fileresource.entity.FileResource;
import org.hj.chain.platform.fileresource.mapper.FileResourceMapper;
import org.hj.chain.platform.mapper.*;
import org.hj.chain.platform.model.Organ;
import org.hj.chain.platform.model.SysDept;
import org.hj.chain.platform.model.SysRole;
import org.hj.chain.platform.model.SysRoleMenu;
import org.hj.chain.platform.organ.service.IOrganService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.SysUserTdo;
import org.hj.chain.platform.tdo.organ.RegOrganTdo;
import org.hj.chain.platform.vo.FactorMethodRelVo;
import org.hj.chain.platform.vo.OrganSearchVo;
import org.hj.chain.platform.vo.OrganVo;
import org.hj.chain.platform.vo.UserPwdResetVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/7  10:56 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/07    create
 */
@Service
public class OrganServiceImpl extends ServiceImpl<OrganMapper, Organ> implements IOrganService {
    @Autowired
    private OrganMapper organMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private FactorMethodInfoMapper factorMethodInfoMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private FileResourceMapper resourceMapper;

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String default_password = "admin123456";

    @Override
    public IPage<OrganVo> findOrgansByCondition(PageVo pageVo, OrganSearchVo sv) {
        sv.setOrganId(StrUtil.trimToNull(sv.getOrganId()))
                .setOrganName(StrUtil.trimToNull(sv.getOrganName()));
        Page<OrganVo> page = PageUtil.initMpPage(pageVo);
        organMapper.findOrgansByCondition(page, sv);
        return page;
    }

    @Override
    public Result<Object> modifyPassword(String userId, String oldPassword, String newPassword) {
        UserPwdResetVo resetVo = new UserPwdResetVo();
        resetVo.setUserId(userId)
                .setOldPwd(oldPassword)
                .setNewPwd(newPassword);
        int res = sysUserService.resetUserPwd(resetVo);

        if (res == -1) {
            return ResultUtil.busiError("用户ID关联的用户在库中不存在！");
        } else if (res == -2) {
            return ResultUtil.busiError("原始密码不正确！");
        } else if (res == 0) {
            return ResultUtil.busiError("修改失败！");
        }
        return ResultUtil.success("修改成功！");
    }

    @Transactional
    @Override
    public Result<Object> registOrigan(RegOrganTdo tdo) {
        @NotBlank(message = "机构编号不能为空！") String organId = tdo.getOrganId();
        Organ dbOrgan = organMapper.selectOne(Wrappers.<Organ>lambdaQuery()
                .select(Organ::getId)
                .eq(Organ::getOrganId, organId));
        if (dbOrgan != null) {
            return ResultUtil.busiError("机构已经注册！");
        }

        LocalDateTime now = LocalDateTime.now();
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String loginUserId = (String) tokenInfo.getLoginId();
        String username = StringUtils.getFixLengStr(organId, 8, 0);

        SysDept sysDept = new SysDept();
        sysDept.setStatus("0")
                .setOrganId(organId)
                .setDeptName(tdo.getOrganName())
                .setParentId(0L)
                .setOrderNum("0")
                .setLeader(username)
                .setAncestors("0")
                .setDelFlag("0")
                .setCreateTime(now);
        sysDeptMapper.insert(sysDept);

        SysUserTdo userTdo = new SysUserTdo();
        userTdo.setEmpName(tdo.getOrganJurPerson())
                .setPhonenumber(tdo.getOrganPhone())
                .setDeptId(-1L)
                .setUserType("0")
                .setOrganId(organId);
        sysUserService.registerUser(userTdo, username);

        Organ organ = new Organ();
        organ.setOrganId(organId)
                .setOrganName(tdo.getOrganName())
                .setOrganPhone(tdo.getOrganPhone())
                .setOrganJurPerson(tdo.getOrganJurPerson())
                .setCreateUserId(loginUserId)
                .setUsername(username)
                .setCreateTime(now);
        organMapper.insert(organ);
        //异步执行
        initCheckStandard(organId);
        //同步部门、职位信息
        initDeptRole(organId, tdo.getOrganName(), username);
        //初始化文件控制号
        initFileResource(organId);

        return ResultUtil.success("注册成功！");
    }


    @Async
    @Transactional
    @Override
    public void initCheckStandard(String organId) {
        Date now = new Date();
        Page<FactorMethodRelVo> page = new Page<>();
        page.setSize(50L);
        long current = 1;
        for (; ; ) {
            page.setCurrent(current);
            factorMethodInfoMapper.findPage(page);
            List<FactorMethodRelVo> records = page.getRecords();
            if (records != null && !records.isEmpty()) {
                List<FactorCheckStandard> standards = records.stream().map(item -> {
                    FactorCheckStandard po = new FactorCheckStandard();
                    po.setStandardCode(item.getStandardCode())
                            .setFactorName(item.getFactorName())
                            .setClassId(item.getClassId())
                            .setOrganId(organId)
                            .setCreateTime(now);
                    return po;
                }).collect(Collectors.toList());
                SqlHelper.executeBatch(FactorCheckStandard.class, this.log, standards, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
                    String sqlStatement = SqlHelper.getSqlStatement(FactorCheckStandardMapper.class, SqlMethod.INSERT_ONE);
                    sqlSession.insert(sqlStatement, entity);
                });
            } else {
                break;
            }
            current++;

        }
    }

    @Async
    @Transactional
    @Override
    public void initDeptRole(String organId, String organName, String leader) {
        LocalDateTime now = LocalDateTime.now();
        List<SysRole> sysRoles = sysRoleMapper.selectList(Wrappers.<SysRole>lambdaQuery()
                .eq(SysRole::getOrganId, "NJSB"));
        if (sysRoles != null && !sysRoles.isEmpty()) {
//            sysRoles.stream().forEach(item -> {
//                item.setOrganId(organId)
//                        .setCreateTime(now);
//            });
//
//            SqlHelper.executeBatch(SysRole.class, this.log, sysRoles, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
//                String sqlStatement = SqlHelper.getSqlStatement(SysRoleMapper.class, SqlMethod.INSERT_ONE);
//                sqlSession.insert(sqlStatement, entity);
//            }
            for(SysRole sr : sysRoles) {
                SysRole sysRole = new SysRole();
                BeanUtils.copyProperties(sr, sysRole);
                sysRole.setRoleId(null).setOrganId(organId).setCreateTime(now);
                sysRoleMapper.insert(sysRole);
                List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery()
                        .eq(SysRoleMenu::getRoleId, sr.getRoleId()));
                if(sysRoleMenus != null && !sysRoleMenus.isEmpty()) {
                    sysRoleMenus.stream().forEach(item -> {
                        item.setRoleId(sysRole.getRoleId());
                    });
                    SqlHelper.executeBatch(SysRoleMenu.class, this.log, sysRoleMenus, DEFAULT_BATCH_SIZE, ((sqlSession, entity) -> {
                        String sqlStatement = SqlHelper.getSqlStatement(SysRoleMenuMapper.class, SqlMethod.INSERT_ONE);
                        sqlSession.insert(sqlStatement, entity);
                    }));
                }
            }
        }
    }

    @Async
    @Transactional
    @Override
    public void initFileResource(String organId) {
        List<FileResource> fileResources = resourceMapper.selectList(Wrappers.<FileResource>lambdaQuery()
                .select(FileResource::getFileCode,
                        FileResource::getFileName,
                        FileResource::getFileNo,
                        FileResource::getFileType,
                        FileResource::getCreateUserId)
                .eq(FileResource::getOrganId, "0"));

        LocalDateTime now = LocalDateTime.now();
        if (fileResources != null && !fileResources.isEmpty()) {
            fileResources.stream().forEach(item -> {
                item.setOrganId(organId)
                        .setCreateTime(now);
            });
            SqlHelper.executeBatch(FileResource.class, this.log, fileResources, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
                String sqlStatement = SqlHelper.getSqlStatement(FileResourceMapper.class, SqlMethod.INSERT_ONE);
                sqlSession.insert(sqlStatement, entity);
            });

        }

    }
}