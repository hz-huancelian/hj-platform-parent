package org.hj.chain.platform.sample.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.CodeBuildUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.check.entity.CheckFactorInfo;
import org.hj.chain.platform.check.entity.CheckTask;
import org.hj.chain.platform.check.mapper.CheckFactorInfoMapper;
import org.hj.chain.platform.check.mapper.CheckTaskMapper;
import org.hj.chain.platform.common.*;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.model.DynamicParamConf;
import org.hj.chain.platform.model.SamplingBasis;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.offer.entity.OfferPlan;
import org.hj.chain.platform.offer.mapper.OfferPlanMapper;
import org.hj.chain.platform.offer.service.IOfferPlanService;
import org.hj.chain.platform.report.mapper.ReportInfoMapper;
import org.hj.chain.platform.report.model.ReportInfo;
import org.hj.chain.platform.sample.entity.SampleItem;
import org.hj.chain.platform.sample.entity.SampleItemAuditRecord;
import org.hj.chain.platform.sample.entity.SampleItemData;
import org.hj.chain.platform.sample.entity.SampleTask;
import org.hj.chain.platform.sample.mapper.SampleItemDataMapper;
import org.hj.chain.platform.sample.mapper.SampleItemFactorDataMapper;
import org.hj.chain.platform.sample.mapper.SampleItemMapper;
import org.hj.chain.platform.sample.mapper.SampleTaskMapper;
import org.hj.chain.platform.sample.service.ISampleItemAuditRecordService;
import org.hj.chain.platform.sample.service.ISampleItemService;
import org.hj.chain.platform.schedule.entity.ScheduleJob;
import org.hj.chain.platform.schedule.entity.ScheduleJobPlan;
import org.hj.chain.platform.schedule.mapper.ScheduleJobMapper;
import org.hj.chain.platform.schedule.mapper.ScheduleJobPlanFactorMapper;
import org.hj.chain.platform.schedule.mapper.ScheduleJobPlanMapper;
import org.hj.chain.platform.schedule.mapper.ScheduleTaskMapper;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.sample.SampleItemAuditTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserVo;
import org.hj.chain.platform.vo.record.Pollutant;
import org.hj.chain.platform.vo.record.RecordRenderData;
import org.hj.chain.platform.vo.record.RecordRenderItemData;
import org.hj.chain.platform.vo.sample.*;
import org.hj.chain.platform.vo.samplebak.SampleCheckItemParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Slf4j
@Service
public class SampleItemServiceImpl extends ServiceImpl<SampleItemMapper, SampleItem> implements ISampleItemService {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISampleItemAuditRecordService sampleItemAuditRecordService;
    @Autowired
    private CheckFactorInfoMapper checkFactorInfoMapper;
    @Autowired
    private SampleTaskMapper sampleTaskMapper;
    @Autowired
    private CheckTaskMapper checkTaskMapper;
    @Autowired
    private ScheduleJobMapper scheduleJobMapper;
    @Autowired
    private ScheduleJobPlanMapper jobPlanMapper;
    @Autowired
    private OfferPlanMapper offerPlanMapper;
    @Autowired
    private IOfferPlanService offerPlanService;
    @Autowired
    private ReportInfoMapper reportInfoMapper;
    @Autowired
    private SampleItemDataMapper sampleItemDataMapper;
    @Autowired
    private SampleItemFactorDataMapper sampleItemFactorDataMapper;
    @Autowired
    private ScheduleJobPlanFactorMapper scheduleJobPlanFactorMapper;


    @Override
    public IPage<SampleTaskListVo> findSampleTaskForLeaderByCondition(PageVo pageVo, SampleTaskSearchVo sv) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String userId = loginOutputVo.getUserId();
        Page<SampleTaskListVo> page = PageUtil.initMpPage(pageVo);
        this.baseMapper.findSampleTaskForLeaderByCondition(page, userId, sv);
        if (page.getRecords() != null && !page.getRecords().isEmpty()) {
            List<String> sampStauts = Arrays.asList("4", "6", "7", "8", "9");
            page.getRecords().forEach(item -> {
                Long sampTaskId = item.getSampTaskId();
                List<SampleItem> sampleItemInfos = this.baseMapper.selectList(Wrappers.<SampleItem>lambdaQuery()
                        .select(SampleItem::getSampleUserId, SampleItem::getSampleStatus)
                        .eq(SampleItem::getSampleTaskId, sampTaskId));
                item.setSampCount(sampleItemInfos.size());
                Integer completeSampCount = sampleItemInfos.stream().filter(s -> sampStauts.contains(s.getSampleStatus())).collect(Collectors.toList()).size();
                item.setCompleteSampCount(completeSampCount);
                List<String> sampleUserIds = sampleItemInfos.stream().map(s -> s.getSampleUserId()).distinct().collect(Collectors.toList());
                List<String> sampLeader = sampleUserIds.stream().map(s -> {
                    UserVo userVo = sysUserService.findUserByUserId(s);
                    return userVo.getEmpName();
                }).collect(Collectors.toList());
                item.setSampLeader(sampLeader);
            });
        }
        return page;
    }

    @Override
    public IPage<SampleItemInfoVo> findAuditSampItemForManageByCondition(PageVo pageVo, SampleItemInfoSearchVo sv) {
        Page<SampleItemInfoVo> page = PageUtil.initMpPage(pageVo);
        this.baseMapper.findAuditSampItemForManageByCondition(page, sv);
        if (page.getRecords() != null && !page.getRecords().isEmpty()) {
            page.getRecords().forEach(item -> {
                if (StrUtil.isNotBlank(item.getSampleUserId())) {
                    SysUser su = sysUserService.getById(item.getSampleUserId());
                    item.setSampleUser(su.getEmpName());
                }
                if (StrUtil.isNotBlank(item.getCollectUserId())) {
                    SysUser su = sysUserService.getById(item.getCollectUserId());
                    item.setCollectUser(su.getEmpName());
                }
                if (StrUtil.isNotBlank(item.getReviewUserId())) {
                    SysUser su = sysUserService.getById(item.getReviewUserId());
                    item.setReviewUser(su.getEmpName());
                }
            });
        }
        return page;
    }

    @Override
    public IPage<SampleItemInfoVo> findAuditSampItemForLeaderByCondition(PageVo pageVo, SampleItemInfoSearchVo sv) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String userId = loginOutputVo.getUserId();
        Page<SampleItemInfoVo> page = PageUtil.initMpPage(pageVo);
        this.baseMapper.findAuditSampItemForLeaderByCondition(page, userId, sv);
        if (page.getRecords() != null && !page.getRecords().isEmpty()) {
            page.getRecords().forEach(item -> {
                if (StrUtil.isNotBlank(item.getSampleUserId())) {
                    SysUser su = sysUserService.getById(item.getSampleUserId());
                    item.setSampleUser(su.getEmpName());
                }
                if (StrUtil.isNotBlank(item.getCollectUserId())) {
                    SysUser su = sysUserService.getById(item.getCollectUserId());
                    item.setCollectUser(su.getEmpName());
                }
                if (StrUtil.isNotBlank(item.getReviewUserId())) {
                    SysUser su = sysUserService.getById(item.getReviewUserId());
                    item.setReviewUser(su.getEmpName());
                }
            });
        }
        return page;
    }

    @Override
    @Transactional
    public Result<Object> doAuditSampItemForLeader(SampleItemAuditTdo tdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LocalDateTime now = LocalDateTime.now();
        SampleItem si = this.getById(Long.parseLong(tdo.getSampItemId()));
        if (si == null) {
            return ResultUtil.busiError("样品信息不存在！");
        }
        if (!si.getSampleStatus().equals("2")) {
            return ResultUtil.busiError("操作失败，样品当前状态不是待组长确认！");
        }
        if (!si.getSampleUserId().equals(loginOutputVo.getUserId())) {
            return ResultUtil.busiError("操作失败，非样品归属人！");
        }
        //保存审核记录
        SampleItemAuditRecord siar = new SampleItemAuditRecord();
        siar.setAuditReason(tdo.getAuditReason()).setAuditStatus(tdo.getAuditFlag())
                .setSampleItemId(si.getId()).setAuditTime(now).setAuditUserId(loginOutputVo.getUserId())
                .setAuditType("1");
        String sampStatus = "3";
        if (tdo.getAuditFlag().equals("1")) {
            this.update(Wrappers.<SampleItem>lambdaUpdate()
                    .set(SampleItem::getUpdateTime, now)
                    .set(SampleItem::getSampleStatus, sampStatus)
                    .set(SampleItem::getAuditTime, now)
                    .eq(SampleItem::getId, tdo.getSampItemId()));
            //保存审批记录
            sampleItemAuditRecordService.save(siar);
            return ResultUtil.success("审批通过成功！");
        } else if (tdo.getAuditFlag().equals("2")) {
            //驳回
            sampStatus = "5";
            this.update(Wrappers.<SampleItem>lambdaUpdate()
                    .set(SampleItem::getUpdateTime, now)
                    .set(SampleItem::getSampleStatus, sampStatus)
                    .set(StrUtil.isNotBlank(tdo.getAuditReason()), SampleItem::getAuditReason, tdo.getAuditReason())
                    .set(SampleItem::getAuditTime, now)
                    .eq(SampleItem::getId, tdo.getSampItemId()));
            //保存审批记录
            sampleItemAuditRecordService.save(siar);
            return ResultUtil.success("审批驳回成功！");
        }

        return ResultUtil.success("操作失败！");
    }

    @Override
    @Transactional
    public Result<Object> batchDoAuditSampItemForLeader(SampleItemAuditTdo tdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LocalDateTime now = LocalDateTime.now();
        String sampItemId = tdo.getSampItemId();
        String[] sampItemIds = sampItemId.split(",");
        String sampStatus = "3";
        if (tdo.getAuditFlag().equals("1")) {
            for (String id : sampItemIds) {
                SampleItem sii = this.getById(Long.parseLong(id));
                if (sii == null) {
                    throw new CustomException(String.format("样品 %s 信息不存在！", id));
                }
                if (!sii.getSampleStatus().equals("2")) {
                    throw new CustomException("操作失败，样品当前状态非待组长确认！");
                }
                if (!sii.getSampleUserId().equals(loginOutputVo.getUserId())) {
                    throw new CustomException("操作失败，非样品归属人！");
                }
                //保存审核记录
                SampleItemAuditRecord siar = new SampleItemAuditRecord();
                siar.setAuditReason(tdo.getAuditReason()).setAuditStatus(tdo.getAuditFlag())
                        .setSampleItemId(sii.getId()).setAuditTime(now).setAuditUserId(loginOutputVo.getUserId())
                        .setAuditType("1");
                int cnt = this.baseMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                        .set(SampleItem::getUpdateTime, now)
                        .set(SampleItem::getSampleStatus, sampStatus)
                        .set(SampleItem::getAuditTime, now)
                        .eq(SampleItem::getId, id));
                if (cnt > 0) {
                    sampleItemAuditRecordService.save(siar);
                } else {
                    throw new CustomException(String.format("样品 %s 审批操作失败！", id));
                }
            }
            return ResultUtil.success("样品批量审核通过成功！");
        } else if (tdo.getAuditFlag().equals("2")) {
            sampStatus = "5";
            for (String id : sampItemIds) {
                SampleItem sii = this.getById(Long.parseLong(id));
                if (sii == null) {
                    throw new CustomException(String.format("样品 %s 信息不存在！", id));
                }
                //保存审核记录
                SampleItemAuditRecord siar = new SampleItemAuditRecord();
                siar.setAuditReason(tdo.getAuditReason()).setAuditStatus(tdo.getAuditFlag())
                        .setSampleItemId(sii.getId()).setAuditTime(now).setAuditUserId(loginOutputVo.getUserId());
                int cnt = this.baseMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                        .set(SampleItem::getUpdateTime, now)
                        .set(SampleItem::getSampleStatus, sampStatus)
                        .set(StrUtil.isNotBlank(tdo.getAuditReason()), SampleItem::getAuditReason, tdo.getAuditReason())
                        .set(SampleItem::getAuditTime, now)
                        .eq(SampleItem::getId, id));
                if (cnt > 0) {
                    sampleItemAuditRecordService.save(siar);
                } else {
                    throw new CustomException(String.format("样品 %s 审批操作失败！", id));
                }
            }
            return ResultUtil.success("样品批量审核驳回成功！");
        }

        return ResultUtil.success("样品批量审核操作失败！");
    }

    @Override
    @Transactional
    public Result<Object> doAuditSampItemForManager(SampleItemAuditTdo tdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LocalDateTime now = LocalDateTime.now();
        SampleItem sii = this.getById(Long.parseLong(tdo.getSampItemId()));
        if (sii == null) {
            return ResultUtil.busiError("样品信息不存在！");
        }
        if (!sii.getSampleStatus().equals("3")) {
            return ResultUtil.busiError("操作失败，样品当前状态非待负责人审核！");
        }
        //保存审核记录
        SampleItemAuditRecord siar = new SampleItemAuditRecord();
        siar.setAuditReason(tdo.getAuditReason()).setAuditStatus(tdo.getAuditFlag())
                .setSampleItemId(sii.getId()).setAuditTime(now).setAuditUserId(loginOutputVo.getUserId())
                .setAuditType("2");
        String sampStatus = "3";
        if (tdo.getAuditFlag().equals("1")) {
            //采样负责人
            if (sii.getFbFlag().equals("0")) {
                sampStatus = "4";
            } else if (sii.getFbFlag().equals("1")) {
                sampStatus = "8";
            }
            this.update(Wrappers.<SampleItem>lambdaUpdate()
                    .set(SampleItem::getUpdateTime, now)
                    .set(SampleItem::getSampleStatus, sampStatus)
                    .set(SampleItem::getAuditTime, now)
                    .eq(SampleItem::getId, sii.getId()));
            if (sii.getFbFlag().equals("1")) {
                //外包样品审批通过后，对应检测列表更新状态为"待检测"
                checkFactorInfoMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                        .set(CheckFactorInfo::getCheckStatus, "2")
                        .eq(CheckFactorInfo::getSampleItemId, sii.getId()));
            } else {
                //自检样品
                if (sii.getStoreFlag().equals("0")) {
                    //不需要入库的样品,检测因子数据录入在采样阶段，状态为”审核通过“
                    checkFactorInfoMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                            .set(CheckFactorInfo::getCheckStatus, "5")
                            .eq(CheckFactorInfo::getSampleItemId, sii.getId())
                            .eq(CheckFactorInfo::getCheckStatus, "0")
                            .eq(CheckFactorInfo::getDataEntryStep, "1"));
                } else {
                    //更新检测列表状态为“已录入”（数据录入环节在采样阶段）
                    checkFactorInfoMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                            .set(CheckFactorInfo::getCheckStatus, "3")
                            .eq(CheckFactorInfo::getSampleItemId, sii.getId())
                            .eq(CheckFactorInfo::getCheckStatus, "0")
                            .eq(CheckFactorInfo::getDataEntryStep, "1"));
                }
            }
            //保存审批记录
            sampleItemAuditRecordService.save(siar);
            //检查更新采样任务状态
            List<String> sampleStatus = Arrays.asList("0", "1", "2", "3", "5", "10");
            List<SampleItem> siis = this.list(Wrappers.<SampleItem>lambdaQuery()
                    .eq(SampleItem::getSampleTaskId, sii.getSampleTaskId())
                    .in(SampleItem::getSampleStatus, sampleStatus));
            if (siis == null || siis.isEmpty()) {
                sampleTaskMapper.update(null, Wrappers.<SampleTask>lambdaUpdate()
                        .set(SampleTask::getFinishTime, now)
                        .set(SampleTask::getTaskStatus, "3")
                        .set(SampleTask::getUpdateTime, now)
                        .eq(SampleTask::getId, sii.getSampleTaskId()));
                //检查更新检测任务状态
                SampleTask sti = sampleTaskMapper.selectById(sii.getSampleTaskId());
                CheckTask cti = checkTaskMapper.selectOne(Wrappers.<CheckTask>lambdaQuery()
                        .eq(CheckTask::getJobId, sti.getJobId()));
                if (cti != null && cti.getTaskStatus().equals("1")) {
                    List<CheckFactorInfo> list = checkFactorInfoMapper
                            .selectList(Wrappers.<CheckFactorInfo>lambdaQuery()
                                    .eq(CheckFactorInfo::getCheckTaskId, cti.getId())
                                    .ne(CheckFactorInfo::getCheckStatus, "5"));
                    if (list == null || list.isEmpty()) {
                        checkTaskMapper.update(null, Wrappers.<CheckTask>lambdaUpdate()
                                .set(CheckTask::getTaskStatus, "2")
                                .set(CheckTask::getUpdateTime, now)
                                .set(CheckTask::getFinshTime, now)
                                .eq(CheckTask::getId, cti.getId()));

                        //修改调度任务进度
                        ScheduleJob job = scheduleJobMapper.selectById(cti.getJobId());
                        if (job != null) {
                            scheduleJobMapper.update(null, Wrappers.<ScheduleJob>lambdaUpdate()
                                    .set(ScheduleJob::getJobStatus, "2")
                                    .set(ScheduleJob::getUpdateTime, now)
                                    .eq(ScheduleJob::getId, cti.getJobId()));
                            List<ScheduleJobPlan> jobPlans = jobPlanMapper.selectList(Wrappers.<ScheduleJobPlan>lambdaQuery()
                                    .eq(ScheduleJobPlan::getJobId, cti.getJobId()));
                            List<Long> offerPlanIds = jobPlans.stream().map(ScheduleJobPlan::getOfferPlanId).collect(Collectors.toList());
                            List<OfferPlan> offerPlans = offerPlanMapper.selectBatchIds(offerPlanIds);
                            for (OfferPlan offerPlan : offerPlans) {
                                offerPlan.setFinishTimes(offerPlan.getFinishTimes() + 1)
                                        .setUpdateTime(now);
                            }
                            offerPlanService.updateBatchById(offerPlans);
                            //创建报告记录
                            ReportInfo info = new ReportInfo();
                            Integer sort = reportInfoMapper.selectCount(Wrappers.<ReportInfo>lambdaQuery().eq(ReportInfo::getOrganId, loginOutputVo.getOrganId())
                                    .apply(" STR_TO_DATE(create_time,'%Y-%m-%d')={0}", now.toLocalDate().toString()));
                            info.setOrganId(loginOutputVo.getOrganId())
                                    .setJobId(cti.getJobId())
                                    .setReportStatus("1")
                                    .setCreateTime(now)
                                    .setReportCode(CodeBuildUtil.genReportCode(loginOutputVo.getOrganId(), sort + 1));
                            reportInfoMapper.insert(info);
                        }
                    }
                }
            }
            return ResultUtil.success("审批通过成功！");
        } else if (tdo.getAuditFlag().equals("2")) {
            //驳回
            sampStatus = "5";
            this.update(Wrappers.<SampleItem>lambdaUpdate()
                    .set(SampleItem::getUpdateTime, now)
                    .set(SampleItem::getSampleStatus, sampStatus)
                    .set(StrUtil.isNotBlank(tdo.getAuditReason()), SampleItem::getAuditReason, tdo.getAuditReason())
                    .set(SampleItem::getAuditTime, now)
                    .eq(SampleItem::getId, sii.getId()));
            //保存审批记录
            sampleItemAuditRecordService.save(siar);
            return ResultUtil.success("审批驳回成功！");
        }
        return ResultUtil.busiError("审批操作失败！");
    }

    @Override
    @Transactional
    public Result<Object> batchDoAuditSampItemForManager(SampleItemAuditTdo tdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LocalDateTime now = LocalDateTime.now();
        String[] sampItemIds = tdo.getSampItemId().split(",");
        String sampStatus = "3";
        if (tdo.getAuditFlag().equals("1")) {
            for (String sampItemId : sampItemIds) {
                SampleItem sii = this.getById(Long.parseLong(sampItemId));
                if (sii == null) {
                    throw new CustomException(String.format("样品 %s 信息不存在！", sii.getSampleNo()));
                }
                if (!sii.getSampleStatus().equals("3")) {
                    throw new CustomException("操作失败，样品当前状态非待负责人审核！");
                }
                //保存审核记录
                SampleItemAuditRecord siar = new SampleItemAuditRecord();
                siar.setAuditReason(tdo.getAuditReason()).setAuditStatus(tdo.getAuditFlag())
                        .setSampleItemId(sii.getId()).setAuditTime(now).setAuditUserId(loginOutputVo.getUserId())
                        .setAuditType("2");
                if (sii.getFbFlag().equals("0")) {
                    sampStatus = "4";
                } else if (sii.getFbFlag().equals("1")) {
                    sampStatus = "8";
                }
                int cnt = this.baseMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                        .set(SampleItem::getUpdateTime, now)
                        .set(SampleItem::getSampleStatus, sampStatus)
                        .set(SampleItem::getAuditTime, now)
                        .eq(SampleItem::getId, sii.getId()));
                if (cnt > 0) {
                    if (sii.getFbFlag().equals("1")) {
                        //外包样品审批通过后，对应检测列表更新状态为"待检测"
                        checkFactorInfoMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                                .set(CheckFactorInfo::getCheckStatus, "2")
                                .eq(CheckFactorInfo::getSampleItemId, sii.getId()));
                    } else {
                        //自检样品
                        if (sii.getStoreFlag().equals("0")) {
                            //不需要入库的样品,检测因子数据录入在采样阶段，状态为”审核通过“
                            checkFactorInfoMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                                    .set(CheckFactorInfo::getCheckStatus, "5")
                                    .eq(CheckFactorInfo::getSampleItemId, sii.getId())
                                    .eq(CheckFactorInfo::getCheckStatus, "0")
                                    .eq(CheckFactorInfo::getDataEntryStep, "1"));
                        } else {
                            //更新检测列表状态为“已录入”（数据录入环节在采样阶段）
                            checkFactorInfoMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                                    .set(CheckFactorInfo::getCheckStatus, "3")
                                    .eq(CheckFactorInfo::getSampleItemId, sii.getId())
                                    .eq(CheckFactorInfo::getCheckStatus, "0")
                                    .eq(CheckFactorInfo::getDataEntryStep, "1"));
                        }
                    }
                    //保存审批记录
                    sampleItemAuditRecordService.save(siar);
                    //检查更新采样任务状态
                    List<String> sampleStatus = Arrays.asList("0", "1", "2", "3", "5","10");
                    List<SampleItem> siis = this.list(Wrappers.<SampleItem>lambdaQuery()
                            .eq(SampleItem::getSampleTaskId, sii.getSampleTaskId())
                            .in(SampleItem::getSampleStatus, sampleStatus));
                    if (siis == null || siis.isEmpty()) {
                        sampleTaskMapper.update(null, Wrappers.<SampleTask>lambdaUpdate()
                                .set(SampleTask::getFinishTime, now)
                                .set(SampleTask::getTaskStatus, "3")
                                .set(SampleTask::getUpdateTime, now)
                                .eq(SampleTask::getId, sii.getSampleTaskId()));
                        //检查更新检测任务状态
                        SampleTask sti = sampleTaskMapper.selectById(sii.getSampleTaskId());
                        CheckTask cti = checkTaskMapper.selectOne(Wrappers.<CheckTask>lambdaQuery()
                                .eq(CheckTask::getJobId, sti.getJobId()));
                        if (cti != null && cti.getTaskStatus().equals("1")) {
                            List<CheckFactorInfo> list = checkFactorInfoMapper
                                    .selectList(Wrappers.<CheckFactorInfo>lambdaQuery()
                                            .eq(CheckFactorInfo::getCheckTaskId, cti.getId())
                                            .ne(CheckFactorInfo::getCheckStatus, "5"));
                            if (list == null || list.isEmpty()) {
                                checkTaskMapper.update(null, Wrappers.<CheckTask>lambdaUpdate()
                                        .set(CheckTask::getTaskStatus, "2")
                                        .set(CheckTask::getUpdateTime, now)
                                        .set(CheckTask::getFinshTime, now)
                                        .eq(CheckTask::getId, cti.getId()));

                                //修改调度任务进度
                                ScheduleJob job = scheduleJobMapper.selectById(cti.getJobId());
                                if (job != null) {
                                    scheduleJobMapper.update(null, Wrappers.<ScheduleJob>lambdaUpdate()
                                            .set(ScheduleJob::getJobStatus, "2")
                                            .set(ScheduleJob::getUpdateTime, now)
                                            .eq(ScheduleJob::getId, cti.getJobId()));
                                    List<ScheduleJobPlan> jobPlans = jobPlanMapper.selectList(Wrappers.<ScheduleJobPlan>lambdaQuery()
                                            .eq(ScheduleJobPlan::getJobId, cti.getJobId()));
                                    List<Long> offerPlanIds = jobPlans.stream().map(ScheduleJobPlan::getOfferPlanId).collect(Collectors.toList());
                                    List<OfferPlan> offerPlans = offerPlanMapper.selectBatchIds(offerPlanIds);
                                    for (OfferPlan offerPlan : offerPlans) {
                                        offerPlan.setFinishTimes(offerPlan.getFinishTimes() + 1)
                                                .setUpdateTime(now);
                                    }
                                    offerPlanService.updateBatchById(offerPlans);
                                    //创建报告记录
                                    ReportInfo info = new ReportInfo();
                                    Integer sort = reportInfoMapper.selectCount(Wrappers.<ReportInfo>lambdaQuery().eq(ReportInfo::getOrganId, loginOutputVo.getOrganId())
                                            .apply(" STR_TO_DATE(create_time,'%Y-%m-%d')={0}", now.toLocalDate().toString()));
                                    info.setOrganId(loginOutputVo.getOrganId())
                                            .setJobId(cti.getJobId())
                                            .setReportStatus("1")
                                            .setCreateTime(now)
                                            .setReportCode(CodeBuildUtil.genReportCode(loginOutputVo.getOrganId(), sort + 1));
                                    reportInfoMapper.insert(info);
                                }
                            }
                        }
                    }
                } else {
                    throw new CustomException(String.format("样品 %s 审批操作失败！", sampItemId));
                }
            }
            return ResultUtil.success("样品批量审批通过成功！");
        } else if (tdo.getAuditFlag().equals("2")) {
            sampStatus = "5";
            for (String sampItemId : sampItemIds) {
                SampleItem sii = this.getById(Long.parseLong(sampItemId));
                if (sii == null) {
                    throw new CustomException(String.format("样品 %s 信息不存在！", sii.getSampleNo()));
                }
                //保存审核记录
                SampleItemAuditRecord siar = new SampleItemAuditRecord();
                siar.setAuditReason(tdo.getAuditReason()).setAuditStatus(tdo.getAuditFlag())
                        .setSampleItemId(sii.getId()).setAuditTime(now).setAuditUserId(loginOutputVo.getUserId())
                        .setAuditType("2");
                int cnt = this.baseMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                        .set(SampleItem::getUpdateTime, now)
                        .set(SampleItem::getSampleStatus, sampStatus)
                        .set(StrUtil.isNotBlank(tdo.getAuditReason()), SampleItem::getAuditReason, tdo.getAuditReason())
                        .set(SampleItem::getAuditTime, now)
                        .eq(SampleItem::getId, sii.getId()));
                if (cnt > 0) {
                    sampleItemAuditRecordService.save(siar);
                } else {
                    throw new CustomException(String.format("样品 %s 审批操作失败！", sampItemId));
                }
            }
            return ResultUtil.success("样品批量驳回成功！");
        }
        return ResultUtil.busiError("样品批量审核操作失败！");
    }

    @Override
    public IPage<SampleVo> getSamplesBySampTaskId(PageVo pageVo, SampleSearchVo sv) {
        Page<SampleVo> page = PageUtil.initMpPage(pageVo);
        this.baseMapper.getSamplesBySampTaskId(page, sv);
        if (page != null && !page.getRecords().isEmpty()) {
            page.getRecords().forEach(p -> {
                SysUser sysUser = sysUserService.getById(p.getReviewUserId());
                p.setReviewUser(sysUser.getEmpName());
            });
        }
        return page;
    }

    @Override
    public Result<SampleItemVo> getSampleDataBySampItemId(Long sampItemId) {
        if (sampItemId == null) {
            return ResultUtil.validateError("样品ID不能为空！");
        }
        SampleItemData sampleItemData = sampleItemDataMapper.selectOne(Wrappers.<SampleItemData>lambdaQuery()
                .eq(SampleItemData::getSampleItemId, sampItemId));
        SampleItemDetailVo vo = sampleItemDataMapper.getSampleDetailBySampleItemId(sampItemId);
        SampleItemVo sampleItemVo = copyProperties(vo);
        if (sampleItemData != null) {
            List<String> imageList = new ArrayList<>();
            if (StrUtil.isNotBlank(sampleItemData.getCollectIamge1Id())) {
                imageList.add(sampleItemData.getCollectIamge1Id());
            }
            if (StrUtil.isNotBlank(sampleItemData.getCollectIamge2Id())) {
                imageList.add(sampleItemData.getCollectIamge2Id());
            }
            if (StrUtil.isNotBlank(sampleItemData.getCollectIamge3Id())) {
                imageList.add(sampleItemData.getCollectIamge3Id());
            }
            if (StrUtil.isNotBlank(sampleItemData.getCollectIamge4Id())) {
                imageList.add(sampleItemData.getCollectIamge4Id());
            }
            if (StrUtil.isNotBlank(vo.getCollectLeaderId())) {
                SysUser user = sysUserService.getById(vo.getCollectLeaderId());
                sampleItemVo.setCollectLeader(user.getEmpName());
            }
            if (StrUtil.isNotBlank(vo.getCollectUserId())) {
                SysUser user = sysUserService.getById(vo.getCollectUserId());
                sampleItemVo.getSampleDataVo().setCollectUser(user.getEmpName());
            }
            if (StrUtil.isNotBlank(vo.getReviewUserId())) {
                SysUser user = sysUserService.getById(vo.getReviewUserId());
                sampleItemVo.getSampleDataVo().setReviewUser(user.getEmpName());
            }
            sampleItemVo.getSampleDataVo().setImageList(imageList);
        }

        JSONArray sampleData = JSON.parseArray(vo.getSampleData());
        sampleItemVo.getSampleDataVo().setSampleData(sampleData);

        List<SampleFactorDataVo> factorDataVos = sampleItemFactorDataMapper.getSampleFactorDataBySampItemId(vo.getSampItemId());
        String factorName = factorDataVos.stream().map(SampleFactorDataVo::getFactorName).collect(Collectors.joining("/"));
        sampleItemVo.setFactorName(factorName);
        sampleItemVo.setFactorDataVos(factorDataVos);
        return ResultUtil.data(sampleItemVo);
    }

    private SampleItemVo copyProperties(SampleItemDetailVo vo) {
        SampleItemVo sampleItemVo = new SampleItemVo();
        sampleItemVo.setSampItemId(vo.getSampItemId())
                .setSampleNo(vo.getSampleNo())
                .setAuditReason(vo.getAuditReason())
                .setFactorPoint(vo.getFactorPoint())
                .setCollectLeaderId(vo.getCollectLeaderId())
                .setSampleStatus(vo.getSampleStatus())
                .setSecdClassId(vo.getSecdClassId())
                .setSecdClassName(vo.getSecdClassName());
        SampleJobVo sampleJobVo = new SampleJobVo();
        BeanUtils.copyProperties(vo, sampleJobVo);
        sampleItemVo.setSampleJobVo(sampleJobVo);
        SampleItemDataVo sampleDataVo = new SampleItemDataVo();
        sampleDataVo.setCollectDate(vo.getCollectDate())
                .setCollectRemark(vo.getCollectRemark())
                .setCollectUserId(vo.getCollectUserId())
                .setCollectLocation(vo.getCollectLocation())
                .setReviewUserId(vo.getReviewUserId())
                .setSpecialNote(vo.getSpecialNote());
        if (Constants.SPECIAL_NOTE_CLASS.contains(vo.getSecdClassId())) {
            sampleDataVo.setSpecialNoteFlag(true);
        } else {
            sampleDataVo.setSpecialNoteFlag(false);
        }
        sampleItemVo.setSampleDataVo(sampleDataVo);
        return sampleItemVo;
    }

    @Override
    public Result<SampleDetailVo> getSampleDetailBySampItemId(Long sampItemId) {
        SampleDetailVo vo = sampleItemDataMapper.getSampleDetailBySampItemId(sampItemId);
        List<SampleFactorDataVo> factorDataVos = sampleItemFactorDataMapper.getSampleFactorDataBySampItemId(sampItemId);
        String factorName = factorDataVos.stream().map(SampleFactorDataVo::getFactorName).collect(Collectors.joining("/"));
        vo.setFactorName(factorName);
        return ResultUtil.data(vo);
    }

    @Override
    public RecordRenderData findSampleRenderDataByTaskIdAndClassId(Long taskId, String secdClassId) {
        RecordRenderData data = new RecordRenderData();
        //根据任务单号查看下面关联的样品信息
        List<SampleItem> sampleItems = this.baseMapper.selectList(Wrappers.<SampleItem>lambdaQuery()
                .select(SampleItem::getSampleNo)
                .eq(SampleItem::getSampleTaskId, taskId)
                .eq(SampleItem::getSecdClassId, secdClassId));
        if (sampleItems != null && !sampleItems.isEmpty()) {
            SaSession session = StpUtil.getSession();
            LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
            List<String> sampleNos = sampleItems.stream().map(item -> item.getSampleNo()).collect(Collectors.toList());
            data = buildRecordRenderData(secdClassId, loginOutputVo.getOrganId(), sampleNos);
            //委托单位
            String condignorName = scheduleJobMapper.findCondignorNameBySampleTaskId(taskId);
            data.setConsignorName(condignorName);
            //审核人
            String reviewerEmp = getReviewerEmpBySampleTaskId(taskId);
            data.setReviewerEmp(reviewerEmp);
        }
        return data;
    }


    /**
     * TODO 根据采样任务ID获取审核人
     *
     * @param taskId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/8 5:01 下午
     */
    private String getReviewerEmpBySampleTaskId(Long taskId) {
        SampleTask dbTaskInfo = sampleTaskMapper.selectOne(Wrappers.<SampleTask>lambdaQuery()
                .select(SampleTask::getManagerUserId)
                .eq(SampleTask::getId, taskId));
        if (dbTaskInfo != null) {
            SysUser userVo = sysUserService.getById(dbTaskInfo.getManagerUserId());
            if (userVo != null) {
                return userVo.getEmpName();
            }
        }

        return null;
    }

    /**
     * TODO 构建采样记录数据
     *
     * @param secdClassId
     * @param sampleNos
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/8 4:42 下午
     */
    private RecordRenderData buildRecordRenderData(String secdClassId, String organId, List<String> sampleNos) {

        RecordRenderData data = new RecordRenderData();
        //根据样品ID查看样品采样数据
        List<SampleDataDetailParam> dataParams = sampleItemDataMapper.findSampleDataDetailsBySampleNos(sampleNos, organId);
        if (dataParams != null && !dataParams.isEmpty()) {
            String checkEmps = dataParams.stream().filter(item -> StrUtil.isNotBlank(item.getCollectUserId()))
                    .map(item -> item.getCollectUserId()).distinct().map(item -> {
                        SysUser userVo = sysUserService.getById(item);
                        if (userVo != null) {
                            return userVo.getEmpName();
                        }
                        return "";
                    }).filter(item -> StrUtil.isNotBlank(item)).findFirst().get();
            data.setCheckEmps(checkEmps);
            List<SampleDataDetailParam> paramList = dataParams.stream().filter(item -> StrUtil.isNotBlank(item.getReviewUserId())).collect(Collectors.toList());
            if (paramList != null && !paramList.isEmpty()) {
                String reviewUserId = paramList.get(0).getReviewUserId();
                SysUser userVo = sysUserService.getById(reviewUserId);
                if (userVo != null) {
                    data.setCheckEmps(data.getCheckEmps() + "、" + userVo.getEmpName());
                }
            }
            //因子数据
            Map<String, Map<String, SampleFactorDataParam>> factorDataMap = new HashMap<>();
            List<SampleFactorDataParam> factorDataParams = sampleItemFactorDataMapper.findFactorDataDetailsBySampleNos(sampleNos, organId);
            if (factorDataParams != null && !factorDataParams.isEmpty()) {
                factorDataMap = factorDataParams.stream()
                        .collect(Collectors.groupingBy(SampleFactorDataParam::getSampleNo, Collectors.toMap(SampleFactorDataParam::getCheckStandardId, Function.identity())));
            }
            Map<String, Map<String, SampleFactorDataParam>> finalFactorDataMap = factorDataMap;
            List<RecordRenderItemData> renderDataList = dataParams.stream().map(item -> {
                String sampleNo = item.getSampleNo();
                RecordRenderItemData renderData = new RecordRenderItemData();
                String groupKey = item.getGroupKey();
                String cgroupKey = groupKey.substring(0, groupKey.lastIndexOf("-"));
                renderData.setSampItemId(sampleNo)
                        .setCollectTime(item.getCollectTime())
                        .setFactorPoint(item.getFactorPoint())
                        .setCollectRemark(item.getCollectRemark())
                        .setSampleFixative(item.getSampleFixative())
                        .setSampleProperties(item.getSampleProperties())
                        .setCollectDate(item.getCollectDate())
                        .setGroupKey(cgroupKey);

                //属性数据
                String sampleData = item.getSampleData();
                Map<String, Object> sampleRecordData = BusiUtils.parseSampleDataJson(sampleData);
                //特殊处理
                //有组织废气
                if (secdClassId.equals("002002") || secdClassId.equals("0020022")) {
                    List<Pollutant> pollutants = BusiUtils.getSamplePollutantData(item.getPollutantInfo());
                    if (pollutants != null && !pollutants.isEmpty()) {
                        pollutants.stream().forEach(pitem -> pitem.setSampItemId(sampleNo));
                    }
                    renderData.setPollist(pollutants);
                }
                sampleRecordData.put("sampItemId", sampleNo);
                renderData.setDataMap(sampleRecordData);
                //因子：
                Map<String, SampleFactorDataParam> dataParamMap = finalFactorDataMap.get(sampleNo);
                renderData.setFactorMap(dataParamMap);

                return renderData;
            }).collect(Collectors.toList());

            //处理分析项目：
            if (!(secdClassId.equals("004001")
                    || secdClassId.equals("0020021"))) {
                List<SampleCheckItemParam> checkItems = scheduleJobPlanFactorMapper.findFactorStandardIdsBySampleNos(sampleNos, organId);
                if (checkItems != null && !checkItems.isEmpty()) {
                    Map<String, RecordRenderItemData> dataMap = renderDataList.stream().collect(Collectors.toMap(RecordRenderItemData::getSampItemId, Function.identity()));
                    checkItems.stream().forEach(item -> {
                        //样品ID
                        String sampleItemId = item.getSampleNo();
                        String factorNames = item.getFactorNames();

                        RecordRenderItemData renderItemData = dataMap.get(sampleItemId);
                        if (renderItemData != null) {
                            renderItemData.setFactorItems(factorNames);
                        }
                    });

                }
            }
            data.setItemDatas(renderDataList);
        }
        return data;
    }

    @Override
    public Map<String, RecordRenderData> findSampleRenderDataByTaskId(Long taskId) {
        Map<String, RecordRenderData> resMap = new HashMap<>();
        //根据任务单号查看下面关联的样品信息
        List<SampleItem> sampleItems = this.baseMapper.selectList(Wrappers.<SampleItem>lambdaQuery()
                .select(SampleItem::getSampleNo,
                        SampleItem::getSecdClassId)
                .eq(SampleItem::getSampleTaskId, taskId));
        if (sampleItems != null && !sampleItems.isEmpty()) {
            SaSession session = StpUtil.getSession();
            LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
            //委托单位
            String condignorName = scheduleJobMapper.findCondignorNameBySampleTaskId(taskId);
            //审核人
            String reviewerEmp = getReviewerEmpBySampleTaskId(taskId);
            Map<String, List<SampleItem>> secdClassIdSamples = sampleItems.stream().collect(Collectors.groupingBy(SampleItem::getSecdClassId));
            secdClassIdSamples.forEach((secdClassId, items) -> {
                List<String> sampleNos = items.stream().map(item -> item.getSampleNo()).collect(Collectors.toList());
                RecordRenderData data = buildRecordRenderData(secdClassId, loginOutputVo.getOrganId(), sampleNos);
                data.setConsignorName(condignorName);
                data.setReviewerEmp(reviewerEmp);
                resMap.put(secdClassId, data);
            });
        }
        return resMap;
    }

    @Override
    public IPage<SampleTaskListVo> findSampTaskForManagerByCondition(PageVo pageVo, SampleTaskSearchVo sv) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Page<SampleTaskListVo> page = PageUtil.initMpPage(pageVo);
        this.baseMapper.findSampTaskForManagerByCondition(page, organId, sv);
        if (page.getRecords() != null && !page.getRecords().isEmpty()) {
            List<String> sampStauts = Arrays.asList("4", "6", "7", "8", "9");
            page.getRecords().forEach(item -> {
                Long sampTaskId = item.getSampTaskId();
                List<SampleItem> sampleItemInfos = this.baseMapper.selectList(Wrappers.<SampleItem>lambdaQuery()
                        .select(SampleItem::getSampleUserId, SampleItem::getSampleStatus)
                        .eq(SampleItem::getSampleTaskId, sampTaskId));
                item.setSampCount(sampleItemInfos.size());
                Integer completeSampCount = sampleItemInfos.stream().filter(s -> sampStauts.contains(s.getSampleStatus())).collect(Collectors.toList()).size();
                item.setCompleteSampCount(completeSampCount);
            });
        }
        return page;
    }
}
