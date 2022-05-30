package org.hj.chain.platform.common.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.MonitorFreqEnum;
import org.hj.chain.platform.common.MonitorFreqVo;
import org.hj.chain.platform.common.service.ICommonService;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.mapper.SysPostMapper;
import org.hj.chain.platform.mapper.SysUserMapper;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.service.ISysDeptService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserParamVo;
import org.hj.chain.platform.vo.sys.SysUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 通用服务
 * @Iteration : 1.0
 * @Date : 2021/5/13  7:22 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/13    create
 */
@Slf4j
@Service
public class CommonServiceImpl implements ICommonService {
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysUserService sysUserService;

    @Override
    public Result<List<UserParamVo>> findUsersByDeptId() {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        Long deptId = loginOutputVo.getDeptId();
        List<Long> deptIds = sysDeptService.selectChildDeptIdsByDeptId(deptId);
        if (deptIds == null) {
            deptIds = new ArrayList<>();
        }
        deptIds.add(deptId);
        List<UserParamVo> userParamVos = sysUserService.selectUsersByDeptIds(deptIds);
        return ResultUtil.data(userParamVos);
    }

    @Override
    public Result<List<UserParamVo>> findSampLeaders() {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        String roleKey = "Po";
        List<UserParamVo> userParamVos = sysUserService.selectUsersByRoleKeyAndOrganId(roleKey, organId);
        return ResultUtil.data(userParamVos);
    }

    @Override
    public Result<List<UserParamVo>> findCheckEmps() {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        String roleKey = "TeA";
        List<UserParamVo> userParamVos = sysUserService.selectUsersByRoleKeyAndOrganId(roleKey, organId);
        return ResultUtil.data(userParamVos);
    }

    @Override
    public Result<List<MonitorFreqVo>> findMonitorFreqs() {
        List<MonitorFreqEnum> freqEnumList = Arrays.stream(MonitorFreqEnum.values()).collect(Collectors.toList());
        List<MonitorFreqVo> freqVos = freqEnumList.stream().map(item -> {
            MonitorFreqVo freqVo = new MonitorFreqVo();
            freqVo.setUnit(item.getUnit())
                    .setCalBaseVal(item.getCalBaseVal())
                    .setDesc(item.getDesc())
                    .setCalVal(item.getCalVal());
            return freqVo;
        }).collect(Collectors.toList());
        return ResultUtil.data(freqVos);
    }
}