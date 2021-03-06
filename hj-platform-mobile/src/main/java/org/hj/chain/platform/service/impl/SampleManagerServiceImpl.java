package org.hj.chain.platform.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.CustomException;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.mapper.*;
import org.hj.chain.platform.model.*;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.service.ISampleManagerService;
import org.hj.chain.platform.tdo.MobileSampleStoreTdo;
import org.hj.chain.platform.vo.*;
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
 * @Iteration : 1.0
 * @Date : 2021-05-10
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-10
 */
@Service
public class SampleManagerServiceImpl implements ISampleManagerService {
    @Autowired
    private SampleDrawApplyMapper sampleDrawApplyMapper;
    @Autowired
    private SampleStoreApplyMapper sampleStoreApplyMapper;
    @Autowired
    private SampleItemMapper sampleItemInfoMapper;
    @Autowired
    private SampleStoreListMapper sampleStoreListMapper;
    @Autowired
    private IFactorService factorService;
    @Autowired
    private CheckFactorInfoMapper checkFactorInfoMapper;
    @Autowired
    private SampleItemFactorDataMapper sampleItemFactorDataMapper;


    @Override
    public IPage<MobileSampleItemVo> findSampleItemByCondition(PageVo pageVo, String sampStatus) {
        //sampStatus 0-????????????1-?????????
        Page<MobileSampleItemVo> page = PageUtil.initMpPage(pageVo);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        if(sampStatus.equals("0")) {
            sampleStoreApplyMapper.findSampleItemByCondition(page, organId);
            if(page.getRecords() != null && !page.getRecords().isEmpty()) {
                page.getRecords().forEach(p -> {
                    List<SampleFactorDataVo> factorDataVos = sampleItemFactorDataMapper.getSampleFactorDataBySampItemId(p.getSampItemId());
                    String factorName = factorDataVos.stream().map(SampleFactorDataVo::getFactorName).collect(Collectors.joining("/"));
                    p.setFactorName(factorName);
                    p.setDayAndCount(String.format("???%s??????%s???", p.getDay(), p.getFrequency()));
                });
            }
        }else if(sampStatus.equals("1")) {
            sampleDrawApplyMapper.findSampleItemByCondition(page, organId);
            if(page.getRecords() != null && !page.getRecords().isEmpty()) {
                page.getRecords().forEach(p -> {
                    List<SampleFactorDataVo> factorDataVos = sampleItemFactorDataMapper.getSampleFactorDataBySampItemId(p.getSampItemId());
                    String factorName = factorDataVos.stream().map(SampleFactorDataVo::getFactorName).collect(Collectors.joining("/"));
                    p.setFactorName(factorName);
                    p.setDayAndCount(String.format("???%s??????%s???", p.getDay(), p.getFrequency()));
                });
            }
        }
        return page;
    }


    @Override
    @Transactional
    public Result<Object> storeSample(MobileSampleStoreTdo tdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String userId = loginOutputVo.getUserId();
        LocalDateTime now = LocalDateTime.now();
        Long storeApplyId = tdo.getStoreApplyId();
        String sendUser = tdo.getSendUser();
        String storeLocation = tdo.getStoreLocation();
        int cnt = sampleStoreApplyMapper.update(null, Wrappers.<SampleStoreApply>lambdaUpdate()
                .set(SampleStoreApply::getApprovalUserId, userId)
                .set(SampleStoreApply::getApprovalStatus, "1")
                .set(SampleStoreApply::getUpdateTime, now)
                .eq(SampleStoreApply::getId, storeApplyId));
        if(cnt > 0) {
            SampleStoreApply ssa = sampleStoreApplyMapper.selectById(storeApplyId);
            Long sampItemId = ssa.getSampleItemId();
            //????????????
            SampleItem sii = sampleItemInfoMapper.selectById(sampItemId);
            SampleStoreList ssl = new SampleStoreList();
            ssl.setSampleItemId(sampItemId).setSendUser(sendUser).setStoreLocation(storeLocation)
                    .setStoreTime(now);
            sampleStoreListMapper.insert(ssl);
            //??????????????????
            sampleItemInfoMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                    .set(SampleItem::getSampleStatus, "6").eq(SampleItem::getId, sampItemId));
            //??????????????????????????????????????????????????????????????????
            checkFactorInfoMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                    .set(CheckFactorInfo::getCheckStatus, "3")
                    .eq(CheckFactorInfo::getSampItemId, sampItemId)
                    .eq(CheckFactorInfo::getCheckStatus, "0")
                    .eq(CheckFactorInfo::getDataEntryStep, "1"));

        }else {
            throw new CustomException("???????????????");
        }
        return ResultUtil.success("???????????????");
    }

    @Override
    @Transactional
    public Result<Object> drawSample(String daId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String userId = loginOutputVo.getUserId();
        if(StrUtil.isBlank(daId)) {
            return ResultUtil.validateError("????????????ID???????????????");
        }
        List<Long> drawApplyIds = Arrays.asList(daId.split(","))
                .stream().map(id -> Long.parseLong(id)).collect(Collectors.toList());
        LocalDateTime now = LocalDateTime.now();
        for(Long drawApplyId : drawApplyIds) {
            SampleDrawApply sda = sampleDrawApplyMapper.selectOne(Wrappers.<SampleDrawApply>lambdaQuery()
                    .eq(SampleDrawApply::getId, drawApplyId));
            if(!sda.getApprovalStatus().equals("0")) {
                continue;
            }
            //??????????????????????????????
            int cnt = sampleDrawApplyMapper.update(null, Wrappers.<SampleDrawApply>lambdaUpdate()
                    .set(SampleDrawApply::getApprovalStatus, "1")
                    .set(SampleDrawApply::getApprovalUserId, userId)
                    .set(SampleDrawApply::getUpdateTime, now)
                    .eq(SampleDrawApply::getId, drawApplyId));
            if(cnt > 0) {
                //???????????????????????????
                SampleItem sii = sampleItemInfoMapper.selectById(sda.getSampleItemId());
                if(sii.getAvalDrawCount() - sii.getDrawCount() == 1) {
                    //?????????
                    sampleItemInfoMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                            .set(SampleItem::getSampleStatus, "8")
                            .set(SampleItem::getDrawCount, sii.getDrawCount() + 1)
                            .set(SampleItem::getUpdateTime, now)
                            .eq(SampleItem::getId, sda.getSampleItemId()));
                }else{
                    sampleItemInfoMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                            .set(SampleItem::getSampleStatus, "7")
                            .set(SampleItem::getDrawCount, sii.getDrawCount() + 1)
                            .set(SampleItem::getUpdateTime, now)
                            .eq(SampleItem::getId, sda.getSampleItemId()));
                }
                //????????????????????????
                checkFactorInfoMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                        .set(CheckFactorInfo::getUpdateTime, now)
                        .set(CheckFactorInfo::getCheckStatus, "2")
                        .eq(CheckFactorInfo::getCheckStatus, "1")
                        .eq(CheckFactorInfo::getSampItemId, sda.getSampleItemId())
                        .eq(CheckFactorInfo::getAssignUserId, sda.getApplyUserId()));
            }else{
                throw new CustomException("????????????!");
            }
        }
        return ResultUtil.success("???????????????");
    }
}
