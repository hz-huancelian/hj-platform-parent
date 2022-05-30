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
import org.hj.chain.platform.CodeBuildUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.check.entity.CheckTask;
import org.hj.chain.platform.check.mapper.CheckTaskMapper;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.report.mapper.ReportInfoMapper;
import org.hj.chain.platform.report.model.ReportInfo;
import org.hj.chain.platform.sample.entity.*;
import org.hj.chain.platform.sample.mapper.SampleItemMapper;
import org.hj.chain.platform.sample.mapper.SampleListMapper;
import org.hj.chain.platform.sample.mapper.SampleTaskMapper;
import org.hj.chain.platform.sample.service.*;
import org.hj.chain.platform.schedule.entity.ScheduleJobPlanFactor;
import org.hj.chain.platform.schedule.mapper.ScheduleJobMapper;
import org.hj.chain.platform.schedule.mapper.ScheduleJobPlanFactorMapper;
import org.hj.chain.platform.service.ISysDeptService;
import org.hj.chain.platform.service.ISysRoleService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserVo;
import org.hj.chain.platform.vo.sample.*;
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
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Service
public class SampleTaskServiceImpl extends ServiceImpl<SampleTaskMapper, SampleTask> implements ISampleTaskService {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ScheduleJobPlanFactorMapper jobPlanFactorMapper;
    @Autowired
    private SampleListMapper sampleListMapper;
    @Autowired
    private ScheduleJobMapper jobMapper;
    @Autowired
    private SampleItemMapper sampleItemMapper;
    @Autowired
    private ISampleItemFactorDataService sampleItemFactorDataService;
    @Autowired
    private CheckTaskMapper checkTaskMapper;
    @Autowired
    private ISampleTaskPointService sampleTaskPointService;
    @Autowired
    private ISampleItemAuditRecordService sampleItemAuditRecordService;
    @Autowired
    private ReportInfoMapper reportInfoMapper;
    @Override
    public Result<IPage<SampleTaskVo>> findByCondition(PageVo pageVo, SampleTaskSearchVo sv, String reqPath) {
        sv.setJobId(StrUtil.trimToNull(sv.getJobId()));
        sv.setSampTaskStatus(StrUtil.trimToNull(sv.getSampTaskStatus()));
        sv.setProjectName(StrUtil.trimToNull(sv.getProjectName()));
        Page<SampleTaskVo> page = PageUtil.initMpPage(pageVo);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String userId = loginOutputVo.getUserId();
        String organId = loginOutputVo.getOrganId();
        String dataScope = sysRoleService.findMaxRoleScopeByPerms(userId, reqPath);
        if(dataScope != null) {
            if (dataScope.equals("4")) {
                this.baseMapper.findForLeaderByCondition(page, userId, sv);
            } else if(dataScope.equals("1")){
                this.baseMapper.findForManageByCondition(page, organId, sv);
            }else{
                List<Long> deptIds = null;
                if(dataScope.equals("2")) {
                    deptIds = sysDeptService.selectChildDeptIdsByDeptId(loginOutputVo.getDeptId());
                }
                if(deptIds == null) {
                    deptIds = new ArrayList<>();
                }
                deptIds.add(loginOutputVo.getDeptId());
                this.baseMapper.findByCondition(page, organId, deptIds, sv);
            }
            if(page.getRecords() != null && !page.getRecords().isEmpty()) {
                page.getRecords().forEach(p -> {
                    if(StrUtil.isNotBlank(p.getManagerUserId())) {
                        SysUser sysUser = sysUserService.getById(p.getManagerUserId());
                        p.setManagerUser(sysUser.getEmpName());
                    }
                });
            }
        }
        return ResultUtil.data(page);
    }

    @Override
    public List<JobFactorVo> getJobFactorsByJobId(String jobId) {
        JobFactorSearchVo sv = new JobFactorSearchVo();
        sv.setJobId(jobId);
        return jobPlanFactorMapper.getJobFactorsByCondition(sv);
    }

    @Override
    public List<String> getFactorPointsByJobId(String jobId) {
        List<String> factorPoints = new ArrayList<>();
        JobFactorSearchVo sv = new JobFactorSearchVo();
        sv.setJobId(jobId);
        List<JobFactorVo> factorVos = jobPlanFactorMapper.getJobFactorsByCondition(sv);
        if(factorVos != null && !factorVos.isEmpty()) {
            factorPoints = factorVos.stream().map(JobFactorVo::getFactorPoint).distinct().collect(Collectors.toList());
        }
        return factorPoints;
    }

    @Override
    public List<JobFactorVo> getJobFactorsByCondition(JobFactorSearchVo sv) {
        sv.setFactorName(StrUtil.trimToNull(sv.getFactorName()));
        sv.setFactorPoint(StrUtil.trimToNull(sv.getFactorPoint()));
        return jobPlanFactorMapper.getJobFactorsByCondition(sv);
    }

    @Override
    public List<SampleList> getSampleListByCondition(Long sampTaskId, String fbFlag) {
        List<SampleList> list = new ArrayList<>();
        list = sampleListMapper.selectList(Wrappers.<SampleList>lambdaQuery()
                .eq(SampleList::getSampleTaskId, sampTaskId).eq(SampleList::getFbFlag, fbFlag));
        return list;
    }

    @Override
    @Transactional
    public Result<Object> saveSampleList(List<SampleList> list) {
        if(list != null && !list.isEmpty()) {
            String jobId = list.get(0).getJobId();
            Long sampleTaskId = list.get(0).getSampleTaskId();
            SampleTask sampleTask = this.getById(sampleTaskId);
            List<String> status = Arrays.asList("2","3");
            if(status.contains(sampleTask.getTaskStatus())) {
                return ResultUtil.busiError("已完成合样确认，操作失败！");
            }
            for(SampleList sampleList : list) {
                if(StrUtil.isBlank(sampleList.getSampleUserId())) {
                    return ResultUtil.validateError("保存失败，样品未指定采样组长！");
                }
            }
            //删除当前采样任务合样列表快照
            sampleListMapper.delete(Wrappers.<SampleList>lambdaQuery().eq(SampleList::getSampleTaskId, sampleTaskId));
            //重置任务下所有因子合样状态为“未合样”
            int cnt = jobPlanFactorMapper.update(null, Wrappers.<ScheduleJobPlanFactor>lambdaUpdate()
                    .set(ScheduleJobPlanFactor::getHyFlag, "0").eq(ScheduleJobPlanFactor::getJobId, jobId));
            if(cnt >= 0) {
                list.forEach(item -> {
                    if(item.getId() == null) {
                        //更新对应任务计划因子合样标志为"已合样"
                        List<Long> jobPlanFactorIds = Arrays.stream(item.getJobPlanFactorId().split(","))
                                .map(v -> Long.parseLong(v)).collect(Collectors.toList());
                        jobPlanFactorMapper.update(null, Wrappers.<ScheduleJobPlanFactor>lambdaUpdate()
                                .set(ScheduleJobPlanFactor::getHyFlag, "1").in(ScheduleJobPlanFactor::getId, jobPlanFactorIds));
                    }else{
                        item.setId(null);
                    }
                    sampleListMapper.insert(item);
                });
            }
            //检查更新采样任务状态
            JobFactorSearchVo sv = new JobFactorSearchVo();
            sv.setJobId(jobId);
            sv.setHyFlag("0");
            List<JobFactorVo> factorVos = jobPlanFactorMapper.getJobFactorsByCondition(sv);
            if(factorVos == null || factorVos.isEmpty()) {
                //不存在“未合样”的因子，更新采样任务状态为“已合样”
                this.baseMapper.update(null, Wrappers.<SampleTask>lambdaUpdate()
                        .set(SampleTask::getTaskStatus, "1").eq(SampleTask::getId, sampleTaskId));
            }
            return ResultUtil.success("保存成功！");
        }else{
            return ResultUtil.validateError("至少包含一条合样信息！");
        }
    }

    @Override
    @Transactional
    public Result<Object> deleteSampleListById(Long sampleListId) {
        SampleList sampleList = sampleListMapper.selectById(sampleListId);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<String> roles = loginOutputVo.getRoles();
        if(!roles.contains("Pm")) {
            if(!sampleList.getSampleUserId().equals(loginOutputVo.getUserId())) {
                return ResultUtil.busiError("组长无法删除非自己的合样列表信息！");
            }
        }
        if(sampleList == null) {
            return ResultUtil.busiError("合样列表信息不存在！");
        }
        //更新对应任务计划因子合样标志为"未合样"
        List<Long> jobPlanFactorIds = Arrays.stream(sampleList.getJobPlanFactorId().split(","))
                .map(v -> Long.parseLong(v)).collect(Collectors.toList());
        int cnt = jobPlanFactorMapper.update(null, Wrappers.<ScheduleJobPlanFactor>lambdaUpdate()
                .set(ScheduleJobPlanFactor::getHyFlag, "0").in(ScheduleJobPlanFactor::getId, jobPlanFactorIds));
        if(cnt > 0) {
            sampleListMapper.deleteById(sampleListId);
            //采样任务状态更新为“待合样”
            this.baseMapper.update(null, Wrappers.<SampleTask>lambdaUpdate()
                    .set(SampleTask::getTaskStatus, "0").eq(SampleTask::getId, sampleList.getSampleTaskId()));
        }
        return ResultUtil.success("删除成功！");
    }

    @Override
    @Transactional
    public Result<Object> confirmSampleList(Long sampTaskId) {
        LocalDateTime now = LocalDateTime.now();
        SampleTask sampleTask = this.getById(sampTaskId);
        if(sampleTask == null) {
            return ResultUtil.busiError("采样任务信息不存在！");
        }
        if(!sampleTask.getTaskStatus().equals("1")) {
            return ResultUtil.busiError("采样任务因子尚未完成合样！");
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<String> roles = loginOutputVo.getRoles();
        if(!roles.contains("Pm")) {
            return ResultUtil.busiError("权限不足，需采样负责人确认！");
        }
        //生成样品
        List<SampleList> sampleLists = sampleListMapper.selectList(Wrappers.<SampleList>lambdaQuery().eq(SampleList::getSampleTaskId, sampTaskId));
        String organId = loginOutputVo.getOrganId();
        Long deptId = loginOutputVo.getDeptId();
        Map<String, Integer> map = new HashMap<>();
        List<SampleItemFactorData> sifs = new ArrayList<>();
        //默认检测任务状态为“已完成”
        String checkTaskStatus = "2";
        for(int i = 0; i < sampleLists.size(); i++) {
            SampleList sampleList = sampleLists.get(i);
            int pointSort = 1;
            List<Long> jobPlanFactorIds = Arrays.stream(sampleList.getJobPlanFactorId().split(","))
                    .map(v -> Long.parseLong(v)).collect(Collectors.toList());
            List<ScheduleJobPlanFactor> sjpfs = jobPlanFactorMapper.selectList(Wrappers.<ScheduleJobPlanFactor>lambdaQuery()
                    .select(ScheduleJobPlanFactor::getId, ScheduleJobPlanFactor::getDataEntryStep)
                    .in(ScheduleJobPlanFactor::getId, jobPlanFactorIds));
            //样品因子有实验室录入的，需要入库
            String storeFlag = sjpfs.stream().filter(item -> "2".equals(item.getDataEntryStep())).collect(Collectors.toList()).size() > 0 ? "1" : "0";
            if("1".equals(storeFlag)) {
                //存在需要入库的样品，检测任务初始状态为“待分配”
                checkTaskStatus = "0";
            }
            String contCode = jobMapper.getContCodeByJobId(sampleTask.getJobId());
            String sampleNo = CodeBuildUtil.genSampleCode(contCode.substring(contCode.length() - 4), sampleList.getSecdClassId());
            if (map.containsKey(sampleNo)) {
                pointSort = map.get(sampleNo);
            } else {
                SampleItem sampleItem = sampleItemMapper.fuzzyQrySampItemByCondition(sampleNo, organId);
                if (sampleItem != null) {
                    pointSort = Integer.parseInt(sampleItem.getSampleNo().split(sampleNo)[1]) + 1;
                    map.put(sampleNo, pointSort);
                }
            }
            for(int j = 1; j <= sampleList.getDayCount(); j ++) {
                StringBuilder groupKey = new StringBuilder();
                groupKey.append(sampleNo).append("-").append(i).append("-").append(j);
                for(int k = 1; k <= sampleList.getFrequency(); k++) {
                    SampleItem si = new SampleItem();
                    si.setFactorPoint(sampleList.getFactorPoint())
                            .setOrganId(organId)
                            .setSecdClassId(sampleList.getSecdClassId())
                            .setSecdClassName(sampleList.getSecdClassName())
                            .setCreateTime(now).setSampleStatus("0")
                            .setSampleTaskId(sampTaskId)
                            .setSampleUserId(sampleList.getSampleUserId())
                            .setDay(j)
                            .setFrequency(k)
                            .setFbFlag(sampleList.getFbFlag())
                            .setGroupKey(groupKey.toString())
                            .setFactorGroupKey(sampleList.getFactorGroupKey());
                    si.setSampleNo(sampleNo + String.format("%04d", pointSort));
                    si.setStoreFlag(storeFlag);
                    sampleItemMapper.insert(si);
                    SampleItemData sid = new SampleItemData();
                    sid.setSampleItemId(si.getId());
                    for(Long jobPlanFactorId : jobPlanFactorIds) {
                        SampleItemFactorData sif = new SampleItemFactorData();
                        sif.setSampleItemId(si.getId());
                        sif.setJobPlanFactorId(jobPlanFactorId);
                        sifs.add(sif);
                    }
                    pointSort++;
                    map.put(sampleNo, pointSort);
                }
            }
        }
        sampleItemFactorDataService.saveBatch(sifs);
        //创建检样任务
        CheckTask cti = new CheckTask();
        cti.setOrganId(organId);
        cti.setJobId(sampleTask.getJobId());
        cti.setCreateTime(now);
        cti.setTaskStatus(checkTaskStatus);
        checkTaskMapper.insert(cti);
        if("2".equals(checkTaskStatus)) {
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

        //更新采样任务状态
        this.update(Wrappers.<SampleTask>lambdaUpdate()
                .set(SampleTask::getTaskStatus, "2")
                .set(SampleTask::getMergeTime, now)
                .set(SampleTask::getUpdateTime, now)
                .set(SampleTask::getDeptId, deptId)
                .set(SampleTask::getManagerUserId, loginOutputVo.getUserId())
                .eq(SampleTask::getId, sampTaskId));

        return ResultUtil.success("合样成功！");
    }

    @Override
    @Transactional
    public Result<Object> assignFactorPoint(String param) {
        JSONObject paramInfo = JSON.parseObject(param);
        Long sampTaskId = paramInfo.getLong("sampTaskId");
        JSONArray params = paramInfo.getJSONArray("params");
        //清楚原位置分配信息
        sampleTaskPointService.remove(Wrappers.<SampleTaskPoint>lambdaQuery().eq(SampleTaskPoint::getSampleTaskId, sampTaskId));
        if(!params.isEmpty()) {
            List<SampleTaskPoint> stps = new ArrayList<>();
            for(int i = 0; i < params.size(); i++) {
                JSONObject object = params.getJSONObject(i);
                String factorPoint = object.getString("factorPoint");
                String[] sampleUserIds = object.getString("sampleUserId").split(",");
                for(String sampleUserId : sampleUserIds) {
                    SampleTaskPoint stp = new SampleTaskPoint();
                    stp.setSampleTaskId(sampTaskId);
                    stp.setFactorPoint(factorPoint);
                    stp.setSampleUserId(sampleUserId);
                    stps.add(stp);
                }
            }
            sampleTaskPointService.saveBatch(stps);
        }
        return ResultUtil.success("采样位置分配成功！");
    }

    @Override
    public List<SampleTaskPointVo> getSampleTaskPointBySampTaskId(Long sampTaskId) {
        SampleTask sampleTask = this.getById(sampTaskId);
        List<SampleTaskPointVo> vos = new ArrayList<>();
        if(sampleTask != null) {
            List<String> factorPoints = getFactorPointsByJobId(sampleTask.getJobId());
            vos = factorPoints.stream().map(item -> {
                SampleTaskPointVo dbVo = this.baseMapper.getSampleTaskPointBySampTaskIdAndFactorPoint(sampTaskId, item);
                SampleTaskPointVo vo = new SampleTaskPointVo();
                vo.setSampleTaskId(sampTaskId).setFactorPoint(item);
                if(dbVo != null) {
                    vo.setSampleUsers(dbVo.getSampleUsers());
                    vo.setSampleUserIds(dbVo.getSampleUserIds());
                }
                return vo;
            }).collect(Collectors.toList());
        }


        return vos;
    }

    @Override
    public Result<Object> saveCombinedRemark(String param) {
        JSONObject paramInfo = JSON.parseObject(param);
        Long sampTaskId = paramInfo.getLong("sampTaskId");
        String combinedRemark = paramInfo.getString("combinedRemark");
        this.baseMapper.update(null, Wrappers.<SampleTask>lambdaUpdate()
                .set(SampleTask::getCombinedRemark, combinedRemark)
                .eq(SampleTask::getId, sampTaskId));
        return ResultUtil.success("合样备注保存成功！");
    }

    @Override
    public IPage<SampleTaskItemVo> getSampleItemBySampTaskId(PageVo pageVo, Long sampTaskId, String reqPath) {
        Page<SampleTaskItemVo> page = PageUtil.initMpPage(pageVo);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String userId = loginOutputVo.getUserId();
        String dataScope = sysRoleService.findMaxRoleScopeByPerms(userId, reqPath);
        if(dataScope != null) {
            if (dataScope.equals("4")) {
                sampleItemMapper.getSampTaskItemByTaskIdAndUserId(page, sampTaskId, userId);
            } else {
                sampleItemMapper.getSampTaskItemById(page, sampTaskId);
            }
            if (page.getRecords() != null && !page.getRecords().isEmpty()) {
                page.getRecords().forEach(p -> {
                    Long sampItemId = p.getSampItemId();
                    List<JobFactorVo> jobFactorVos = jobPlanFactorMapper.getJobFactorsBySampItemId(sampItemId);
                    String factorNames = jobFactorVos.stream().map(JobFactorVo::getFactorName)
                            .collect(Collectors.joining("/"));
                    p.setFactorName(factorNames);
                    p.setDayAndCount(String.format("第%s天第%s次", p.getDay(), p.getFrequency()));
                });
            }
        }
        return page;
    }


    @Override
    public IPage<SampleTaskListVo> findSampTaskByCondition(PageVo pageVo, SampleTaskSearchVo sv, String reqPath) {
        sv.setJobId(StrUtil.trimToNull(sv.getJobId()));
        sv.setSampTaskStatus(StrUtil.trimToNull(sv.getSampTaskStatus()));
        SaSession session = StpUtil.getSession();
        Page<SampleTaskListVo> page = PageUtil.initMpPage(pageVo);
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String userId = loginOutputVo.getUserId();
        String dataScope = sysRoleService.findMaxRoleScopeByPerms(userId, reqPath);
        String organId = loginOutputVo.getOrganId();
        if(dataScope != null) {
            if (dataScope.equals("4")) {
                this.baseMapper.findSampTaskForLeaderByCondition(page, userId, sv);

            } else if(dataScope.equals("1")) {
                this.baseMapper.findSampTaskForManageByCondition(page, organId, sv);
            }else {
                List<Long> deptIds = null;
                if(dataScope.equals("2")) {
                    deptIds = sysDeptService.selectChildDeptIdsByDeptId(loginOutputVo.getDeptId());
                }
                if(deptIds == null) {
                    deptIds = new ArrayList<>();
                }
                deptIds.add(loginOutputVo.getDeptId());
                this.baseMapper.findSampTasksByCondition(page, organId, deptIds, sv);
            }
        }
        if (page.getRecords() != null && !page.getRecords().isEmpty()) {
            List<String> sampStauts = Arrays.asList("4", "6", "7", "8", "9");
            page.getRecords().forEach(item -> {
                Long sampTaskId = item.getSampTaskId();
                List<SampleItem> sampleItemInfos = sampleItemMapper.selectList(Wrappers.<SampleItem>lambdaQuery()
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
    public IPage<SampleTaskDetailVo> findSampTaskDetailByCondition(PageVo pageVo, SampleTaskDetailSearchVo sv) {
        sv.setSampleNo(StrUtil.trimToNull(sv.getSampleNo()));
        sv.setSampStatus(StrUtil.trimToNull(sv.getSampStatus()));
        Page<SampleTaskDetailVo> page = PageUtil.initMpPage(pageVo);
        sampleItemMapper.findSampTaskDetailByCondition(page, sv);
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            page.getRecords().forEach(p -> {
                if("5".equals(p.getSmapStatus())) {
                    SampleItemAuditRecord record = sampleItemAuditRecordService.getOne(Wrappers.<SampleItemAuditRecord>lambdaQuery()
                            .eq(SampleItemAuditRecord::getSampleItemId, p.getSampItemId())
                            .orderByDesc(SampleItemAuditRecord::getAuditTime), false);
                    if(record != null) {
                        p.setAuditReason(record.getAuditReason());
                    }
                }
            });
        }
        return page;
    }

    @Override
    public IPage<SampleManageVo> findSamplesForJobByCondition(PageVo pageVo, SampCommSearchVo sv) {
        Page<SampleManageVo> page = PageUtil.initMpPage(pageVo);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        this.baseMapper.findSamplesForJobByCondition(page, organId, sv);
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            page.getRecords().forEach(item -> {
                List<SampleItem> sampleItems = sampleItemMapper.selectList(Wrappers.<SampleItem>lambdaQuery()
                        .select(SampleItem::getId, SampleItem::getSampleStatus)
                        .eq(SampleItem::getSampleTaskId, item.getSampTaskId()));
                if(sampleItems != null && !sampleItems.isEmpty()) {
                    item.setTotalSamples(sampleItems.size());
                    List<String> sampStatus = Arrays.asList("6","7","8");
                    Integer inBoundSamples = sampleItems.stream().filter(s -> sampStatus.contains(s.getSampleStatus())).collect(Collectors.toList()).size();
                    item.setInBoundSamples(inBoundSamples);
                    Integer outBoundSamples = sampleItems.stream().filter(s -> "8".contains(s.getSampleStatus())).collect(Collectors.toList()).size();
                    item.setOutBoundSamples(outBoundSamples);
                }else{
                    item.setTotalSamples(0).setInBoundSamples(0).setOutBoundSamples(0);
                }
            });
        }
        return page;
    }

}
