package org.hj.chain.platform.report.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.data.Pictures;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.*;
import org.hj.chain.platform.check.entity.CheckFactorSubset;
import org.hj.chain.platform.check.mapper.CheckFactorInfoMapper;
import org.hj.chain.platform.check.mapper.CheckFactorSubsetMapper;
import org.hj.chain.platform.common.BusiUtils;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.component.DictUtils;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.mapper.SysUserMapper;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.offer.entity.OfferInfo;
import org.hj.chain.platform.offer.mapper.OfferInfoMapper;
import org.hj.chain.platform.region.entity.SysRegion;
import org.hj.chain.platform.region.mapper.SysRegionMapper;
import org.hj.chain.platform.report.mapper.ReportBaseInfoMapper;
import org.hj.chain.platform.report.mapper.ReportCheckMapper;
import org.hj.chain.platform.report.mapper.ReportInfoMapper;
import org.hj.chain.platform.report.mapper.ReportSignMapper;
import org.hj.chain.platform.report.model.ReportBaseInfo;
import org.hj.chain.platform.report.model.ReportCheck;
import org.hj.chain.platform.report.model.ReportInfo;
import org.hj.chain.platform.report.model.ReportSign;
import org.hj.chain.platform.report.service.IReportService;
import org.hj.chain.platform.sample.mapper.SampleItemFactorDataMapper;
import org.hj.chain.platform.sample.mapper.SampleItemMapper;
import org.hj.chain.platform.schedule.entity.ScheduleJob;
import org.hj.chain.platform.schedule.mapper.ScheduleJobMapper;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.tdo.report.ReportInfoAuditTdo;
import org.hj.chain.platform.vo.FactorMethodInfoVo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.check.CheckResParam;
import org.hj.chain.platform.vo.check.ReportCheckDetailVo;
import org.hj.chain.platform.vo.equipment.EquipmentTreeVo;
import org.hj.chain.platform.vo.report.*;
import org.hj.chain.platform.vo.sample.SampleFactorDataParam;
import org.hj.chain.platform.vo.samplebak.ReportEquipVo;
import org.hj.chain.platform.vo.samplebak.ReportSampleVo;
import org.hj.chain.platform.word.ReportMainData;
import org.hj.chain.platform.word.ReportTableData;
import org.hj.chain.platform.word.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/23  7:39 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@Slf4j
@Service
public class ReportServiceImpl implements IReportService {
    @Autowired
    private ReportInfoMapper reportInfoMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private CheckFactorInfoMapper checkFactorInfoMapper;
    @Autowired
    private OfferInfoMapper offerInfoMapper;
    @Autowired
    private ReportBaseInfoMapper reportBaseInfoMapper;
    @Autowired
    private ScheduleJobMapper jobMapper;
    @Autowired
    private SampleItemMapper sampleItemMapper;
    @Autowired
    private ReportCheckMapper reportCheckMapper;
    @Autowired
    private ReportSignMapper reportSignMapper;
    @Autowired
    private IFactorService factorService;
    @Autowired
    private SysRegionMapper sysRegionMapper;
    @Autowired
    private SampleItemFactorDataMapper sampleItemFactorDataMapper;
    @Autowired
    private CheckFactorSubsetMapper checkFactorSubsetMapper;
    @Autowired
    private DictUtils dictUtils;
    @Value("${file.template.path}")
    private String templatePath;
    @Value("${file.template.report}")
    private String reportGenPath;
    @Value("${image.upload.report}")
    private String organImgPath;

    @Override
    public IPage<ReportVo> findReportInfosByCondition(PageVo pageVo, ReportSearchVo sv) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Page<ReportVo> page = PageUtil.initMpPage(pageVo);
        reportInfoMapper.findReportInfosByCondition(page, organId, sv);
        page.getRecords().stream().forEach(item -> {
            String reportMakeUserId = item.getReportMakeUserId();
            if (StrUtil.isNotBlank(reportMakeUserId)) {
                SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                        .select(SysUser::getEmpName)
                        .eq(SysUser::getUserId, reportMakeUserId));
                if (sysUser != null) {
                    item.setReportMakeUser(sysUser.getEmpName());
                }
            }
        });

        return page;
    }

    @Override
    public IPage<ReportVo> findHisReportInfosByCondition(PageVo pageVo, ReportSearchVo sv) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Page<ReportVo> page = PageUtil.initMpPage(pageVo);
        reportInfoMapper.findHisReportInfosByCondition(page, organId, sv);
        page.getRecords().stream().forEach(item -> {
            String reportMakeUserId = item.getReportMakeUserId();
            if (StrUtil.isNotBlank(reportMakeUserId)) {
                SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                        .select(SysUser::getEmpName)
                        .eq(SysUser::getUserId, reportMakeUserId));
                if (sysUser != null) {
                    item.setReportMakeUser(sysUser.getEmpName());
                }
            }
        });

        return page;
    }

    @Override
    public Result<Object> submitReport(Long id) {
        if (id == null) {
            return ResultUtil.validateError("报告ID不能为空！");
        }
        ReportInfo ri = reportInfoMapper.selectById(id);
        if (ri == null) {
            return ResultUtil.busiError("报告信息不存在！");
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        if (!ri.getReportMakeUserId().equals(loginOutputVo.getUserId())) {
            return ResultUtil.busiError("非报告制作人不能提交审核！");
        }
        LocalDateTime now = LocalDateTime.now();
        int cnt = reportInfoMapper.update(null, Wrappers.<ReportInfo>lambdaUpdate()
                .set(ReportInfo::getReportStatus, "4")
                .set(ReportInfo::getUpdateTime, now)
                .eq(ReportInfo::getId, id));
        if (cnt > 0) {
            ReportCheck reportCheck = reportCheckMapper.selectOne(Wrappers.<ReportCheck>lambdaQuery()
                    .eq(ReportCheck::getReportId, id).eq(ReportCheck::getCheckStatus, '0'));
            if (reportCheck == null) {
                reportCheck = new ReportCheck();
                reportCheck.setReportId(id)
                        .setOrganId(loginOutputVo.getOrganId())
                        .setCreateTime(now);
                reportCheckMapper.insert(reportCheck);
            }
            return ResultUtil.success("报告提交审核成功！");
        } else {
            return ResultUtil.busiError("报告提交审核失败！");
        }
    }

    @Override
    public Result<Object> doAuditReport(ReportInfoAuditTdo tdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String userId = loginOutputVo.getUserId();
        Long reportId = tdo.getReportId();
        ReportInfo ri = reportInfoMapper.selectById(reportId);
        if (ri == null) {
            return ResultUtil.busiError("报告信息不存在！");
        }
//        if (ri.getReportMakeUserId().equals(loginOutputVo.getUserId()) ||
//                loginOutputVo.getPlatformPositionId().equals(PlatformConstants.REPORT_SIGN_ID) ||
//                !loginOutputVo.getPlatformPositionId().equals(PlatformConstants.REPORT_CHK_ID)) {
//            return ResultUtil.busiError("无审核权限！");
//        }
        LocalDateTime now = LocalDateTime.now();
        String auditFlag = tdo.getAuditFlag();
        ReportCheck reportCheck = new ReportCheck();
        reportCheck.setReportId(reportId)
                .setCheckReason(tdo.getAuditReason())
                .setCheckUserId(userId)
                .setCheckTime(now)
                .setUpdateTime(now);
        if (auditFlag.equals("1")) {
            reportCheck.setCheckStatus("1");
            reportCheckMapper.update(reportCheck, Wrappers.<ReportCheck>lambdaUpdate().eq(ReportCheck::getReportId, reportId));
            ReportSign sign = reportSignMapper.selectOne(Wrappers.<ReportSign>lambdaQuery()
                    .eq(ReportSign::getReportId, reportId).eq(ReportSign::getSignStatus, '0'));
            if (sign == null) {
                sign = new ReportSign();
                sign.setReportId(reportId)
                        .setOrganId(loginOutputVo.getOrganId())
                        .setCreateTime(now);
                reportSignMapper.insert(sign);
            }
            int cnt = reportInfoMapper.update(null, Wrappers.<ReportInfo>lambdaUpdate()
                    .set(ReportInfo::getReportStatus, "5")
                    .eq(ReportInfo::getId, reportId));
            if (cnt > 0) {
                return ResultUtil.success("报告审核通过成功！");
            } else {
                return ResultUtil.busiError("报告审核通过失败！");
            }
        } else {
            reportCheck.setCheckStatus("2");
            reportCheckMapper.update(reportCheck, Wrappers.<ReportCheck>lambdaUpdate().eq(ReportCheck::getReportId, reportId));
            int cnt = reportInfoMapper.update(null, Wrappers.<ReportInfo>lambdaUpdate()
                    .set(ReportInfo::getReportStatus, "7")
                    .eq(ReportInfo::getId, reportId));
            if (cnt > 0) {
                return ResultUtil.success("报告驳回成功！");
            } else {
                return ResultUtil.busiError("报告驳回失败！");
            }
        }
    }

    @Override
    public Result<Object> doSignReport(ReportInfoAuditTdo tdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String userId = loginOutputVo.getUserId();
        Long reportId = tdo.getReportId();
        ReportInfo ri = reportInfoMapper.selectById(reportId);
        if (ri == null) {
            return ResultUtil.busiError("报告信息不存在！");
        }
//        if (!loginOutputVo.getPlatformPositionId().equals(PlatformConstants.REPORT_SIGN_ID)) {
//            return ResultUtil.busiError("无审核权限！");
//        }
        LocalDateTime now = LocalDateTime.now();
        String auditFlag = tdo.getAuditFlag();
        ReportSign sign = new ReportSign();
        sign.setReportId(reportId)
                .setSignUserId(userId)
                .setSignTime(now)
                .setRjctReason(tdo.getAuditReason())
                .setUpdateTime(now);
        if (auditFlag.equals("1")) {
            sign.setSignStatus("1");
            reportSignMapper.update(sign, Wrappers.<ReportSign>lambdaUpdate().eq(ReportSign::getReportId, reportId));
            int cnt = reportInfoMapper.update(null, Wrappers.<ReportInfo>lambdaUpdate()
                    .set(ReportInfo::getReportStatus, "6")
                    .eq(ReportInfo::getId, reportId));
            if (cnt > 0) {
                return ResultUtil.success("报告签发成功！");
            } else {
                return ResultUtil.busiError("报告签发失败！");
            }
        } else {
            sign.setSignStatus("2");
            reportSignMapper.update(sign, Wrappers.<ReportSign>lambdaUpdate().eq(ReportSign::getReportId, reportId));
            int cnt = reportInfoMapper.update(null, Wrappers.<ReportInfo>lambdaUpdate()
                    .set(ReportInfo::getReportStatus, "7")
                    .eq(ReportInfo::getId, reportId));
            if (cnt > 0) {
                return ResultUtil.success("报告驳回成功！");
            } else {
                return ResultUtil.busiError("报告驳回失败！");
            }
        }
    }

    @Override
    public ReportGenDataVo findReportDataByReportCode(String reportCode) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        ReportBaseParam baseParam = reportInfoMapper.findReportBaseByCondition(reportCode, organId);

        LocalDateTime now = LocalDateTime.now();
        ReportGenDataVo genDataVo = new ReportGenDataVo();
        if (baseParam != null) {
            genDataVo.setReportId(baseParam.getReportId());
            String jobId = baseParam.getJobId();
            String offerId = baseParam.getOfferId();
            OfferInfo dbOffer = offerInfoMapper.selectOne(Wrappers.<OfferInfo>lambdaQuery()
                    .select(OfferInfo::getOrganId,
                            OfferInfo::getCheckType,
                            OfferInfo::getCertificationType)
                    .eq(OfferInfo::getId, offerId));
            ReportTableData rtd = new ReportTableData();
            genDataVo.setTableData(rtd);
            rtd.setReportCode(reportCode)
                    .setReportDate(DateUtils.getDateStr(now.toLocalDate(), "yyyy-MM-dd"))
                    .setProjectName(baseParam.getProjectName())
                    .setOwnerOrganName(loginOutputVo.getOrganName());
            if (dbOffer != null) {
//                rtd.setProjectName(dbOffer.getProjectName());
                if (dbOffer.getCheckType().equals("0")) {
//                    0-委托检测；1-样品送检
                    rtd.setCheckType("委托检测");
                } else {
                    rtd.setCheckType("样品送检");
                }
                ReportBaseInfo dbBase = reportBaseInfoMapper.selectOne(Wrappers.<ReportBaseInfo>lambdaQuery().eq(ReportBaseInfo::getOrganId, dbOffer.getOrganId()));
                if (dbBase != null) {
                    rtd.setAddress(dbBase.getAddress())
                            .setFax(dbBase.getFax())
                            .setTel(dbBase.getTel())
                            .setPostCode(dbBase.getPostCode())
                            .setWebAddress(dbBase.getWebsite())
                            .setExplains(dbBase.getExplains());
                    String certTypeImg = templatePath + "/CMA.png";
                    if (dbOffer.getCertificationType().equals("1")) {
                        rtd.setCertCode(dbBase.getCmaNumber());
                    } else {
                        rtd.setCertCode(dbBase.getCnasNumber());
                        certTypeImg = templatePath + "/CNAS.png";
                    }

                    rtd.setCertImg(Pictures.ofLocal(certTypeImg).size(179, 100).create());
                    rtd.setOrganLogo(Pictures.ofLocal(organImgPath + "/" + dbBase.getLogoImageId()).size(200, 100).create());
                    genDataVo.setImagePath(organImgPath + "/" + dbBase.getWatermarkImageId());
                }
            }


            ReportMainData md = new ReportMainData();
            rtd.setMainData(md);
            ScheduleJob dbJob = jobMapper.selectOne(Wrappers.<ScheduleJob>lambdaQuery()
                    .select(ScheduleJob::getCheckGoal,
                            ScheduleJob::getConsignorLinker,
                            ScheduleJob::getConsignorLinkerPhone,
                            ScheduleJob::getConsignorName,
                            ScheduleJob::getCounty,
                            ScheduleJob::getProjectAddress)
                    .eq(ScheduleJob::getId, jobId));
            if (dbJob != null) {
                SysRegion sysRegion = sysRegionMapper.selectOne(Wrappers.<SysRegion>lambdaQuery()
                        .select(SysRegion::getMerName)
                        .eq(SysRegion::getRegionCode, dbJob.getCounty()));
                if (sysRegion != null) {
                    md.setAddress(sysRegion.getMerName().substring(3));
                }
                String address = StrUtil.isBlank(md.getAddress()) ? "" : md.getAddress();
                md.setCheckGoal(dbJob.getCheckGoal())
                        .setAddress(address + dbJob.getProjectAddress())
                        .setConsignorLinker(dbJob.getConsignorLinker())
                        .setConsignorLinkerPhone(dbJob.getConsignorLinkerPhone())
                        .setConsignorName(dbJob.getConsignorName())
                        .setCheckType(rtd.getCheckType());
                rtd.setConsignorName(dbJob.getConsignorName());
            }

            md.setMakeUserName(loginOutputVo.getEmpName());


            List<ReportSampleVo> sampleList = sampleItemMapper.findReportSampleInfoByJobId(jobId, organId);
            if (sampleList != null && !sampleList.isEmpty()) {
                //二级分类的因子名称
                Map<String, List<String>> secdClassFactorMap = new HashMap<>();
                //二级分类及对应的value
                Map<String, String> secdClassMap = new HashMap<>();
                //数据表
                //二级分类下 样品
                Map<String, List<String>> secdClassSampMap = new HashMap<>();

                //样品下的实验室因子值集合
                Map<String, Map<String, String>> sampIdValMap = new HashMap<>();
                //样品下的检测因子项下的子因子值集合
                Map<String, Map<Long, LinkedHashMap<String, String>>> sampIdSubValMap = new HashMap<>();
                //二级分类下的 因子名称和单位
                Map<String, LinkedHashMap<String, FactorParam>> sedClassFactorMap = new HashMap<>();
                //现场检测设备编号
                Map<String, List<ReportEquipVo>> sceneEquipVoMap = new HashMap<>();
                //实验室检测设备编号
                Map<String, List<ReportEquipVo>> laborEquipVoMap = new HashMap<>();

                //样品下的均值类型
                Map<String, String> meanTypeMap = new HashMap<>();
                genDataVo.setMeanTypeMap(meanTypeMap);
                genDataVo.setSecdClassLaborFactorMap(sedClassFactorMap);
                genDataVo.setSampIdLaborFactorValMap(sampIdValMap);
                genDataVo.setSampIdLaborSubFactorValMap(sampIdSubValMap);
                genDataVo.setSecdClassSampMap(secdClassSampMap);
                genDataVo.setSecdClassNameMap(secdClassMap);
                genDataVo.setSceneEquipVoMap(sceneEquipVoMap);
                genDataVo.setLaborEquipVoMap(laborEquipVoMap);
                sampleList.forEach(item -> {
                    String groupKey = item.getGroupKey();
                    String cgroupKey = groupKey.substring(0, groupKey.lastIndexOf("-"));
                    item.setGroupKey(cgroupKey);
                    item.setOriginGroupKey(groupKey);
                    String sampItemId = item.getSampItemId();
                    //二级类别
                    String secdClassId = item.getSecdClassId();
                    String secdClassName = dictUtils.getFactorClassMap().get(secdClassId);
                    secdClassMap.put(secdClassId, secdClassName);
                    List<String> sampleIds = secdClassSampMap.get(secdClassId);
                    if (sampleIds != null && !sampleIds.isEmpty()) {
                        if (!sampleIds.contains(sampItemId)) {
                            sampleIds.add(sampItemId);
                        }
                    } else {
                        sampleIds = new ArrayList<>();
                        sampleIds.add(sampItemId);
                        secdClassSampMap.put(secdClassId, sampleIds);
                    }
                });

                String collectDate = sampleList.get(0).getCollectDate();
                md.setCollectDate(collectDate);
                Map<String, ReportSampleVo> sampleVoMap = sampleList.stream().collect(Collectors.toMap(ReportSampleVo::getSampItemId, Function.identity()));
                genDataVo.setSampleVoMap(sampleVoMap);

                Set<String> checkEmpNames = new HashSet<>();
                StringBuilder checkDetail = new StringBuilder();

                List<String> sampleItemIds = sampleList.stream().map(item -> item.getSampItemId()).collect(Collectors.toList());
                //现场因子集合
                List<SampleFactorDataParam> factorDataDetailsBySampleNos = sampleItemFactorDataMapper.findFactorDataDetailsBySampleNos(sampleItemIds, organId);
                if (factorDataDetailsBySampleNos != null && !factorDataDetailsBySampleNos.isEmpty()) {
                    Map<String, List<SampleFactorDataParam>> sceneSampleFactorDataMap = factorDataDetailsBySampleNos.stream().collect(Collectors.groupingBy(SampleFactorDataParam::getSecdClassId));
                    genDataVo.setSceneSampleFactorDataMap(sceneSampleFactorDataMap);
                    sceneSampleFactorDataMap.forEach((fscdClassId, list) -> {
                        List<ReportEquipVo> equipVoList = new ArrayList<>();
                        list.stream().forEach(item -> {
                            String checkStandardId = item.getCheckStandardId();
                            FactorMethodInfoVo dbMethod = factorService.findFactorMethodById(checkStandardId);
                            buildCheckEquipment(item.getCheckEquipment(), dbMethod, equipVoList);
                        });
                        sceneEquipVoMap.put(fscdClassId, equipVoList);
                    });
                }

                //实验室因子
                List<ReportCheckDetailVo> checkDetails = checkFactorInfoMapper.findCheckDetailBySampleItemIds(sampleItemIds, organId);
                if (checkDetails != null && !checkDetails.isEmpty()) {
                    checkDetails.forEach(item -> {
                        ReportSampleVo reportSampleVo = sampleVoMap.get(item.getSampItemId());
                        if (reportSampleVo != null) {
                            item.setCollectDate(reportSampleVo.getCollectDate());
                            item.setCollectUserId(reportSampleVo.getCollectUserId());
                        }

                    });

                    //同系物检测因子项
                    Map<Long, List<CheckFactorSubset>> subFactorMap = null;
                    List<Long> checkFactorIds = checkDetails.stream().filter(item -> item.getFactorFlag().equals("1"))
                            .map(item -> item.getCheckFactorId()).collect(Collectors.toList());
                    if (checkFactorIds != null && !checkFactorIds.isEmpty()) {
                        List<CheckFactorSubset> checkFactorSubsets = checkFactorSubsetMapper.selectList(Wrappers.<CheckFactorSubset>lambdaQuery()
                                .select(CheckFactorSubset::getCheckStandardId,
                                        CheckFactorSubset::getCheckSubRes,
                                        CheckFactorSubset::getCheckFactorId)
                                .in(CheckFactorSubset::getCheckFactorId, checkFactorIds));
                        if (checkFactorSubsets != null) {
                            Map<Long, String> checkFactorSampleNoMap = checkDetails.stream().collect(Collectors.toMap(ReportCheckDetailVo::getCheckFactorId, ReportCheckDetailVo::getSampItemId));
                            BusiUtils.buidSubCheckFactorSubSet(sampIdSubValMap, checkFactorSubsets, checkFactorSampleNoMap);
                            subFactorMap = checkFactorSubsets.stream().collect(Collectors.groupingBy(CheckFactorSubset::getCheckFactorId));
                        }
                    }

                    //同系物因子套餐名称
                    Map<Long, String> checkFactorIdHomeFactorNameMap = checkDetails.stream().filter(item -> item.getFactorFlag().equals("1")).collect(Collectors.toMap(ReportCheckDetailVo::getCheckFactorId, ReportCheckDetailVo::getFactorName));
                    genDataVo.setCheckFactorIdHomeFactorNameMap(checkFactorIdHomeFactorNameMap);

                    Map<Long, List<CheckFactorSubset>> finalSubFactorMap = subFactorMap != null ? subFactorMap : new HashMap<>();
                    //实验室因子检测设备
                    checkDetails.stream().forEach(item -> {
                        Long checkFactorId = item.getCheckFactorId();
                        String secdClassId = item.getSecdClassId();
                        List<ReportEquipVo> reportEquipVos = laborEquipVoMap.get(secdClassId);
                        if (reportEquipVos == null) {
                            reportEquipVos = new ArrayList<>();
                            laborEquipVoMap.put(secdClassId, reportEquipVos);
                        }
                        String checkStandardId = item.getCheckStandardId();
                        String checkEquipment = item.getCheckEquipment();
                        String factorFlag = item.getFactorFlag();
                        if (factorFlag.equals("1")) {
                            List<CheckFactorSubset> subFactors = finalSubFactorMap.get(checkFactorId);
                            for (CheckFactorSubset subCheckFactor : subFactors) {
                                FactorMethodInfoVo dbMethod = factorService.findFactorMethodById(subCheckFactor.getCheckStandardId());
                                buildLaborCheckEquipment(checkEquipment, dbMethod, reportEquipVos);
                            }
                        } else {
                            FactorMethodInfoVo dbMethod = factorService.findFactorMethodById(checkStandardId);
                            buildLaborCheckEquipment(checkEquipment, dbMethod, reportEquipVos);
                        }
                    });

                    //实验室数据
                    checkDetails.stream().filter(item -> item.getFactorFlag().equals("0"))
                            .sorted(Comparator.comparing(ReportCheckDetailVo::getCollectDate)).forEach(item -> {
                        //样品ID
                        String sampItemId = item.getSampItemId();
                        //采集人员
                        String collectUserId = item.getCollectUserId();
                        String checkStandardId = item.getCheckStandardId();
                        //因子检测结果
                        String checkRes = item.getCheckRes();
                        //二级类别
                        String secdClassId = item.getSecdClassId();
//                        log.info("二级类别因子数据：二级类别:" + secdClassId + " 因子：" + checkStandardId + " 值：" + checkRes);
                        //  搜集因子项的检测数据
                        String checkResVal = null;
                        if (StrUtil.isNotBlank(checkRes)) {
                            CheckResParam checkResBean = JSON.parseObject(checkRes, CheckResParam.class);
                            checkResVal = BusiUtils.getFactorVal(checkResBean);
                        }

                        Map<String, String> sampleFactorValMap = sampIdValMap.get(sampItemId);
                        if (sampleFactorValMap != null) {
                            sampleFactorValMap.put(checkStandardId, checkResVal);
                        } else {
                            sampleFactorValMap = new HashMap<>();
                            sampleFactorValMap.put(checkStandardId, checkResVal);
                            sampIdValMap.put(sampItemId, sampleFactorValMap);
                        }


                        String dynamicParam = item.getDynamicParam();
                        if (StrUtil.isNotBlank(dynamicParam)) {
                            MeanTypeParam param = JSON.parseObject(dynamicParam, MeanTypeParam.class);
                            if (param != null) {
                                String meanType = meanTypeMap.get(sampItemId);
                                if (meanType == null) {
                                    meanTypeMap.put(sampItemId, param.getMeanName());
                                }
                            }
                        }

                        FactorMethodInfoVo factorMethodId = factorService.findFactorMethodById(checkStandardId);
                        if (factorMethodId != null) {
                            String factorName = item.getFactorName();
                            List<String> list = secdClassFactorMap.get(secdClassId);
                            if (list != null) {
                                list.add(factorName);
                            } else {
                                list = new ArrayList<>();
                                list.add(factorName);
                                secdClassFactorMap.put(secdClassId, list);
                            }


                            LinkedHashMap<String, FactorParam> factorParamMap = sedClassFactorMap.get(secdClassId);
                            if (factorParamMap != null) {
                                FactorParam factorParam = factorParamMap.get(checkStandardId);
                                if (factorParam == null) {
                                    factorParam = new FactorParam()
                                            .setCheckStandardId(checkStandardId)
                                            .setFactorName(factorName)
                                            .setDataEntryStep(factorMethodId.getDataEntryStep())
                                            .setUnitVal(factorMethodId.getDefaultUnitName());
                                    factorParamMap.put(checkStandardId, factorParam);
                                }
                            } else {
                                factorParamMap = new LinkedHashMap<>();
                                FactorParam factorParam = new FactorParam()
                                        .setCheckStandardId(checkStandardId)
                                        .setFactorName(factorName)
                                        .setUnitVal(factorMethodId.getDefaultUnitName());
                                factorParamMap.put(checkStandardId, factorParam);
                                sedClassFactorMap.put(secdClassId, factorParamMap);
                            }
                        }
                        SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                                .select(SysUser::getEmpName)
                                .eq(SysUser::getUserId, collectUserId));
                        if (sysUser != null) {
                            checkEmpNames.add(sysUser.getEmpName());
                        }
                    });
                }


                secdClassFactorMap.keySet().stream().collect(Collectors.toList()).stream().sorted().forEach(item -> {
                    List<String> factNameList = secdClassFactorMap.get(item);
                    String joinStr = factNameList.stream().collect(Collectors.toSet()).stream().collect(Collectors.joining("、"));
                    checkDetail.append(secdClassMap.get(item)).append(":").append(joinStr).append(" ");
                });

                String secdClassNames = secdClassMap.keySet().stream().collect(Collectors.toList())
                        .stream().sorted().map(item -> dictUtils.getFactorClassMap().get(item))
                        .collect(Collectors.joining("、"));
                md.setSecdClassNames(secdClassNames);
                md.setCheckDetails(checkDetail.toString().trim() + ";");
                String empNames = checkEmpNames.stream().collect(Collectors.joining("、"));
                md.setCheckEmpNames(empNames);

            }

            return genDataVo;
        }
        return null;
    }

    /**
     * TODO 构建设备数据（主因子和子因子）
     *
     * @param checkEquipment
     * @param dbMethod
     * @param reportEquipVos
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/5/17 5:05 下午
     */
    private void buildCheckEquipment(String checkEquipment, FactorMethodInfoVo dbMethod, List<ReportEquipVo> reportEquipVos) {
        if (dbMethod != null) {
            if (StrUtil.isNotBlank(checkEquipment)) {
                ReportEquipVo equipVo = new ReportEquipVo();
                equipVo.setFactorName(dbMethod.getFactorName())
                        .setCheckStandardId(dbMethod.getId())
                        .setAnalysisMethod(dbMethod.getStandardNo() + ":" + dbMethod.getStandardName())
                        .setDetectionLimit(dbMethod.getDetectionLimit());
                String[] split = checkEquipment.split("\\^_\\^");
                if (split.length > 2) {
                    equipVo.setEquipModeAndCode(split[1] + split[2]);
                }
                reportEquipVos.add(equipVo);

            }
        }
    }

    /**
     * TODO 构建设备数据（主因子和子因子）
     *
     * @param checkEquipment
     * @param dbMethod
     * @param reportEquipVos
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/5/17 5:05 下午
     */
    private void buildLaborCheckEquipment(String checkEquipment, FactorMethodInfoVo dbMethod, List<ReportEquipVo> reportEquipVos) {
        if (dbMethod != null) {
            if (StrUtil.isNotBlank(checkEquipment)) {
                EquipmentTreeVo treeVo = JSON.parseObject(checkEquipment, EquipmentTreeVo.class);
                ReportEquipVo equipVo = new ReportEquipVo();
                equipVo.setFactorName(dbMethod.getFactorName())
                        .setCheckStandardId(dbMethod.getId())
                        .setAnalysisMethod(dbMethod.getStandardNo() + ":" + dbMethod.getStandardName())
                        .setDetectionLimit(dbMethod.getDetectionLimit());
                equipVo.setEquipModeAndCode(treeVo.getEquipmentModel() + "/" + treeVo.getEquipmentNumber());
                reportEquipVos.add(equipVo);

            }
        }
    }


    @Transactional
    @Override
    public Result<Object> genReport(String reportCode) {
        ReportGenDataVo genDataVo = this.findReportDataByReportCode(reportCode);
//        log.info("genDataVo->" + genDataVo);

        try {
            WordUtils.genReportByCondition(genDataVo, templatePath, reportGenPath);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.busiError("生成报告异常");
        }

        FileConvertUtil.wordBytes2PdfFile(FileUtil.readBytes(reportGenPath + "/" + reportCode + ".docx"), reportGenPath + "/" + reportCode + ".pdf", genDataVo.getImagePath());
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        reportInfoMapper.update(null, Wrappers.<ReportInfo>lambdaUpdate()
                .set(ReportInfo::getReportMakeTime, LocalDateTime.now())
                .set(ReportInfo::getReportMakeUserId, loginOutputVo.getUserId())
                .set(ReportInfo::getReportFileId, reportCode + ".docx")
                .set(ReportInfo::getReportStatus, "2")
                .eq(ReportInfo::getId, genDataVo.getReportId()));
        return ResultUtil.success("生成成功！");
    }

    @Transactional
    @Override
    public Result<Object> uploadReportFileById(MultipartFile file, Long reportId) {
        ReportInfo dbReport = reportInfoMapper.selectOne(Wrappers.<ReportInfo>lambdaQuery()
                .select(ReportInfo::getReportStatus,
                        ReportInfo::getReportCode)
                .eq(ReportInfo::getId, reportId));

        if (dbReport == null) {
            return ResultUtil.busiError("报告ID关联的报告记录，库中不存在！");
        }

//        if (!dbReport.getReportStatus().equals("2")) {
//            return ResultUtil.busiError("只有待制作可以完善！");
//        }

        String reportCode = dbReport.getReportCode();
        String fileName = FileUtil.fileReportUploadByFileName(file, reportCode, reportGenPath);
        if (fileName != null) {
            FileConvertUtil.wordBytes2PdfFile(FileUtil.readBytes(reportGenPath + "/" + fileName), reportGenPath + "/" + reportCode + ".pdf");

            reportInfoMapper.update(null, Wrappers.<ReportInfo>lambdaUpdate()
                    .set(ReportInfo::getReportStatus, "3")
                    .set(ReportInfo::getReportFileId, fileName)
                    .set(ReportInfo::getUpdateTime, LocalDateTime.now())
                    .eq(ReportInfo::getId, reportId));
            return ResultUtil.success("上传成功！");

        }

        return ResultUtil.error("上传失败!");

    }

    @Override
    public IPage<ReportCheckVo> findReportCheckByPage(IPage<ReportCheckVo> page, String jobId, String projectName) {
        jobId = StrUtil.trimToNull(jobId);
        projectName = StrUtil.trimToNull(projectName);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        reportCheckMapper.findReportCheckByPage(page, loginOutputVo.getOrganId(), jobId, projectName);
        page.getRecords().stream().forEach(item -> {
            String reportMakeUserId = item.getReportMakeUserId();
            if (StrUtil.isNotBlank(reportMakeUserId)) {
                SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                        .select(SysUser::getEmpName)
                        .eq(SysUser::getUserId, reportMakeUserId));
                if (sysUser != null) {
                    item.setReportMakeUser(sysUser.getEmpName());
                }
            }
        });
        return page;
    }

    @Override
    public IPage<ReportSignVo> findReportSignByPage(IPage<ReportSignVo> page, String jobId, String projectName) {
        jobId = StrUtil.trimToNull(jobId);
        projectName = StrUtil.trimToNull(projectName);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        reportSignMapper.findReportSignByPage(page, loginOutputVo.getOrganId(), jobId, projectName);
        page.getRecords().stream().forEach(item -> {
            String reportMakeUserId = item.getReportMakeUserId();
            if (StrUtil.isNotBlank(reportMakeUserId)) {
                SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                        .select(SysUser::getEmpName)
                        .eq(SysUser::getUserId, reportMakeUserId));
                if (sysUser != null) {
                    item.setReportMakeUser(sysUser.getEmpName());
                }
            }
        });
        return page;
    }


}