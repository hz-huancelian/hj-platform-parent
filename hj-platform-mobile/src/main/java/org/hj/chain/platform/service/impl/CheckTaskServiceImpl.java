package org.hj.chain.platform.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.CustomException;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.mapper.CheckFactorInfoMapper;
import org.hj.chain.platform.mapper.SampleDrawApplyMapper;
import org.hj.chain.platform.mapper.SampleItemMapper;
import org.hj.chain.platform.model.*;
import org.hj.chain.platform.service.ICheckTaskService;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.service.ISamplerService;
import org.hj.chain.platform.tdo.MobileCheckFactorTdo;
import org.hj.chain.platform.vo.FactorMethodInfoVo;
import org.hj.chain.platform.vo.MobileCheckFactorInfoVo;
import org.hj.chain.platform.vo.MobileSampleItemDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-09
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-09
 */
@Service
public class CheckTaskServiceImpl implements ICheckTaskService {
    @Autowired
    private CheckFactorInfoMapper checkFactorInfoMapper;
    @Autowired
    private SampleDrawApplyMapper sampleDrawApplyMapper;
    @Autowired
    private ISamplerService samplerService;
    @Autowired
    private IFactorService factorService;
    @Autowired
    private SampleItemMapper sampleItemInfoMapper;


    @Override
    public IPage<MobileCheckFactorInfoVo> findCheckTaskByCondition(PageVo pageVo, String userId, String taskStatus) {
        //taskStatus 1-待领样；2-待检测；3-已驳回
        IPage<MobileCheckFactorInfoVo> page = PageUtil.initMpPage(pageVo);
        List<CheckFactorInfo> checkFactorInfos = checkFactorInfoMapper.selectList(Wrappers.<CheckFactorInfo>lambdaQuery()
                .select(CheckFactorInfo::getId, CheckFactorInfo::getCheckStatus)
                .eq(CheckFactorInfo::getAssignUserId, userId)
                .eq(taskStatus.equals("1"), CheckFactorInfo::getCheckStatus, "0")
                .eq(taskStatus.equals("2"), CheckFactorInfo::getCheckStatus, "2")
                .eq(taskStatus.equals("3"), CheckFactorInfo::getCheckStatus, "6"));
        if(checkFactorInfos != null && !checkFactorInfos.isEmpty()) {
            List<Long> list = checkFactorInfos.stream().map(d -> d.getId()).collect(Collectors.toList());
            checkFactorInfoMapper.getCheckFactorByIds(page, list);
            if(page.getRecords() != null && !page.getRecords().isEmpty()) {
                page.getRecords().forEach(p -> {
                    FactorMethodInfoVo fmi = factorService.findFactorMethodById(p.getCheckStandardId());
                    p.setFactorName(fmi.getFactorName());
                    p.setDayAndCount(String.format("第%s天第%s次", p.getDay(), p.getFrequency()));
                });
            }
        }
        return page;
    }

    @Override
    @Transactional
    public Result<Object> batchSampDrawApply(String ids) {
        if(StrUtil.isBlank(ids)) {
            return ResultUtil.busiError("检测列表ID不能为空！");
        }
        LocalDateTime now = LocalDateTime.now();
        String userId = (String) StpUtil.getLoginId();
        JSONObject param = JSON.parseObject(ids);
        List<Long> checkFactorIds = Arrays.asList(param.getString("ids").split(","))
                .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<CheckFactorInfo> checkFactorInfos = checkFactorInfoMapper.selectBatchIds(checkFactorIds);
        Set<Long> sampItemIds = new HashSet<>();
        for(CheckFactorInfo info : checkFactorInfos) {
            if(!sampItemIds.contains(info.getSampItemId())) {
                sampItemIds.add(info.getSampItemId());
                SampleDrawApply sda = sampleDrawApplyMapper.selectOne(Wrappers.<SampleDrawApply>lambdaQuery()
                        .eq(SampleDrawApply::getApplyUserId, userId)
                        .eq(SampleDrawApply::getSampleItemId, info.getSampItemId()));
                if(sda == null) {
                    sda = new SampleDrawApply();
                    sda.setApplyUserId(userId).setApprovalStatus("0")
                            .setCreateTime(now)
                            .setSampleItemId(info.getSampItemId());
                    sampleDrawApplyMapper.insert(sda);
                    checkFactorInfoMapper.update(null,  Wrappers.<CheckFactorInfo>lambdaUpdate()
                            .set(CheckFactorInfo::getUpdateTime, now)
                            .set(CheckFactorInfo::getCheckStatus, "1")
                            .eq(CheckFactorInfo::getCheckStatus, "0")
                            .eq(CheckFactorInfo::getSampItemId, info.getSampItemId())
                            .eq(CheckFactorInfo::getAssignUserId, userId));
                }
            }
        }
        return ResultUtil.success("申请领样成功！");
    }

    @Override
    @Transactional
    public Result<Object> sampDrawApply(String id) {
        if(StrUtil.isBlank(id)) {
            return ResultUtil.busiError("检测列表ID不能为空！");
        }
        LocalDateTime now = LocalDateTime.now();
        String userId = (String) StpUtil.getLoginId();
        JSONObject param = JSON.parseObject(id);
        Long checkFactorId = param.getLong("id");
        CheckFactorInfo info = checkFactorInfoMapper.selectById(checkFactorId);
        SampleItem sii = sampleItemInfoMapper.selectById(info.getSampItemId());
        List<String> sampStatus = Arrays.asList("6","7");
        if(!sampStatus.contains(sii.getSampleStatus())) {
            if((!sii.getSampleStatus().equals("4") && !sii.getStoreFlag().equals("0")) ||
                    (!sii.getSampleStatus().equals("8") && !sii.getFbFlag().equals("1"))) {
                return ResultUtil.busiError("样品采样尚未完成，无法申请领样！");
            }
        }
        SampleDrawApply sda = sampleDrawApplyMapper.selectOne(Wrappers.<SampleDrawApply>lambdaQuery()
                .eq(SampleDrawApply::getApplyUserId, userId)
                .eq(SampleDrawApply::getSampleItemId, info.getSampItemId()));
        if(sda == null) {
            sda = new SampleDrawApply();
            sda.setApplyUserId(userId).setApprovalStatus("0")
                    .setCreateTime(now)
                    .setSampleItemId(info.getSampItemId());
            int cnt = sampleDrawApplyMapper.insert(sda);
            if(cnt > 0) {
                 cnt = checkFactorInfoMapper.update(null,  Wrappers.<CheckFactorInfo>lambdaUpdate()
                        .set(CheckFactorInfo::getUpdateTime, now)
                        .set(CheckFactorInfo::getCheckStatus, "1")
                        .eq(CheckFactorInfo::getSampItemId, info.getSampItemId())
                        .eq(CheckFactorInfo::getCheckStatus, "0")
                        .eq(CheckFactorInfo::getAssignUserId, userId));
                 if(cnt <= 0) {
                     throw new CustomException("申请领样失败！");
                 }
            }else{
                throw new CustomException("申请领样失败！");
            }
        }
        return ResultUtil.success("申请领样成功！");
    }

    @Override
    public Result<Object> saveCheckFactorData(MobileCheckFactorTdo tdo) {
        checkFactorInfoMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                .set(CheckFactorInfo::getCheckRes, tdo.getCheckRes())
                .set(CheckFactorInfo::getUpdateTime, LocalDateTime.now())
                .set(CheckFactorInfo::getCheckStatus, "2")
                .eq(CheckFactorInfo::getId, tdo.getId()));
        return ResultUtil.success("检测数据录入成功！");
    }

    @Override
    public Result<MobileSampleItemDetailVo> getCheckSampItemDetail(Long id) {
        CheckFactorInfo info = checkFactorInfoMapper.selectById(id);
        return null;
    }
}
