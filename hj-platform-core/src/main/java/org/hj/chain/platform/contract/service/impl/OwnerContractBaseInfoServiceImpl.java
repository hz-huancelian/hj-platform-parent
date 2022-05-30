package org.hj.chain.platform.contract.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.contract.entity.OwnerContractBaseInfo;
import org.hj.chain.platform.contract.mapper.OwnerContractBaseInfoMapper;
import org.hj.chain.platform.contract.service.IOwnerContractBaseInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.tdo.contract.OwnerContractAddTdo;
import org.hj.chain.platform.tdo.contract.OwnerContractModifyTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.contract.OwnerContBaseInfoVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Service
public class OwnerContractBaseInfoServiceImpl extends ServiceImpl<OwnerContractBaseInfoMapper, OwnerContractBaseInfo> implements IOwnerContractBaseInfoService {

    @Override
    public Result<Object> addCont(OwnerContractAddTdo addTdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        OwnerContractBaseInfo dbPo = this.baseMapper.selectOne(Wrappers.<OwnerContractBaseInfo>lambdaQuery()
                .select(OwnerContractBaseInfo::getId)
                .eq(OwnerContractBaseInfo::getOrganId, organId));

        if (dbPo != null) {
            return ResultUtil.busiError("该机构的信息已经存在！");
        }

        LocalDateTime now = LocalDateTime.now();
        OwnerContractBaseInfo po = new OwnerContractBaseInfo();
        po.setAddress(addTdo.getAddress())
                .setAgentPerson(addTdo.getAgentPerson())
                .setBankName(addTdo.getBankName())
                .setBankNo(addTdo.getBankNo())
                .setJurPerson(addTdo.getJurPerson())
                .setOrganId(organId)
                .setOrganName(addTdo.getOrganName())
                .setPostCode(addTdo.getPostCode())
                .setTaxNumber(addTdo.getTaxNumber())
                .setTelFax(addTdo.getTelFax())
                .setContControlId(addTdo.getContControlId())
                .setCreateTime(now)
                .setCreateUserId(loginOutputVo.getUserId());
        this.baseMapper.insert(po);
        return ResultUtil.success("添加成功！");
    }

    @Override
    public Result<Object> modifyContById(OwnerContractModifyTdo modifyTdo) {
        Long id = modifyTdo.getId();
        OwnerContractBaseInfo dbPo = this.baseMapper.selectOne(Wrappers.<OwnerContractBaseInfo>lambdaQuery()
                .select(OwnerContractBaseInfo::getId)
                .eq(OwnerContractBaseInfo::getId, id));

        if (dbPo == null) {
            return ResultUtil.busiError("该ID关联的机构在库中不存在！");
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LocalDateTime now = LocalDateTime.now();
        OwnerContractBaseInfo po = new OwnerContractBaseInfo();
        po.setAddress(modifyTdo.getAddress())
                .setAgentPerson(modifyTdo.getAgentPerson())
                .setBankName(modifyTdo.getBankName())
                .setBankNo(modifyTdo.getBankNo())
                .setJurPerson(modifyTdo.getJurPerson())
                .setOrganName(modifyTdo.getOrganName())
                .setPostCode(modifyTdo.getPostCode())
                .setTaxNumber(modifyTdo.getTaxNumber())
                .setTelFax(modifyTdo.getTelFax())
                .setContControlId(modifyTdo.getContControlId())
                .setUpdateUserId(loginOutputVo.getUserId())
                .setUpdateTime(now)
                .setId(id);

        this.baseMapper.updateById(po);
        return ResultUtil.success("更新成功！");
    }

    @Override
    public OwnerContBaseInfoVo findById(Long id) {
        OwnerContractBaseInfo dbPo = this.baseMapper.selectOne(Wrappers.<OwnerContractBaseInfo>lambdaQuery()
                .eq(OwnerContractBaseInfo::getId, id));

        if (dbPo != null) {
            OwnerContBaseInfoVo vo = new OwnerContBaseInfoVo();
            vo.setId(id)
                    .setAddress(dbPo.getAddress())
                    .setAgentPerson(dbPo.getAgentPerson())
                    .setBankName(dbPo.getBankName())
                    .setBankNo(dbPo.getBankNo())
                    .setJurPerson(dbPo.getJurPerson())
                    .setOrganName(dbPo.getOrganName())
                    .setPostCode(dbPo.getPostCode())
                    .setTaxNumber(dbPo.getTaxNumber())
                    .setTelFax(dbPo.getTelFax())
                    .setContControlId(dbPo.getContControlId());
            return vo;
        }

        return null;
    }

    @Override
    public OwnerContBaseInfoVo findByOrganId(String organId) {
        OwnerContractBaseInfo dbPo = this.baseMapper.selectOne(Wrappers.<OwnerContractBaseInfo>lambdaQuery()
                .eq(OwnerContractBaseInfo::getOrganId, organId));

        if (dbPo != null) {
            OwnerContBaseInfoVo vo = new OwnerContBaseInfoVo();
            vo.setId(dbPo.getId())
                    .setAddress(dbPo.getAddress())
                    .setAgentPerson(dbPo.getAgentPerson())
                    .setBankName(dbPo.getBankName())
                    .setBankNo(dbPo.getBankNo())
                    .setJurPerson(dbPo.getJurPerson())
                    .setOrganName(dbPo.getOrganName())
                    .setPostCode(dbPo.getPostCode())
                    .setTaxNumber(dbPo.getTaxNumber())
                    .setTelFax(dbPo.getTelFax())
                    .setContControlId(dbPo.getContControlId());
            return vo;
        }

        return null;
    }
}
