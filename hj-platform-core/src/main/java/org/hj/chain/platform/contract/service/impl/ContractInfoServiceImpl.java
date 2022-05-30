package org.hj.chain.platform.contract.service.impl;

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
import com.deepoove.poi.data.Includes;
import com.deepoove.poi.data.Pictures;
import org.hj.chain.platform.*;
import org.hj.chain.platform.approval.entity.ContractAuditRecord;
import org.hj.chain.platform.approval.mapper.ContractAuditRecordMapper;
import org.hj.chain.platform.common.CustomException;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.component.DictUtils;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.contract.entity.ContractInfo;
import org.hj.chain.platform.contract.entity.CusContractBaseInfo;
import org.hj.chain.platform.contract.entity.SubcontractInfo;
import org.hj.chain.platform.contract.mapper.ContractInfoMapper;
import org.hj.chain.platform.contract.mapper.CusContractBaseInfoMapper;
import org.hj.chain.platform.contract.mapper.SubcontractInfoMapper;
import org.hj.chain.platform.contract.service.IContractInfoService;
import org.hj.chain.platform.contract.service.ICusContractBaseInfoService;
import org.hj.chain.platform.contract.service.IOwnerContractBaseInfoService;
import org.hj.chain.platform.judge.entity.JudgeTask;
import org.hj.chain.platform.judge.mapper.JudgeTaskMapper;
import org.hj.chain.platform.judge.service.IJudgeTaskService;
import org.hj.chain.platform.offer.entity.OfferInfo;
import org.hj.chain.platform.offer.entity.OfferPlan;
import org.hj.chain.platform.offer.mapper.OfferInfoMapper;
import org.hj.chain.platform.offer.mapper.OfferJudgeInfoMapper;
import org.hj.chain.platform.offer.mapper.OfferPlanMapper;
import org.hj.chain.platform.offer.service.IOfferInfoService;
import org.hj.chain.platform.report.mapper.ReportBaseInfoMapper;
import org.hj.chain.platform.report.model.ReportBaseInfo;
import org.hj.chain.platform.schedule.entity.ScheduleTask;
import org.hj.chain.platform.schedule.mapper.ScheduleTaskMapper;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.service.ISysDeptService;
import org.hj.chain.platform.service.ISysRoleService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.contract.*;
import org.hj.chain.platform.vo.FactorMethodInfoVo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserVo;
import org.hj.chain.platform.vo.approval.ContractAuditRecordVo;
import org.hj.chain.platform.vo.contract.*;
import org.hj.chain.platform.vo.offer.OfferContVo;
import org.hj.chain.platform.vo.offer.OfferJudgeInfoVo;
import org.hj.chain.platform.word.ContractData;
import org.hj.chain.platform.word.OfferTableData;
import org.hj.chain.platform.word.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
public class ContractInfoServiceImpl extends ServiceImpl<ContractInfoMapper, ContractInfo> implements IContractInfoService {

    @Autowired
    private ContractInfoMapper contractInfoMapper;
    @Autowired
    private SubcontractInfoMapper subcontractInfoMapper;
    @Autowired
    private ICusContractBaseInfoService cusContractBaseInfoService;
    @Autowired
    private CusContractBaseInfoMapper cusContractBaseInfoMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private JudgeTaskMapper judgeTaskMapper;
    @Autowired
    private IJudgeTaskService judgeTaskService;
    @Autowired
    private OfferInfoMapper offerInfoMapper;
    @Autowired
    private IOwnerContractBaseInfoService ownerContractBaseInfoService;
    @Autowired
    private IOfferInfoService offerInfoService;
    @Autowired
    private ReportBaseInfoMapper reportBaseInfoMapper;
    @Autowired
    private ContractAuditRecordMapper contractAuditRecordMapper;
    @Autowired
    private OfferJudgeInfoMapper offerJudgeInfoMapper;
    @Autowired
    private IFactorService factorService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ScheduleTaskMapper scheduleTaskMapper;
    @Autowired
    private OfferPlanMapper offerPlanMapper;
    @Autowired
    private DictUtils dictUtils;
    @Value("${file.ht.upload}")
    private String uploadContPath;
    @Value("${file.template.path}")
    private String templatePath;
    @Value("${image.upload.report}")
    private String organImgPath;

    private static final String CONT_PERM = "contract:contract:list";
    private static final String CONT_APPEOVAL_PERM = "contract:contract:list";

    @Override
    public IPage<ContractInfoVo> findContInfosByCondition(PageVo pageVo, ContractSearchVo sv) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Page<ContractInfoVo> page = PageUtil.initMpPage(pageVo);
        String userType = loginOutputVo.getUserType();
        if (userType.equals("1")) {
            String maxScope = sysRoleService.findMaxRoleScopeByPerms(loginOutputVo.getUserId(), CONT_PERM);
            if (maxScope != null) {
                List<Long> deptIds = new ArrayList<>();
                String userId = null;
                if (maxScope.equals("2")) {
                    //2：本部门数据权限；
                    deptIds.add(loginOutputVo.getDeptId());
                } else if (maxScope.equals("3")) {
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
                contractInfoMapper.findPageByCondition(page, organId, deptIds, userId, sv);

            }
        } else {
            contractInfoMapper.findPageByCondition(page, organId, null, null, sv);
        }
        page.getRecords().stream().forEach(item -> {
            UserVo userVo = sysUserService.findUserByUserId(item.getMakeUserId());
            if (userVo != null) {
                item.setMakeUserId(userVo.getUsername());
            }
            if ("4".equals(item.getContStatus())) {
                List<ContractAuditRecord> cars = contractAuditRecordMapper.selectList(Wrappers.<ContractAuditRecord>lambdaQuery()
                        .eq(ContractAuditRecord::getContId, item.getId()).orderByDesc(ContractAuditRecord::getCreateTime));
                if (cars != null && !cars.isEmpty()) {
                    item.setAuditReason(cars.get(0).getAuditReason());
                }
            }
        });
        return page;
    }

    @Override
    public IPage<ContractInfoVo> findContApprovalsByCondition(PageVo pageVo, ContractSearchVo sv) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Page<ContractInfoVo> page = PageUtil.initMpPage(pageVo);
        String userType = loginOutputVo.getUserType();
        if (userType.equals("1")) {
            String maxScope = sysRoleService.findMaxRoleScopeByPerms(loginOutputVo.getUserId(), CONT_APPEOVAL_PERM);
            if (maxScope != null) {
                List<Long> deptIds = new ArrayList<>();
                String userId = null;
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
                contractInfoMapper.findPageByCondition(page, organId, deptIds, userId, sv);

            }
        } else {
            contractInfoMapper.findPageByCondition(page, organId, null, null, sv);
        }
        page.getRecords().stream().forEach(item -> {
            if (StrUtil.isBlank(item.getPartA())) {
                item.setPartA(item.getConsignorName());
            }
            UserVo userVo = sysUserService.findUserByUserId(item.getMakeUserId());
            if (userVo != null) {
                item.setMakeUserId(userVo.getUsername());
            }
            if ("4".equals(item.getContStatus())) {
                List<ContractAuditRecord> cars = contractAuditRecordMapper.selectList(Wrappers.<ContractAuditRecord>lambdaQuery()
                        .eq(ContractAuditRecord::getContId, item.getId()).orderByDesc(ContractAuditRecord::getCreateTime));
                if (cars != null && !cars.isEmpty()) {
                    item.setAuditReason(cars.get(0).getAuditReason());
                }
            }
        });
        return page;
    }

    @Override
    public int addContract(ContractBo bo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
//        Integer count = contractInfoMapper.selectCount(Wrappers.<ContractInfo>lambdaQuery().eq(ContractInfo::getOrganId, organId));
//        CodeBuildUtil.genContractCode(organId, , count + 1);

        LocalDateTime now = LocalDateTime.now();
        ContractInfo po = new ContractInfo();
//        po.setContName(bo.getContName())
        po.setMakeUserId(bo.getMakeUserId())
                .setOrganId(organId)
                .setOfferId(bo.getOfferId())
                .setContStatus("0")
                .setContCheckUserId(bo.getContCheckUserId())
                .setDeptId(bo.getDeptId())
                .setCreateTime(now);
        return contractInfoMapper.insert(po);
    }

    @Override
    public IPage<SubcontractVo> findSubContInfosByCondition(PageVo pageVo, ContractSearchVo sv) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Page<SubcontractVo> page = PageUtil.initMpPage(pageVo);
        subcontractInfoMapper.findPageByCondition(page, organId, sv);
        page.getRecords().forEach(item -> {
            String makeUserId = item.getMakeUserId();
            if (StrUtil.isNotBlank(makeUserId)) {
                UserVo userVo = sysUserService.findUserByUserId(item.getMakeUserId());
                if (userVo != null) {
                    item.setMakeUserId(userVo.getUsername());
                }
            }
        });
        return page;
    }


    @Transactional
    @Override
    public int addSubContract(List<SubcontractBo> bos) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        LocalDateTime now = LocalDateTime.now();
        List<SubcontractInfo> infos = bos.stream().map(item -> {
            SubcontractInfo info = new SubcontractInfo();
            info.setJudgeOrganId(item.getJudgeOrganId())
                    .setOfferId(item.getOfferId())
                    .setOrganId(organId)
                    .setContStatus("0")
                    .setCreateTime(now);
            return info;
        }).collect(Collectors.toList());

        SqlHelper.executeBatch(SubcontractInfo.class, this.log, infos, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
            String sqlStatement = SqlHelper.getSqlStatement(SubcontractInfoMapper.class, SqlMethod.INSERT_ONE);
            sqlSession.insert(sqlStatement, entity);
        });
        return 1;
    }

    @Transactional
    @Override
    public Result<Object> pefectContOnline(ContractSaveTdo tdo) {
        @NotNull(message = "合同ID不能为空！") Long contId = tdo.getContId();
        ContractInfo dbCont = contractInfoMapper.selectOne(Wrappers.<ContractInfo>lambdaQuery()
                .select(ContractInfo::getContStatus,
                        ContractInfo::getContCode,
                        ContractInfo::getOfferId)
                .eq(ContractInfo::getId, contId));

        if (dbCont == null) {
            return ResultUtil.busiError("合同ID关联的合同信息库中不存在！");
        }
        List<String> conStatusList = Arrays.asList("0", "2", "6");
        if (!conStatusList.contains(dbCont.getContStatus())) {
            return ResultUtil.busiError("合同状态不正确！");
        }


        LocalDateTime now = LocalDateTime.now();
        @NotNull(message = "合同基本信息不能为空！") ContractInfoTdo infoTdo = tdo.getContractInfoTdo();
        @NotBlank(message = "签定时间不能为空！") String signDate = infoTdo.getSignDate();
        String contCode = dbCont.getContCode();
        if (contCode != null && contCode.length() < 5) {
            SaSession session = StpUtil.getSession();
            LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
            String organId = loginOutputVo.getOrganId();
            Integer count = contractInfoMapper.selectCount(Wrappers.<ContractInfo>lambdaQuery()
                    .gt(ContractInfo::getContStatus, "0")
                    .eq(ContractInfo::getOrganId, organId));
            String signDateStr = signDate.replaceAll("-", "");
            contCode = CodeBuildUtil.genContractCode(organId, signDateStr, count + 1);

        }
        ContractInfo po = new ContractInfo();
        po.setId(contId)
                .setContStatus("6")
                .setContCode(contCode)
                .setValidity(infoTdo.getValidity())
                .setSignDate(infoTdo.getSignDate())
                .setMakeType("0")
                .setPayMethod(infoTdo.getPayMethod())
                .setPayMethodDesc(infoTdo.getPayMethodDesc())
                .setSignLocation(infoTdo.getSignLocation())
                .setSupContCode(infoTdo.getSupContCode())
                .setUpdateTime(now);
        @NotNull(message = "甲方信息不能为空！") CusContBaseAddTdo addTdo = tdo.getAddTdo();
        Long cusContBaseInfoId = tdo.getCusContBaseInfoId();
        if (cusContBaseInfoId == null) {
            cusContBaseInfoId = cusContractBaseInfoService.addCusContBase(addTdo);
        } else {
            CusContBaseModifyTdo modifyTdo = new CusContBaseModifyTdo();
            modifyTdo.setCusContBaseInfoId(cusContBaseInfoId)
                    .setAddress(addTdo.getAddress())
                    .setAgentPerson(addTdo.getAgentPerson())
                    .setBankName(addTdo.getBankName())
                    .setBankNo(addTdo.getBankNo())
                    .setCompanyName(addTdo.getCompanyName())
                    .setJurPerson(addTdo.getJurPerson())
                    .setPostCode(addTdo.getPostCode())
                    .setTaxNumber(addTdo.getTaxNumber())
                    .setTelFax(addTdo.getTelFax());
            Result<Object> res = cusContractBaseInfoService.modifyCusContBaseById(modifyTdo);
            if (!res.isSuccess()) {
                return res;
            }
        }
        po.setCusContBaseInfoId(cusContBaseInfoId);

        contractInfoMapper.updateById(po);

        //生成word和pdf
        ContractData contractData = this.findLoadContractByContCode(contCode);
        if (contractData.getOfferId() != null) {
            OfferTableData offerTable = offerInfoService.findDownloadOfferInfoById(dbCont.getOfferId());
            contractData.setTotalAmount(offerTable.getCapitalAmount())
                    .setSmallTotalAmount(offerTable.getDraftAmount());
            String offerTemplate = templatePath + "/offer_template.docx";
            contractData.setOfferTable(
                    Includes.ofLocal(offerTemplate).setRenderModel(offerTable).create());
        }
        String contTemplate = templatePath + "/contract_template.docx";
        String contFileId = contCode + ".docx";
        String genUrl = uploadContPath + "/" + contFileId;
        boolean isPass = false;
        try {
            WordUtils.genContTableWord(contTemplate, contractData, genUrl);
            isPass = true;
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("生成word失败！");
        }

        ContractInfo cont = new ContractInfo();
        cont.setId(contId);
        if (isPass) {
            String genContUrl = contCode + ".pdf";
            FileConvertUtil.wordBytes2PdfFile(FileUtil.readBytes(genUrl), uploadContPath + "/" + genContUrl);
            cont.setContGenUrl(genContUrl)
                    .setContFileId(contFileId);
        }

        contractInfoMapper.updateById(cont);

        return ResultUtil.success("完善成功！");
    }

    @Transactional
    @Override
    public Result<Object> perfectContByUpload(ContractSave2Tdo tdo) {
        @NotNull(message = "合同ID不能为空！") Long contId = tdo.getContId();
        ContractInfo dbCont = contractInfoMapper.selectOne(Wrappers.<ContractInfo>lambdaQuery()
                .select(ContractInfo::getContStatus,
                        ContractInfo::getContCode,
                        ContractInfo::getOfferId)
                .eq(ContractInfo::getId, contId));

        if (dbCont == null) {
            return ResultUtil.busiError("合同ID关联的合同信息库中不存在！");
        }

        if (!dbCont.getContStatus().equals("0")) {
            return ResultUtil.busiError("合同状态不正确！");
        }

        LocalDateTime now = LocalDateTime.now();
        @NotBlank(message = "签定时间不能为空！") String signDate = tdo.getSignDate();
        String contCode = dbCont.getContCode();
        if (contCode != null && contCode.length() < 5) {
            SaSession session = StpUtil.getSession();
            LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
            String organId = loginOutputVo.getOrganId();
            Integer count = contractInfoMapper.selectCount(Wrappers.<ContractInfo>lambdaQuery()
                    .gt(ContractInfo::getContStatus, "0")
                    .eq(ContractInfo::getOrganId, organId));
            String signDateStr = signDate.replaceAll("-", "");
            contCode = CodeBuildUtil.genContractCode(organId, signDateStr, count + 1);

        }

        String absoluteUrl = uploadContPath + "/" + tdo.getContFileId();
        String contGenUrl = contCode + ".pdf";
        FileConvertUtil.wordBytes2PdfFile(FileUtil.readBytes(absoluteUrl), uploadContPath + "/" + contGenUrl);
        ContractInfo po = new ContractInfo();
        po.setId(contId)
                .setContStatus("6")
                .setContCode(contCode)
                .setValidity(tdo.getValidity())
                .setSignDate(signDate)
                .setMakeType("1")
                .setContFileId(tdo.getContFileId())
                .setContGenUrl(contGenUrl)
                .setUpdateTime(now);
        contractInfoMapper.updateById(po);

        return ResultUtil.success("完善成功！");
    }

    @Transactional
    @Override
    public Result<Object> submitCont(Long contId) {
        ContractInfo dbCont = contractInfoMapper.selectOne(Wrappers.<ContractInfo>lambdaQuery()
                .select(ContractInfo::getContStatus)
                .eq(ContractInfo::getId, contId));

        if (dbCont == null) {
            return ResultUtil.busiError("合同ID关联的合同信息库中不存在！");
        }

        if (!dbCont.getContStatus().equals("6")) {
            return ResultUtil.busiError("合同状态不正确！");
        }

        contractInfoMapper.update(null, Wrappers.<ContractInfo>lambdaUpdate()
                .set(ContractInfo::getContStatus, "1")
                .set(ContractInfo::getUpdateTime, LocalDateTime.now())
                .eq(ContractInfo::getId, contId));
        return ResultUtil.success("提交成功");
    }

    @Transactional
    @Override
    public Result<Object> auditCont(ContractAuditTdo tdo) {
        @NotBlank(message = "合同ID不能为空！") Long contId = tdo.getContId();
        ContractInfo dbCont = contractInfoMapper.selectOne(Wrappers.<ContractInfo>lambdaQuery()
                .select(ContractInfo::getContStatus,
                        ContractInfo::getOfferId,
                        ContractInfo::getContCode)
                .eq(ContractInfo::getId, contId));

        if (dbCont == null) {
            return ResultUtil.busiError("合同ID关联的合同信息库中不存在！");
        }

        if (!dbCont.getContStatus().equals("1")) {
            return ResultUtil.busiError("合同状态不正确！");
        }

//      合同状态：0：待完善 1:待审核，2审核失败，3:待制作，4：完成，5-已作废；6-待提交

        String offerId = dbCont.getOfferId();
        LocalDateTime now = LocalDateTime.now();
        @NotBlank(message = "审核标识不能为空！") String auditFlag = tdo.getAuditFlag();
        //失败
        String contStatus = "2";
        if (auditFlag.equals("1")) {
            //待制作
            contStatus = "3";
//            JudgeTask dbJudgeTask = judgeTaskMapper.selectOne(Wrappers.<JudgeTask>lambdaQuery()
//                    .select(JudgeTask::getTaskStatus)
//                    .eq(JudgeTask::getOfferId, offerId));
//            if (dbJudgeTask != null) {
//                String taskStatus = dbJudgeTask.getTaskStatus();
//                //成功
//                if (taskStatus.equals("2")) {
            contStatus = "4";
            JudgeTask judgeTask = judgeTaskMapper.selectOne(Wrappers.<JudgeTask>lambdaQuery()
                    .eq(JudgeTask::getOfferId, offerId));
            String judgeTaskStatus = judgeTask.getTaskStatus();
            if (judgeTaskStatus.equals("2") || judgeTaskStatus.equals("6")) {
                /* 创建任务调度 */
                ScheduleTask scheduleTask = scheduleTaskMapper.selectOne(Wrappers.<ScheduleTask>lambdaQuery()
                        .eq(ScheduleTask::getContCode, dbCont.getContCode()).eq(ScheduleTask::getOfferId, offerId));
                if(scheduleTask == null) {
                    scheduleTask = new ScheduleTask();
                    List<OfferPlan> offerPlans = offerPlanMapper.selectList(Wrappers.<OfferPlan>lambdaQuery()
                            .select(OfferPlan::getId, OfferPlan::getOfferId, OfferPlan::getScheduleTimes)
                            .eq(OfferPlan::getOfferId, offerId));
                    Integer planNum = offerPlans.stream().collect(Collectors.summingInt(OfferPlan::getScheduleTimes));
                    scheduleTask.setOfferId(offerId)
                            .setScheduleStatus("0")
                            .setCreateTime(now)
                            .setScheduleFlag("0")
                            .setPlanNum(planNum)
                            .setContCode(dbCont.getContCode());
                    scheduleTaskMapper.insert(scheduleTask);
                }
            }

//                } else if (taskStatus.equals("3")) {
//                    //作废
//                    contStatus = "5";
//                }
//            }

        }

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String userId = (String) tokenInfo.getLoginId();
        ContractAuditRecord auditRecord = new ContractAuditRecord();
        auditRecord.setAuditFlag(auditFlag)
                .setAuditReason(tdo.getAuditReason())
                .setContId(contId)
                .setAuditUserId(userId)
                .setCreateTime(now);
        contractAuditRecordMapper.insert(auditRecord);

        contractInfoMapper.update(null, Wrappers.<ContractInfo>lambdaUpdate()
                .set(ContractInfo::getContStatus, contStatus)
                .set(ContractInfo::getUpdateTime, now)
                .eq(ContractInfo::getId, contId));
        return ResultUtil.success("审核成功");
    }

    @Override
    public Result<List<ContractAuditRecordVo>> findAuditRecordsByContId(Long contId) {
        List<ContractAuditRecord> records = contractAuditRecordMapper.selectList(Wrappers.<ContractAuditRecord>lambdaQuery().eq(ContractAuditRecord::getContId, contId));
        if (records != null && !records.isEmpty()) {
            List<ContractAuditRecordVo> recordVos = records.stream().map(item -> {
                ContractAuditRecordVo recordVo = new ContractAuditRecordVo();
                recordVo.setAuditFlag(item.getAuditFlag())
                        .setAuditReason(item.getAuditReason())
                        .setCreateTime(item.getCreateTime());
                UserVo userVo = sysUserService.findUserByUserId(item.getAuditUserId());
                if (userVo != null) {
                    recordVo.setAuditUserName(userVo.getUsername());
                }
                return recordVo;
            }).collect(Collectors.toList());
            return ResultUtil.data(recordVos);
        }
        return ResultUtil.data(null);
    }

    @Transactional
    @Override
    public Result<Object> invalidCont(Long contId) {
        ContractInfo dbCont = contractInfoMapper.selectOne(Wrappers.<ContractInfo>lambdaQuery()
                .select(ContractInfo::getContStatus,
                        ContractInfo::getOfferId)
                .eq(ContractInfo::getId, contId));

        if (dbCont == null) {
            return ResultUtil.busiError("合同ID关联的合同信息库中不存在！");
        }
        List<String> contStatus = Arrays.asList("0", "6", "2");
        if (!contStatus.contains(dbCont.getContStatus())) {
            return ResultUtil.busiError("合同状态不正确！");
        }
        LocalDateTime now = LocalDateTime.now();
        int cnt = contractInfoMapper.update(null, Wrappers.<ContractInfo>lambdaUpdate()
                .set(ContractInfo::getContStatus, "5")
                .set(ContractInfo::getUpdateTime, now)
                .eq(ContractInfo::getId, contId));
        if (cnt <= 0) {
            throw new CustomException("合同作废失败！");
        }
        cnt = offerInfoMapper.update(null, Wrappers.<OfferInfo>lambdaUpdate()
                .set(OfferInfo::getStatus, "4")
                .set(OfferInfo::getUpdateTime, now)
                .eq(OfferInfo::getId, dbCont.getOfferId()));
        if (cnt <= 0) {
            throw new CustomException("报价单作废失败！");
        }
        JudgeTask judgeTask = judgeTaskService.getOne(Wrappers.<JudgeTask>lambdaQuery()
                .eq(JudgeTask::getOfferId, dbCont.getOfferId()).orderByDesc(JudgeTask::getCreateTime), false);
        if (judgeTask != null) {
            cnt = judgeTaskMapper.update(null, Wrappers.<JudgeTask>lambdaUpdate()
                    .set(JudgeTask::getTaskStatus, "3")
                    .set(JudgeTask::getUpdateTime, now)
                    .eq(JudgeTask::getId, judgeTask.getId()));
            if (cnt < 0) {
                throw new CustomException("技术评审作废失败！");
            }
        }
        return ResultUtil.success("合同作废成功！");
    }

    @Transactional
    @Override
    public Result<Object> restoreCont(Long contId) {
        ContractInfo dbCont = contractInfoMapper.selectOne(Wrappers.<ContractInfo>lambdaQuery()
                .select(ContractInfo::getContStatus)
                .eq(ContractInfo::getId, contId));

        if (dbCont == null) {
            return ResultUtil.busiError("合同ID关联的合同信息库中不存在！");
        }

        if (!dbCont.getContStatus().equals("5")) {
            return ResultUtil.busiError("合同状态不正确！");
        }
        contractInfoMapper.update(null, Wrappers.<ContractInfo>lambdaUpdate()
                .set(ContractInfo::getContStatus, "0")
                .set(ContractInfo::getUpdateTime, LocalDateTime.now())
                .eq(ContractInfo::getId, contId));
        return ResultUtil.success("恢复成功");
    }

    @Override
    public Result<ContPerfectQryVo> findContPerfectInfoById(Long contId) {
        ContractInfo dbCont = contractInfoMapper.selectOne(Wrappers.<ContractInfo>lambdaQuery()
                .select(ContractInfo::getContCode,
                        ContractInfo::getPayMethod,
                        ContractInfo::getOfferId,
                        ContractInfo::getValidity,
                        ContractInfo::getPayMethodDesc,
                        ContractInfo::getSignDate,
                        ContractInfo::getSignLocation,
                        ContractInfo::getCusContBaseInfoId,
                        ContractInfo::getSupContCode)
                .eq(ContractInfo::getId, contId));

        if (dbCont == null) {
            return ResultUtil.busiError("合同ID关联的合同信息库中不存在！");
        }
        ContPerfectQryVo vo = new ContPerfectQryVo();
        Long cusContBaseInfoId = dbCont.getCusContBaseInfoId();
        vo.setContCode(dbCont.getContCode())
                .setPayMethod(dbCont.getPayMethod())
                .setPayMethodDesc(dbCont.getPayMethodDesc())
                .setSignDate(dbCont.getSignDate())
                .setSignLocation(dbCont.getSignLocation())
                .setValidity(dbCont.getValidity())
                .setCusContBaseInfoId(cusContBaseInfoId)
                .setSupContCode(dbCont.getSupContCode());
        OfferContVo offerContVo = offerInfoMapper.findOfferContInfoByOfferId(dbCont.getOfferId());
        if (offerContVo != null) {
            vo.setProjectName(offerContVo.getProjectName())
                    .setConsignorName(offerContVo.getConsignorName())
                    .setTotalCost(offerContVo.getTotalCost());
        }

        if (cusContBaseInfoId != null) {
            CusContractBaseInfo dbCusContBase = cusContractBaseInfoMapper.selectOne(Wrappers.<CusContractBaseInfo>lambdaQuery()
                    .select(CusContractBaseInfo::getId,
                            CusContractBaseInfo::getCompanyName,
                            CusContractBaseInfo::getAddress,
                            CusContractBaseInfo::getAgentPerson,
                            CusContractBaseInfo::getBankName,
                            CusContractBaseInfo::getBankNo,
                            CusContractBaseInfo::getJurPerson,
                            CusContractBaseInfo::getPostCode,
                            CusContractBaseInfo::getTaxNumber,
                            CusContractBaseInfo::getTelFax)
                    .eq(CusContractBaseInfo::getId, cusContBaseInfoId));
            if (dbCusContBase != null) {
                CusContBaseInfoVo infoVo = new CusContBaseInfoVo();
                infoVo.setId(cusContBaseInfoId)
                        .setTelFax(dbCusContBase.getTelFax())
                        .setTaxNumber(dbCusContBase.getTaxNumber())
                        .setPostCode(dbCusContBase.getPostCode())
                        .setAgentPerson(dbCusContBase.getAgentPerson())
                        .setJurPerson(dbCusContBase.getJurPerson())
                        .setCompanyName(dbCusContBase.getCompanyName())
                        .setBankNo(dbCusContBase.getBankNo())
                        .setAddress(dbCusContBase.getAddress())
                        .setBankName(dbCusContBase.getBankName());
                vo.setCusContBaseInfoVo(infoVo);
            }
        }
        return ResultUtil.data(vo);
    }

    @Override
    public ContractData findLoadContractByContCode(String contCode) {
        ContractData data = new ContractData();
        ContractInfo dbCont = contractInfoMapper.selectOne(Wrappers.<ContractInfo>lambdaQuery()
                .select(ContractInfo::getContCode,
                        ContractInfo::getOfferId,
                        ContractInfo::getSignDate,
                        ContractInfo::getSignLocation,
                        ContractInfo::getPayMethod,
                        ContractInfo::getPayMethodDesc,
                        ContractInfo::getSignDate,
                        ContractInfo::getValidity,
                        ContractInfo::getOrganId,
                        ContractInfo::getCusContBaseInfoId)
                .eq(ContractInfo::getContCode, contCode));

        if (dbCont == null) {
            return data;
        }

        String signDate = dbCont.getSignDate();
        String validity = dbCont.getValidity();
        LocalDate localDate = DateUtils.parseDate(signDate, "yyyy-MM-dd");
        LocalDate date2 = localDate.plus(Long.parseLong(validity), ChronoUnit.YEARS);
        LocalDate endDate = date2.minus(1, ChronoUnit.DAYS);
        String endDateStr = DateUtils.getDateStr(endDate, "yyyy-MM-dd");
        String datePeriod = signDate + "~" + endDateStr;
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        ReportBaseInfo reportBase = reportBaseInfoMapper.selectOne(Wrappers.<ReportBaseInfo>lambdaQuery()
                .select(ReportBaseInfo::getLogoImageId)
                .eq(ReportBaseInfo::getOrganId, loginOutputVo.getOrganId()));
        if (reportBase != null) {
            data.setOrganLogo(Pictures.ofLocal(organImgPath + "/" + reportBase.getLogoImageId()).size(60, 40).create());
        }

        String[] startPeriods = signDate.split("-");
        String[] endPeriods = endDateStr.split("-");
        String offerId = dbCont.getOfferId();
        data.setContCode(dbCont.getContCode())
                .setOfferId(offerId)
                .setSignDate(signDate)
                .setSignLocation(dbCont.getSignLocation())
                .setTotalYear(validity)
                .setStartYear(startPeriods[0])
                .setStartMonth(startPeriods[1])
                .setStartDay(startPeriods[2])
                .setEndYear(endPeriods[0])
                .setEndMonth(endPeriods[1])
                .setEndDay(endPeriods[2])
                .setDatePeriod(datePeriod);
        String payMethod = dbCont.getPayMethod();
        data.setPayDesc1("/")
                .setPayDesc2("/")
                .setPayDesc3("/");
        if (("1").equals(payMethod)) {
            data.setPayMethod("(1)");
            data.setPayDesc1(dbCont.getPayMethodDesc());
        } else if (("2").equals(payMethod)) {
            data.setPayMethod("(2)");
            data.setPayDesc2(dbCont.getPayMethodDesc());
        } else {
            data.setPayMethod("(3)");
            data.setPayDesc3(dbCont.getPayMethodDesc());
        }

        String judgeInfo = getJudgeInfo(dbCont.getOrganId(), offerId);

        data.setJudgeInfo(judgeInfo);

        Long cusContBaseInfoId = dbCont.getCusContBaseInfoId();
        if (cusContBaseInfoId != null) {
            CusContractBaseInfo dbCusContBase = cusContractBaseInfoMapper.selectOne(Wrappers.<CusContractBaseInfo>lambdaQuery()
                    .select(CusContractBaseInfo::getId,
                            CusContractBaseInfo::getCompanyName,
                            CusContractBaseInfo::getAddress,
                            CusContractBaseInfo::getAgentPerson,
                            CusContractBaseInfo::getBankName,
                            CusContractBaseInfo::getBankNo,
                            CusContractBaseInfo::getJurPerson,
                            CusContractBaseInfo::getPostCode,
                            CusContractBaseInfo::getTaxNumber,
                            CusContractBaseInfo::getTelFax)
                    .eq(CusContractBaseInfo::getId, cusContBaseInfoId));
            if (dbCusContBase != null) {
                CusContBaseInfoVo partA = new CusContBaseInfoVo();//
                partA.setTelFax(dbCusContBase.getTelFax())
                        .setTaxNumber(dbCusContBase.getTaxNumber())
                        .setPostCode(dbCusContBase.getPostCode())
                        .setAgentPerson(dbCusContBase.getAgentPerson())
                        .setJurPerson(dbCusContBase.getJurPerson())
                        .setCompanyName(dbCusContBase.getCompanyName())
                        .setBankNo(dbCusContBase.getBankNo())
                        .setAddress(dbCusContBase.getAddress())
                        .setBankName(dbCusContBase.getBankName());
                data.setPartA(partA);
            }
        }

        String organId = loginOutputVo.getOrganId();
        OwnerContBaseInfoVo owerOrgan = ownerContractBaseInfoService.findByOrganId(organId);
        if (owerOrgan != null) {
            data.setPartB(owerOrgan);
            data.setContControlId(owerOrgan.getContControlId());
        }
        return data;

    }

    /**
     * TODO 获取分包信息
     *
     * @param organId
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/9/7 7:52 下午
     */
    private String getJudgeInfo(String organId, String offerId) {
        StringBuilder sb = new StringBuilder();
        List<OfferJudgeInfoVo> offerJudges = offerJudgeInfoMapper.findOfferJudgeInfoByCondition(offerId, organId);
        if (offerJudges != null && !offerJudges.isEmpty()) {
            Map<String, List<OfferJudgeInfoVo>> offerJudgeMap = offerJudges.stream().collect(Collectors.groupingBy(OfferJudgeInfoVo::getJudgeOrganName));
            sb.append("（5）客户同意将");
            offerJudgeMap.forEach((organName, factorList) -> {
                Map<String, List<OfferJudgeInfoVo>> secdClassMap = factorList.stream().collect(Collectors.groupingBy(OfferJudgeInfoVo::getSecdClassId));
                secdClassMap.forEach((secdClassId, subFactorList) -> {
                    String secdClassName = dictUtils.getFactorClassMap().get(secdClassId);
                    sb.append(secdClassName);
                    List<String> factorNameList = subFactorList.stream().map(item -> {
                        FactorMethodInfoVo factorMethodId = factorService.findFactorMethodById(item.getCheckStandardId());
                        if (factorMethodId != null) {
                            return factorMethodId.getFactorName();
                        }
                        return null;
                    }).collect(Collectors.toList());
                    String factorNames = factorNameList.stream().filter(item -> item != null).collect(Collectors.joining("、"));
                    sb.append(factorNames).append("；");
                });
                sb.deleteCharAt(sb.lastIndexOf("；"));
                sb.append("分包给" + organName + "。");
            });
        }
        return sb.toString();
    }

    @Override
    public Result<String> findContFileById(Long contId) {

        ContractInfo dbCont = contractInfoMapper.selectOne(Wrappers.<ContractInfo>lambdaQuery()
                .select(ContractInfo::getContGenUrl,
                        ContractInfo::getOfferId,
                        ContractInfo::getContCode)
                .eq(ContractInfo::getId, contId));
        if (dbCont == null) {
            return ResultUtil.busiError("合同ID关联的合同信息库中不存在！");
        }

        String path = null;
        if (StrUtil.isNotBlank(dbCont.getContGenUrl())) {
            path = dbCont.getContGenUrl();
            return ResultUtil.data(path);
        }

        String contCode = dbCont.getContCode();
        ContractData contractData = this.findLoadContractByContCode(contCode);

        if (contractData.getOfferId() != null) {
            OfferTableData offerTable = offerInfoService.findDownloadOfferInfoById(dbCont.getOfferId());
            contractData.setTotalAmount(offerTable.getCapitalAmount())
                    .setSmallTotalAmount(offerTable.getDraftAmount());
            String offerTemplate = templatePath + "/offer_template.docx";
            contractData.setOfferTable(
                    Includes.ofLocal(offerTemplate).setRenderModel(offerTable).create());
        }
        String contTemplate = templatePath + "/contract_template.docx";
        String contFileId = contCode + ".docx";
        String genUrl = uploadContPath + "/" + contFileId;
        boolean isPass = false;
        try {
            WordUtils.genContTableWord(contTemplate, contractData, genUrl);
            isPass = true;
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("生成word失败！");
        }

        if (isPass) {
            String genContUrl = contCode + ".pdf";
            path = genContUrl;
            FileConvertUtil.wordBytes2PdfFile(FileUtil.readBytes(genUrl), uploadContPath + "/" + genContUrl);

            contractInfoMapper.update(null, Wrappers.<ContractInfo>lambdaUpdate()
                    .set(ContractInfo::getContGenUrl, genContUrl)
                    .set(ContractInfo::getContFileId, contFileId)
                    .set(ContractInfo::getUpdateTime, LocalDateTime.now())
                    .eq(ContractInfo::getId, contId));
        }
        return ResultUtil.data(path);
    }

    @Transactional
    @Override
    public Result<Object> uploadSubContFile(MultipartFile file, Long id, String signDate, String validity) {
        SubcontractInfo dbCont = subcontractInfoMapper.selectOne(Wrappers.<SubcontractInfo>lambdaQuery()
                .select(SubcontractInfo::getContStatus)
                .eq(SubcontractInfo::getId, id));
        if (dbCont == null) {
            return ResultUtil.busiError("分包合同在库中不存在！");
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Integer count = subcontractInfoMapper.selectCount(Wrappers.<SubcontractInfo>lambdaQuery().eq(SubcontractInfo::getOrganId, organId));
        String contCode = CodeBuildUtil.genContractCode(organId, signDate, count);

        LocalDateTime now = LocalDateTime.now();
        String contFileId = FileUtil.fileUploadByFileName(file, contCode, uploadContPath);
        SubcontractInfo sc = new SubcontractInfo();
        sc.setContStatus("1")
                .setContCode(contCode)
                .setSignDate(signDate)
                .setValidity(validity)
                .setContFileId(contFileId)
                .setUpdateTime(now)
                .setId(id);
        subcontractInfoMapper.updateById(sc);

        return ResultUtil.success("上传成功！");
    }

    @Override
    public Result<Object> checkSupContCode(String supContCode) {
        ContractInfo ci = this.getOne(Wrappers.<ContractInfo>lambdaQuery()
                .eq(ContractInfo::getContCode, supContCode));
        if (ci == null) {
            return ResultUtil.busiError(String.format("主合同%s不存在！", supContCode));
        }
        if ("5".equals(ci.getContStatus())) {
            return ResultUtil.busiError(String.format("主合同%s已作废！", supContCode));
        }
        if ("2".equals(ci.getContStatus())) {
            return ResultUtil.busiError(String.format("主合同%s审核失败！", supContCode));

        }
        return ResultUtil.success(String.format("主合同%s校验有效！", supContCode));
    }
}
