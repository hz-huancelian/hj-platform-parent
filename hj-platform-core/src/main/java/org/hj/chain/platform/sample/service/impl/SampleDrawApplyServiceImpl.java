package org.hj.chain.platform.sample.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.DateUtils;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.check.entity.CheckFactorInfo;
import org.hj.chain.platform.check.mapper.CheckFactorInfoMapper;
import org.hj.chain.platform.common.BusiUtils;
import org.hj.chain.platform.common.CustomException;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.sample.entity.SampleDrawApply;
import org.hj.chain.platform.sample.entity.SampleItem;
import org.hj.chain.platform.sample.mapper.*;
import org.hj.chain.platform.sample.service.ISampleDrawApplyService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserVo;
import org.hj.chain.platform.vo.sample.*;
import org.hj.chain.platform.vo.samplebak.HandoverParam;
import org.hj.chain.platform.word.HandoverItemData;
import org.hj.chain.platform.word.HandoverTableData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Service
public class SampleDrawApplyServiceImpl extends ServiceImpl<SampleDrawApplyMapper, SampleDrawApply> implements ISampleDrawApplyService {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private SampleItemMapper sampleItemMapper;
    @Autowired
    private CheckFactorInfoMapper checkFactorInfoMapper;
    @Autowired
    private SampleItemDataMapper sampleItemDataMapper;
    @Autowired
    private SampleItemFactorDataMapper sampleItemFactorDataMapper;
    @Autowired
    private SampleStoreListMapper sampleStoreListMapper;
    @Autowired
    private SampleStoreApplyMapper applyMapper;

    @Override
    public IPage<SampleVo> getSampDrawApplyList(PageVo pageVo, SampleSearchVo sv) {
        Page<SampleVo> page = PageUtil.initMpPage(pageVo);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        this.baseMapper.getSampDrawApplyList(page, organId, sv);
        if (page != null && !page.getRecords().isEmpty()) {
            page.getRecords().forEach(p -> {
                SysUser sysUser = sysUserService.getById(p.getReviewUserId());
                p.setReviewUser(sysUser.getEmpName());
            });
        }
        return page;
    }

    @Override
    @Transactional
    public Result<Object> doDrawSample(String daId) {
        if (StrUtil.isBlank(daId)) {
            return ResultUtil.validateError("出库申请ID不能为空！");
        }
        List<Long> drawApplyIds = Arrays.asList(daId.split(","))
                .stream().map(id -> Long.parseLong(id)).collect(Collectors.toList());
        String userId = (String) StpUtil.getLoginId();
        LocalDateTime now = LocalDateTime.now();
        for (Long drawApplyId : drawApplyIds) {
            SampleDrawApply sda = this.baseMapper.selectOne(Wrappers.<SampleDrawApply>lambdaQuery()
                    .eq(SampleDrawApply::getId, drawApplyId));
            if (!sda.getApprovalStatus().equals("0")) {
                continue;
            }
            //更新出库申请记录状态
            int cnt = this.baseMapper.update(null, Wrappers.<SampleDrawApply>lambdaUpdate()
                    .set(SampleDrawApply::getApprovalStatus, "1")
                    .set(SampleDrawApply::getApprovalUserId, userId)
                    .set(SampleDrawApply::getUpdateTime, now)
                    .eq(SampleDrawApply::getId, drawApplyId));
            if (cnt > 0) {
                //更新样品库出库次数
                SampleItem sii = sampleItemMapper.selectById(sda.getSampleItemId());
                if (sii.getAvalDrawCount() - sii.getDrawCount() == 1) {
                    //出库完
                    sampleItemMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                            .set(SampleItem::getSampleStatus, "8")
                            .set(SampleItem::getDrawCount, sii.getDrawCount() + 1)
                            .set(SampleItem::getUpdateTime, now)
                            .eq(SampleItem::getId, sda.getSampleItemId()));
                } else {
                    sampleItemMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                            .set(SampleItem::getSampleStatus, "7")
                            .set(SampleItem::getDrawCount, sii.getDrawCount() + 1)
                            .set(SampleItem::getUpdateTime, now)
                            .eq(SampleItem::getId, sda.getSampleItemId()));
                }
                //更新检测列表状态
                checkFactorInfoMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                        .set(CheckFactorInfo::getUpdateTime, now)
                        .set(CheckFactorInfo::getCheckStatus, "2")
                        .eq(CheckFactorInfo::getCheckStatus, "1")
                        .eq(CheckFactorInfo::getSampleItemId, sda.getSampleItemId())
                        .eq(CheckFactorInfo::getAssignUserId, sda.getApplyUserId()));
            } else {
                throw new CustomException("出库失败!");
            }
        }
        return ResultUtil.success("出库成功！");
    }

    @Override
    public HandoverTableData findHandoverTableDataBySampIds(List<String> sampIds) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        //样品编号排序
        HandoverTableData tableData = new HandoverTableData();
        //获取取样因子列表
        List<SampleItem> itemInfos = sampleItemMapper.selectList(Wrappers.<SampleItem>lambdaQuery()
                .select(SampleItem::getId,
                        SampleItem::getSampleNo,
                        SampleItem::getFactorPoint,
                        SampleItem::getDrawCount,
                        SampleItem::getAvalDrawCount)
                .in(SampleItem::getSampleNo, sampIds)
                .eq(SampleItem::getOrganId, organId));

        if (itemInfos != null && !itemInfos.isEmpty()) {
            Map<String, SampleItem> itemInfoMap = itemInfos.stream().collect(Collectors.toMap(SampleItem::getSampleNo, Function.identity()));
            List<SampleDataParam> sampleItemDates = sampleItemDataMapper.findSampleDatasBySampleNos(sampIds, organId);
            if (sampleItemDates != null && !sampleItemDates.isEmpty()) {
                SampleDataParam sd = sampleItemDates.get(0);
                tableData.setCollectDate(sd.getCollectDate());
                List<HandoverItemData> itemDataList = sampleItemDates
                        .stream().sorted(Comparator.comparing(SampleDataParam::getSampleNo)).map(item -> {
                            String sampleItemId = item.getSampleNo();
                            SampleItem sampleItem = itemInfoMap.get(sampleItemId);
                            HandoverItemData itemData = new HandoverItemData();
                            itemData.setSampItemId(sampleItemId)
                                    .setCollectLocation(sampleItem.getFactorPoint())
                                    .setSampleFixative(item.getSampleFixative())
                                    .setSampleProperties(item.getSampleProperties());
                            List<SampleFactorDataVo> datas = sampleItemFactorDataMapper.getSampleFactorDataBySampItemId(sampleItem.getId());
                            if (datas != null && !datas.isEmpty()) {
                                String factorNames = datas.stream().map(factorData -> factorData.getFactorName()).collect(Collectors.joining("、"));
                                itemData.setFactorNames(factorNames);
                            }

                            return itemData;
                        }).collect(Collectors.toList());
                tableData.setItemDataList(itemDataList);
                List<HandoverParam> hdps = sampleStoreListMapper.findHandoverParamBySampNos(sampIds, organId);
                if (hdps != null && !hdps.isEmpty()) {
                    HandoverParam hdp = hdps.get(0);
                    tableData.setConsignorName(hdp.getConsignorName())
                            .setStoreTime(hdp.getStoreTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

                    String users = hdps.stream().map(item -> item.getSendUser()).distinct().collect(Collectors.joining(","));
                    tableData.setSendUsers(users);
                }

                List<String> applyUsers = applyMapper.findApplyUsersBySampleNos(sampIds, organId);
                if (applyUsers != null && !applyUsers.isEmpty()) {
                    List<String> recieveUsers = applyUsers.stream().map(item -> {
                        UserVo user = sysUserService.findUserByUserId(item);
                        if (user != null) {
                            return user.getEmpName();
                        }
                        return null;
                    }).collect(Collectors.toList());
                    String users = recieveUsers.stream().distinct().collect(Collectors.joining(","));
                    tableData.setRecieveUsers(users);
                }


            }
        }
        return tableData;
    }
}
