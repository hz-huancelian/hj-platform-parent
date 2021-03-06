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
 * ???????????????
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
            return ResultUtil.busiError("?????????????????????????????????");
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        if(!cfi.getAssignUserId().equals(loginOutputVo.getUserId())) {
            return ResultUtil.busiError("????????????????????????????????????????????????");
        }
        List<String> allowStatus = Arrays.asList("2", "3", "6");
        if (!allowStatus.contains(cfi.getCheckStatus())) {
            return ResultUtil.busiError("??????????????????????????????????????????????????????????????????");
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
        return ResultUtil.success("???????????????????????????");
    }

    @Override
    public Result<Object> submitCheckFactor(Long checkFactorId) {
        if (checkFactorId == null) {
            return ResultUtil.validateError("????????????ID???????????????");
        }
        CheckFactorInfo cfi = this.baseMapper.selectById(checkFactorId);
        if (cfi == null) {
            return ResultUtil.busiError("?????????????????????????????????");
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        if(!cfi.getAssignUserId().equals(loginOutputVo.getUserId())) {
            return ResultUtil.busiError("????????????????????????????????????????????????");
        }
        if (!cfi.getCheckStatus().equals("3")) {
            return ResultUtil.busiError("?????????????????????????????????????????????????????????????????????");
        }
        this.update(Wrappers.<CheckFactorInfo>lambdaUpdate()
                .set(CheckFactorInfo::getCheckStatus, "4")
                .set(CheckFactorInfo::getUpdateTime, LocalDateTime.now())
                .eq(CheckFactorInfo::getId, checkFactorId));
        return ResultUtil.success("?????????????????????");
    }

    @Override
    @Transactional
    public Result<Object> auditCheckFactor(CheckFactorAuditTdo tdo) {
        CheckFactorInfo cfi = this.getById(tdo.getCheckFactorId());
        if (cfi == null) {
            return ResultUtil.busiError("?????????????????????????????????");
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LocalDateTime now = LocalDateTime.now();
        //??????????????????
        CheckFactorAuditRecord record = new CheckFactorAuditRecord();
        record.setAuditUserId(loginOutputVo.getUserId())
                .setAuditStatus(tdo.getAuditFlag())
                .setAuditReason(tdo.getAuditReason())
                .setAuditTime(now)
                .setCheckFactorId(tdo.getCheckFactorId());
        if (tdo.getAuditFlag().equals("1")) {
            //????????????
            //????????????????????????
            this.update(Wrappers.<CheckFactorInfo>lambdaUpdate()
                    .set(CheckFactorInfo::getCheckStatus, "5")
                    .set(CheckFactorInfo::getUpdateTime, now)
                    .eq(CheckFactorInfo::getId, tdo.getCheckFactorId()));
            //??????????????????????????????
            Long checkTaskId = cfi.getCheckTaskId();
            List<CheckFactorInfo> cfis = this.list(Wrappers.<CheckFactorInfo>lambdaQuery()
                    .select(CheckFactorInfo::getId, CheckFactorInfo::getCheckStatus)
                    .eq(CheckFactorInfo::getCheckTaskId, checkTaskId)
                    .ne(CheckFactorInfo::getCheckStatus, "5"));
            if (cfis == null || cfis.isEmpty()) {
                //??????????????????
                CheckTask cti = checkTaskMapper.selectById(checkTaskId);
                if (cti != null) {
                    checkTaskMapper.update(null, Wrappers.<CheckTask>lambdaUpdate()
                            .set(CheckTask::getFinshTime, now)
                            .set(CheckTask::getTaskStatus, "2")
                            .set(CheckTask::getUpdateTime, now)
                            .eq(CheckTask::getId, checkTaskId));
                    //????????????????????????
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

                        //??????????????????
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
            return ResultUtil.success("?????????????????????");
        } else {
            //???????????????
            //????????????????????????
            this.update(Wrappers.<CheckFactorInfo>lambdaUpdate()
                    .set(CheckFactorInfo::getCheckStatus, "6")
                    .set(CheckFactorInfo::getUpdateTime, now)
                    .eq(CheckFactorInfo::getId, tdo.getCheckFactorId()));
            checkFactorAuditRecordMapper.insert(record);
            return ResultUtil.success("?????????????????????");
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
                    p.setDayAndCount(String.format("???%s??????%s???", p.getDay(), p.getFrequency()));
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
            return ResultUtil.validateError("????????????ID???????????????");
        }
        String userId = (String) StpUtil.getLoginId();
        LocalDateTime now = LocalDateTime.now();
        CheckFactorInfo info = this.getById(checkFactorId);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        if(!info.getAssignUserId().equals(loginOutputVo.getUserId())) {
            return ResultUtil.busiError("????????????????????????????????????????????????");
        }
        SampleItem sii = sampleItemMapper.selectById(info.getSampleItemId());
        List<String> sampStatus = Arrays.asList("6","7");
        if(!sampStatus.contains(sii.getSampleStatus())) {
            if((!sii.getSampleStatus().equals("4") && !sii.getStoreFlag().equals("0")) ||
                    (!sii.getSampleStatus().equals("8") && !sii.getFbFlag().equals("1"))) {
                return ResultUtil.busiError("????????????????????????????????????????????????");
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
        return ResultUtil.success("?????????????????????");
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
                    throw new CustomException("??????????????????????????????");
                }
                //????????????????????????
                int cnt = this.baseMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                        .set(CheckFactorInfo::getCheckStatus, "5")
                        .set(CheckFactorInfo::getUpdateTime, now)
                        .eq(CheckFactorInfo::getId, checkFactorId));
                if(cnt > 0) {
                    //??????????????????
                    CheckFactorAuditRecord record = new CheckFactorAuditRecord();
                    record.setAuditUserId(loginOutputVo.getUserId())
                            .setAuditStatus(tdo.getAuditFlag())
                            .setAuditReason(tdo.getAuditReason())
                            .setAuditTime(now)
                            .setCheckFactorId(checkFactorId);
                    checkFactorAuditRecordMapper.insert(record);
                    //??????????????????????????????
                    Long checkTaskId = cfi.getCheckTaskId();
                    List<CheckFactorInfo> cfis = this.list(Wrappers.<CheckFactorInfo>lambdaQuery()
                            .select(CheckFactorInfo::getId, CheckFactorInfo::getCheckStatus)
                            .eq(CheckFactorInfo::getCheckTaskId, checkTaskId)
                            .ne(CheckFactorInfo::getCheckStatus, "5"));
                    if (cfis == null || cfis.isEmpty()) {
                        //??????????????????
                        CheckTask cti = checkTaskMapper.selectById(checkTaskId);
                        if (cti != null) {
                            checkTaskMapper.update(null, Wrappers.<CheckTask>lambdaUpdate()
                                    .set(CheckTask::getFinshTime, now)
                                    .set(CheckTask::getTaskStatus, "2")
                                    .set(CheckTask::getUpdateTime, now)
                                    .eq(CheckTask::getId, checkTaskId));
                            //????????????????????????
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
                                //??????????????????
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
                    throw new CustomException("???????????????????????????????????????");
                }
            }
            return ResultUtil.success("?????????????????????");
        }else {
            for(Long checkFactorId : checkFactorIds) {
                CheckFactorInfo cfi = this.getById(checkFactorId);
                if (cfi == null) {
                    throw new CustomException("??????????????????????????????");
                }
                //????????????????????????
                int cnt = this.baseMapper.update(null, Wrappers.<CheckFactorInfo>lambdaUpdate()
                        .set(CheckFactorInfo::getCheckStatus, "6")
                        .set(CheckFactorInfo::getUpdateTime, now)
                        .eq(CheckFactorInfo::getId, checkFactorId));
                if(cnt > 0) {
                    //??????????????????
                    CheckFactorAuditRecord record = new CheckFactorAuditRecord();
                    record.setAuditUserId(loginOutputVo.getUserId())
                            .setAuditStatus(tdo.getAuditFlag())
                            .setAuditReason(tdo.getAuditReason())
                            .setAuditTime(now)
                            .setCheckFactorId(checkFactorId);
                    checkFactorAuditRecordMapper.insert(record);
                }else{
                    throw new CustomException("?????????????????????????????????");
                }
            }
            return ResultUtil.success("???????????????????????????");
        }
    }

    @Transactional
    @Override
    public Result<Object> batchSampDrawApply(String checkFactorId) {
        if(StrUtil.isBlank(checkFactorId)) {
            return ResultUtil.validateError("???????????????????????????????????????");
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
                throw new CustomException("????????????????????????????????????????????????");
            }
            SampleItem sii = sampleItemMapper.selectById(info.getSampleItemId());
            if(!sampStatus.contains(sii.getSampleStatus())) {
                if((!sii.getSampleStatus().equals("4") && !sii.getStoreFlag().equals("0")) ||
                        (!sii.getSampleStatus().equals("8") && !sii.getFbFlag().equals("1"))) {
                    throw new CustomException(String.format("?????? %s ??????????????????????????????????????????", sii.getSampleNo()));
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
        return ResultUtil.success("???????????????????????????");
    }
}
