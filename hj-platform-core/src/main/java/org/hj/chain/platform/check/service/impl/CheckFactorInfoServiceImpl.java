package org.hj.chain.platform.check.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hj.chain.platform.CodeBuildUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.check.entity.CheckFactorAuditRecord;
import org.hj.chain.platform.check.entity.CheckFactorInfo;
import org.hj.chain.platform.check.entity.CheckFactorSubset;
import org.hj.chain.platform.check.entity.CheckTask;
import org.hj.chain.platform.check.mapper.CheckFactorAuditRecordMapper;
import org.hj.chain.platform.check.mapper.CheckFactorInfoMapper;
import org.hj.chain.platform.check.mapper.CheckFactorSubsetMapper;
import org.hj.chain.platform.check.mapper.CheckTaskMapper;
import org.hj.chain.platform.check.service.ICheckFactorInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.check.service.ICheckFactorSubsetService;
import org.hj.chain.platform.common.CustomException;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.offer.entity.OfferPlan;
import org.hj.chain.platform.offer.mapper.OfferPlanMapper;
import org.hj.chain.platform.offer.service.IOfferPlanService;
import org.hj.chain.platform.report.mapper.ReportInfoMapper;
import org.hj.chain.platform.report.model.ReportInfo;
import org.hj.chain.platform.sample.entity.SampleDrawApply;
import org.hj.chain.platform.sample.entity.SampleItem;
import org.hj.chain.platform.sample.mapper.SampleDrawApplyMapper;
import org.hj.chain.platform.sample.mapper.SampleItemMapper;
import org.hj.chain.platform.schedule.entity.ScheduleJob;
import org.hj.chain.platform.schedule.entity.ScheduleJobPlan;
import org.hj.chain.platform.schedule.mapper.ScheduleJobMapper;
import org.hj.chain.platform.schedule.mapper.ScheduleJobPlanMapper;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.service.ISysDeptService;
import org.hj.chain.platform.service.ISysRoleService;
import org.hj.chain.platform.tdo.check.BatchCheckFactorAuditTdo;
import org.hj.chain.platform.tdo.check.CheckFactorAuditTdo;
import org.hj.chain.platform.tdo.check.CheckFactorInfoTdo;
import org.hj.chain.platform.vo.FactorMethodInfoVo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.check.CheckFactorInfoVo;
import org.hj.chain.platform.vo.check.CheckFactorSearchVo;
import org.hj.chain.platform.vo.check.CheckFactorSubsetVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class CheckFactorInfoServiceImpl extends ServiceImpl<CheckFactorInfoMapper, CheckFactorInfo> implements ICheckFactorInfoService {

    @Autowired
    private IFactorService factorService;
    @Autowired
    private CheckFactorAuditRecordMapper checkFactorAuditRecordMapper;
    @Autowired
    private SampleDrawApplyMapper sampleDrawApplyMapper;
    @Autowired
    private ReportInfoMapper reportInfoMapper;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ICheckFactorSubsetService checkFactorSubsetService;
    @Autowired
    private CheckFactorSubsetMapper checkFactorSubsetMapper;
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
    private SampleItemMapper sampleItemMapper;

    @Override
    @Transactional
    public Result<Object> saveCheckFactorData(CheckFactorInfoTdo tdo) {
        tdo.setRemark(StrUtil.trimToNull(tdo.getRemark()));
        tdo.setCheckEquipment(StrUtil.trimToNull(tdo.getCheckEquipment()));
        CheckFactorInfo cfi = this.baseMapper.selectById(tdo.getCheckFactorId());
        if (cfi == null) {
            return ResultUtil.busiError("该检测列表信息不存在！");
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        if(!cfi.getAssignUserId().equals(loginOutputVo.getUserId())) {
            return ResultUtil.busiError("身份不符，非检测列表指定检测员！");
        }
        List<String> allowStatus = Arrays.asList("2", "3", "6");
        if (!allowStatus.contains(cfi.getCheckStatus())) {
            return ResultUtil.busiError("未领样或提交审核的检测样品无法录入检测数据！");
        }
        this.update(Wrappers.<CheckFactorInfo>lambdaUpdate()
                .set(CheckFactorInfo::getCheckRes, tdo.getCheckRes())
                .set(CheckFactorInfo::getUpdateTime, LocalDateTime.now())
                .set(CheckFactorInfo::getCheckStatus, "3")
                .set(tdo.getRemark() != null, CheckFactorInfo::getRemark, tdo.getRemark())
                .set(tdo.getCheckEquipment() != null, CheckFactorInfo::getCheckEquipment, tdo.getCheckEquipment())
                .eq(CheckFactorInfo::getId, tdo.getCheckFactorId()));
        List<CheckFactorSubsetVo> factorSubsetVos = tdo.getFactorSubsetVos();
        if(factorSubsetVos != null && !factorSubsetVos.isEmpty()) {
            List<CheckFactorSubset> list = factorSubsetVos.stream().map(item -> {
                CheckFactorSubset checkFactorSubset = new CheckFactorSubset();
                checkFactorSubset.setId(item.getId()).setCheckSubRes(item.getCheckSubRes());
                return checkFactorSubset;
            }).collect(Collectors.toList());
            checkFactorSubsetService.updateBatchById(list);
        }
        return ResultUtil.success("检测数据录入成功！");
    }

    @Override
    public Result<Object> submitCheckFactor(Long checkFactorId) {
        if (checkFactorId == null) {
            return ResultUtil.validateError("检测列表ID不能为空！");
        }
        CheckFactorInfo cfi = this.baseMapper.selectById(checkFactorId);
        if (cfi == null) {
            return ResultUtil.busiError("该检测列表信息不存在！");
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        if(!cfi.getAssignUserId().equals(loginOutputVo.getUserId())) {
            return ResultUtil.busiError("身份不符，非检测列表指定检测员！");
        }
        if (!cfi.getCheckStatus().equals("3")) {
            return ResultUtil.busiError("未录入、待审核、已驳回的检测列表无法提交审核！");
        }
        this.update(Wrappers.<CheckFactorInfo>lambdaUpdate()
                .set(CheckFactorInfo::getCheckStatus, "4")
                .set(CheckFactorInfo::getUpdateTime, LocalDateTime.now())
                .eq(CheckFactorInfo::getId, checkFactorId));
        return ResultUtil.success("提交审核成功！");
    }

    @Override
    @Transactional
    public Result<Object> auditCheckFactor(CheckFactorAuditTdo tdo) {
        CheckFactorInfo cfi = this.getById(tdo.getCheckFactorId());
        if (cfi == null) {
            return ResultUtil.busiError("该监测列表信息不存在！");
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LocalDateTime now = LocalDateTime.now();
        //保存审核记录
        CheckFactorAuditRecord record = new CheckFactorAuditRecord();
        record.setAuditUserId(loginOutputVo.getUserId())
                .setAuditStatus(tdo.getAuditFlag())
                .setAuditReason(tdo.getAuditReason())
                .setAuditTime(now)
                .setCheckFactorId(tdo.getCheckFactorId());
        if (tdo.getAuditFlag().equals("1")) {
            //审核通过
            //修改检测列表状态
            this.update(Wrappers.<CheckFactorInfo>lambdaUpdate()
                    .set(CheckFactorInfo::getCheckStatus, "5")
                    .set(CheckFactorInfo::getUpdateTime, now)
                    .eq(CheckFactorInfo::getId, tdo.getCheckFactorId()));
            //检查更新检测任务状态
            Long checkTaskId = cfi.getCheckTaskId();
            List<CheckFactorInfo> cfis = this.list(Wrappers.<CheckFactorInfo>lambdaQuery()
                    .select(CheckFactorInfo::getId, CheckFactorInfo::getCheckStatus)
                    .eq(CheckFactorInfo::getCheckTaskId, checkTaskId)
                    .ne(CheckFactorInfo::getCheckStatus, "5"));
            if (cfis == null || cfis.isEmpty()) {
                //检测任务完成
                CheckTask cti = checkTaskMapper.selectById(checkTaskId);
                if (cti != null) {
                    checkTaskMapper.update(null, Wrappers.<CheckTask>lambdaUpdate()
                            .set(CheckTask::getFinshTime, now)
                            .set(CheckTask::getTaskStatus, "2")
                            .set(CheckTask::getUpdateTime, now)
                            .eq(CheckTask::getId, checkTaskId));
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
                        for(OfferPlan offerPlan : offerPlans) {
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
            checkFactorAuditRecordMapper.insert(record);
            return ResultUtil.success("审批通过成功！");
        } else {
            //审核不通过
            //修改检测列表状态
            this.update(Wrappers.<CheckFactorInfo>lambdaUpdate()
                    .set(CheckFactorInfo::getCheckStatus, "6")
                    .set(CheckFactorInfo::getUpdateTime, now)
                    .eq(CheckFactorInfo::getId, tdo.getCheckFactorId()));
            checkFactorAuditRecordMapper.insert(record);
            return ResultUtil.success("审批驳回成功！");
        }
    }

    @Override
    public Result<IPage<CheckFactorInfoVo>> findAuditCheckFactorByCondition(PageVo pageVo, CheckFactorSearchVo sv, String reqPath) {
        sv.setCheckStatus(StrUtil.trimToNull(sv.getCheckStatus()));
        sv.setFactorName(StrUtil.trimToNull(sv.getFactorName()));
        sv.setSampleNo(StrUtil.trimToNull(sv.getSampleNo()));
        Page<CheckFactorInfoVo> page = PageUtil.initMpPage(pageVo);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        String userId = loginOutputVo.getUserId();
        String dataScope = sysRoleService.findMaxRoleScopeByPerms(userId, reqPath);
        if(dataScope != null) {
            if(dataScope.equals("1")) {
                this.baseMapper.findCheckFactorForCheckManagerByCondition(page, organId, sv);
            }else{
                List<Long> deptIds = null;
                if(dataScope.equals("2")) {
                    deptIds = sysDeptService.selectChildDeptIdsByDeptId(loginOutputVo.getDeptId());
                }
                if(deptIds == null) {
                    deptIds = new ArrayList<>();
                }
                deptIds.add(loginOutputVo.getDeptId());
                this.baseMapper.findCheckFactorForDeptByCondition(page, deptIds, organId, sv);
            }
            if (page.getRecords() != null && !page.getRecords().isEmpty()) {
                page.getRecords().forEach(p -> {
                    p.setDayAndCount(String.format("第%s天第%s次", p.getDay(), p.getFrequency()));
                    FactorMethodInfoVo fmiVo = factorService.findFactorMethodById(p.getCheckStandardId());
                    if("1".equals(p.getIsFactor())) {
                        List<CheckFactorSubset> factorSubsets = checkFactorSubsetMapper.selectList(Wrappers.<CheckFactorSubset>lambdaQuery()
                                .eq(CheckFactorSubset::getCheckFactorId, p.getCheckFactorId()));
                        List<CheckFactorSubsetVo> factorSubsetVos = factorSubsets.stream().map(item -> {
                            CheckFactorSubsetVo factorSubsetVo = new CheckFactorSubsetVo();
                            factorSubsetVo.setId(item.getId()).setCheckStandardId(item.getCheckStandardId()).setCheckSubRes(item.getCheckSubRes());
                            FactorMethodInfoVo vo = factorService.findFactorMethodById(factorSubsetVo.getCheckStandardId());
                            factorSubsetVo.setFactorName(vo.getFactorName()).setUnitName(vo.getDefaultUnitName())
                                    .setStandardNo(vo.getStandardNo()).setStandardName(vo.getStandardName());
                            return factorSubsetVo;
                        }).collect(Collectors.toList());
                        p.setStandardNo(fmiVo.getStandardNo()).setStandardName(fmiVo.getStandardName());
                        p.setFactorSubsetVos(factorSubsetVos);
                    }else{
                        p.setStandardNo(fmiVo.getStandardNo()).setStandardName(fmiVo.getStandardName()).setUnitName(fmiVo.getDefaultUnitName());
                    }
                });
            }
        }
        return ResultUtil.data(page);
    }

    @Override
    @Transactional
    public Result<Object> sampDrawApply(Long checkFactorId) {
        if (checkFactorId == null) {
            return ResultUtil.validateError("检测列表ID不能为空！");
        }
        String userId = (String) StpUtil.getLoginId();
        LocalDateTime now = LocalDateTime.now();
        CheckFactorInfo info = this.getById(checkFactorId);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        if(!info.getAssignUserId().equals(loginOutputVo.getUserId())) {
            return ResultUtil.busiError("身份不符，非检测列表指定检测员！");
        }
        SampleItem sii = sampleItemMapper.selectById(info.getSampleItemId());
        List<String> sampStatus = Arrays.asList("6","7");
        if(!sampStatus.contains(sii.getSampleStatus())) {
            if((!sii.getSampleStatus().equals("4") && !sii.getStoreFlag().equals("0")) ||
                    (!sii.getSampleStatus().equals("8") && !sii.getFbFlag().equals("1"))) {
                return ResultUtil.busiError("样品采样尚未完成，无法申请领样！");
            }
        }
        SampleDrawApply sda = sampleDrawApplyMapper.selectOne(Wrappers.<SampleDrawApply>lambdaQuery()
                .eq(SampleDrawApply::getApplyUserId, userId)
                .eq(SampleDrawApply::getSampleItemId, info.getSampleItemId()));
        if (sda == null) {
            sda = new SampleDrawApply();
            sda.setApplyUserId(userId).setApprovalStatus("0")
                    .setCreateTime(now)
                    .setSampleItemId(info.getSampleItemId());
            sampleDrawApplyMapper.insert(sda);
        }
        this.update(Wrappers.<CheckFactorInfo>lambdaUpdate()
                .set(CheckFactorInfo::getUpdateTime, now)
                .set(CheckFactorInfo::getCheckStatus, "1")
                .eq(CheckFactorInfo::getSampleItemId, info.getSampleItemId())
                .eq(CheckFactorInfo::getCheckStatus, "0")
                .eq(CheckFactorInfo::getAssignUserId, userId));
        return ResultUtil.success("申请领样成功！");
    }

    @Transactional
    @Override
    public Result<Object> batchAuditCheckFactor(BatchCheckFactorAuditTdo tdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LocalDateTime now = LocalDateTime.now();
        List<Long> checkFactorIds =Arrays.stream(tdo.getCheckFactorId().split(","))
                .map(s -> Long.parseLong(s)).collect(Collectors.toList());
        if(tdo.getAuditFlag().equals("1")) {
            for(Long checkFactorId : checkFactorIds) {
                CheckFactorInfo cfi = this.getById(checkFactorId);
                if (cfi == null) {
                    throw new CustomException("检测列表信息不存在！");
                }
                //修改检测列表状态
                int cnt = this.baseMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                        .set(CheckFactorInfo::getCheckStatus, "5")
                        .set(CheckFactorInfo::getUpdateTime, now)
                        .eq(CheckFactorInfo::getId, checkFactorId));
                if(cnt > 0) {
                    //保存审核记录
                    CheckFactorAuditRecord record = new CheckFactorAuditRecord();
                    record.setAuditUserId(loginOutputVo.getUserId())
                            .setAuditStatus(tdo.getAuditFlag())
                            .setAuditReason(tdo.getAuditReason())
                            .setAuditTime(now)
                            .setCheckFactorId(checkFactorId);
                    checkFactorAuditRecordMapper.insert(record);
                    //检查更新检测任务状态
                    Long checkTaskId = cfi.getCheckTaskId();
                    List<CheckFactorInfo> cfis = this.list(Wrappers.<CheckFactorInfo>lambdaQuery()
                            .select(CheckFactorInfo::getId, CheckFactorInfo::getCheckStatus)
                            .eq(CheckFactorInfo::getCheckTaskId, checkTaskId)
                            .ne(CheckFactorInfo::getCheckStatus, "5"));
                    if (cfis == null || cfis.isEmpty()) {
                        //检测任务完成
                        CheckTask cti = checkTaskMapper.selectById(checkTaskId);
                        if (cti != null) {
                            checkTaskMapper.update(null, Wrappers.<CheckTask>lambdaUpdate()
                                    .set(CheckTask::getFinshTime, now)
                                    .set(CheckTask::getTaskStatus, "2")
                                    .set(CheckTask::getUpdateTime, now)
                                    .eq(CheckTask::getId, checkTaskId));
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
                                for(OfferPlan offerPlan : offerPlans) {
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
                }else {
                    throw new CustomException("批量检测列表审核通过失败！");
                }
            }
            return ResultUtil.success("审批通过成功！");
        }else {
            for(Long checkFactorId : checkFactorIds) {
                CheckFactorInfo cfi = this.getById(checkFactorId);
                if (cfi == null) {
                    throw new CustomException("检测列表信息不存在！");
                }
                //修改检测列表状态
                int cnt = this.baseMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                        .set(CheckFactorInfo::getCheckStatus, "6")
                        .set(CheckFactorInfo::getUpdateTime, now)
                        .eq(CheckFactorInfo::getId, checkFactorId));
                if(cnt > 0) {
                    //保存审核记录
                    CheckFactorAuditRecord record = new CheckFactorAuditRecord();
                    record.setAuditUserId(loginOutputVo.getUserId())
                            .setAuditStatus(tdo.getAuditFlag())
                            .setAuditReason(tdo.getAuditReason())
                            .setAuditTime(now)
                            .setCheckFactorId(checkFactorId);
                    checkFactorAuditRecordMapper.insert(record);
                }else{
                    throw new CustomException("检测列表审核驳回失败！");
                }
            }
            return ResultUtil.success("批量审批驳回成功！");
        }
    }

    @Transactional
    @Override
    public Result<Object> batchSampDrawApply(String checkFactorId) {
        if(StrUtil.isBlank(checkFactorId)) {
            return ResultUtil.validateError("至少选择一条检测列表信息！");
        }
        String userId = (String) StpUtil.getLoginId();
        LocalDateTime now = LocalDateTime.now();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<Long> checkFactorIds = Arrays.stream(checkFactorId.split(","))
                .map(v -> Long.parseLong(v)).collect(Collectors.toList());
        List<String> sampStatus = Arrays.asList("6","7");
        for(Long id : checkFactorIds) {
            CheckFactorInfo info = this.getById(id);
            if(!info.getAssignUserId().equals(loginOutputVo.getUserId())) {
                throw new CustomException("身份不符，非检测列表指定检测员！");
            }
            SampleItem sii = sampleItemMapper.selectById(info.getSampleItemId());
            if(!sampStatus.contains(sii.getSampleStatus())) {
                if((!sii.getSampleStatus().equals("4") && !sii.getStoreFlag().equals("0")) ||
                        (!sii.getSampleStatus().equals("8") && !sii.getFbFlag().equals("1"))) {
                    throw new CustomException(String.format("样品 %s 采样尚未完成，无法申请领样！", sii.getSampleNo()));
                }
            }
            SampleDrawApply sda = sampleDrawApplyMapper.selectOne(Wrappers.<SampleDrawApply>lambdaQuery()
                    .eq(SampleDrawApply::getApplyUserId, userId)
                    .eq(SampleDrawApply::getSampleItemId, info.getSampleItemId()));
            if (sda == null) {
                sda = new SampleDrawApply();
                sda.setApplyUserId(userId).setApprovalStatus("0")
                        .setCreateTime(now)
                        .setSampleItemId(info.getSampleItemId());
                sampleDrawApplyMapper.insert(sda);
            }
            this.update(Wrappers.<CheckFactorInfo>lambdaUpdate()
                    .set(CheckFactorInfo::getUpdateTime, now)
                    .set(CheckFactorInfo::getCheckStatus, "1")
                    .eq(CheckFactorInfo::getSampleItemId, info.getSampleItemId())
                    .eq(CheckFactorInfo::getCheckStatus, "0")
                    .eq(CheckFactorInfo::getAssignUserId, userId));
        }
        return ResultUtil.success("批量申请领样成功！");
    }
}
