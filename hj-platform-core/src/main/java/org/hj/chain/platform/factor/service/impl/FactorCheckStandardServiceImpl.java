package org.hj.chain.platform.factor.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.factor.entity.FactorCheckStandard;
import org.hj.chain.platform.factor.mapper.FactorCheckStandardMapper;
import org.hj.chain.platform.factor.service.IFactorCheckStandardService;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.tdo.factor.FactorCheckStandardModifyTdo;
import org.hj.chain.platform.vo.FactorMethodInfoVo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.factor.FactorCheckStandardVo;
import org.hj.chain.platform.vo.factor.FactorSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 因子检测能力、费用表 服务实现类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Service
public class FactorCheckStandardServiceImpl extends ServiceImpl<FactorCheckStandardMapper, FactorCheckStandard> implements IFactorCheckStandardService {

    @Autowired
    private FactorCheckStandardMapper factorCheckStandardMapper;
    @Autowired
    private IFactorService factorService;


    @Override
    public IPage<FactorCheckStandardVo> findCheckStandardsByCondition(PageVo pageVo, FactorSearchVo sv) {
        String classId = StrUtil.trimToNull(sv.getClassId());
        String secdClassId = StrUtil.trimToNull(sv.getSecdClassId());
        List<String> checkStandardIds = null;
        if (secdClassId != null && classId != null && classId.equals("004")) {
            checkStandardIds = new ArrayList<>();
            if (secdClassId.equals("004001")) {
                checkStandardIds.add("100004-0001");
            } else if (secdClassId.equals("004002")) {
                checkStandardIds.add("100004-0003");
            } else if (secdClassId.equals("004003")) {
                checkStandardIds.add("100004-0009");
            } else if (secdClassId.equals("004004")) {
                checkStandardIds.add("100004-0005");
            } else if (secdClassId.equals("004005")) {
                checkStandardIds.add("100004-0007");
            }
        }
        sv.setFactorName(StrUtil.trimToNull(sv.getFactorName()))
                .setClassId(classId)
                .setStandardNo(StrUtil.trimToNull(sv.getStandardNo()))
                .setStandardName(StrUtil.trimToNull(sv.getStandardName()))
                .setAuthType(StrUtil.trimToNull(sv.getAuthType()))
                .setDataEntryStep(StrUtil.trimToNull(sv.getAuthType()));
        Page<FactorCheckStandardVo> page = PageUtil.initMpPage(pageVo);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        factorCheckStandardMapper.findCheckStandardsByCondition(page, loginOutputVo.getOrganId(), checkStandardIds, sv);
        page.getRecords().forEach(item -> {
            FactorMethodInfoVo methodInfoVo = factorService.findFactorMethodById(item.getStandardCode());
            if (methodInfoVo != null) {
                item.setFactorUnit(methodInfoVo.getDefaultUnitName());
                item.setStandardName(methodInfoVo.getStandardName());
                item.setClassId(methodInfoVo.getClassId());
                item.setClassName(methodInfoVo.getClassName());
                item.setDataEntryStep(methodInfoVo.getDataEntryStep());
            }
        });

        return page;
    }

    @Override
    public Result<Object> modifyCheckStandardById(FactorCheckStandardModifyTdo tdo) {
        @NotNull(message = "主键ID不能为空") Long id = tdo.getId();
        FactorCheckStandard dbPo = factorCheckStandardMapper.selectOne(Wrappers.<FactorCheckStandard>lambdaQuery()
                .select(FactorCheckStandard::getId)
                .eq(FactorCheckStandard::getId, id));

        if (dbPo == null) {
            return ResultUtil.busiError("id关联的检测记录在库中不存在！");
        }

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String userID = (String) tokenInfo.getLoginId();
        Date now = new Date();
        FactorCheckStandard po = new FactorCheckStandard();
        po.setId(id)
                .setCmaFlg(tdo.getCmaFlg())
                .setCnasFlg(tdo.getCnasFlg())
                .setPrice(tdo.getPrice())
                .setUpdateUserId(userID)
                .setUpdateTime(now);
        if (po.getCmaFlg().equals("1") || po.getCnasFlg().equals("1")) {
            po.setExtAssistFlg("0");
        } else {
            po.setExtAssistFlg("1");
        }

        factorCheckStandardMapper.updateById(po);
        return ResultUtil.success("修改成功！");
    }

}
