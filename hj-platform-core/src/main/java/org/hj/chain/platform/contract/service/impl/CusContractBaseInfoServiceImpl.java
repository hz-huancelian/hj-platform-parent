package org.hj.chain.platform.contract.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.RegexUtils;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.contract.entity.CusContractBaseInfo;
import org.hj.chain.platform.contract.mapper.CusContractBaseInfoMapper;
import org.hj.chain.platform.contract.service.ICusContractBaseInfoService;
import org.hj.chain.platform.service.ISysDeptService;
import org.hj.chain.platform.service.ISysRoleService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.contract.CusContBaseAddTdo;
import org.hj.chain.platform.tdo.contract.CusContBaseModifyTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserVo;
import org.hj.chain.platform.vo.contract.CusContBaseInfoListVo;
import org.hj.chain.platform.vo.contract.CusContBaseInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Service
public class CusContractBaseInfoServiceImpl extends ServiceImpl<CusContractBaseInfoMapper, CusContractBaseInfo> implements ICusContractBaseInfoService {

    @Autowired
    private CusContractBaseInfoMapper cusContractBaseInfoMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDeptService sysDeptService;
    private static final String CUS_PERM = "";

    @Transactional
    @Override
    public Long addCusContBase(CusContBaseAddTdo addTdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        @NotBlank(message = "单位名称不能为空") String companyName = addTdo.getCompanyName();
        CusContractBaseInfo dbPo = cusContractBaseInfoMapper.selectOne(Wrappers.<CusContractBaseInfo>lambdaQuery()
                .select(CusContractBaseInfo::getId)
                .eq(CusContractBaseInfo::getOrganId, organId)
                .eq(CusContractBaseInfo::getCompanyName, companyName));

        if (dbPo != null) {
            return -1L;
        }

        LocalDateTime now = LocalDateTime.now();
        CusContractBaseInfo po = new CusContractBaseInfo();
        po.setCompanyName(companyName)
                .setAddress(addTdo.getAddress())
                .setAgentPerson(addTdo.getAgentPerson())
                .setBankName(addTdo.getBankName())
                .setJurPerson(addTdo.getJurPerson())
                .setOrganId(organId)
                .setPostCode(addTdo.getPostCode())
                .setTaxNumber(addTdo.getTaxNumber())
                .setTelFax(addTdo.getTelFax())
                .setCreateUserId(loginOutputVo.getUserId())
                .setBankNo(addTdo.getBankNo())
                .setDeptId(loginOutputVo.getDeptId())
                .setCreateTime(now);

        cusContractBaseInfoMapper.insert(po);
        return po.getId();

    }

    @Transactional
    @Override
    public Result<Object> modifyCusContBaseById(CusContBaseModifyTdo modifyTdo) {
        Long cusContBaseInfoId = modifyTdo.getCusContBaseInfoId();
        CusContractBaseInfo dbPo = cusContractBaseInfoMapper.selectOne(Wrappers.<CusContractBaseInfo>lambdaQuery()
                .select(CusContractBaseInfo::getId)
                .eq(CusContractBaseInfo::getId, cusContBaseInfoId));

        if (dbPo == null) {
            return ResultUtil.busiError("该公司在库中没有登记！");
        }

        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LocalDateTime now = LocalDateTime.now();
        CusContractBaseInfo po = new CusContractBaseInfo();
        po.setCompanyName(modifyTdo.getCompanyName())
                .setAddress(modifyTdo.getAddress())
                .setAgentPerson(modifyTdo.getAgentPerson())
                .setBankName(modifyTdo.getBankName())
                .setJurPerson(modifyTdo.getJurPerson())
                .setPostCode(modifyTdo.getPostCode())
                .setTaxNumber(modifyTdo.getTaxNumber())
                .setTelFax(modifyTdo.getTelFax())
                .setBankNo(modifyTdo.getBankNo())
                .setUpdateUserId(loginOutputVo.getUserId())
                .setDeptId(loginOutputVo.getDeptId())
                .setId(cusContBaseInfoId)
                .setUpdateTime(now);

        cusContractBaseInfoMapper.updateById(po);
        return ResultUtil.success("更新成功！");
    }

    @Override
    public IPage<CusContBaseInfoListVo> findByCondition(PageVo pageVo, String companyName) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Page<CusContBaseInfoListVo> page = PageUtil.initMpPage(pageVo);
        String userType = loginOutputVo.getUserType();
        if (userType.equals("1")) {
            String maxScope = sysRoleService.findMaxRoleScopeByPerms(loginOutputVo.getUserId(), CUS_PERM);
            if (maxScope != null) {
                List<Long> deptIds = new ArrayList<>();
                String userId = null;
                if (maxScope.equals("3")) {
                    //2：本部门数据权限；
                    deptIds.add(loginOutputVo.getDeptId());
                } else if (maxScope.equals("2")) {
                    deptIds.add(loginOutputVo.getDeptId());
                    //3：本部门及以下数据权限；
                    List<Long> childDeptIds = sysDeptService.selectChildDeptIdsByDeptId(loginOutputVo.getDeptId());
                    if (childDeptIds != null && !childDeptIds.isEmpty()) {
                        deptIds.addAll(childDeptIds);
                    }
                } else if (maxScope.equals("4")) {
                    //本人
                    userId = loginOutputVo.getUserId();
                }

                if (deptIds == null || deptIds.isEmpty()) {
                    deptIds = null;
                }
                cusContractBaseInfoMapper.findPageByCondition(page, organId, deptIds, userId, companyName);

            }
        } else {
            cusContractBaseInfoMapper.findPageByCondition(page, organId, null, null, companyName);
        }
        page.getRecords().forEach(item -> {
            String createUserId = item.getCreateUserId();
            if (StrUtil.isNotBlank(createUserId)) {
                UserVo userVo = sysUserService.findUserByUserId(createUserId);
                if (userVo != null) {
                    item.setCreateUserId(userVo.getEmpName());
                }
            }
        });
        return page;
    }

    @Override
    public CusContBaseInfoVo findById(Long id) {
        CusContractBaseInfo dbPo = cusContractBaseInfoMapper.selectOne(Wrappers.<CusContractBaseInfo>lambdaQuery()
                .eq(CusContractBaseInfo::getId, id));

        if (dbPo != null) {
            CusContBaseInfoVo vo = new CusContBaseInfoVo();
            vo.setAddress(dbPo.getAddress())
                    .setAgentPerson(dbPo.getAgentPerson())
                    .setBankName(dbPo.getBankName())
                    .setBankNo(dbPo.getBankNo())
                    .setCompanyName(dbPo.getCompanyName())
                    .setJurPerson(dbPo.getJurPerson())
                    .setPostCode(dbPo.getPostCode())
                    .setTaxNumber(dbPo.getTaxNumber())
                    .setTelFax(dbPo.getTelFax())
                    .setId(id);
            return vo;

        }
        return null;
    }

    @Override
    public Map<Long, String> fuzzyQuery(String companyName) {
        companyName = StrUtil.trimToNull(companyName);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        String userId = loginOutputVo.getUserId();
        List<CusContractBaseInfo> baseInfos = cusContractBaseInfoMapper.selectList(Wrappers.<CusContractBaseInfo>lambdaQuery()
                .select(CusContractBaseInfo::getId,
                        CusContractBaseInfo::getCompanyName)
                .eq(CusContractBaseInfo::getOrganId, organId)
                .eq(CusContractBaseInfo::getCreateUserId, userId)
                .eq(CusContractBaseInfo::getDelStatus, '0')
                .like(CusContractBaseInfo::getCompanyName, companyName));
        if (baseInfos != null) {
            Map<Long, String> resMap = baseInfos.stream().collect(Collectors.toMap(CusContractBaseInfo::getId, CusContractBaseInfo::getCompanyName));
            return resMap;
        }
        return null;
    }

    @Override
    public Result<Object> removeById(Long id) {
        CusContractBaseInfo ccbi = cusContractBaseInfoMapper.selectById(id);
        if (ccbi == null) {
            return ResultUtil.busiError("客户不存在！");
        }
        cusContractBaseInfoMapper.update(null, Wrappers.<CusContractBaseInfo>lambdaUpdate()
                .set(CusContractBaseInfo::getDelStatus, "1")
                .set(CusContractBaseInfo::getUpdateTime, LocalDateTime.now())
                .eq(CusContractBaseInfo::getId, id));
//        int cnt = cusContractBaseInfoMapper.deleteById(id);
//        if (cnt < 0) {
//            return ResultUtil.busiError("客户信息删除失败！");
//        }
        return ResultUtil.success("客户删除成功！");
    }
}
