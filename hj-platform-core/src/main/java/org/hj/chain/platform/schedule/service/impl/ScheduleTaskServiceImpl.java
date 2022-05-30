package org.hj.chain.platform.schedule.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.CodeBuildUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.check.entity.CheckFactorInfo;
import org.hj.chain.platform.check.mapper.CheckFactorInfoMapper;
import org.hj.chain.platform.common.CustomException;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.component.DictUtils;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.offer.entity.OfferPlan;
import org.hj.chain.platform.offer.entity.OfferPlanFactor;
import org.hj.chain.platform.offer.entity.OfferPlanFactorSubset;
import org.hj.chain.platform.offer.mapper.OfferPlanFactorMapper;
import org.hj.chain.platform.offer.mapper.OfferPlanFactorSubsetMapper;
import org.hj.chain.platform.offer.mapper.OfferPlanMapper;
import org.hj.chain.platform.offer.service.IOfferInfoService;
import org.hj.chain.platform.offer.service.IOfferPlanService;
import org.hj.chain.platform.report.mapper.ReportInfoMapper;
import org.hj.chain.platform.report.model.ReportInfo;
import org.hj.chain.platform.sample.entity.SampleItem;
import org.hj.chain.platform.sample.entity.SampleTask;
import org.hj.chain.platform.sample.mapper.SampleItemMapper;
import org.hj.chain.platform.sample.mapper.SampleTaskMapper;
import org.hj.chain.platform.schedule.entity.*;
import org.hj.chain.platform.schedule.mapper.*;
import org.hj.chain.platform.schedule.service.*;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.tdo.schedule.ScheduleJobPlanFactorTdo;
import org.hj.chain.platform.tdo.schedule.ScheduleJobTdo;
import org.hj.chain.platform.vo.FactorMethodInfoVo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.offer.OfferPlanFactorVo;
import org.hj.chain.platform.vo.schedule.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Service
public class ScheduleTaskServiceImpl extends ServiceImpl<ScheduleTaskMapper, ScheduleTask> implements IScheduleTaskService {

    @Autowired
    private IScheduleJobService jobService;
    @Autowired
    private ScheduleJobMapper jobMapper;
    @Autowired
    private IScheduleJobPlanService jobPlanService;
    @Autowired
    private ScheduleJobPlanMapper jobPlanMapper;
    @Autowired
    private IScheduleJobPlanFactorService jobPlanFactorService;
    @Autowired
    private ScheduleJobPlanFactorMapper jobPlanFactorMapper;
    @Autowired
    private IScheduleJobPlanFactorSubsetService jobPlanFactorSubsetService;
    @Autowired
    private ScheduleJobPlanFactorSubsetMapper jobPlanFactorSubsetMapper;
    @Autowired
    private ReportInfoMapper reportInfoMapper;
    @Autowired
    private SampleItemMapper sampleItemMapper;
    @Autowired
    private CheckFactorInfoMapper checkFactorInfoMapper;
    @Autowired
    private IOfferPlanService offerPlanService;
    @Autowired
    private OfferPlanMapper offerPlanMapper;
    @Autowired
    private IOfferInfoService offerInfoService;
    @Autowired
    private OfferPlanFactorMapper offerPlanFactorMapper;
    @Autowired
    private OfferPlanFactorSubsetMapper offerPlanFactorSubsetMapper;
    @Autowired
    private SampleTaskMapper sampleTaskMapper;
    @Autowired
    private IFactorService factorService;
    @Autowired
    private DictUtils dictUtils;

    @Override
    public IPage<ScheduleTaskVo> findByCondition(PageVo pageVo, ScheduleTaskSearchVo sv) {
        Page<ScheduleTaskVo> page = PageUtil.initMpPage(pageVo);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        this.baseMapper.findByCondition(page, loginOutputVo.getOrganId(), sv);
        return page;
    }

    @Override
    public IPage<ScheduleJobVo> findJobForTask(PageVo pageVo, ScheduleJobSearchVo sv) {
        List<ScheduleJobVo> jobVos = new ArrayList<>();
        Page<ScheduleJob> page = PageUtil.initMpPage(pageVo);
        jobService.page(page, Wrappers.<ScheduleJob>lambdaQuery().eq(ScheduleJob::getTaskId, sv.getTaskId())
                .eq(StrUtil.isNotBlank(sv.getJobId()), ScheduleJob::getId, sv.getJobId()));
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            jobVos = page.getRecords().stream().map(job -> {
                ScheduleJobVo jobVo = new ScheduleJobVo();
                BeanUtils.copyProperties(job, jobVo);
                jobVo.setJobId(job.getId());
                ReportInfo ri = reportInfoMapper.selectOne(Wrappers.<ReportInfo>lambdaQuery()
                        .select(ReportInfo::getId, ReportInfo::getJobId, ReportInfo::getReportStatus)
                        .eq(ReportInfo::getJobId, job.getId()));
                if(ri != null) {
                    jobVo.setReportStatus(ri.getReportStatus());
                }else{
                    jobVo.setReportStatus("0");
                }

                List<SampleItem> siis = sampleItemMapper.findCompleteSampleByJobId(job.getId());
                if(siis != null && !siis.isEmpty()) {
                    jobVo.setIsSampled("1");
                }else{
                    jobVo.setIsSampled("0");
                }

                List<CheckFactorInfo> cfis = checkFactorInfoMapper.findCompleteCheckFactorByJobId(job.getId());
                if(cfis != null && !cfis.isEmpty()) {
                    jobVo.setIsChecked("1");
                }else{
                    jobVo.setIsChecked("0");
                }
                return jobVo;
            }).collect(Collectors.toList());
        }
        return PageUtil.convertPageVo(page, jobVos);
    }

    @Override
    public Result<ScheduleJobVo> findJobByTaskId(Long taskId) {
        ScheduleJob job = jobMapper.selectOne(Wrappers.<ScheduleJob>lambdaQuery().eq(ScheduleJob::getTaskId, taskId)
                .eq(ScheduleJob::getJobStatus, "0"));
        ScheduleJobVo jobVo = new ScheduleJobVo();
        BeanUtils.copyProperties(job, jobVo);
        jobVo.setJobId(job.getId());
        List<ScheduleJobPlan> jobPlans = jobPlanMapper.selectList(Wrappers.<ScheduleJobPlan>lambdaQuery()
                .eq(ScheduleJobPlan::getJobId, job.getId()));
        List<Long> offerPlanIds = jobPlans.stream().map(ScheduleJobPlan::getOfferPlanId).collect(Collectors.toList());
        jobVo.setOfferPlanIds(offerPlanIds);
        return ResultUtil.data(jobVo);
    }

    @Override
    public IPage<ScheduleJobVo> findJobsByCondition(PageVo pageVo, ScheduleJobSearchVo sv) {
        List<ScheduleJobVo> jobVos = new ArrayList<>();
        Page<ScheduleJob> page = PageUtil.initMpPage(pageVo);
        jobService.page(page, Wrappers.<ScheduleJob>lambdaQuery()
                .eq(ScheduleJob::getDelStatus, '0')
                .eq(sv.getJobId() != null, ScheduleJob::getId, sv.getJobId())
                .like(sv.getProjectName() != null, ScheduleJob::getProjectName, sv.getProjectName())
                .orderByDesc(ScheduleJob::getCreateTime));
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            jobVos = page.getRecords().stream().map(job -> {
                ScheduleJobVo jobVo = new ScheduleJobVo();
                BeanUtils.copyProperties(job, jobVo);
                jobVo.setJobId(job.getId());
                ReportInfo ri = reportInfoMapper.selectOne(Wrappers.<ReportInfo>lambdaQuery()
                        .select(ReportInfo::getId, ReportInfo::getJobId, ReportInfo::getReportStatus)
                        .eq(ReportInfo::getJobId, job.getId()));
                if(ri != null) {
                    jobVo.setReportStatus(ri.getReportStatus());
                }else{
                    jobVo.setReportStatus("0");
                }

                List<SampleItem> siis = sampleItemMapper.findCompleteSampleByJobId(job.getId());
                if(siis != null && !siis.isEmpty()) {
                    jobVo.setIsSampled("1");
                }else{
                    jobVo.setIsSampled("0");
                }

                List<CheckFactorInfo> cfis = checkFactorInfoMapper.findCompleteCheckFactorByJobId(job.getId());
                if(cfis != null && !cfis.isEmpty()) {
                    jobVo.setIsChecked("1");
                }else{
                    jobVo.setIsChecked("0");
                }
                return jobVo;
            }).collect(Collectors.toList());
        }
        return PageUtil.convertPageVo(page, jobVos);
    }

    @Override
    public List<ScheduleOfferPlanVo> findOfferPlanByOfferId(String offerId) {
        List<OfferPlan> offerPlans = offerPlanMapper.selectList(Wrappers.<OfferPlan>lambdaQuery()
                .eq(OfferPlan::getOfferId, offerId).orderByDesc(OfferPlan::getScheduledTimes));
        offerPlans = offerPlans.stream().filter(d -> d.getScheduledTimes() < d.getScheduleTimes()).collect(Collectors.toList());
        List<ScheduleOfferPlanVo> offerPlanVos = new ArrayList<>();
        if(offerPlans != null && !offerPlans.isEmpty()) {
            offerPlanVos = offerPlans.stream().map(item -> {
                ScheduleOfferPlanVo vo = new ScheduleOfferPlanVo();
                String lastScheduleDate = StrUtil.isNotBlank(item.getLastScheduleDate()) ? item.getLastScheduleDate() : "/";
                vo.setOfferPlanId(item.getId())
                        .setPlanName(item.getPlanName())
                        .setCheckFactorCnt(item.getCheckFactorCnt())
                        .setLastScheduleDate(lastScheduleDate)
                        .setCheckFreq(item.getCheckFreq())
                        .setScheduledTimes(item.getScheduledTimes())
                        .setScheduleTimes(item.getScheduleTimes())
                        .setFinishTimes(item.getFinishTimes());
                return vo;
            }).collect(Collectors.toList());
        }
        return offerPlanVos;
    }

    @Override
    public List<OfferPlanFactorVo> findFactorsByOfferPlanId(Long offerPlanId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<OfferPlanFactorVo> vos = offerInfoService.getFactorsByOfferPlanId(offerPlanId);
        vos.forEach(vo -> {
            vo.setSecdClassName(dictUtils.getFactorClassMap().get(vo.getSecdClassId()));
            vo.setFbFlag(organId.equals(vo.getOrganId()) ? "0" : "1");
            //获取监测方法ID对应的信息
            FactorMethodInfoVo factorMethod = factorService.findFactorMethodById(vo.getCheckStandardId());
            if (factorMethod != null) {
                String classId = factorMethod.getClassId();
                vo.setClassId(classId);
                vo.setClassName(dictUtils.getFactorClassMap().get(classId));
                vo.setStandardName(factorMethod.getStandardName());
                vo.setDataEntryStep(factorMethod.getDataEntryStep());
                vo.setStandardNo(factorMethod.getStandardNo());
            }
        });
        return vos;
    }

    @Override
    public List<OfferPlanFactorVo> findFactorsByOfferPlanIds(List<Long> offerPlanIds) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<OfferPlanFactorVo> vos = offerInfoService.getFactorsByOfferPlanIds(offerPlanIds);
        vos.forEach(vo -> {
            vo.setSecdClassName(dictUtils.getFactorClassMap().get(vo.getSecdClassId()));
            vo.setFbFlag(organId.equals(vo.getOrganId()) ? "0" : "1");
            //获取监测方法ID对应的信息
            FactorMethodInfoVo factorMethod = factorService.findFactorMethodById(vo.getCheckStandardId());
            if (factorMethod != null) {
                String classId = factorMethod.getClassId();
                vo.setClassId(classId);
                vo.setClassName(dictUtils.getFactorClassMap().get(classId));
                vo.setStandardName(factorMethod.getStandardName());
                vo.setDataEntryStep(factorMethod.getDataEntryStep());
                vo.setStandardNo(factorMethod.getStandardNo());
            }
        });
        return vos;
    }

    @Override
    @Transactional
    public Result<Object> doScheduleJob(ScheduleJobTdo tdo, String jobStatus) {
        if("1".equals(jobStatus)) {
            StringBuilder sb = new StringBuilder();
            if(StrUtil.isBlank(tdo.getInspectionLinker())) {
                sb.append("受检单位联系人、");
            }
            if(StrUtil.isBlank(tdo.getInspectionName())) {
                sb.append("受检单位、");
            }
            if(StrUtil.isBlank(tdo.getInspectionLinkerPhone())) {
                sb.append("受检单位联系人电话、");
            }
            if(StrUtil.isBlank(tdo.getStartDate())) {
                sb.append("开始日期、");
            }
            if(StrUtil.isBlank(tdo.getEndDate())) {
                sb.append("结束日期、");
            }
            if(StrUtil.isBlank(tdo.getProjectAddress())) {
                sb.append("项目地址");
            }
            if(StrUtil.isNotBlank(sb.toString())) {
                return ResultUtil.validateError(sb.append("等不能为空！").toString());
            }
        }
        ScheduleTask scheduleTask = this.getById(tdo.getTaskId());
        if(scheduleTask == null) {
            return ResultUtil.busiError("任务调度信息不存在！");
        }
        ScheduleJob scheduleJob = jobMapper.selectOne(Wrappers.<ScheduleJob>lambdaQuery()
                .eq(ScheduleJob::getTaskId, tdo.getTaskId()).eq(ScheduleJob::getJobStatus, "0"));
        if(scheduleJob != null && StrUtil.isBlank(tdo.getJobId())) {
            return ResultUtil.busiError("任务单号不能为空！");
        }
        LocalDateTime now = LocalDateTime.now();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        ScheduleJob job = new ScheduleJob();
        BeanUtils.copyProperties(tdo, job);
        job.setId(tdo.getJobId()).setCreateUserId(loginOutputVo.getUserId()).setJobStatus(jobStatus).setOrganId(loginOutputVo.getOrganId());
        if(StrUtil.isBlank(tdo.getJobId())) {
            Integer sort = jobMapper.getCount(loginOutputVo.getOrganId(), now.toLocalDate().toString());
            job.setId(CodeBuildUtil.genTaskCode(loginOutputVo.getOrganId(), sort + 1))
                    .setCreateTime(now);
            jobMapper.insert(job);
        }else{
            job.setUpdateTime(now);
            jobMapper.updateById(job);
            jobPlanMapper.delete(Wrappers.<ScheduleJobPlan>lambdaQuery().eq(ScheduleJobPlan::getJobId, job.getId()));
            jobPlanFactorMapper.delete(Wrappers.<ScheduleJobPlanFactor>lambdaQuery().eq(ScheduleJobPlanFactor::getJobId, job.getId()));
            jobPlanFactorSubsetMapper.delete(Wrappers.<ScheduleJobPlanFactorSubset>lambdaQuery().eq(ScheduleJobPlanFactorSubset::getJobId, job.getId()));
        }
        String jobId = job.getId();
        List<ScheduleJobPlanFactorTdo> factorTdos = tdo.getFactorTdos();
        List<Long> offerPlanIds = factorTdos.stream().filter(d -> d.getOfferPlanId() != null).map(ScheduleJobPlanFactorTdo::getOfferPlanId).distinct().collect(Collectors.toList());
        Map<Long, Long> map = new HashMap<>();
        if(offerPlanIds != null && !offerPlanIds.isEmpty()) {
            List<ScheduleJobPlan> jobPlans = offerPlanIds.stream().map(item -> {
                ScheduleJobPlan jobPlan = new ScheduleJobPlan();
                jobPlan.setJobId(jobId).setOfferPlanId(item).setScheduleDate(now.toLocalDate().toString());
                return jobPlan;
            }).collect(Collectors.toList());
            jobPlanService.saveBatch(jobPlans);
            jobPlans.forEach(item -> {
                map.put(item.getOfferPlanId(), item.getId());
            });
        }

        List<ScheduleJobPlanFactor> jobPlanFactors = factorTdos.stream().map(item -> {
            ScheduleJobPlanFactor jobPlanFactor = new ScheduleJobPlanFactor();
            BeanUtils.copyProperties(item, jobPlanFactor);
            jobPlanFactor.setHyFlag("0");
            jobPlanFactor.setJobId(jobId);
            if(item.getOfferPlanId() != null) {
                jobPlanFactor.setJobPlanId(map.get(item.getOfferPlanId()));
            }
            return jobPlanFactor;
        }).collect(Collectors.toList());
        jobPlanFactorService.saveBatch(jobPlanFactors);
        jobPlanFactors.forEach(item -> {
            if("1".equals(item.getIsFactor())) {
                Long offerPlanId = jobPlanService.getById(item.getJobPlanId()).getOfferPlanId();
                OfferPlanFactor planFactor = offerPlanFactorMapper.selectList(Wrappers.<OfferPlanFactor>lambdaQuery()
                        .eq(OfferPlanFactor::getCheckStandardId, item.getCheckStandardId())
                        .eq(OfferPlanFactor::getOfferPlanId, offerPlanId)).get(0);
                List<OfferPlanFactorSubset> factorSubsets = offerPlanFactorSubsetMapper.selectList(Wrappers.<OfferPlanFactorSubset>lambdaQuery()
                        .select(OfferPlanFactorSubset::getCheckStandardId)
                        .eq(OfferPlanFactorSubset::getPlanFactorId, planFactor.getId()));
                List<String> subsetFactors = factorSubsets.stream().map(OfferPlanFactorSubset::getCheckStandardId).collect(Collectors.toList());
                List<ScheduleJobPlanFactorSubset> jobPlanFactorSubsets = subsetFactors.stream().map(s -> {
                    ScheduleJobPlanFactorSubset jobPlanFactorSubset = new ScheduleJobPlanFactorSubset();
                    jobPlanFactorSubset.setJobPlanFactorId(item.getId()).setJobId(jobId).setCheckStandardId(s);
                    return jobPlanFactorSubset;
                }).collect(Collectors.toList());
                jobPlanFactorSubsetService.saveBatch(jobPlanFactorSubsets);
            }
        });

        if("1".equals(jobStatus)) {
            int scheduledPlanNum = scheduleTask.getScheduledPlanNum();
            /* 更新报价单监测计划调度次数 */
            if(offerPlanIds != null && !offerPlanIds.isEmpty()) {
                List<OfferPlan> offerPlans = offerPlanMapper.selectList(Wrappers.<OfferPlan>lambdaQuery()
                        .select(OfferPlan::getId, OfferPlan::getScheduledTimes, OfferPlan::getScheduleTimes)
                        .in(OfferPlan::getId, offerPlanIds));
                for(OfferPlan offerPlan : offerPlans) {
                    offerPlan.setScheduledTimes(offerPlan.getScheduledTimes() + 1)
                            .setLastScheduleDate(now.toLocalDate().toString())
                            .setUpdateTime(now);
                }
                offerPlanService.updateBatchById(offerPlans);
                scheduledPlanNum += offerPlanIds.size();
            }
            /* 创建采样任务 */
            SampleTask sampleTask = new SampleTask();
            sampleTask.setJobId(jobId).setOrganId(loginOutputVo.getOrganId())
                    .setTaskStatus("0").setCreateTime(now);
            int cnt = sampleTaskMapper.insert(sampleTask);
            if (cnt < 0) {
                throw new CustomException("任务调度失败");
            }

            String scheduleStatus = scheduledPlanNum  == scheduleTask.getPlanNum() ? "2" : "1";
            this.update(Wrappers.<ScheduleTask>lambdaUpdate()
                    .set(ScheduleTask::getScheduleFlag, "0")
                    .set(ScheduleTask::getLatestScheduleDate, now.toLocalDate().toString())
                    .set(ScheduleTask::getScheduleStatus, scheduleStatus)
                    .set(ScheduleTask::getUpdateTime, now)
                    .set(scheduledPlanNum > 0, ScheduleTask::getScheduledPlanNum, scheduledPlanNum)
                    .eq(ScheduleTask::getId, tdo.getTaskId()));

            return ResultUtil.success("任务调度成功！");
        }else{
            this.update(Wrappers.<ScheduleTask>lambdaUpdate()
                    .set(ScheduleTask::getScheduleFlag, "1")
                    .eq(ScheduleTask::getId, tdo.getTaskId()));
            return ResultUtil.success("任务保存成功！");
        }
    }

    @Override
    public List<ScheduleJobFactorVo> getJobFactorsByJobId(String jobId) {
        List<ScheduleJobFactorVo> vos = jobPlanFactorMapper.getJobFactorsByJobId(jobId);
        vos.forEach(item -> {
            item.setSecdClassName(dictUtils.getFactorClassMap().get(item.getSecdClassId()));
        });
        return vos;
    }

}
