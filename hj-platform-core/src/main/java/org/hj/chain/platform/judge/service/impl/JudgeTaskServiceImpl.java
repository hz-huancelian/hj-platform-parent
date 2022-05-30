package org.hj.chain.platform.judge.service.impl;

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
import org.hj.chain.platform.contract.entity.ContractInfo;
import org.hj.chain.platform.contract.mapper.ContractInfoMapper;
import org.hj.chain.platform.judge.entity.JudgeRecord;
import org.hj.chain.platform.judge.entity.JudgeTask;
import org.hj.chain.platform.judge.mapper.JudgeRecordMapper;
import org.hj.chain.platform.judge.mapper.JudgeTaskMapper;
import org.hj.chain.platform.judge.service.IJudgeRecordService;
import org.hj.chain.platform.judge.service.IJudgeTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.mapper.SysUserMapper;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.offer.entity.OfferInfo;
import org.hj.chain.platform.offer.entity.OfferPlan;
import org.hj.chain.platform.offer.mapper.OfferInfoMapper;
import org.hj.chain.platform.offer.mapper.OfferPlanMapper;
import org.hj.chain.platform.schedule.entity.ScheduleTask;
import org.hj.chain.platform.schedule.mapper.ScheduleTaskMapper;
import org.hj.chain.platform.tdo.judgetask.JudgeRecordAuditTdo;
import org.hj.chain.platform.tdo.judgetask.JudgeTaskAuditTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserParamVo;
import org.hj.chain.platform.vo.judgetask.JudgeRecordSearchVo;
import org.hj.chain.platform.vo.judgetask.JudgeRecordVo;
import org.hj.chain.platform.vo.judgetask.JudgeTaskSearchVo;
import org.hj.chain.platform.vo.judgetask.JudgeTaskVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 评审任务表：需要技术负责人处理；与合同签订是平行的，但是评审失败，整个合同失效 服务实现类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Service
public class JudgeTaskServiceImpl extends ServiceImpl<JudgeTaskMapper, JudgeTask> implements IJudgeTaskService {

    @Autowired
    private ContractInfoMapper contractInfoMapper;
    @Autowired
    private JudgeRecordMapper judgeRecordMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private OfferInfoMapper offerInfoMapper;
    @Autowired
    private IJudgeRecordService judgeRecordService;
    @Autowired
    private ScheduleTaskMapper scheduleTaskMapper;
    @Autowired
    private OfferPlanMapper offerPlanMapper;

    @Override
    public Result<IPage<JudgeTaskVo>> findJudgeTaskForTecManagerByCondition(PageVo pageVo, JudgeTaskSearchVo sv) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        Page<JudgeTaskVo> page = PageUtil.initMpPage(pageVo);
        this.baseMapper.findJudgeTaskForTecManagerByCondition(page, loginOutputVo.getOrganId(), sv);
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            page.getRecords().forEach(item -> {
                String offerId = item.getOfferId();
                Integer planNum = offerPlanMapper.selectCount(Wrappers.<OfferPlan>lambdaQuery().eq(OfferPlan::getOfferId, offerId));
                item.setPlanNum(planNum);
            });
        }
        return ResultUtil.data(page);
    }

    @Override
    @Transactional
    public Result<Object> doAuditTask(JudgeTaskAuditTdo tdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        Long judgeTaskId = tdo.getJudgeTaskId();
        JudgeTask judgeTask = this.getById(judgeTaskId);
        LocalDateTime now = LocalDateTime.now();
        String auditFlag = tdo.getAuditFlag();
        String auditUsers = tdo.getAuditUsers();
        Integer reviewNum = StrUtil.isBlank(auditUsers) ? 0 : auditUsers.split(",").length;
        int cnt = this.baseMapper.update(null, Wrappers.<JudgeTask>lambdaUpdate()
                .set(JudgeTask::getTaskStatus, auditFlag)
                .set(JudgeTask::getRemark, tdo.getRemark())
                .set(JudgeTask::getUpdateTime, now)
                .set(JudgeTask::getOperUserId, loginOutputVo.getUserId())
                .set(auditFlag.equals("1"), JudgeTask::getReviewedNum, reviewNum)
                .eq(JudgeTask::getId, judgeTaskId));
        if (cnt > 0) {
            if(auditFlag.equals("1")) {
                if(StrUtil.isBlank(tdo.getAuditUsers())) {
                    throw new CustomException("参与评审人员不能为空！");
                }
                List<JudgeRecord> judgeRecords = Arrays.stream(tdo.getAuditUsers().split(",")).map(u -> {
                    JudgeRecord judgeRecord = new JudgeRecord();
                    judgeRecord.setJudgeTaskId(judgeTaskId).setCreateTime(now).setJudgeStatus("0").setJudgeUserId(u);
                    return judgeRecord;
                }).collect(Collectors.toList());
                judgeRecordService.saveBatch(judgeRecords);
            }
            if (auditFlag.equals("3")) {
                cnt = contractInfoMapper.update(null, Wrappers.<ContractInfo>lambdaUpdate()
                        .set(ContractInfo::getContStatus, "5")
                        .set(ContractInfo::getUpdateTime, now)
                        .eq(ContractInfo::getOfferId, judgeTask.getOfferId()));
                if (cnt <= 0) {
                    throw new CustomException("评审操作失败（合同作废失败）！");
                }
                cnt = offerInfoMapper.update(null, Wrappers.<OfferInfo>lambdaUpdate()
                        .set(OfferInfo::getStatus, "4")
                        .set(OfferInfo::getUpdateTime, now)
                        .eq(OfferInfo::getId, judgeTask.getOfferId()));
                if (cnt <= 0) {
                    throw new CustomException("评审操作失败（报价单作废失败）！");
                }
            }
            if (auditFlag.equals("2") || auditFlag.equals("6")) {
                ContractInfo contractInfo = contractInfoMapper.selectOne(Wrappers.<ContractInfo>lambdaQuery()
                        .eq(ContractInfo::getOfferId, judgeTask.getOfferId()));
                if (contractInfo.getContStatus().equals("4")) {
                    /* 创建任务调度 */
                    ScheduleTask scheduleTask = scheduleTaskMapper.selectOne(Wrappers.<ScheduleTask>lambdaQuery()
                            .eq(ScheduleTask::getContCode, contractInfo.getContCode())
                            .eq(ScheduleTask::getOfferId, judgeTask.getOfferId()));
                    if(scheduleTask == null) {
                        scheduleTask = new ScheduleTask();
                        List<OfferPlan> offerPlans = offerPlanMapper.selectList(Wrappers.<OfferPlan>lambdaQuery()
                                .select(OfferPlan::getId, OfferPlan::getOfferId, OfferPlan::getScheduleTimes)
                                .eq(OfferPlan::getOfferId, judgeTask.getOfferId()));
                        Integer planNum = offerPlans.stream().collect(Collectors.summingInt(OfferPlan::getScheduleTimes));
                        scheduleTask.setOfferId(judgeTask.getOfferId())
                                .setScheduleStatus("0")
                                .setCreateTime(now)
                                .setScheduleFlag("0")
                                .setPlanNum(planNum).setContCode(contractInfo.getContCode());
                        scheduleTaskMapper.insert(scheduleTask);
                    }
                }
            }
            return ResultUtil.success("评审操作成功！");
        } else {
            throw new CustomException("评审操作失败！");
        }
    }

    @Override
    public Result<IPage<JudgeRecordVo>> findJudgeTaskForDeptManagerByCondition(PageVo pageVo, JudgeRecordSearchVo sv) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        Page<JudgeRecordVo> page = PageUtil.initMpPage(pageVo);
        this.baseMapper.findJudgeTaskForDeptManagerByCondition(page, loginOutputVo.getUserId(), sv);
        return ResultUtil.data(page);
    }

    @Override
    public Result<Object> doAuditRecord(JudgeRecordAuditTdo tdo) {
        JudgeRecord judgeRecord = judgeRecordMapper.selectById(tdo.getJudgeRecordId());
        if (!judgeRecord.getJudgeStatus().equals("0")) {
            return ResultUtil.busiError("不允许重复评审！");
        }
        LocalDateTime now = LocalDateTime.now();
        int cnt = judgeRecordMapper.update(null, Wrappers.<JudgeRecord>lambdaUpdate()
                .set(JudgeRecord::getJudgeStatus, tdo.getAuditFlag())
                .set(JudgeRecord::getRemark, tdo.getRemark())
                .set(JudgeRecord::getJudgeTime, now)
                .set(JudgeRecord::getUpdateTime, now)
                .eq(JudgeRecord::getId, tdo.getJudgeRecordId()));
        if (cnt > 0) {
            List<JudgeRecord> judgeRecords = judgeRecordMapper.selectList(Wrappers.<JudgeRecord>lambdaQuery()
                    .eq(JudgeRecord::getJudgeTaskId, judgeRecord.getJudgeTaskId())
                    .eq(JudgeRecord::getJudgeStatus, "0"));
            String taskStatus = "4";
            if (judgeRecords == null || judgeRecords.isEmpty()) {
                taskStatus = "5";
            }
            JudgeTask judgeTask = this.getById(judgeRecord.getJudgeTaskId());
            cnt = this.baseMapper.update(null, Wrappers.<JudgeTask>lambdaUpdate()
                    .set(JudgeTask::getTaskStatus, taskStatus)
                    .set(JudgeTask::getUpdateTime, now)
                    .set(JudgeTask::getReviewedNum, judgeTask.getReviewedNum() + 1)
                    .eq(JudgeTask::getId, judgeRecord.getJudgeTaskId()));
            if (cnt <= 0) {
                throw new CustomException("评审操作失败！");
            }
        } else {
            throw new CustomException("评审操作失败！");
        }
        return ResultUtil.success("评审操作成功！");
    }

    @Override
    public Result<List<JudgeRecordVo>> getJudgeRecordByJudgeTaskId(Long judgeTaskId) {
        if (judgeTaskId == null) {
            return ResultUtil.validateError("技术评审任务ID不能为空！");
        }
        List<JudgeRecord> list = judgeRecordMapper.selectList(Wrappers.<JudgeRecord>lambdaQuery()
                .eq(JudgeRecord::getJudgeTaskId, judgeTaskId));
        List<JudgeRecordVo> vos = list.stream().map(item -> {
            JudgeRecordVo vo = new JudgeRecordVo();
            vo.setJudgeRecordId(item.getId())
                    .setJudgeRecordStatus(item.getJudgeStatus())
                    .setJudgeTaskId(item.getJudgeTaskId())
                    .setRemark(item.getRemark())
                    .setJudgeTime(item.getJudgeTime());
            String judgeUserId = item.getJudgeUserId();
            SysUser sysUser = sysUserMapper.selectById(judgeUserId);
            vo.setJudgeUser(sysUser.getEmpName());
            return vo;
        }).collect(Collectors.toList());
        return ResultUtil.data(vos);
    }

    @Override
    public Result<List<UserParamVo>> findAuditUser() {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<String> roleKeys = Arrays.asList("Pm","LMa");
        List<UserParamVo> userParamVos = sysUserMapper.selectUserParamByRoleKeysAndOrganId(roleKeys, organId);
        return ResultUtil.data(userParamVos);
    }
}
