package org.hj.chain.platform.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hj.chain.platform.StringUtils;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.mapper.SysDeptMapper;
import org.hj.chain.platform.mapper.SysUserMapper;
import org.hj.chain.platform.model.SysDept;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.service.ISysDeptService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.SysDeptTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.SysDeptVo;
import org.hj.chain.platform.vo.TreeSelect;
import org.hj.chain.platform.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/7/25  11:16 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/25    create
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysUserService sysUserService;

    @Override
    public List<SysDeptVo> selectDeptList(String deptName, String status) {
        deptName = StrUtil.trimToNull(deptName);
        status = StrUtil.trimToNull(status);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LambdaQueryWrapper queryWrapper = Wrappers.<SysDept>lambdaQuery().eq(SysDept::getOrganId, loginOutputVo.getOrganId())
                .eq(SysDept::getDelFlag, "0")
                .eq(status != null, SysDept::getStatus, status)
                .like(deptName != null, SysDept::getDeptName, deptName);
        queryWrapper.last("ORDER BY status , CONVERT( dept_name USING gbk ) COLLATE gbk_chinese_ci ASC");
        List<SysDept> sysDepts = sysDeptMapper.selectList(queryWrapper);
        if (sysDepts != null) {
            List<SysDeptVo> voList = sysDepts.stream().map(item -> {
                SysDeptVo deptVo = new SysDeptVo();
                deptVo.setDeptId(item.getDeptId())
                        .setAncestors(item.getAncestors())
                        .setCreateTime(item.getCreateTime())
                        .setDelFlag(item.getDelFlag())
                        .setDeptName(item.getDeptName())
                        .setLeader(item.getLeader())
                        .setParentId(item.getParentId())
                        .setRemark(item.getRemark())
                        .setStatus(item.getStatus())
                        .setOrderNum(item.getOrderNum())
                        .setCreateTime(item.getCreateTime())
                        .setUpdateTime(item.getUpdateTime());


                UserVo userVo = sysUserService.selectUserByLoginName(item.getLeader());
                if (userVo != null) {
                    item.setLeader(userVo.getEmpName());
                }

                return deptVo;
            }).collect(Collectors.toList());

            return voList;
        }
        return null;
    }

    @Override
    public List<SysDeptVo> selectAllDepts() {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<SysDept> sysDepts = sysDeptMapper.selectList(Wrappers.<SysDept>lambdaQuery()
                .eq(SysDept::getOrganId, loginOutputVo.getOrganId())
                .eq(SysDept::getStatus, "0")
                .eq(SysDept::getDelFlag, "0")
                .orderByAsc(SysDept::getParentId)
                .orderByAsc(SysDept::getOrderNum));

        if (sysDepts != null) {
            List<SysDeptVo> voList = sysDepts.stream().map(item -> {
                SysDeptVo deptVo = new SysDeptVo();
                deptVo.setDeptId(item.getDeptId())
                        .setAncestors(item.getAncestors())
                        .setCreateTime(item.getCreateTime())
                        .setDelFlag(item.getDelFlag())
                        .setDeptName(item.getDeptName())
                        .setLeader(item.getLeader())
                        .setParentId(item.getParentId())
                        .setRemark(item.getRemark())
                        .setOrderNum(item.getOrderNum())
                        .setStatus(item.getStatus())
                        .setCreateTime(item.getCreateTime())
                        .setUpdateTime(item.getUpdateTime());

                UserVo userVo = sysUserService.selectUserByLoginName(item.getLeader());
                if (userVo != null) {
                    item.setLeader(userVo.getEmpName());
                }

                return deptVo;
            }).collect(Collectors.toList());

            return voList;
        }
        return null;
    }

    @Override
    public int selectDeptCount(Long parentId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        Integer count = sysDeptMapper.selectCount(Wrappers.<SysDept>lambdaQuery()
                .eq(SysDept::getOrganId, loginOutputVo.getOrganId())
                .eq(SysDept::getParentId, parentId));
        return count == null ? 0 : count;
    }

    @Override
    public boolean checkDeptExistUser(Long deptId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        Integer count = sysUserMapper.selectCount(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getOrganId, loginOutputVo.getOrganId())
                .eq(SysUser::getDeptId, deptId));
        return count != null && count > 0;
    }

    @Override
    public int insertDept(SysDeptTdo dept) {
        Long parentId = dept.getParentId();
        String deptName = dept.getDeptName();
//        int count = checkDeptNameUnique(parentId, deptName);
//        if (count > 0) {
//            return 0;
//        }

        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        SysDept parentDept = sysDeptMapper.selectOne(Wrappers.<SysDept>lambdaQuery()
                .select(SysDept::getAncestors)
                .eq(SysDept::getDeptId, parentId)
                .eq(SysDept::getOrganId, organId));
        String ancestors = null;
        if (parentDept != null) {
            ancestors = parentDept.getAncestors() + "," + parentId;
        }
        LocalDateTime now = LocalDateTime.now();
        SysDept po = new SysDept();
        po.setParentId(parentId)
                .setDeptName(deptName)
                .setDelFlag("0")
                .setLeader(dept.getLeader())
                .setOrderNum(dept.getOrderNum())
                .setRemark(dept.getRemark())
                .setAncestors(ancestors)
                .setStatus("0")
                .setOrganId(organId)
                .setCreateTime(now);
        int count = sysDeptMapper.insert(po);
        return count;
    }

    @Override
    public int updateDept(SysDeptTdo dept) {
        Long deptId = dept.getDeptId();
        String deptName = dept.getDeptName();
        Long parentId = dept.getParentId();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        SysDept dbPo = sysDeptMapper.selectOne(Wrappers.<SysDept>lambdaQuery()
//                .select(SysDept::getDeptId)
//                .eq(SysDept::getOrganId, loginOutputVo.getOrganId())
//                .eq(SysDept::getParentId, parentId)
//                .eq(SysDept::getDeptName, deptName));
//
//        if (dbPo != null && !dbPo.getDeptId().equals(deptId)) {
//            return 0;
//        }

        LocalDateTime now = LocalDateTime.now();
        SysDept po = new SysDept();

        po.setParentId(parentId)
                .setDeptName(deptName)
                .setLeader(dept.getLeader())
                .setOrderNum(dept.getOrderNum())
                .setRemark(dept.getRemark())
                .setOrganId(loginOutputVo.getOrganId())
                .setStatus(dept.getStatus())
                .setDeptId(deptId)
                .setUpdateTime(now);

        int count = sysDeptMapper.updateById(po);
        return count;
    }

    @Override
    public SysDeptVo selectDeptById(Long deptId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        SysDept dbPo = sysDeptMapper.selectOne(Wrappers.<SysDept>lambdaQuery()
                .eq(SysDept::getOrganId, loginOutputVo.getOrganId())
                .eq(SysDept::getDeptId, deptId));

        if (dbPo != null) {
            SysDeptVo deptVo = new SysDeptVo();
            deptVo.setDeptId(dbPo.getDeptId())
                    .setAncestors(dbPo.getAncestors())
                    .setCreateTime(dbPo.getCreateTime())
                    .setDelFlag(dbPo.getDelFlag())
                    .setDeptName(dbPo.getDeptName())
                    .setLeader(dbPo.getLeader())
                    .setParentId(dbPo.getParentId())
                    .setOrderNum(dbPo.getOrderNum())
                    .setStatus(dbPo.getStatus())
                    .setRemark(dbPo.getRemark());

            UserVo userVo = sysUserService.selectUserByLoginName(dbPo.getLeader());
            if (userVo != null) {
                deptVo.setLeader(userVo.getEmpName());
            }
            return deptVo;
        }
        return null;
    }

    @Override
    public int deleteByDeptId(Long deptId) {
        LocalDateTime now = LocalDateTime.now();
        int count = sysDeptMapper.update(null, Wrappers.<SysDept>lambdaUpdate()
                .set(SysDept::getDelFlag, "1")
                .set(SysDept::getUpdateTime, now)
                .eq(SysDept::getDeptId, deptId));
        return count;
    }

    @Override
    public int checkDeptNameUnique(Long parentId, String deptName) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        Integer count = sysDeptMapper.selectCount(Wrappers.<SysDept>lambdaQuery()
                .eq(SysDept::getOrganId, loginOutputVo.getOrganId())
                .eq(SysDept::getParentId, parentId)
                .eq(SysDept::getDeptName, deptName));
        return count;
    }

    @Override
    public List<TreeSelect> selectDeptTrees() {
        List<SysDeptVo> sysDeptVos = selectAllDepts();
        List<SysDeptVo> sysDeptTreeVos = buildDeptTree(sysDeptVos);
        if (sysDeptTreeVos != null) {
            return sysDeptTreeVos.stream().map(TreeSelect::new).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<Long> selectChildDeptIdsByDeptId(Long deptId) {
        List<SysDept> list = sysDeptMapper.selectList(Wrappers.<SysDept>lambdaQuery()
                .select(SysDept::getDeptId)
                .like(SysDept::getAncestors, String.valueOf(deptId)));
        if (list != null && !list.isEmpty()) {
            List<Long> childDeptIds = list.stream().map(item -> item.getDeptId()).collect(Collectors.toList());
            return childDeptIds;
        }
        return null;
    }


    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    private List<SysDeptVo> buildDeptTree(List<SysDeptVo> depts) {
        List<SysDeptVo> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysDeptVo dept : depts) {
            tempList.add(dept.getDeptId());
        }
        for (Iterator<SysDeptVo> iterator = depts.iterator(); iterator.hasNext(); ) {
            SysDeptVo dept = (SysDeptVo) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDeptVo> list, SysDeptVo t) {
        // 得到子节点列表
        List<SysDeptVo> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDeptVo tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDeptVo> getChildList(List<SysDeptVo> list, SysDeptVo t) {
        List<SysDeptVo> tlist = new ArrayList<>();
        Iterator<SysDeptVo> it = list.iterator();
        while (it.hasNext()) {
            SysDeptVo n = (SysDeptVo) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDeptVo> list, SysDeptVo t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

}