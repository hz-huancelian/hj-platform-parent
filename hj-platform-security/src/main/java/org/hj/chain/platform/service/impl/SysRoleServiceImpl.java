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
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.mapper.SysRoleMapper;
import org.hj.chain.platform.mapper.SysRoleMenuMapper;
import org.hj.chain.platform.mapper.SysUserRoleMapper;
import org.hj.chain.platform.model.SysRole;
import org.hj.chain.platform.model.SysRoleMenu;
import org.hj.chain.platform.model.SysUserRole;
import org.hj.chain.platform.service.ISysRoleService;
import org.hj.chain.platform.tdo.RoleMenuRelTdo;
import org.hj.chain.platform.tdo.SysRoleTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.RoleQueryVo;
import org.hj.chain.platform.vo.SysRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/7/25  9:26 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/25    create
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public IPage<SysRoleVo> selectRoleListByCondition(PageVo pageVo, RoleQueryVo queryVo) {
        queryVo.setRoleKey(StrUtil.trimToNull(queryVo.getRoleKey()))
                .setRoleName(StrUtil.trimToNull(queryVo.getRoleName()))
                .setStatus(StrUtil.trimToNull(queryVo.getStatus()));

        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        queryVo.setOrganId(loginOutputVo.getOrganId());
        Page<SysRoleVo> page = PageUtil.initMpPage(pageVo);
        sysRoleMapper.selectRoleListByCondition(page, queryVo);
        return page;
    }

    @Override
    public Set<String> selectRoleKeys(String userId) {
        List<String> list = sysRoleMapper.selectRoleKeysByUserId(userId);
        Set<String> set = new HashSet<>(list);
        return set;
    }

    @Override
    public List<Long> selectRoleIdsByUserId(String userId) {
        return sysRoleMapper.selectRoleIdsByUserId(userId);
    }

    @Override
    public List<SysRoleVo> selectRolesByUserId(String userId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<SysRoleVo> roleVos = sysRoleMapper.selectRolesByUserId(loginOutputVo.getOrganId(), userId);
        return roleVos;
    }

    @Override
    public List<SysRoleVo> selectRoleAll() {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<SysRole> sysRoles = sysRoleMapper.selectList(Wrappers.<SysRole>lambdaQuery()
                .eq(SysRole::getOrganId, loginOutputVo.getOrganId())
                .eq(SysRole::getDelFlag, "0")
                .eq(SysRole::getStatus, "0"));
        if (sysRoles != null) {
            List<SysRoleVo> roleVos = sysRoles.stream().map(item -> {
                SysRoleVo roleVo = new SysRoleVo();
                roleVo.setRoleId(item.getRoleId())
                        .setDataScope(item.getDataScope())
                        .setDelFlag(item.getDelFlag())
                        .setRemark(item.getRemark())
                        .setRoleKey(item.getRoleKey())
                        .setRoleName(item.getRoleName())
                        .setRoleSort(item.getRoleSort())
                        .setStatus(item.getStatus())
                        .setUpdateTime(item.getUpdateTime())
                        .setCreateTime(item.getCreateTime());
                return roleVo;
            }).collect(Collectors.toList());

            return roleVos;
        }
        return null;
    }

    @Override
    public SysRoleVo selectRoleById(Long roleId) {
        SysRole dbPo = sysRoleMapper.selectOne(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getRoleId, roleId));
        if (dbPo != null) {
            SysRoleVo roleVo = new SysRoleVo();
            roleVo.setRoleId(dbPo.getRoleId())
                    .setDataScope(dbPo.getDataScope())
                    .setDelFlag(dbPo.getDelFlag())
                    .setRemark(dbPo.getRemark())
                    .setRoleKey(dbPo.getRoleKey())
                    .setRoleName(dbPo.getRoleName())
                    .setRoleSort(dbPo.getRoleSort())
                    .setStatus(dbPo.getStatus())
                    .setUpdateTime(dbPo.getUpdateTime())
                    .setCreateTime(dbPo.getCreateTime());
            return roleVo;
        }
        return null;
    }

    @Override
    public boolean deleteRoleById(Long roleId) {

        LocalDateTime now = LocalDateTime.now();
        int count = sysRoleMapper.update(null, Wrappers.<SysRole>lambdaUpdate()
                .set(SysRole::getDelFlag, "1")
                .set(SysRole::getUpdateTime, now)
                .eq(SysRole::getRoleId, roleId));
        return count > 0;
    }

    @Transactional
    @Override
    public int deleteRoleByIds(String ids) {
        LocalDateTime now = LocalDateTime.now();
        String[] idArray = ids.split(",");
        List<Long> idList = Arrays.stream(idArray).map(item -> Long.valueOf(item)).collect(Collectors.toList());
        int count = sysRoleMapper.update(null, Wrappers.<SysRole>lambdaUpdate()
                .set(SysRole::getDelFlag, "1")
                .set(SysRole::getUpdateTime, now)
                .in(SysRole::getRoleId, idList));
        return count;
    }

    @Transactional
    @Override
    public int insertRole(SysRoleTdo role) {
        Long roleId = role.getRoleId();
        String roleName = role.getRoleName();
        String roleKey = role.getRoleKey();
        int res = checkRoleNameUnique(roleId, roleName);
        if (res == 0) {
            return -1;
        }
//        res = checkRoleKeyUnique(roleId, roleKey);
//        if (res == 0) {
//            return -2;
//        }
        LocalDateTime now = LocalDateTime.now();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        SysRole po = new SysRole();
        po.setOrganId(loginOutputVo.getOrganId())
                .setRoleName(roleName)
                .setDataScope(role.getDataScope())
                .setRoleKey(roleKey)
                .setRoleSort(role.getRoleSort())
                .setRemark(role.getRemark())
                .setStatus("0")
                .setDelFlag("0")
                .setCreateTime(now);

        int count = sysRoleMapper.insert(po);

        inserRoleMenuRelation(role.getMenuIds(), po.getRoleId());
        return count;
    }

    /**
     * TODO 批量插入角色菜单关系
     *
     * @param menuIds
     * @param roleId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 7:01 下午
     */
    private void inserRoleMenuRelation(List<Long> menuIds, Long roleId) {
        if (menuIds != null && !menuIds.isEmpty()) {
            List<SysRoleMenu> roleMenuList = menuIds.stream().map(item -> {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setMenuId(item)
                        .setRoleId(roleId);
                return roleMenu;
            }).collect(Collectors.toList());
            SqlHelper.executeBatch(SysRoleMenu.class, this.log, roleMenuList, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
                String sqlStatement = SqlHelper.getSqlStatement(SysRoleMenuMapper.class, SqlMethod.INSERT_ONE);
                sqlSession.insert(sqlStatement, entity);
            });
        }
    }

    @Transactional
    @Override
    public int updateRole(SysRoleTdo role) {
        Long roleId = role.getRoleId();
        String roleName = role.getRoleName();
        String roleKey = role.getRoleKey();
        int res = checkRoleNameUnique(roleId, roleName);
        if (res == 0) {
            return 0;
        }

//        res = checkRoleKeyUnique(roleId, roleKey);
//        if (res == 0) {
//            return 0;
//        }
        LocalDateTime now = LocalDateTime.now();
        SysRole po = new SysRole();
        po.setRoleName(roleName)
                .setDataScope(role.getDataScope())
                .setRoleKey(roleKey)
                .setRoleSort(role.getRoleSort())
                .setStatus(role.getStatus())
                .setRemark(role.getRemark())
                .setRoleId(roleId)
                .setUpdateTime(now);
        int count = sysRoleMapper.updateById(po);
        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, roleId));

        inserRoleMenuRelation(role.getMenuIds(), roleId);

        return count;
    }

    @Override
    public int authDataScope(Long roleId, String dataScope) {
        LocalDateTime now = LocalDateTime.now();
        int count = sysRoleMapper.update(null, Wrappers.<SysRole>lambdaUpdate()
                .set(SysRole::getDataScope, dataScope)
                .set(SysRole::getUpdateTime, now)
                .eq(SysRole::getRoleId, roleId));
        return count;
    }

    @Override
    public int checkRoleNameUnique(Long roleId, String roleName) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        SysRole dbRole = sysRoleMapper.selectOne(Wrappers.<SysRole>lambdaQuery()
                .select(SysRole::getRoleId)
                .eq(SysRole::getOrganId, loginOutputVo.getOrganId())
                .eq(SysRole::getDelFlag, "0")
                .eq(SysRole::getRoleName, roleName));

        if (dbRole != null && !dbRole.getRoleId().equals(roleId)) {
            return 0;
        }
        return 1;
    }

    @Override
    public int checkRoleKeyUnique(Long roleId, String roleKey) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        SysRole dbRole = sysRoleMapper.selectOne(Wrappers.<SysRole>lambdaQuery()
                .select(SysRole::getRoleId)
                .eq(SysRole::getOrganId, loginOutputVo.getOrganId())
                .eq(SysRole::getDelFlag, "0")
                .eq(SysRole::getRoleKey, roleKey));

        if (dbRole != null && !dbRole.getRoleId().equals(roleId)) {
            return 0;
        }
        return 1;
    }


    @Override
    public int changeStatus(Long roleId, String status) {
        LocalDateTime now = LocalDateTime.now();
        int count = sysRoleMapper.update(null, Wrappers.<SysRole>lambdaUpdate()
                .set(SysRole::getStatus, status)
                .set(SysRole::getUpdateTime, now)
                .eq(SysRole::getRoleId, roleId));
        return count;
    }


    @Override
    public String findMaxRoleScopeByPerms(String userId, String perms) {
        return sysRoleMapper.findMaxRoleScopeByPerms(userId, perms);
    }

    @Override
    public int deleteAuthUser(String userId, Long roleId) {
        int count = sysUserRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery()
                .eq(SysUserRole::getUserId, userId)
                .eq(SysUserRole::getRoleId, roleId));
        return count;
    }

    @Transactional
    @Override
    public int deleteAuthUsers(Long roleId, String userIds) {
        String[] userIdArrays = userIds.split(",");
        List<Long> userIdList = Arrays.stream(userIdArrays).map(item -> Long.valueOf(item)).collect(Collectors.toList());
        int count = sysUserRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery()
                .in(SysUserRole::getUserId, userIdList)
                .eq(SysUserRole::getRoleId, roleId));
        return count;
    }

    @Transactional
    @Override
    public int insertAuthUsers(Long roleId, String userIds) {
        String[] userIdArrays = userIds.split(",");
        List<SysUserRole> userIdList = Arrays.stream(userIdArrays).map(item -> {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(item)
                            .setRoleId(roleId);
                    return userRole;
                }
        ).collect(Collectors.toList());

        SqlHelper.executeBatch(SysUserRole.class, this.log, userIdList, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
            String sqlStatement = SqlHelper.getSqlStatement(SysUserRoleMapper.class, SqlMethod.INSERT_ONE);
            sqlSession.insert(sqlStatement, entity);
        });
        return 1;
    }

    @Transactional
    @Override
    public int saveRoleMenuRel(RoleMenuRelTdo tdo) {
        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, tdo.getRoleId()));
        inserRoleMenuRelation(tdo.getMenuIds(), tdo.getRoleId());
        return 1;
    }
}