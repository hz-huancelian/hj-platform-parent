package org.hj.chain.platform.approval.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.approval.entity.OfferApprovalRecord;
import org.hj.chain.platform.approval.mapper.OfferApprovalRecordMapper;
import org.hj.chain.platform.approval.service.IOfferApprovalService;
import org.hj.chain.platform.common.CustomException;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.contract.service.IContractInfoService;
import org.hj.chain.platform.judge.entity.JudgeTask;
import org.hj.chain.platform.judge.mapper.JudgeTaskMapper;
import org.hj.chain.platform.offer.entity.OfferFactorOrgan;
import org.hj.chain.platform.offer.entity.OfferInfo;
import org.hj.chain.platform.offer.entity.OfferJudgeInfo;
import org.hj.chain.platform.offer.mapper.OfferFactorOrganMapper;
import org.hj.chain.platform.offer.mapper.OfferInfoMapper;
import org.hj.chain.platform.offer.mapper.OfferJudgeInfoMapper;
import org.hj.chain.platform.service.ISysDeptService;
import org.hj.chain.platform.service.ISysMenuService;
import org.hj.chain.platform.service.ISysRoleService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.approval.OfferJudgeItemTdo;
import org.hj.chain.platform.tdo.approval.OfferJudgeTdo;
import org.hj.chain.platform.tdo.contract.ContractBo;
import org.hj.chain.platform.tdo.contract.SubcontractBo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserVo;
import org.hj.chain.platform.vo.approval.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OfferApprovalServiceImpl extends ServiceImpl<OfferJudgeInfoMapper, OfferJudgeInfo> implements IOfferApprovalService {
    @Autowired
    private OfferInfoMapper offerInfoMapper;
    @Autowired
    private OfferJudgeInfoMapper offerJudgeInfoMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IContractInfoService contractInfoService;
    @Autowired
    private JudgeTaskMapper judgeTaskMapper;
    @Autowired
    private OfferApprovalRecordMapper offerApprovalRecordMapper;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDeptService sysDeptService;

    //审批权限标示
    private static final String APPROVAL_PERM = "sale:order:approve";


    public IPage<OfferApprovalVo> findOfferByCondition(PageVo pageVo, OfferApprovalSearchVo sv) {
        sv.setId(StrUtil.trimToNull(sv.getId()))
                .setProjectName(StrUtil.trimToNull(sv.getProjectName()));
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Page<OfferApprovalVo> page = PageUtil.initMpPage(pageVo);
        String userType = loginOutputVo.getUserType();
        if (userType.equals("1")) {
            String maxScope = sysRoleService.findMaxRoleScopeByPerms(loginOutputVo.getUserId(), APPROVAL_PERM);
            if (maxScope != null) {
                List<Long> deptIds = new ArrayList<>();
                String userId = null;
//            if (maxScope.equals("1")) {
//                //所有数据权限；
//                deptIds = null;
//            } else
                if (maxScope.equals("3")) {
                    //2：本部门数据权限；
                    deptIds.add(loginOutputVo.getDeptId());
                } else if (maxScope.equals("2")) {
                    deptIds.add(loginOutputVo.getDeptId());
                    //3：本部门及以下数据权限；
                    List<Long> childDeptIds = sysDeptService.selectChildDeptIdsByDeptId(loginOutputVo.getDeptId());
                    if (childDeptIds != null && !childDeptIds.isEmpty()) {
                        deptIds.addAll(childDeptIds);
                    }
                } else if (maxScope.equals("4")) {
                    //本人
                    userId = loginOutputVo.getUserId();
                }
                if (deptIds == null || deptIds.isEmpty()) {
                    deptIds = null;
                }
                offerInfoMapper.findOfferApprovalsByCondition(page, organId, deptIds, userId, sv);
                page.getRecords().forEach(item -> {
                    UserVo userVo = sysUserService.findUserByUserId(item.getCreateUserId());
                    if (userVo != null) {
                        item.setCreateUserId(userVo.getEmpName());
                    }
                    if("2".equals(item.getStatus())) {
                        List<OfferApprovalRecord> oars = offerApprovalRecordMapper.selectList(Wrappers.<OfferApprovalRecord>lambdaQuery()
                                .eq(OfferApprovalRecord::getOfferId, item.getId()).orderByDesc(OfferApprovalRecord::getCreateTime));
                        if(oars != null && !oars.isEmpty()) {
                            item.setAuditReason(oars.get(0).getRemark());
                        }
                    }
                });
            }

        } else {
            offerInfoMapper.findOfferApprovalsByCondition(page, organId, null, null, sv);
            page.getRecords().forEach(item -> {
                UserVo userVo = sysUserService.findUserByUserId(item.getCreateUserId());
                if (userVo != null) {
                    item.setCreateUserId(userVo.getEmpName());
                }
                if("2".equals(item.getStatus())) {
                    List<OfferApprovalRecord> oars = offerApprovalRecordMapper.selectList(Wrappers.<OfferApprovalRecord>lambdaQuery()
                            .eq(OfferApprovalRecord::getOfferId, item.getId()).orderByDesc(OfferApprovalRecord::getCreateTime));
                    if(oars != null && !oars.isEmpty()) {
                        item.setAuditReason(oars.get(0).getRemark());
                    }
                }
            });
        }

        return page;
    }

    @Transactional
    @Override
    public Result<Object> check(String offerId, Integer isPass, String remark) {
        OfferInfo dbPo = offerInfoMapper.selectOne(Wrappers.<OfferInfo>lambdaQuery().select(OfferInfo::getId,
                OfferInfo::getStatus)
                .eq(OfferInfo::getId, offerId));

        if (dbPo == null) {
            return ResultUtil.busiError("报价单号关联的报价单不存在！");
        }

        if (!dbPo.getStatus().equals("1")) {
            return ResultUtil.busiError("报价单状态不正确！");
        }

        LocalDateTime date = LocalDateTime.now();
        String status = "2";
        if (isPass.equals(0)) {
            status = "3";
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        offerInfoMapper.update(null, Wrappers.<OfferInfo>lambdaUpdate()
                .set(OfferInfo::getStatus, status)
                .set(OfferInfo::getAuditTime, date)
                .set(OfferInfo::getAuditUserId, loginOutputVo.getUserId())
                .set(OfferInfo::getUpdateTime, date)
                .eq(OfferInfo::getId, offerId));

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String userId = (String) tokenInfo.getLoginId();
        OfferApprovalRecord record = new OfferApprovalRecord();
        record.setOfferId(offerId)
                .setIsPass(isPass)
                .setRemark(remark)
                .setCreateTime(date)
                .setApprovalUserId(userId);

        offerApprovalRecordMapper.insert(record);

        if (isPass.equals(0)) {
            OfferJudgeInfo judgeInfo = new OfferJudgeInfo();
            judgeInfo.setOfferId(offerId)
                    .setStatus("0")
                    .setCreateUserId(userId)
                    .setCreateTime(date);

            offerJudgeInfoMapper.insert(judgeInfo);
        }
        return ResultUtil.success("审核成功！");
    }

    @Override
    public Result<List<OfferApprovalRecordVo>> findOfferApprovalRecordByOfferId(String offerId) {
        List<OfferApprovalRecord> records = offerApprovalRecordMapper.selectList(Wrappers.<OfferApprovalRecord>lambdaQuery().eq(OfferApprovalRecord::getOfferId, offerId));
        if (records != null && !records.isEmpty()) {
            List<OfferApprovalRecordVo> recordVos = records.stream().map(item -> {
                OfferApprovalRecordVo recordVo = new OfferApprovalRecordVo();
                recordVo.setIsPass(item.getIsPass())
                        .setCreateTime(item.getCreateTime())
                        .setRemark(item.getRemark());
                UserVo userVo = sysUserService.findUserByUserId(item.getApprovalUserId());
                if (userVo != null) {
                    recordVo.setApprovalUserName(userVo.getEmpName());
                }
                return recordVo;
            }).collect(Collectors.toList());

            return ResultUtil.data(recordVos);
        }
        return ResultUtil.data(null);
    }

    @Override
    public IPage<OfferJudgeApprovalVo> findOfferJudgeByCondition(PageVo pageVo, OfferJudgeApprovalSearchVo sv) {
        sv.setOfferId(StrUtil.trimToNull(sv.getOfferId()))
                .setProjectName(StrUtil.trimToNull(sv.getProjectName()))
                .setStatus(StrUtil.trimToNull(sv.getStatus()));
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Page<OfferJudgeApprovalVo> page = PageUtil.initMpPage(pageVo);
        offerJudgeInfoMapper.findOfferJudgeApprovalsByCondition(page, organId, sv);
        page.getRecords().stream().forEach(item -> {
            UserVo userVo = sysUserService.findUserByUserId(item.getCreateUserId());
            if (userVo != null) {
                item.setCreateUserId(userVo.getEmpName());
            }
        });
        return page;
    }

    @Transactional
    @Override
    public Result<Object> judge(OfferJudgeTdo judgeTdo) {
        @NotBlank(message = "报价单号不能为空") String offerId = judgeTdo.getOfferId();
        OfferJudgeInfo dbPo = offerJudgeInfoMapper.selectOne(Wrappers.<OfferJudgeInfo>lambdaQuery()
                .select(OfferJudgeInfo::getStatus)
                .eq(OfferJudgeInfo::getOfferId, offerId));

        if (dbPo == null) {
            return ResultUtil.busiError("报价单号对应的分包记录任务不存在！");
        }

        if (!dbPo.getStatus().equals("0")) {
            return ResultUtil.busiError("分包状态不正确！");
        }

        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        OfferInfo dbOffer = offerInfoMapper.selectOne(Wrappers.<OfferInfo>lambdaQuery()
                .select(OfferInfo::getCreateUserId,
                        OfferInfo::getDeptId,
                        OfferInfo::getAuditUserId)
                .eq(OfferInfo::getOrganId, organId)
                .eq(OfferInfo::getId, offerId));

        if (dbOffer == null) {
            return ResultUtil.busiError("报价单关联的报价单在库中不存在！");
        }

        LocalDateTime now = LocalDateTime.now();
        offerJudgeInfoMapper.update(null, Wrappers.<OfferJudgeInfo>lambdaUpdate()
                .set(OfferJudgeInfo::getStatus, "1")
                .set(OfferJudgeInfo::getUpdateTime, now)
                .eq(OfferJudgeInfo::getOfferId, offerId));

        if(judgeTdo.getItemTdoList() == null || judgeTdo.getItemTdoList().isEmpty()) {
            throw new CustomException("分包因子不能为空!");
        }
        List<OfferJudgeItemTdo> itemTdoList = judgeTdo.getItemTdoList().stream().distinct().collect(Collectors.toList());
        itemTdoList.stream().forEach(item -> {
            if (StrUtil.isBlank(item.getOrganId())) {
                item.setOrganId(organId);
            }
        });

        List<OfferFactorOrgan> factorOrgans = itemTdoList.stream().map(item -> {
            OfferFactorOrgan organ = new OfferFactorOrgan();
            organ.setPlanFactorId(item.getPlanFactorId())
                    .setOrganId(item.getOrganId())
                    .setCreateTime(now);
            return organ;
        }).collect(Collectors.toList());
        SqlHelper.executeBatch(OfferFactorOrgan.class, this.log, factorOrgans, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
            String sqlStatement = SqlHelper.getSqlStatement(OfferFactorOrganMapper.class, SqlMethod.INSERT_ONE);
            sqlSession.insert(sqlStatement, entity);
        });

        //生成合同

        ContractBo contractBo = new ContractBo();
        contractBo.setOfferId(offerId)
//                .setContName(dbOffer.getProjectName())
                .setContCheckUserId(dbOffer.getAuditUserId())
                .setMakeUserId(dbOffer.getCreateUserId())
                .setDeptId(dbOffer.getDeptId());
        contractInfoService.addContract(contractBo);

        Set<String> organSet = factorOrgans.stream().map(item -> item.getOrganId()).collect(Collectors.toSet());
        List<SubcontractBo> subcontractBos = organSet.stream().map(item -> {
            SubcontractBo bo = new SubcontractBo();
            bo.setJudgeOrganId(item)
                    .setOfferId(offerId);
            return bo;
        }).collect(Collectors.toList());

        contractInfoService.addSubContract(subcontractBos);

        JudgeTask judgeTask = new JudgeTask();
        judgeTask.setOfferId(offerId)
                .setTaskStatus("0")
                .setCreateTime(now)
                .setReviewNum(0)
                .setReviewedNum(0);
        judgeTaskMapper.insert(judgeTask);
        return ResultUtil.success("分包成功！");
    }
}
