package org.hj.chain.platform.check.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.CodeBuildUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.check.entity.CheckFactorAuditRecord;
import org.hj.chain.platform.check.entity.CheckFactorInfo;
import org.hj.chain.platform.check.entity.CheckFactorSubset;
import org.hj.chain.platform.check.entity.CheckTask;
import org.hj.chain.platform.check.mapper.CheckFactorInfoMapper;
import org.hj.chain.platform.check.mapper.CheckFactorSubsetMapper;
import org.hj.chain.platform.check.mapper.CheckTaskMapper;
import org.hj.chain.platform.check.service.ICheckFactorAuditRecordService;
import org.hj.chain.platform.check.service.ICheckFactorInfoService;
import org.hj.chain.platform.check.service.ICheckFactorSubsetService;
import org.hj.chain.platform.check.service.ICheckTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.common.service.ICommonService;
import org.hj.chain.platform.component.DictUtils;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.report.mapper.ReportInfoMapper;
import org.hj.chain.platform.report.model.ReportInfo;
import org.hj.chain.platform.sample.entity.SampleItem;
import org.hj.chain.platform.sample.entity.SampleItemFactorData;
import org.hj.chain.platform.sample.mapper.SampleItemFactorDataMapper;
import org.hj.chain.platform.sample.mapper.SampleItemMapper;
import org.hj.chain.platform.schedule.entity.ScheduleJobPlanFactor;
import org.hj.chain.platform.schedule.entity.ScheduleJobPlanFactorSubset;
import org.hj.chain.platform.schedule.mapper.ScheduleJobPlanFactorMapper;
import org.hj.chain.platform.schedule.mapper.ScheduleJobPlanFactorSubsetMapper;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.service.ISysDeptService;
import org.hj.chain.platform.service.ISysRoleService;
import org.hj.chain.platform.vo.FactorMethodInfoVo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserParamVo;
import org.hj.chain.platform.vo.check.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 检测任务信息 服务实现类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Slf4j
@Service
public class CheckTaskServiceImpl extends ServiceImpl<CheckTaskMapper, CheckTask> implements ICheckTaskService {

    @Autowired
    private ICheckFactorInfoService checkFactorInfoService;
    @Autowired
    private CheckFactorInfoMapper checkFactorInfoMapper;
    @Autowired
    private ICheckFactorSubsetService checkFactorSubsetService;
    @Autowired
    private CheckFactorSubsetMapper checkFactorSubsetMapper;
    @Autowired
    private IFactorService factorService;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ICheckFactorAuditRecordService checkFactorAuditRecordService;
    @Autowired
    private ScheduleJobPlanFactorMapper jobPlanFactorMapper;
    @Autowired
    private ScheduleJobPlanFactorSubsetMapper scheduleJobPlanFactorSubsetMapper;
    @Autowired
    private SampleItemFactorDataMapper sampleItemFactorDataMapper;
    @Autowired
    private SampleItemMapper sampleItemMapper;
    @Autowired
    private ReportInfoMapper reportInfoMapper;
    @Autowired
    private DictUtils dictUtils;

    @Override
    public Result<IPage<CheckTaskInfoVo>> findByCondition(PageVo pageVo, CheckTaskInfoSearchVo sv, String reqPath) {
        sv.setJobId(StrUtil.trimToNull(sv.getJobId()));
        sv.setCheckTaskStatus(StrUtil.trimToNull(sv.getCheckTaskStatus()));
        sv.setProjectName(StrUtil.trimToNull(sv.getProjectName()));
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        String userId = loginOutputVo.getUserId();
        String dataScope = sysRoleService.findMaxRoleScopeByPerms(userId, reqPath);
        Page<CheckTaskInfoVo> page = PageUtil.initMpPage(pageVo);
        if(dataScope != null) {
            if(dataScope.equals("1")) {
                this.baseMapper.findByCondition(page, organId, sv);
            }else{
                List<Long> deptIds = null;
                if(dataScope.equals("2")) {
                    deptIds = sysDeptService.selectChildDeptIdsByDeptId(loginOutputVo.getDeptId());
                }
                if(deptIds == null) {
                    deptIds = new ArrayList<>();
                }
                deptIds.add(loginOutputVo.getDeptId());
                this.baseMapper.findForDeptByCondition(page, organId, deptIds, sv);
            }
        }
        return ResultUtil.data(page);
    }

    @Override
    public Result<List<CheckTaskFactorVo>> getOfferFactorsByTaskId(Long checkTaskId) {
        CheckTask cti = this.getById(checkTaskId);
        if(cti == null) {
            return ResultUtil.busiError("检测任务不存在！");
        }
        List<CheckTaskFactorVo> vos = jobPlanFactorMapper.getOfferFactorsByJobId(cti.getJobId());
        if(vos != null && !vos.isEmpty()) {
            vos.forEach(item -> {
                item.setSecdClassName(dictUtils.getFactorClassMap().get(item.getSecdClassId()));
            });
        }
        return ResultUtil.data(vos);
    }

    @Override
    @Transactional
    public Result<Object> assignmentsFactor(String param) {
        if(StrUtil.isBlank(param)) {
            return ResultUtil.validateError("因子分配参数不能为空！");
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LocalDateTime now = LocalDateTime.now();
        JSONObject paramInfo = JSON.parseObject(param);
        Long checkTaskId = paramInfo.getLong("checkTaskId");

        List<CheckFactorInfo> checkFactors = checkFactorInfoService.list(Wrappers.<CheckFactorInfo>lambdaQuery()
                .select(CheckFactorInfo::getId).eq(CheckFactorInfo::getCheckTaskId, checkTaskId));
        if(checkFactors != null && !checkFactors.isEmpty()) {
            return ResultUtil.busiError("任务已分配，请勿重复操作！");
        }

        CheckTask cti = this.getById(checkTaskId);
        JSONArray p = paramInfo.getJSONArray("params");
        List<String> sampStatus = Arrays.asList("6","7","8");
        Set<String> set = new HashSet<>();
        for(int i = 0; i < p.size(); i++) {
            JSONObject item = p.getJSONObject(i);
            String checkUser = item.getString("checkUser");
            List<Long> jobPlanFactorIds = Arrays.asList(item.getString("jobPlanFactorId").split(","))
                    .stream().map(Long::parseLong).collect(Collectors.toList());
            for(Long jobPlanFactorId : jobPlanFactorIds) {
                ScheduleJobPlanFactor jobPlanFactor = jobPlanFactorMapper.selectById(jobPlanFactorId);
                FactorMethodInfoVo vo = factorService.findFactorMethodById(jobPlanFactor.getCheckStandardId());
                List<SampleItemFactorData> sampleFactors = sampleItemFactorDataMapper.selectList(Wrappers.<SampleItemFactorData>lambdaQuery()
                        .eq(SampleItemFactorData::getJobPlanFactorId, jobPlanFactorId));
                List<Long> sampItemIds = sampleFactors.stream().map(SampleItemFactorData::getSampleItemId).collect(Collectors.toList());
                if(sampItemIds != null && !sampItemIds.isEmpty()) {
                    for(Long sampItemId : sampItemIds){
                        CheckFactorInfo cfi = new CheckFactorInfo();
                        cfi.setAssignUserId(checkUser);
                        cfi.setCheckTaskId(checkTaskId);
                        cfi.setJobPlanFactorId(jobPlanFactorId);
                        cfi.setCreateTime(now);
                        cfi.setSampleItemId(sampItemId);
                        cfi.setDataEntryStep(vo.getDataEntryStep());
                        SampleItem sii = sampleItemMapper.selectById(sampItemId);
                        String checkStatus = "0";
                        if(sampStatus.contains(sii.getSampleStatus())) {
                            if(sii.getFbFlag().equals("1")) {
                                checkStatus = "2";
                            }else {
                                if(cfi.getDataEntryStep().equals("1")) {
                                    checkStatus = "3";
                                }
                            }
                        }
                        if(sii.getSampleStatus().equals("4") && sii.getStoreFlag().equals("0")) {
                            if(cfi.getDataEntryStep().equals("1")) {
                                checkStatus = "5";
                            }else {
                                checkStatus = "3";
                            }
                        }
                        cfi.setCheckStatus(checkStatus);
                        if("1".equals(jobPlanFactor.getIsFactor())) {
                            //同系物
                            List<ScheduleJobPlanFactorSubset> jobPlanFactorSubsets = scheduleJobPlanFactorSubsetMapper
                                    .selectList(Wrappers.<ScheduleJobPlanFactorSubset>lambdaQuery()
                                            .eq(ScheduleJobPlanFactorSubset::getJobPlanFactorId, jobPlanFactorId));
                            checkFactorInfoService.save(cfi);
                            List<CheckFactorSubset> checkFactorSubsets = jobPlanFactorSubsets.stream().map(d -> {
                                CheckFactorSubset checkFactorSubset = new CheckFactorSubset();
                                checkFactorSubset.setCheckFactorId(cfi.getId());
                                checkFactorSubset.setCheckStandardId(d.getCheckStandardId());
                                return checkFactorSubset;
                            }).collect(Collectors.toList());
                            checkFactorSubsetService.saveBatch(checkFactorSubsets);
                        }else{
                            checkFactorInfoService.save(cfi);
                        }
                        if(cfi.getDataEntryStep().equals("2") && sii.getFbFlag().equals("0")) {
                            set.add(sampItemId + "," + cfi.getAssignUserId());
                        }
                    }
                }else{
                    //分包因子不要领样，直接录入检测数据
                    CheckFactorInfo cfi = new CheckFactorInfo();
                    cfi.setAssignUserId(checkUser);
                    cfi.setCheckTaskId(checkTaskId);
                    cfi.setJobPlanFactorId(jobPlanFactorId);
                    cfi.setCheckStatus("2");
                    cfi.setDataEntryStep("2");
                    cfi.setCreateTime(now);
                    checkFactorInfoService.save(cfi);
                }
            }
        }
        List<String> sampItemIds = set.stream().map(s -> s.split(",")[0]).collect(Collectors.toList());
        Map<String, Long> map = sampItemIds.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
        map.forEach((k, v) -> {
            sampleItemMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                    .set(SampleItem::getAvalDrawCount, v.intValue())
                    .eq(SampleItem::getId, k));
        });
        List<CheckFactorInfo> list = checkFactorInfoService
                .list(Wrappers.<CheckFactorInfo>lambdaQuery()
                        .eq(CheckFactorInfo::getCheckTaskId, checkTaskId)
                        .ne(CheckFactorInfo::getCheckStatus, "5"));
        String taskStatus = "1";
        if(list == null || list.isEmpty()) {
            taskStatus = "2";
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
        this.baseMapper.update(null, Wrappers.<CheckTask>lambdaUpdate()
                .set(CheckTask::getTaskStatus, taskStatus)
                .set(CheckTask::getUpdateTime, now)
                .set(CheckTask::getDeptId, loginOutputVo.getDeptId())
                .eq(CheckTask::getId, checkTaskId));
        return ResultUtil.success("因子分配成功！");
    }

    @Override
    public Result<IPage<CheckFactorInfoVo>> getCheckFactorByCondition(PageVo pageVo, CheckFactorSearchVo sv, Long checkTaskId) {
        sv.setCheckStatus(StrUtil.trimToNull(sv.getCheckStatus()));
        sv.setFactorName(StrUtil.trimToNull(sv.getFactorName()));
        sv.setSampleNo(StrUtil.trimToNull(sv.getSampleNo()));
        Page<CheckFactorInfoVo> page = PageUtil.initMpPage(pageVo);
        checkFactorInfoMapper.getCheckFactorByCondition(page, sv, checkTaskId);
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            page.getRecords().forEach(p -> {
                p.setDayAndCount(String.format("第%s天第%s次", p.getDay(), p.getFrequency()));
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
                    p.setStandardNo(factorSubsetVos.get(0).getStandardNo()).setStandardName(factorSubsetVos.get(0).getStandardName());
                    p.setFactorSubsetVos(factorSubsetVos);
                }else{
                    FactorMethodInfoVo vo = factorService.findFactorMethodById(p.getCheckStandardId());
                    p.setStandardNo(vo.getStandardNo()).setStandardName(vo.getStandardName()).setUnitName(vo.getDefaultUnitName());
                }
            });
        }
        return ResultUtil.data(page);
    }

    @Override
    public Result<List<CheckUserVo>> getCheckUsers() {
        List<UserParamVo> users = commonService.findUsersByDeptId().getResult();
        List<CheckUserVo> vos = checkFactorInfoMapper.getCheckUserFactors();
        Map<String, Integer> map = new HashMap<>();
        if(vos != null && !vos.isEmpty()) {
            vos.forEach(v -> map.put(v.getUserId(), v.getCnt()));
        }
        List<CheckUserVo> checkUserVos = new ArrayList<>();
        if(users != null && !users.isEmpty()) {
            checkUserVos = users.stream().map(u -> {
                CheckUserVo vo = new CheckUserVo();
                BeanUtils.copyProperties(u, vo);
                if(map.containsKey(vo.getUserId())) {
                    vo.setCnt(map.get(vo.getUserId()));
                }else{
                    vo.setCnt(0);
                }
                return vo;
            }).collect(Collectors.toList());
        }
        return ResultUtil.data(checkUserVos);
    }

    @Override
    public Result<IPage<CheckTaskVo>> findCheckTaskByCondition(PageVo pageVo, CheckTaskSearchVo sv, String reqPath) {
        sv.setJobId(StrUtil.trimToNull(sv.getJobId()));
        sv.setCheckTaskStatus(StrUtil.trimToNull(sv.getCheckTaskStatus()));
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        Page<CheckTaskVo> page = PageUtil.initMpPage(pageVo);
        String dataScope = sysRoleService.findMaxRoleScopeByPerms(loginOutputVo.getUserId(), reqPath);
        if(dataScope != null) {
            if(dataScope.equals("4")) {
                this.baseMapper.findCheckTaskForEmpByCondition(page, loginOutputVo.getUserId(), sv);
            }else if(dataScope.equals("1")){
                this.baseMapper.findCheckTaskForManageByCondition(page, loginOutputVo.getOrganId(), sv);
            }else{
                List<Long> deptIds = null;
                if(dataScope.equals("2")) {
                    deptIds = sysDeptService.selectChildDeptIdsByDeptId(loginOutputVo.getDeptId());
                }
                if(deptIds == null) {
                    deptIds = new ArrayList<>();
                }
                deptIds.add(loginOutputVo.getDeptId());
                this.baseMapper.findCheckTaskForDeptByCondition(page, deptIds, sv);
            }
        }
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            page.getRecords().forEach(item -> {
                List<CheckFactorInfo> cfis = checkFactorInfoService.list(Wrappers.<CheckFactorInfo>lambdaQuery()
                        .select(CheckFactorInfo::getId, CheckFactorInfo::getCheckTaskId, CheckFactorInfo::getCheckStatus)
                        .eq(CheckFactorInfo::getCheckTaskId, item.getCheckTaskId())
                        .eq(dataScope.equals("4"), CheckFactorInfo::getAssignUserId, loginOutputVo.getUserId()));
                item.setCheckFactorCount(cfis.size());
                Integer completeCheckFactorCount = cfis.stream().filter(s -> "5".contains(s.getCheckStatus())).collect(Collectors.toList()).size();
                item.setCompleteCheckFactorCount(completeCheckFactorCount);
            });
        }
        return ResultUtil.data(page);
    }

    @Override
    public Result<IPage<CheckFactorInfoVo>> findCheckTaskDetailByCondition(PageVo pageVo, CheckFactorSearchVo sv, String reqPath, String type) {
        sv.setCheckStatus(StrUtil.trimToNull(sv.getCheckStatus()));
        sv.setFactorName(StrUtil.trimToNull(sv.getFactorName()));
        sv.setSampleNo(StrUtil.trimToNull(sv.getSampleNo()));
        Page<CheckFactorInfoVo> page = PageUtil.initMpPage(pageVo);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String userId = loginOutputVo.getUserId();
        String organId = loginOutputVo.getOrganId();
        String dataScope = sysRoleService.findMaxRoleScopeByPerms(userId, reqPath);
        List<String> statusList = null;
        if("0".equals(type)) {
            statusList = Arrays.asList("0","1","2","6");
        }else if("1".equals(type)) {
            statusList = Arrays.asList("3","4","5");
        }
        if(dataScope != null) {
            if(dataScope.equals("4")) {
                checkFactorInfoMapper.findCheckTaskDetailForEmpByCondition(page, userId, organId, sv, statusList);
            }else{
                checkFactorInfoMapper.findCheckTaskDetailForManageByCondition(page, organId, sv, statusList);
            }
            if (page.getRecords() != null && !page.getRecords().isEmpty()) {
                page.getRecords().forEach(p -> {
                    p.setDayAndCount(String.format("第%s天第%s次", p.getDay(), p.getFrequency()));
                    FactorMethodInfoVo fmiVo = factorService.findFactorMethodById(p.getCheckStandardId());
                    p.setStandardNo(fmiVo.getStandardNo()).setStandardName(fmiVo.getStandardName()).setUnitName(fmiVo.getDefaultUnitName());
                    if("1".equals(p.getIsFactor())) {
                        List<CheckFactorSubset> factorSubsets = checkFactorSubsetMapper.selectList(Wrappers.<CheckFactorSubset>lambdaQuery()
                                .eq(CheckFactorSubset::getCheckFactorId, p.getCheckFactorId()));
                        if(factorSubsets != null && !factorSubsets.isEmpty()) {
                            List<CheckFactorSubsetVo> factorSubsetVos = factorSubsets.stream().map(item -> {
                                CheckFactorSubsetVo factorSubsetVo = new CheckFactorSubsetVo();
                                factorSubsetVo.setId(item.getId()).setCheckStandardId(item.getCheckStandardId()).setCheckSubRes(item.getCheckSubRes());
                                FactorMethodInfoVo vo = factorService.findFactorMethodById(factorSubsetVo.getCheckStandardId());
                                factorSubsetVo.setFactorName(vo.getFactorName()).setUnitName(vo.getDefaultUnitName())
                                        .setStandardNo(vo.getStandardNo()).setStandardName(vo.getStandardName());
                                return factorSubsetVo;
                            }).collect(Collectors.toList());
                            p.setStandardNo(factorSubsetVos.get(0).getStandardNo()).setStandardName(factorSubsetVos.get(0).getStandardName());
                            p.setFactorSubsetVos(factorSubsetVos);
                        }
                    }
                    if("6".equals(p.getCheckStatus())) {
                        CheckFactorAuditRecord record = checkFactorAuditRecordService.getOne(Wrappers.<CheckFactorAuditRecord>lambdaQuery()
                                .eq(CheckFactorAuditRecord::getCheckFactorId, p.getCheckFactorId())
                                .orderByDesc(CheckFactorAuditRecord::getAuditTime), false);
                        if(record != null) {
                            p.setAuditReason(record.getAuditReason());
                        }
                    }
                });
            }
        }
        return ResultUtil.data(page);
    }
}
