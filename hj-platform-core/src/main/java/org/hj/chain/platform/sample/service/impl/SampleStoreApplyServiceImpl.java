package org.hj.chain.platform.sample.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.check.entity.CheckFactorInfo;
import org.hj.chain.platform.check.mapper.CheckFactorInfoMapper;
import org.hj.chain.platform.common.CustomException;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.sample.entity.SampleItem;
import org.hj.chain.platform.sample.entity.SampleStoreApply;
import org.hj.chain.platform.sample.entity.SampleStoreList;
import org.hj.chain.platform.sample.mapper.SampleItemMapper;
import org.hj.chain.platform.sample.mapper.SampleStoreApplyMapper;
import org.hj.chain.platform.sample.mapper.SampleStoreListMapper;
import org.hj.chain.platform.sample.service.ISampleStoreApplyService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.sample.SampleStoreTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.sample.SampleSearchVo;
import org.hj.chain.platform.vo.sample.SampleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Service
public class SampleStoreApplyServiceImpl extends ServiceImpl<SampleStoreApplyMapper, SampleStoreApply> implements ISampleStoreApplyService {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private SampleStoreListMapper sampleStoreListMapper;
    @Autowired
    private SampleItemMapper sampleItemMapper;
    @Autowired
    private CheckFactorInfoMapper checkFactorInfoMapper;
    @Override
    public IPage<SampleVo> getSampStoreApplyList(PageVo pageVo, SampleSearchVo sv) {
        Page<SampleVo> page = PageUtil.initMpPage(pageVo);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        this.baseMapper.getSampStoreApplyList(page, organId, sv);
        if(page != null && !page.getRecords().isEmpty()) {
            page.getRecords().forEach(p -> {
                SysUser sysUser = sysUserService.getById(p.getReviewUserId());
                p.setReviewUser(sysUser.getEmpName());
            });
        }
        return page;
    }

    @Override
    @Transactional
    public Result<Object> doStoreSample(SampleStoreTdo tdo) {
        String userId = (String) StpUtil.getLoginId();
        LocalDateTime now = LocalDateTime.now();
        Long storeApplyId = Long.parseLong(tdo.getStoreApplyId());
        String sendUser = tdo.getSendUser();
        String storeLocation = tdo.getStoreLocation();
        SampleStoreApply ssa = this.getById(storeApplyId);
        if(ssa == null) {
            return ResultUtil.busiError("入库申请记录不存在！");
        }
        SampleStoreList ssl = sampleStoreListMapper.selectOne(Wrappers.<SampleStoreList>lambdaQuery()
                .eq(SampleStoreList::getSampleItemId, ssa.getSampleItemId()));
        if(ssl != null) {
            return ResultUtil.busiError("样品已入库！");
        }
        ssa.setApprovalUserId(userId).setApprovalStatus("1").setUpdateTime(now);
        int cnt = this.baseMapper.updateById(ssa);
        SampleItem sii = sampleItemMapper.selectById(ssa.getSampleItemId());
        if(cnt > 0) {
            //样品入库
            ssl = new SampleStoreList();
            ssl.setSampleItemId(sii.getId()).setSendUser(sendUser).setStoreLocation(storeLocation)
                    .setStoreTime(now);
            cnt = sampleStoreListMapper.insert(ssl);
            if(cnt > 0) {
                //更新样品状态
                cnt = sampleItemMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                        .set(SampleItem::getSampleStatus, "6").eq(SampleItem::getId, sii.getId()));
                //现场录入的因子，更新检测列表状态为“已录入”
                checkFactorInfoMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                        .set(CheckFactorInfo::getCheckStatus, "3")
                        .eq(CheckFactorInfo::getSampleItemId, sii.getId())
                        .eq(CheckFactorInfo::getCheckStatus, "0")
                        .eq(CheckFactorInfo::getDataEntryStep, "1"));
                if(cnt <= 0) {
                    throw new CustomException(String.format("样品 %s 入库失败！", sii.getSampleNo()));
                }
            }else {
                throw new CustomException(String.format("样品 %s 入库失败！", sii.getSampleNo()));
            }
        }else {
            throw new CustomException(String.format("样品 %s 入库失败！", sii.getSampleNo()));
        }
        return ResultUtil.success(String.format("样品 %s 入库成功！", sii.getSampleNo()));
    }

    @Override
    @Transactional
    public Result<Object> batchDoStoreSample(SampleStoreTdo tdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String userId = loginOutputVo.getUserId();
        LocalDateTime now = LocalDateTime.now();
        String sendUser = tdo.getSendUser();
        String storeLocation = tdo.getStoreLocation();
        List<Long> storeApplyIds = Arrays.stream(tdo.getStoreApplyId().split(",")).map(v -> Long.parseLong(v)).collect(Collectors.toList());
        for(Long storeApplyId : storeApplyIds) {
            SampleStoreApply ssa = this.getById(storeApplyId);
            if(ssa == null) {
                throw new CustomException("入库申请记录不存在！");
            }
            SampleItem sii = sampleItemMapper.selectById(ssa.getSampleItemId());
            SampleStoreList ssl = sampleStoreListMapper.selectOne(Wrappers.<SampleStoreList>lambdaQuery()
                    .eq(SampleStoreList::getSampleItemId, ssa.getSampleItemId()));
            if(ssl != null) {
                throw new CustomException(String.format("样品 %s 不能重复入库！", sii.getSampleNo()));
            }
            ssa.setApprovalUserId(userId).setApprovalStatus("1").setUpdateTime(now);
            int cnt = this.baseMapper.updateById(ssa);
            if(cnt > 0) {
                //样品入库
                ssl = new SampleStoreList();
                ssl.setSampleItemId(sii.getId()).setSendUser(sendUser).setStoreLocation(storeLocation)
                        .setStoreTime(now);
                cnt = sampleStoreListMapper.insert(ssl);
                if(cnt > 0) {
                    //更新样品状态
                    cnt = sampleItemMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                            .set(SampleItem::getSampleStatus, "6").eq(SampleItem::getId, sii.getId()));
                    //现场录入的因子，更新检测列表状态为“已录入”
                    checkFactorInfoMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                            .set(CheckFactorInfo::getCheckStatus, "3")
                            .eq(CheckFactorInfo::getSampleItemId, sii.getId())
                            .eq(CheckFactorInfo::getCheckStatus, "0")
                            .eq(CheckFactorInfo::getDataEntryStep, "1"));
                    if(cnt <= 0) {
                        throw new CustomException(String.format("样品 %s 入库失败！", sii.getSampleNo()));
                    }
                }else {
                    throw new CustomException(String.format("样品 %s 入库失败！", sii.getSampleNo()));
                }
            }else {
                throw new CustomException(String.format("样品 %s 入库失败！", sii.getSampleNo()));
            }
        }
        return ResultUtil.success("样品批量入库成功！");
    }
}
