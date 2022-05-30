package org.hj.chain.platform.offer.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.NumberChineseFormater;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import org.hj.chain.platform.CodeBuildUtil;
import org.hj.chain.platform.DateUtils;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.approval.entity.OfferApprovalRecord;
import org.hj.chain.platform.approval.mapper.OfferApprovalRecordMapper;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.component.DictUtils;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.factor.entity.FactorCheckStandard;
import org.hj.chain.platform.factor.mapper.FactorCheckStandardMapper;
import org.hj.chain.platform.offer.entity.*;
import org.hj.chain.platform.offer.mapper.*;
import org.hj.chain.platform.offer.service.IOfferInfoService;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.service.ISysDeptService;
import org.hj.chain.platform.service.ISysRoleService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.offer.*;
import org.hj.chain.platform.vo.FactorMethodInfoVo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserVo;
import org.hj.chain.platform.vo.offer.*;
import org.hj.chain.platform.word.FactorData;
import org.hj.chain.platform.word.OfferTableData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
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
public class OfferInfoServiceImpl extends ServiceImpl<OfferInfoMapper, OfferInfo> implements IOfferInfoService {

    @Autowired
    private OfferInfoMapper offerInfoMapper;
    @Autowired
    private OfferCostMapper offerCostMapper;
    @Autowired
    private OfferSelfCostMapper offerSelfCostMapper;
    @Autowired
    private OfferFactorInfoMapper offerFactorInfoMapper;
    @Autowired
    private IFactorService factorService;
    @Autowired
    private DictUtils dictUtils;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private FactorCheckStandardMapper factorCheckStandardMapper;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private OfferApprovalRecordMapper offerApprovalRecordMapper;
    @Autowired
    private OfferPlanMapper offerPlanMapper;
    @Autowired
    private OfferPlanFactorMapper offerPlanFactorMapper;
    @Autowired
    private OfferPlanFactorSubsetMapper offerPlanFactorSubsetMapper;

    @Value("${file.template.path}")
    private String templatePath;
    private static final String OFFER_LIST_PERM = "sale:order:list";

    @Override
    public IPage<OfferInfoVo> findOfferInfosByCondition(PageVo pageVo, OfferSearchVo sv) {
        sv.setStatus(StrUtil.trimToNull(sv.getStatus()))
                .setProjectName(StrUtil.trimToNull(sv.getProjectName()))
                .setId(StrUtil.trimToNull(sv.getId()));
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Page<OfferInfoVo> page = PageUtil.initMpPage(pageVo);
        String userType = loginOutputVo.getUserType();
        if (userType.equals("1")) {
            String maxScope = sysRoleService.findMaxRoleScopeByPerms(loginOutputVo.getUserId(), OFFER_LIST_PERM);
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
                offerInfoMapper.findOfferInfosByCondition(page, organId, deptIds, userId, sv);
                page.getRecords().forEach(item -> {
                    UserVo userVo = sysUserService.findUserByUserId(item.getCreateUserId());
                    if (userVo != null) {
                        item.setCreateUserId(userVo.getEmpName());
                    }
                    if ("2".equals(item.getStatus())) {
                        List<OfferApprovalRecord> oars = offerApprovalRecordMapper.selectList(Wrappers.<OfferApprovalRecord>lambdaQuery()
                                .eq(OfferApprovalRecord::getOfferId, item.getId()).orderByDesc(OfferApprovalRecord::getCreateTime));
                        if (oars != null && !oars.isEmpty()) {
                            item.setAuditReason(oars.get(0).getRemark());
                        }
                    }
                });
            }
        } else {
            offerInfoMapper.findOfferInfosByCondition(page, organId, null, null, sv);
            page.getRecords().forEach(item -> {
                UserVo userVo = sysUserService.findUserByUserId(item.getCreateUserId());
                if (userVo != null) {
                    item.setCreateUserId(userVo.getEmpName());
                }
                if ("2".equals(item.getStatus())) {
                    List<OfferApprovalRecord> oars = offerApprovalRecordMapper.selectList(Wrappers.<OfferApprovalRecord>lambdaQuery()
                            .eq(OfferApprovalRecord::getOfferId, item.getId()).orderByDesc(OfferApprovalRecord::getCreateTime));
                    if (oars != null && !oars.isEmpty()) {
                        item.setAuditReason(oars.get(0).getRemark());
                    }
                }
            });
        }
        return page;
    }

    @Override
    public IPage<OfferInfoVo> findHistoryOfferInfosByCondition(PageVo pageVo, OfferSearchVo sv) {
        sv.setStatus(StrUtil.trimToNull(sv.getStatus()))
                .setProjectName(StrUtil.trimToNull(sv.getProjectName()))
                .setId(StrUtil.trimToNull(sv.getId()));
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Page<OfferInfoVo> page = PageUtil.initMpPage(pageVo);
        String userType = loginOutputVo.getUserType();
        if (userType.equals("1")) {
            String maxScope = sysRoleService.findMaxRoleScopeByPerms(loginOutputVo.getUserId(), OFFER_LIST_PERM);
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
                offerInfoMapper.findHistoryOfferInfosByCondition(page, organId, deptIds, userId, sv);
                page.getRecords().forEach(item -> {
                    UserVo userVo = sysUserService.findUserByUserId(item.getCreateUserId());
                    if (userVo != null) {
                        item.setCreateUserId(userVo.getEmpName());
                    }
                    if ("2".equals(item.getStatus())) {
                        List<OfferApprovalRecord> oars = offerApprovalRecordMapper.selectList(Wrappers.<OfferApprovalRecord>lambdaQuery()
                                .eq(OfferApprovalRecord::getOfferId, item.getId()).orderByDesc(OfferApprovalRecord::getCreateTime));
                        if (oars != null && !oars.isEmpty()) {
                            item.setAuditReason(oars.get(0).getRemark());
                        }
                    }
                });
            }
        } else {
            offerInfoMapper.findHistoryOfferInfosByCondition(page, organId, null, null, sv);
            page.getRecords().forEach(item -> {
                UserVo userVo = sysUserService.findUserByUserId(item.getCreateUserId());
                if (userVo != null) {
                    item.setCreateUserId(userVo.getEmpName());
                }
                if ("2".equals(item.getStatus())) {
                    List<OfferApprovalRecord> oars = offerApprovalRecordMapper.selectList(Wrappers.<OfferApprovalRecord>lambdaQuery()
                            .eq(OfferApprovalRecord::getOfferId, item.getId()).orderByDesc(OfferApprovalRecord::getCreateTime));
                    if (oars != null && !oars.isEmpty()) {
                        item.setAuditReason(oars.get(0).getRemark());
                    }
                }
            });
        }
        return page;
    }

    @Transactional
    @Override
    public Result<Object> saveOffer(OfferAddTdo addTdo) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = LocalDateTime.now();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        OfferInfo dbOffer = offerInfoMapper.selectOne(Wrappers.<OfferInfo>lambdaQuery()
                .select(OfferInfo::getId)
                .eq(OfferInfo::getOrganId, organId)
                .apply("YEAR(create_time)={0}", dateTime.getYear())
                .orderByDesc(OfferInfo::getId)
                .last("limit 0,1"));
        String offerId;
        if (dbOffer != null) {
            String maxOfferId = dbOffer.getId();
            String[] split = maxOfferId.split("-");
            int sort = Integer.parseInt(split[1]);
            offerId = CodeBuildUtil.genOfferCode(organId, sort + 1);
        } else {
            offerId = CodeBuildUtil.genOfferCode(organId, 0 + 1);

        }

        @NotNull(message = "报采基本信息不能为空！") OfferInfoTdo offerInfoTdo = addTdo.getOfferInfoTdo();
        @NotEmpty(message = "请至少选择一个监测计划！") List<OfferPlanTdo> offerPlanTdos = addTdo.getOfferPlanTdos();
        //新增监测计划和监测计划相关的因子
        insertOfferInfo(offerPlanTdos, offerId, now);
        OfferInfo info = buildOfferInfoFromTdo(offerInfoTdo, organId, offerId);
        info.setDeptId(loginOutputVo.getDeptId());
        info.setCreateUserId(loginOutputVo.getUserId())
                .setCreateTime(now);
        offerInfoMapper.insert(info);

        @NotNull(message = "收费信息不能为空！") OfferCostTdo offerCostTdo = addTdo.getOfferCostTdo();
        if (offerCostTdo != null) {
            OfferCost offerCost = buildOfferCostFromTdo(offerCostTdo, offerId);
            offerCost.setCreateTime(now);
            offerCostMapper.insert(offerCost);
        }
        List<OfferSelfCostTdo> selfCostTdos = addTdo.getSelfCostTdos();
        //新增自定义花费
        insertSelfCost(selfCostTdos, offerId, now);
        return ResultUtil.success("新增成功！");
    }


    /**
     * TODO 新增自定义花费
     *
     * @param selfCostTdos
     * @param offerId
     * @param now
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/3/7 6:21 下午
     */
    private void insertSelfCost(List<OfferSelfCostTdo> selfCostTdos, String offerId, LocalDateTime now) {
        if (selfCostTdos != null && !selfCostTdos.isEmpty()) {
            List<OfferSelfCost> selfCosts = selfCostTdos.stream().map(item -> {
                OfferSelfCost selfCost = new OfferSelfCost();
                selfCost.setOfferId(offerId)
                        .setAmount(item.getAmount())
                        .setSelfName(item.getSelfName())
                        .setCreateTime(now);
                return selfCost;
            }).collect(Collectors.toList());

            SqlHelper.executeBatch(OfferSelfCost.class, this.log, selfCosts, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
                String sqlStatement = SqlHelper.getSqlStatement(OfferSelfCostMapper.class, SqlMethod.INSERT_ONE);
                sqlSession.insert(sqlStatement, entity);
            });
        }
    }

    /**
     * TODO 新增监测计划及监测因子
     *
     * @param offerPlanTdos
     * @param offerId
     * @param now
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/3/7 6:19 下午
     */
    private void insertOfferInfo(List<OfferPlanTdo> offerPlanTdos, String offerId, LocalDateTime now) {
        offerPlanTdos.stream().forEach(offerPlanTdo -> {
            OfferPlan offerPlan = new OfferPlan();
            offerPlan.setCheckFactorCnt(offerPlanTdo.getCheckFactorCnt())
                    .setCheckFee(offerPlanTdo.getCheckFee())
                    .setCheckFreq(offerPlanTdo.getCheckFreq())
                    .setExecTimes(offerPlanTdo.getExecTimes())
                    .setOfferId(offerId)
                    .setPlanName(offerPlanTdo.getPlanName())
                    .setPlanTime(offerPlanTdo.getPlanTime())
                    .setScheduleTimes(offerPlanTdo.getScheduleTimes())
                    .setCreateTime(now);
            offerPlanMapper.insert(offerPlan);

            Long offerPlanId = offerPlan.getId();
            @NotEmpty(message = "请至少选择一个监测因子！") List<OfferPlanFactorTdo> factorTdoList = offerPlanTdo.getFactorTdoList();
            factorTdoList.stream().forEach(factor -> {
                OfferPlanFactor planFactor = new OfferPlanFactor();
                planFactor.setCheckStandardId(factor.getCheckStandardId())
                        .setDayCount(factor.getDayCount())
                        .setDynamicParam(factor.getDynamicParam())
                        .setFactorGroupKey(factor.getFactorGroupKey())
                        .setFactorName(factor.getFactorName())
                        .setFactorPoint(factor.getFactorPoint())
                        .setFrequency(factor.getFrequency())
                        .setOfferPlanId(offerPlanId)
                        .setOfferId(offerId)
                        .setRemark(factor.getRemark())
                        .setSecdClassId(factor.getSecdClassId())
                        .setCostPerTime(factor.getCostPerTime())
                        .setIsFactor(factor.getIsFactor())
                        .setCreateTime(now);

                offerPlanFactorMapper.insert(planFactor);
                Long planFactorId = planFactor.getId();
                List<String> subCheckStandardIds = factor.getSubCheckStandardIds();
                if (subCheckStandardIds != null && !subCheckStandardIds.isEmpty()) {
                    subCheckStandardIds.stream().forEach(item -> {
                        OfferPlanFactorSubset factorSubset = new OfferPlanFactorSubset();
                        factorSubset.setCheckStandardId(item)
                                .setPlanFactorId(planFactorId)
                                .setOfferId(offerId)
                                .setCreateTime(now);
                        offerPlanFactorSubsetMapper.insert(factorSubset);

                    });
                }
            });
        });
    }


    /**
     * TODO 转化报价PO
     *
     * @param offerCostTdo
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 3:09 下午
     */
    private OfferCost buildOfferCostFromTdo(@NotNull(message = "收费信息不能为空！") OfferCostTdo offerCostTdo, String offerId) {
        OfferCost offerCost = new OfferCost();
        offerCost.setOfferId(offerId)
//                .setCheckCount(offerCostTdo.getCheckCount())
                .setCheckAmount(offerCostTdo.getCheckAmount())
                .setExpediteAmount(offerCostTdo.getExpediteAmount())
                .setLaborAmount(offerCostTdo.getLaborAmount())
                .setReportAmount(offerCostTdo.getReportAmount())
                .setReportAmountRate(offerCostTdo.getReportAmountRate())
                .setSysAmount(offerCostTdo.getSysAmount())
                .setTaxAmountRate(offerCostTdo.getTaxAmountRate())
                .setTaxAmount(offerCostTdo.getTaxAmount())
                .setTripAmount(offerCostTdo.getTripAmount())
                .setDraftAmount(offerCostTdo.getDraftAmount());
//                .setExpediteAmountRate(offerCostTdo.getExpediteAmountRate());
        return offerCost;
    }


    /**
     * TODO 转化po
     *
     * @param offerInfoTdo
     * @param organId
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 3:07 下午
     */
    private OfferInfo buildOfferInfoFromTdo(@NotNull(message = "报采基本信息不能为空！") OfferInfoTdo offerInfoTdo, String organId, String offerId) {
        OfferInfo info = new OfferInfo();
        info.setId(offerId)
                .setCertificationType(offerInfoTdo.getCertificationType())
                .setCheckType(offerInfoTdo.getCheckType())
                .setConsignorLinker(offerInfoTdo.getConsignorLinker())
                .setConsignorLinkerPhone(offerInfoTdo.getConsignorLinkerPhone())
                .setConsignorName(offerInfoTdo.getConsignorName())
                .setDelStatus("0")
                .setFinishDate(offerInfoTdo.getFinishDate())
                .setProjectName(offerInfoTdo.getProjectName())
                .setRemark(offerInfoTdo.getRemark())
                .setExplains(offerInfoTdo.getExplains())
                .setStatus("0")
                .setOrganId(organId);

        return info;
    }

    @Transactional
    @Override
    public Result<Object> modifyOfferByOfferId(OfferModifyTdo modifyTdo) {
        @NotBlank(message = "报价单号不能为空！") String offerId = modifyTdo.getOfferId();
        OfferInfo dbPo = offerInfoMapper.selectOne(Wrappers.<OfferInfo>lambdaQuery()
                .select(OfferInfo::getOrganId)
                .eq(OfferInfo::getId, offerId));
        if (dbPo == null) {
            return ResultUtil.busiError("报价单号关联的记录在库中不存在！");
        }

        LocalDateTime now = LocalDateTime.now();
        String organId = dbPo.getOrganId();
        @NotNull(message = "报采基本信息不能为空！") OfferInfoTdo offerInfoTdo = modifyTdo.getOfferInfoTdo();
        @NotEmpty(message = "请至少选择一个检测计划！") List<OfferPlanTdo> factorPlanTdos = modifyTdo.getOfferPlanTdos();

        OfferInfo info = buildOfferInfoFromTdo(offerInfoTdo, organId, offerId);
        info.setUpdateTime(now);
        offerInfoMapper.updateById(info);

        //删除监测计划（因为监测计划有可能新增，有可能删除，所以采取全量覆盖）
        offerPlanMapper.delete(Wrappers.<OfferPlan>lambdaQuery().eq(OfferPlan::getOfferId, offerId));
        //删除计划因子
        offerPlanFactorMapper.delete(Wrappers.<OfferPlanFactor>lambdaQuery().eq(OfferPlanFactor::getOfferId, offerId));
        //删除子因子
        offerPlanFactorSubsetMapper.delete(Wrappers.<OfferPlanFactorSubset>lambdaQuery().eq(OfferPlanFactorSubset::getOfferId, offerId));

        //新增计划及相关因子
        insertOfferInfo(factorPlanTdos, offerId, now);

        @NotNull(message = "收费信息不能为空！") OfferCostTdo offerCostTdo = modifyTdo.getOfferCostTdo();
        if (offerCostTdo != null) {
            OfferCost offerCost = buildOfferCostFromTdo(offerCostTdo, offerId);
            offerCost.setId(offerCostTdo.getId());
            offerCost.setUpdateTime(now);
            offerCostMapper.updateById(offerCost);
        }


        offerSelfCostMapper.delete(Wrappers.<OfferSelfCost>lambdaQuery().eq(OfferSelfCost::getOfferId, offerId));
        List<OfferSelfCostTdo> selfCostTdos = modifyTdo.getSelfCostTdos();
        insertSelfCost(selfCostTdos, offerId, now);
        return ResultUtil.success("修改成功！");
    }

    @Transactional
    @Override
    public Result<Object> commitByOfferId(String offerId) {
        OfferInfo dbPo = offerInfoMapper.selectOne(Wrappers.<OfferInfo>lambdaQuery()
                .select(OfferInfo::getStatus)
                .eq(OfferInfo::getId, offerId));
        if (dbPo == null) {
            return ResultUtil.busiError("报价单号关联的记录在库中不存在！");
        }

        if (!dbPo.getStatus().equals("0")) {
            return ResultUtil.busiError("只有草稿状态下的数据才可以提交！");
        }
        LocalDateTime now = LocalDateTime.now();
        offerInfoMapper.update(null, Wrappers.<OfferInfo>lambdaUpdate()
                .set(OfferInfo::getStatus, "1")
                .set(OfferInfo::getUpdateTime, now)
                .eq(OfferInfo::getId, offerId));
        return ResultUtil.success("提交成功！");
    }

    @Transactional
    @Override
    public Result<Object> delByOfferId(String offerId) {
        OfferInfo dbPo = offerInfoMapper.selectOne(Wrappers.<OfferInfo>lambdaQuery()
                .select(OfferInfo::getStatus,
                        OfferInfo::getDelStatus)
                .eq(OfferInfo::getId, offerId));
        if (dbPo == null) {
            return ResultUtil.busiError("报价单号关联的记录在库中不存在！");
        }

        if (!dbPo.getStatus().equals("0")) {
            return ResultUtil.busiError("只有草稿状态下的数据才可以删除！");
        }

        if (!dbPo.getDelStatus().equals("0")) {
            return ResultUtil.busiError("该报价单已经被删除！");
        }

        offerInfoMapper.update(null, Wrappers.<OfferInfo>lambdaUpdate()
                .set(OfferInfo::getDelStatus, "1")
                .set(OfferInfo::getUpdateTime, LocalDateTime.now())
                .eq(OfferInfo::getId, offerId));
//        offerPlanMapper.delete(Wrappers.<OfferPlan>lambdaQuery().eq(OfferPlan::getOfferId, offerId));
//        offerPlanFactorMapper.delete(Wrappers.<OfferPlanFactor>lambdaQuery().eq(OfferPlanFactor::getOfferId, offerId));
//        offerPlanFactorSubsetMapper.delete(Wrappers.<OfferPlanFactorSubset>lambdaQuery().eq(OfferPlanFactorSubset::getOfferId, offerId));
//        offerCostMapper.delete(Wrappers.<OfferCost>lambdaQuery().eq(OfferCost::getOfferId, offerId));
//        offerSelfCostMapper.delete(Wrappers.<OfferSelfCost>lambdaQuery().eq(OfferSelfCost::getOfferId, offerId));
        return ResultUtil.success("删除成功！");
    }

    @Transactional
    @Override
    public Result<Object> copyOfferByOfferId(String offerId) {
        OfferInfo dbPo = offerInfoMapper.selectOne(Wrappers.<OfferInfo>lambdaQuery()
                .select(OfferInfo::getOrganId,
                        OfferInfo::getCertificationType,
                        OfferInfo::getCheckType,
                        OfferInfo::getConsignorLinker,
                        OfferInfo::getConsignorLinkerPhone,
                        OfferInfo::getConsignorName,
                        OfferInfo::getFinishDate,
                        OfferInfo::getProjectName,
                        OfferInfo::getDeptId,
                        OfferInfo::getRemark,
                        OfferInfo::getExplains)
                .eq(OfferInfo::getId, offerId));
        if (dbPo == null) {
            return ResultUtil.busiError("报价单号关联的记录在库中不存在！");
        }

        String organId = dbPo.getOrganId();
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = LocalDateTime.now();
        OfferInfo dbOffer = offerInfoMapper.selectOne(Wrappers.<OfferInfo>lambdaQuery()
                .select(OfferInfo::getId)
                .eq(OfferInfo::getOrganId, organId)
                .apply("YEAR(create_time)={0}", dateTime.getYear())
                .orderByDesc(OfferInfo::getId)
                .last("limit 0,1"));
        String nofferId;
        if (dbOffer != null) {
            String maxOfferId = dbOffer.getId();
            String[] split = maxOfferId.split("-");
            int sort = Integer.parseInt(split[1]);
            nofferId = CodeBuildUtil.genOfferCode(organId, sort + 1);
        } else {
            nofferId = CodeBuildUtil.genOfferCode(organId, 0 + 1);

        }
        dbPo.setId(nofferId)
                .setStatus("0")
                .setCreateTime(now)
                .setCreateUserId((String) tokenInfo.getLoginId());
        offerInfoMapper.insert(dbPo);

        String fofferId = nofferId;
        List<OfferPlan> offerPlans = offerPlanMapper.selectList(Wrappers.<OfferPlan>lambdaQuery()
                .select(OfferPlan::getId,
                        OfferPlan::getCheckFactorCnt,
                        OfferPlan::getCheckFee,
                        OfferPlan::getCheckFreq,
                        OfferPlan::getExecTimes,
                        OfferPlan::getScheduleTimes,
                        OfferPlan::getFinishTimes,
                        OfferPlan::getPlanName,
                        OfferPlan::getPlanTime)
                .eq(OfferPlan::getOfferId, offerId));

        if (offerPlans != null && !offerPlans.isEmpty()) {
            List<OfferPlanFactor> offerPlanFactors = offerPlanFactorMapper.selectList(Wrappers.<OfferPlanFactor>lambdaQuery()
                    .select(OfferPlanFactor::getId,
                            OfferPlanFactor::getOfferPlanId,
                            OfferPlanFactor::getSecdClassId,
                            OfferPlanFactor::getDayCount,
                            OfferPlanFactor::getDynamicParam,
                            OfferPlanFactor::getFactorGroupKey,
                            OfferPlanFactor::getFactorName,
                            OfferPlanFactor::getFactorPoint,
                            OfferPlanFactor::getFrequency,
                            OfferPlanFactor::getIsFactor,
                            OfferPlanFactor::getRemark,
                            OfferPlanFactor::getCostPerTime,
                            OfferPlanFactor::getCheckStandardId)
                    .eq(OfferPlanFactor::getOfferId, offerId));

            Map<Long, List<OfferPlanFactor>> planFactorMap = new HashMap<>();
            Map<Long, List<OfferPlanFactorSubset>> planFactorSubsetMap = new HashMap<>();
            if (offerPlanFactors != null && !offerPlanFactors.isEmpty()) {
                Map<Long, List<OfferPlanFactor>> planFactorGroupMap = offerPlanFactors.stream().collect(Collectors.groupingBy(OfferPlanFactor::getOfferPlanId));
                planFactorMap.putAll(planFactorGroupMap);
                List<OfferPlanFactorSubset> offerPlanFactorSubsets = offerPlanFactorSubsetMapper.selectList(Wrappers.<OfferPlanFactorSubset>lambdaQuery()
                        .select(OfferPlanFactorSubset::getCheckStandardId,
                                OfferPlanFactorSubset::getPlanFactorId)
                        .eq(OfferPlanFactorSubset::getOfferId, offerId));
                if (offerPlanFactorSubsets != null && !offerPlanFactorSubsets.isEmpty()) {
                    Map<Long, List<OfferPlanFactorSubset>> planFactorSubsetGroupMap = offerPlanFactorSubsets.stream().collect(Collectors.groupingBy(OfferPlanFactorSubset::getPlanFactorId));
                    planFactorSubsetMap.putAll(planFactorSubsetGroupMap);
                }
            }

            offerPlans.stream().forEach(offerPlan -> {
                offerPlan.setCreateTime(now)
                        .setOfferId(fofferId);
                Long offerPlanId = offerPlan.getId();

                offerPlan.setId(null);
                offerPlanMapper.insert(offerPlan);
                Long newOfferPlanId = offerPlan.getId();

                List<OfferPlanFactor> planFactors = planFactorMap.get(offerPlanId);
                if (planFactors != null && !planFactors.isEmpty()) {
                    planFactors.stream().forEach(factor -> {
                        factor.setOfferPlanId(newOfferPlanId)
                                .setOfferId(fofferId)
                                .setCreateTime(now);
                        Long offerPlanFactorId = factor.getId();
                        factor.setId(null);
                        offerPlanFactorMapper.insert(factor);
                        Long newOfferPlanFactorId = factor.getId();
                        List<OfferPlanFactorSubset> offerPlanFactorSubsets = planFactorSubsetMap.get(offerPlanFactorId);
                        if (offerPlanFactorSubsets != null && !offerPlanFactorSubsets.isEmpty()) {
                            offerPlanFactorSubsets.stream().forEach(factorSubset -> {
                                factorSubset.setOfferId(fofferId)
                                        .setPlanFactorId(newOfferPlanFactorId)
                                        .setCreateTime(now);
                                offerPlanFactorSubsetMapper.insert(factorSubset);
                            });
                        }

                    });

                }
            });
        }

        OfferCost offerCost = offerCostMapper.selectOne(Wrappers.<OfferCost>lambdaQuery().eq(OfferCost::getOfferId, offerId));
        if (offerCost != null) {
            offerCost.setId(null)
                    .setOfferId(nofferId)
                    .setCreateTime(now);
            offerCostMapper.insert(offerCost);
        }

        List<OfferSelfCost> selfCosts = offerSelfCostMapper.selectList(Wrappers.<OfferSelfCost>lambdaQuery().eq(OfferSelfCost::getOfferId, offerId));
        if (selfCosts != null && !selfCosts.isEmpty()) {
            selfCosts.stream().forEach(item -> {
                item.setId(null)
                        .setCreateTime(now)
                        .setOfferId(nofferId);
            });
            SqlHelper.executeBatch(OfferSelfCost.class, this.log, selfCosts, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
                String sqlStatement = SqlHelper.getSqlStatement(OfferSelfCostMapper.class, SqlMethod.INSERT_ONE);
                sqlSession.insert(sqlStatement, entity);
            });
        }

        return ResultUtil.success("复制成功！");
    }

    @Override
    public Result<OfferDetailVo> findOfferDetailsByOfferId(String offerId) {
        OfferInfo dbPo = offerInfoMapper.selectOne(Wrappers.<OfferInfo>lambdaQuery()
                .select(OfferInfo::getOrganId,
                        OfferInfo::getCertificationType,
                        OfferInfo::getCheckType,
                        OfferInfo::getConsignorLinker,
                        OfferInfo::getConsignorLinkerPhone,
                        OfferInfo::getConsignorName,
                        OfferInfo::getFinishDate,
                        OfferInfo::getAuditTime,
                        OfferInfo::getAuditUserId,
                        OfferInfo::getCreateUserId,
                        OfferInfo::getProjectName,
                        OfferInfo::getStatus,
                        OfferInfo::getRemark,
                        OfferInfo::getExplains)
                .eq(OfferInfo::getId, offerId));
        if (dbPo == null) {
            return ResultUtil.busiError("报价单号关联的记录在库中不存在！");
        }

        OfferDetailVo detailVo = new OfferDetailVo();
        detailVo.setOfferId(offerId);
        OfferBaseVo baseVo = new OfferBaseVo();
        baseVo.setProjectName(dbPo.getProjectName())
                .setAuditTime(dbPo.getAuditTime())
                .setAuditUserId(dbPo.getAuditUserId())
                .setCertificationType(dbPo.getCertificationType())
                .setCheckType(dbPo.getCheckType())
                .setConsignorLinker(dbPo.getConsignorLinker())
                .setConsignorLinkerPhone(dbPo.getConsignorLinkerPhone())
                .setConsignorName(dbPo.getConsignorName())
                .setFinishDate(dbPo.getFinishDate())
                .setCreateUserId(dbPo.getCreateUserId())
                .setRemark(dbPo.getRemark())
                .setExplains(dbPo.getExplains())
                .setStatus(dbPo.getStatus());
        UserVo userVo = sysUserService.findUserByUserId(dbPo.getCreateUserId());
        if (userVo != null) {
            baseVo.setCreateUserName(userVo.getEmpName());
        }
        if (dbPo.getAuditUserId() != null) {
            userVo = sysUserService.findUserByUserId(dbPo.getAuditUserId());
            if (userVo != null) {
                baseVo.setAuditUser(userVo.getEmpName());
            }

        }

        detailVo.setOfferBaseVo(baseVo);

        //监测计划
        List<OfferPlan> planList = offerPlanMapper.selectList(Wrappers.<OfferPlan>lambdaQuery()
                .select(OfferPlan::getCheckFactorCnt,
                        OfferPlan::getCheckFee,
                        OfferPlan::getCheckFreq,
                        OfferPlan::getScheduleTimes,
                        OfferPlan::getExecTimes,
                        OfferPlan::getPlanName,
                        OfferPlan::getPlanTime,
                        OfferPlan::getId
                )
                .eq(OfferPlan::getOfferId, offerId));
        if (planList != null && !planList.isEmpty()) {
            Map<Long, List<OfferPlanFactor>> factorMap = new HashMap<>();
            Map<Long, List<OfferPlanFactorSubset>> subFactorsetMap = new HashMap<>();
            List<OfferPlanFactor> factorList = offerPlanFactorMapper.selectList(Wrappers.<OfferPlanFactor>lambdaQuery()
                    .select(OfferPlanFactor::getSecdClassId,
                            OfferPlanFactor::getCheckStandardId,
                            OfferPlanFactor::getCostPerTime,
                            OfferPlanFactor::getDayCount,
                            OfferPlanFactor::getDynamicParam,
                            OfferPlanFactor::getFactorGroupKey,
                            OfferPlanFactor::getFactorName,
                            OfferPlanFactor::getFactorPoint,
                            OfferPlanFactor::getFrequency,
                            OfferPlanFactor::getIsFactor,
                            OfferPlanFactor::getOfferPlanId,
                            OfferPlanFactor::getRemark,
                            OfferPlanFactor::getId)
                    .eq(OfferPlanFactor::getOfferId, offerId));
            if (factorList != null && !factorList.isEmpty()) {
                Map<Long, List<OfferPlanFactor>> planFactorMap = factorList.stream().collect(Collectors.groupingBy(OfferPlanFactor::getOfferPlanId));
                factorMap.putAll(planFactorMap);
                List<OfferPlanFactorSubset> offerPlanFactorSubsets = offerPlanFactorSubsetMapper.selectList(Wrappers.<OfferPlanFactorSubset>lambdaQuery()
                        .select(OfferPlanFactorSubset::getCheckStandardId,
                                OfferPlanFactorSubset::getPlanFactorId)
                        .eq(OfferPlanFactorSubset::getOfferId, offerId));
                if (offerPlanFactorSubsets != null && !offerPlanFactorSubsets.isEmpty()) {
                    Map<Long, List<OfferPlanFactorSubset>> factorSubsetMap = offerPlanFactorSubsets.stream().collect(Collectors.groupingBy(OfferPlanFactorSubset::getPlanFactorId));
                    subFactorsetMap.putAll(factorSubsetMap);
                }
            }

            List<OfferPlanVo> voList = planList.stream().map(item -> {
                Long offerPlanId = item.getId();
                OfferPlanVo planVo = new OfferPlanVo();
                planVo.setOfferPlanId(offerPlanId)
                        .setCheckFactorCnt(item.getCheckFactorCnt())
                        .setCheckFee(item.getCheckFee())
                        .setCheckFreq(item.getCheckFreq())
                        .setScheduleTimes(item.getScheduleTimes())
                        .setExecTimes(item.getExecTimes())
                        .setPlanName(item.getPlanName())
                        .setPlanTime(item.getPlanTime());

                List<OfferPlanFactor> factors = factorMap.get(offerPlanId);
                if (factors != null && !factors.isEmpty()) {
                    List<OfferPlanFactorQryVo> planFactorVos = factors.stream().map(factorItem -> {
                        Long factorId = factorItem.getId();
                        OfferPlanFactorQryVo factorVo = new OfferPlanFactorQryVo();
                        factorVo.setSecdClassId(factorItem.getSecdClassId())
                                .setSecdClassName(dictUtils.getFactorClassMap().get(factorItem.getSecdClassId()))
                                .setCheckStandardId(factorItem.getCheckStandardId())
                                .setCostPerTime(factorItem.getCostPerTime())
                                .setDayCount(factorItem.getDayCount())
                                .setDynamicParam(factorItem.getDynamicParam())
                                .setFactorGroupKey(factorItem.getFactorGroupKey())
                                .setFactorName(factorItem.getFactorName())
                                .setFactorPoint(factorItem.getFactorPoint())
                                .setFrequency(factorItem.getFrequency())
                                .setIsFactor(factorItem.getIsFactor())
                                .setRemark(factorItem.getRemark());
                        //获取监测方法ID对应的信息
                        FactorMethodInfoVo factorMethod = factorService.findFactorMethodById(factorItem.getCheckStandardId());
                        if (factorMethod != null) {
                            String classId = factorMethod.getClassId();
                            factorVo.setClassId(classId);
                            factorVo.setStandardNo(factorMethod.getStandardNo())
                                    .setStandardName(factorMethod.getStandardName())
                                    .setMethodStatus(factorMethod.getStatus())
                                    .setDataEntryStep(factorMethod.getDataEntryStep())
                                    .setAnalysisMethod(factorMethod.getAnalysisMethod())
                                    .setClassName(dictUtils.getFactorClassMap().get(classId));

                        }

                        List<OfferPlanFactorSubset> factorSubsets = subFactorsetMap.get(factorId);
                        if (factorSubsets != null && !factorSubsets.isEmpty()) {
                            List<OfferPlanFactorSubSetVo> subSetVoList = factorSubsets.stream().map(subFactorItem -> {
                                OfferPlanFactorSubSetVo factorSubSetVo = new OfferPlanFactorSubSetVo();
                                factorSubSetVo.setCheckStandardId(subFactorItem.getCheckStandardId());
                                //获取监测方法ID对应的信息
                                FactorMethodInfoVo subFactorMethod = factorService.findFactorMethodById(subFactorItem.getCheckStandardId());
                                if (subFactorMethod != null) {
                                    String classId = subFactorMethod.getClassId();
                                    factorSubSetVo.setClassId(classId);
                                    factorSubSetVo.setStandardNo(subFactorMethod.getStandardNo())
                                            .setStandardName(subFactorMethod.getStandardName())
                                            .setMethodStatus(subFactorMethod.getStatus())
                                            .setAnalysisMethod(subFactorMethod.getAnalysisMethod());

                                }
                                return factorSubSetVo;
                            }).collect(Collectors.toList());

                            factorVo.setFactorSubSetVos(subSetVoList);

                        }
                        return factorVo;
                    }).collect(Collectors.toList());

                    planVo.setPlanFactorVoList(planFactorVos);
                }

                return planVo;
            }).collect(Collectors.toList());

            detailVo.setOfferPlanVos(voList);
        }

        OfferCost offerCost = offerCostMapper.selectOne(Wrappers.<OfferCost>lambdaQuery().eq(OfferCost::getOfferId, offerId));
        if (offerCost != null) {
            OfferCostVo costVo = new OfferCostVo();
            costVo.setCheckAmount(offerCost.getCheckAmount())
                    .setDraftAmount(offerCost.getDraftAmount())
                    .setExpediteAmount(offerCost.getExpediteAmount())
                    .setLaborAmount(offerCost.getLaborAmount())
                    .setReportAmount(offerCost.getReportAmount())
                    .setReportAmountRate(offerCost.getReportAmountRate())
                    .setSysAmount(offerCost.getSysAmount())
                    .setTaxAmount(offerCost.getTaxAmount())
                    .setTaxAmountRate(offerCost.getTaxAmountRate())
                    .setTripAmount(offerCost.getTripAmount())
                    .setId(offerCost.getId());
            detailVo.setCostVo(costVo);
        }

        List<OfferSelfCost> selfCosts = offerSelfCostMapper.selectList(Wrappers.<OfferSelfCost>lambdaQuery().eq(OfferSelfCost::getOfferId, offerId));
        if (selfCosts != null && !selfCosts.isEmpty()) {
            List<OfferSelfCostVo> selfCostVos = selfCosts.stream().map(item -> {
                OfferSelfCostVo selfCostVo = new OfferSelfCostVo();
                selfCostVo.setAmount(item.getAmount())
                        .setSelfName(item.getSelfName());
                return selfCostVo;
            }).collect(Collectors.toList());
            detailVo.setSelfCostVos(selfCostVos);
        }
        return ResultUtil.data(detailVo);
    }

    @Override
    public List<JudgeOfferFactorVo> findJudgeOfferFactorVosByOfferId(String offerId) {
        List<OfferPlanFactor> factors = offerPlanFactorMapper.selectList(Wrappers.<OfferPlanFactor>lambdaQuery()
                .eq(OfferPlanFactor::getOfferId, offerId));
        if (factors != null && !factors.isEmpty()) {
            SaSession session = StpUtil.getSession();
            LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
            String organId = loginOutputVo.getOrganId();
            List<JudgeOfferFactorVo> factorVos = factors.stream().map(item -> {
                JudgeOfferFactorVo factorVo = new JudgeOfferFactorVo();
                factorVo.setPlanFactorId(item.getId())
                        .setOfferId(item.getOfferId())
                        .setOfferPlanId(item.getOfferPlanId())
                        .setIsFactor(item.getIsFactor())
                        .setCheckStandardId(item.getCheckStandardId())
                        .setDayCount(item.getDayCount())
                        .setDynamicParam(item.getDynamicParam())
                        .setFactorPoint(item.getFactorPoint())
                        .setFrequency(item.getFrequency())
                        .setCostPerTime(item.getCostPerTime())
                        .setSecdClassId(item.getSecdClassId())
                        .setRemark(item.getRemark());

                FactorMethodInfoVo factorMethodId = factorService.findFactorMethodById(item.getCheckStandardId());
                if (factorMethodId != null) {
                    factorVo.setStandardName(factorMethodId.getStandardName())
                            .setClassName(factorMethodId.getClassName())
                            .setFactorName(factorMethodId.getFactorName())
                            .setStandardNo(factorMethodId.getStandardNo());
                    factorVo.setSecdClassName(dictUtils.getFactorClassMap().get(item.getSecdClassId()));
                }
                FactorCheckStandard dbCheckStandardPo = factorCheckStandardMapper.selectOne(Wrappers.<FactorCheckStandard>lambdaQuery()
                        .select(FactorCheckStandard::getPrice,
                                FactorCheckStandard::getExtAssistFlg)
                        .eq(FactorCheckStandard::getOrganId, organId)
                        .eq(FactorCheckStandard::getStandardCode, item.getCheckStandardId()));
                if (dbCheckStandardPo != null) {
                    factorVo.setPrice(dbCheckStandardPo.getPrice());
                    factorVo.setExtAssistFlg(dbCheckStandardPo.getExtAssistFlg());
                }
                return factorVo;
            }).collect(Collectors.toList());
            return factorVos;
        }
        return null;
    }

    @Override
    public List<OfferFactorVo> getOfferFactorByOfferId(String offerId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<OfferFactorVo> factorVos = offerFactorInfoMapper.getOfferFactorByOfferId(offerId);
        if (factorVos != null && !factorVos.isEmpty()) {
            factorVos.forEach(f -> {
                String fbFlag = f.getOrganId().equals(organId) ? "0" : "1";
                f.setFbFlag(fbFlag);
                FactorMethodInfoVo factorMethodId = factorService.findFactorMethodById(f.getCheckStandardId());
                if (factorMethodId != null) {
                    f.setStandardName(factorMethodId.getStandardName())
                            .setClassName(factorMethodId.getClassName())
                            .setDefaultUnitName(factorMethodId.getDefaultUnitName());
                    f.setSecdClassName(dictUtils.getFactorClassMap().get(f.getSecdClassId()));
                    f.setFactorName(factorMethodId.getFactorName());
                }
            });
        }
        return factorVos;
    }

    @Override
    public List<JudgeOfferFactorQryVo> findJudgeOfferFactorQryVoByOfferId(String offerId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<OfferPlanFactorVo> vos = offerPlanFactorMapper.findJudgeOfferFactorByOfferId(offerId, organId);
        if (vos != null && !vos.isEmpty()) {
            vos.stream().forEach(item -> {
                FactorMethodInfoVo factorMethodId = factorService.findFactorMethodById(item.getCheckStandardId());
                if (factorMethodId != null) {
                    item.setStandardName(factorMethodId.getStandardName())
                            .setClassName(factorMethodId.getClassName())
                            .setStandardNo(factorMethodId.getStandardNo())
                            .setFactorName(factorMethodId.getFactorName());
                    item.setSecdClassName(dictUtils.getFactorClassMap().get(item.getSecdClassId()));
                }
                FactorCheckStandard dbCheckStandardPo = factorCheckStandardMapper.selectOne(Wrappers.<FactorCheckStandard>lambdaQuery()
                        .select(FactorCheckStandard::getPrice,
                                FactorCheckStandard::getExtAssistFlg)
                        .eq(FactorCheckStandard::getOrganId, organId)
                        .eq(FactorCheckStandard::getStandardCode, item.getCheckStandardId()));
                if (dbCheckStandardPo != null) {
                    item.setPrice(dbCheckStandardPo.getPrice());
                }
            });

            List<JudgeOfferFactorQryVo> qryVos = new ArrayList<>();

            //自检因子
            List<OfferPlanFactorVo> selfFactors = vos.stream().filter(item -> item.getOrganName() == null).collect(Collectors.toList());
            if (selfFactors != null && !selfFactors.isEmpty()) {
                JudgeOfferFactorQryVo qryVo = new JudgeOfferFactorQryVo();
                qryVo.setOrganName("-1");
                qryVo.setFactorVos(selfFactors);
                qryVos.add(qryVo);
            }
            //分包因子
            List<OfferPlanFactorVo> judgeFactors = vos.stream().filter(item -> item.getOrganName() != null).collect(Collectors.toList());
            if (judgeFactors != null && !judgeFactors.isEmpty()) {
                Map<String, List<OfferPlanFactorVo>> listMap = judgeFactors.stream().collect(Collectors.groupingBy(OfferPlanFactorVo::getOrganName));
                listMap.forEach((organName, factorList) -> {
                    JudgeOfferFactorQryVo qryVo = new JudgeOfferFactorQryVo();
                    qryVo.setOrganName(organName);
                    qryVo.setFactorVos(factorList);
                    qryVos.add(qryVo);
                });
            }

            return qryVos;
        }
        return null;
    }

    @Override
    public OfferTableData findDownloadOfferInfoById(String offerId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        OfferTableData model = new OfferTableData();
        model.setOfferId(offerId)
                .setOrganName(loginOutputVo.getOrganName());
        FactorData factorData = new FactorData();
        model.setFactorData(factorData);
        OfferInfo dbPo = offerInfoMapper.selectOne(Wrappers.<OfferInfo>lambdaQuery()
                .select(OfferInfo::getOrganId,
                        OfferInfo::getConsignorLinker,
                        OfferInfo::getConsignorLinkerPhone,
                        OfferInfo::getConsignorName,
                        OfferInfo::getCreateTime,
                        OfferInfo::getCheckType,
                        OfferInfo::getExplains,
                        OfferInfo::getCreateUserId)
                .eq(OfferInfo::getId, offerId));

        if (dbPo != null) {
            String checkType = dbPo.getCheckType();
            String checkTypeVal = "样品送检";
            if (checkType.equals("0")) {
                checkTypeVal = "委托检测";
            }

            model.setOfferTime(DateUtils.getDateStr(dbPo.getCreateTime().toLocalDate(), "yyyy年M月dd日"))
                    .setConsignorName(dbPo.getConsignorName())
                    .setConsignorLinker(dbPo.getConsignorLinker())
                    .setConsignorLinkerPhone(dbPo.getConsignorLinkerPhone())
                    .setCheckType(checkTypeVal)
                    .setExplains(dbPo.getExplains())
                    .setOwnerOrganName(loginOutputVo.getOrganName());

            UserVo dbUser = sysUserService.findUserByUserId(dbPo.getCreateUserId());
            if (dbUser != null) {
                model.setCreateUserName(dbUser.getEmpName());
            }
        } else {
            return model;
        }

        OfferCost offerCost = offerCostMapper.selectOne(Wrappers.<OfferCost>lambdaQuery()
                .select(OfferCost::getCheckAmount,
                        OfferCost::getDraftAmount,
                        OfferCost::getExpediteAmount,
                        OfferCost::getLaborAmount,
                        OfferCost::getReportAmount,
                        OfferCost::getSysAmount,
                        OfferCost::getTaxAmount,
                        OfferCost::getTripAmount)
                .eq(OfferCost::getOfferId, offerId));
        if (offerCost != null) {
            BigDecimal draftAmount = offerCost.getDraftAmount();
            model.setTripAmount(offerCost.getTripAmount().toString())
                    .setTaxAmount(offerCost.getTaxAmount().toString())
                    .setSysAmount(offerCost.getSysAmount().toString())
                    .setReportAmount(offerCost.getReportAmount().toString())
                    .setLaborAmount(offerCost.getLaborAmount().toString())
                    .setExpediteAmount(offerCost.getExpediteAmount().toString())
                    .setDraftAmount(draftAmount.toString())
                    .setCheckAmount(offerCost.getCheckAmount().toString());
//            String captialAmount = MoneyConvertUtils.number2CNMontrayUnit(draftAmount);
            String captialAmount = NumberChineseFormater.format(draftAmount.doubleValue(), true, true);
            model.setCapitalAmount(captialAmount);
        }

        List<OfferSelfCost> selfCostList = offerSelfCostMapper.selectList(Wrappers.<OfferSelfCost>lambdaQuery()
                .select(OfferSelfCost::getSelfName,
                        OfferSelfCost::getAmount)
                .eq(OfferSelfCost::getOfferId, offerId));
        if (selfCostList != null && !selfCostList.isEmpty()) {
            LinkedHashMap<String, String> selfCosts = new LinkedHashMap<>();
            selfCostList.stream().forEach(item -> {
                selfCosts.put(item.getSelfName(), item.getAmount().toString());
            });
            factorData.setSelfCosts(selfCosts);
        }


        List<OfferPlan> dbOfferPlans = offerPlanMapper.selectList(Wrappers.<OfferPlan>lambdaQuery()
                .select(OfferPlan::getId,
                        OfferPlan::getCheckFreq,
                        OfferPlan::getScheduleTimes,
                        OfferPlan::getExecTimes)
                .eq(OfferPlan::getOfferId, offerId));
        if (dbOfferPlans != null && !dbOfferPlans.isEmpty()) {
            List<OfferPlanFactor> factors = offerPlanFactorMapper.selectList(Wrappers.<OfferPlanFactor>lambdaQuery()
                    .select(OfferPlanFactor::getSecdClassId,
                            OfferPlanFactor::getCheckStandardId,
                            OfferPlanFactor::getFactorName,
                            OfferPlanFactor::getIsFactor,
                            OfferPlanFactor::getDayCount,
                            OfferPlanFactor::getFactorPoint,
                            OfferPlanFactor::getFrequency,
                            OfferPlanFactor::getCostPerTime,
                            OfferPlanFactor::getOfferPlanId,
                            OfferPlanFactor::getId)
                    .eq(OfferPlanFactor::getOfferId, offerId));

            LinkedHashMap<String, List<RowRenderData>> factorsMap = new LinkedHashMap<>();
            if (factors != null && !factors.isEmpty()) {
                Map<Long, OfferPlan> offerPlanMap = dbOfferPlans.stream().collect(Collectors.toMap(OfferPlan::getId, Function.identity()));
                Map<String, List<OfferPlanFactor>> listMap = factors.stream().collect(Collectors.groupingBy(OfferPlanFactor::getSecdClassId));
                AtomicInteger serNoAtom = new AtomicInteger(1);
                AtomicReference<BigDecimal> totalAmount = new AtomicReference<BigDecimal>(new BigDecimal(0));
                listMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
                    String secdClassId = entry.getKey();
                    List<OfferPlanFactor> factorList = entry.getValue();
                    String secdClassName = dictUtils.getFactorClassMap().get(secdClassId);
                    int serNo = serNoAtom.getAndIncrement();
                    AtomicReference<BigDecimal> singleAmount = new AtomicReference<BigDecimal>(new BigDecimal(0));
                    AtomicInteger subSerNoAtom = new AtomicInteger(1);
                    List<RowRenderData> renderDataList = factorList.stream().map(item -> {
                        OfferPlan dbPlan = offerPlanMap.get(item.getOfferPlanId());
                        Long planFactorId = item.getId();
                        String subSerNo = serNo + "." + subSerNoAtom.getAndIncrement();
                        FactorMethodInfoVo factorMethodId = factorService.findFactorMethodById(item.getCheckStandardId());
                        String factorName = item.getFactorName();
                        String standardName = null;
                        if (factorMethodId != null) {
                            standardName = factorMethodId.getStandardName();
                        }

//                        String isFactor = item.getIsFactor();
//                        if (isFactor.equals("1")) {
//                            List<OfferPlanFactorSubset> factorSubsets = offerPlanFactorSubsetMapper.selectList(Wrappers.<OfferPlanFactorSubset>lambdaQuery()
//                                    .select(OfferPlanFactorSubset::getCheckStandardId)
//                                    .eq(OfferPlanFactorSubset::getPlanFactorId, planFactorId));
//                            if (factorSubsets != null && !factorSubsets.isEmpty()) {
//                                String subFactorNames = factorSubsets.stream().map(factorSubItem -> {
//                                    FactorMethodInfoVo methodInfoVo = factorService.findFactorMethodById(factorSubItem.getCheckStandardId());
//                                    if (methodInfoVo != null) {
//                                        return methodInfoVo.getFactorName();
//                                    }
//                                    return null;
//
//                                }).filter(subFactorName -> subFactorName != null).collect(Collectors.joining("、"));
//                                factorName.append("(").append(subFactorNames).append(")");
//                            }
//                        }
                        //频次
                        Integer frequency = item.getFrequency();
                        //天数
                        Integer dayCount = item.getDayCount();
                        //样品数
                        int sampleCount = frequency * dayCount;
                        //单次费用
                        BigDecimal costPerTime = item.getCostPerTime();
                        //总次数
                        Integer totalCount = dbPlan.getScheduleTimes() * sampleCount;
                        //该因子检测总费用
                        BigDecimal totalCost = costPerTime.multiply(new BigDecimal(totalCount));
                        singleAmount.getAndAccumulate(totalCost, BigDecimal::add);
                        RowRenderData factor = Rows.of(
                                String.valueOf(serNo),//序号
                                secdClassName,//检测对象
                                subSerNo,//子序号
                                factorName.toString(),//因子名称
                                item.getFactorPoint(),//检测位置
                                standardName,//标准名称
                                String.valueOf(frequency),//频次
                                String.valueOf(dayCount),//天数
                                dbPlan.getCheckFreq(),//监测频次
                                String.valueOf(dbPlan.getExecTimes()),//执行次数
                                String.valueOf(totalCount),//总数
                                costPerTime.toString(),//单次检测费用
                                totalCost.toString())  //检测费用
                                .textFontFamily("宋体")
                                .center().create();
                        return factor;

                    }).collect(Collectors.toList());

                    totalAmount.getAndAccumulate(singleAmount.get(), BigDecimal::add);
                    RowRenderData secdClassTotal = Rows.of(String.valueOf(serNo),
                            secdClassName,
                            "小计",
                            singleAmount.get().toString())
                            .textFontFamily("宋体")
                            .center().create();
                    renderDataList.add(secdClassTotal);
                    factorsMap.put(secdClassName, renderDataList);
                });
                factorData.setTotalFactorAmount(totalAmount.get().toString());
            }
            factorData.setFactorsMap(factorsMap);

        }


        return model;
    }

    @Override
    public Map<String, Object> findDownloadExcelOfferInfoById(String offerId) {
        Map<String, Object> dataMap = new HashMap<>();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        dataMap.put("offerId", offerId);
        dataMap.put("organName", loginOutputVo.getOrganName());
        dataMap.put("offerId", offerId);
        OfferInfo dbPo = offerInfoMapper.selectOne(Wrappers.<OfferInfo>lambdaQuery()
                .select(OfferInfo::getOrganId,
                        OfferInfo::getConsignorLinker,
                        OfferInfo::getConsignorLinkerPhone,
                        OfferInfo::getConsignorName,
                        OfferInfo::getCreateTime,
                        OfferInfo::getCheckType,
                        OfferInfo::getExplains,
                        OfferInfo::getCreateUserId)
                .eq(OfferInfo::getId, offerId));

        if (dbPo != null) {
            String checkType = dbPo.getCheckType();
            String checkTypeVal = "样品送检";
            if (checkType.equals("0")) {
                checkTypeVal = "委托检测";
            }

            dataMap.put("offerTime", DateUtils.getDateStr(dbPo.getCreateTime().toLocalDate(), "yyyy年M月dd日"));
            dataMap.put("consignorName", dbPo.getConsignorName());
            dataMap.put("consignorLinker", dbPo.getConsignorLinker());
            dataMap.put("consignorLinkerPhone", dbPo.getConsignorLinkerPhone());
            dataMap.put("checkType", checkTypeVal);
            dataMap.put("explains", dbPo.getExplains());
//            dataMap.put("ownerOrganName", loginOutputVo.getOrganName());

            UserVo dbUser = sysUserService.findUserByUserId(dbPo.getCreateUserId());
            if (dbUser != null) {
                dataMap.put("createUserName", dbUser.getEmpName());
            }
        } else {
            return dataMap;
        }

        OfferCost offerCost = offerCostMapper.selectOne(Wrappers.<OfferCost>lambdaQuery()
                .select(OfferCost::getCheckAmount,
                        OfferCost::getDraftAmount,
                        OfferCost::getExpediteAmount,
                        OfferCost::getLaborAmount,
                        OfferCost::getReportAmount,
                        OfferCost::getSysAmount,
                        OfferCost::getTaxAmount,
                        OfferCost::getTripAmount)
                .eq(OfferCost::getOfferId, offerId));
        if (offerCost != null) {
            BigDecimal draftAmount = offerCost.getDraftAmount();
            String captialAmount = NumberChineseFormater.format(draftAmount.doubleValue(), true, true);
            dataMap.put("tripAmount", offerCost.getTripAmount());
            dataMap.put("taxAmount", offerCost.getTaxAmount());
            dataMap.put("sysAmount", offerCost.getSysAmount());
            dataMap.put("reportAmount", offerCost.getReportAmount());
            dataMap.put("laborAmount", offerCost.getLaborAmount());
            dataMap.put("expediteAmount", offerCost.getExpediteAmount());
            dataMap.put("draftAmount", draftAmount);
            dataMap.put("capitalAmount", captialAmount);
            dataMap.put("checkAmount", offerCost.getCheckAmount());
        }

        List<OfferSelfCost> selfCostList = offerSelfCostMapper.selectList(Wrappers.<OfferSelfCost>lambdaQuery()
                .select(OfferSelfCost::getSelfName,
                        OfferSelfCost::getAmount)
                .eq(OfferSelfCost::getOfferId, offerId));
        if (selfCostList != null) {
            List<Map<String, Object>> selfCosts = new ArrayList<>();
            selfCostList.stream().forEach(item -> {
                Map<String, Object> selfCost = new HashMap<>();
                selfCost.put("selfName", item.getSelfName());
                selfCost.put("amount", item.getAmount());
                selfCosts.add(selfCost);
            });
            dataMap.put("selfCosts", selfCosts);
        }


        List<OfferPlan> dbOfferPlans = offerPlanMapper.selectList(Wrappers.<OfferPlan>lambdaQuery()
                .select(OfferPlan::getId,
                        OfferPlan::getCheckFreq,
                        OfferPlan::getScheduleTimes,
                        OfferPlan::getExecTimes)
                .eq(OfferPlan::getOfferId, offerId));
        if (dbOfferPlans != null && !dbOfferPlans.isEmpty()) {
            List<OfferPlanFactor> factors = offerPlanFactorMapper.selectList(Wrappers.<OfferPlanFactor>lambdaQuery()
                    .select(OfferPlanFactor::getSecdClassId,
                            OfferPlanFactor::getCheckStandardId,
                            OfferPlanFactor::getFactorName,
                            OfferPlanFactor::getIsFactor,
                            OfferPlanFactor::getDayCount,
                            OfferPlanFactor::getFactorPoint,
                            OfferPlanFactor::getFrequency,
                            OfferPlanFactor::getCostPerTime,
                            OfferPlanFactor::getOfferPlanId,
                            OfferPlanFactor::getId)
                    .eq(OfferPlanFactor::getOfferId, offerId));

            if (factors != null && !factors.isEmpty()) {
                Map<Long, OfferPlan> offerPlanMap = dbOfferPlans.stream().collect(Collectors.toMap(OfferPlan::getId, Function.identity()));
                Map<String, List<OfferPlanFactor>> listMap = factors.stream().collect(Collectors.groupingBy(OfferPlanFactor::getSecdClassId));
                List<Map<String, Object>> factorMapList = new ArrayList<Map<String, Object>>();
                dataMap.put("list", factorMapList);


                StringBuilder mergeRows = new StringBuilder();
                AtomicInteger serNoAtom = new AtomicInteger(1);
                listMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
                    String secdClassId = entry.getKey();
                    List<OfferPlanFactor> factorList = entry.getValue();
                    String secdClassName = dictUtils.getFactorClassMap().get(secdClassId);
                    int serNo = serNoAtom.getAndIncrement();
                    AtomicReference<BigDecimal> singleAmount = new AtomicReference<BigDecimal>(new BigDecimal(0));
                    AtomicInteger subSerNoAtom = new AtomicInteger(1);
                    factorList.stream().forEach(item -> {
                        Map<String, Object> factorMap = new HashMap<>();
                        OfferPlan dbPlan = offerPlanMap.get(item.getOfferPlanId());
//                        Long planFactorId = item.getId();
                        String subSerNo = serNo + "." + subSerNoAtom.getAndIncrement();
                        FactorMethodInfoVo factorMethodId = factorService.findFactorMethodById(item.getCheckStandardId());
                        String factorName = item.getFactorName();
                        String standardName = null;
                        if (factorMethodId != null) {
                            standardName = factorMethodId.getStandardName();
                        }

//                        String isFactor = item.getIsFactor();
//                        if (isFactor.equals("1")) {
//                            List<OfferPlanFactorSubset> factorSubsets = offerPlanFactorSubsetMapper.selectList(Wrappers.<OfferPlanFactorSubset>lambdaQuery()
//                                    .select(OfferPlanFactorSubset::getCheckStandardId)
//                                    .eq(OfferPlanFactorSubset::getPlanFactorId, planFactorId));
//                            if (factorSubsets != null && !factorSubsets.isEmpty()) {
//                                String subFactorNames = factorSubsets.stream().map(factorSubItem -> {
//                                    FactorMethodInfoVo methodInfoVo = factorService.findFactorMethodById(factorSubItem.getCheckStandardId());
//                                    if (methodInfoVo != null) {
//                                        return methodInfoVo.getFactorName();
//                                    }
//                                    return null;
//
//                                }).filter(subFactorName -> subFactorName != null).collect(Collectors.joining("、"));
//                                factorName.append("(").append(subFactorNames).append(")");
//                            }
//                        }
                        //频次
                        Integer frequency = item.getFrequency();
                        //天数
                        Integer dayCount = item.getDayCount();
                        //样品数
                        int sampleCount = frequency * dayCount;
                        //单次费用
                        BigDecimal costPerTime = item.getCostPerTime();
                        //总次数
                        Integer totalCount = dbPlan.getScheduleTimes() * sampleCount;
                        //该因子检测总费用
                        BigDecimal totalCost = costPerTime.multiply(new BigDecimal(totalCount));
                        singleAmount.getAndAccumulate(totalCost, BigDecimal::add);
                        factorMap.put("serNo", serNo);
                        factorMap.put("secdClassName", secdClassName);
                        factorMap.put("standardName", standardName);
                        factorMap.put("subSerNo", subSerNo);
                        factorMap.put("factorName", factorName);
                        factorMap.put("factorPoint", item.getFactorPoint());
                        factorMap.put("frequency", item.getFrequency());
                        factorMap.put("dayCount", item.getDayCount());
                        factorMap.put("checkFreq", dbPlan.getCheckFreq());
                        factorMap.put("execTimes", dbPlan.getExecTimes());
                        factorMap.put("totalCount", totalCount);
                        factorMap.put("costPerTime", costPerTime);
                        factorMap.put("totalCost", totalCost);
                        factorMapList.add(factorMap);

                    });

                    Map<String, Object> sumMap = new HashMap<>();
                    sumMap.put("serNo", serNo);
                    sumMap.put("secdClassName", secdClassName);
                    sumMap.put("subSerNo", "小计");
                    sumMap.put("standardName", "");
                    sumMap.put("factorName", "");
                    sumMap.put("factorPoint", "");
                    sumMap.put("frequency", "");
                    sumMap.put("dayCount", "");
                    sumMap.put("checkFreq", "");
                    sumMap.put("execTimes", "");
                    sumMap.put("totalCount", "");
                    sumMap.put("costPerTime", "");
                    sumMap.put("totalCost", singleAmount.get());
                    factorMapList.add(sumMap);
                    mergeRows.append(subSerNoAtom.get() - 1).append("|");
                });
                dataMap.put("101", mergeRows.toString());
            }


        }


        return dataMap;
    }

    @Override
    public List<OfferPlanFactorVo> getFactorsByOfferPlanId(Long offerPlanId) {
        return offerPlanFactorMapper.getFactorsByOfferPlanId(offerPlanId);
    }

    @Override
    public List<OfferPlanFactorVo> getFactorsByOfferPlanIds(List<Long> offerPlanIds) {
        return offerPlanFactorMapper.getFactorsByOfferPlanIds(offerPlanIds);
    }
}
