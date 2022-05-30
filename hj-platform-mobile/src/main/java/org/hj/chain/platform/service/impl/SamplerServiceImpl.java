package org.hj.chain.platform.service.impl;

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
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.Constants;
import org.hj.chain.platform.common.CustomException;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.mapper.*;
import org.hj.chain.platform.model.*;
import org.hj.chain.platform.service.*;
import org.hj.chain.platform.tdo.MobileSampItemDetailTdo;
import org.hj.chain.platform.tdo.MobileSampItemReviewTdo;
import org.hj.chain.platform.tdo.MobileSampleAuditTdo;
import org.hj.chain.platform.tdo.MobileSignTdo;
import org.hj.chain.platform.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-05
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-05
 */
@Service
@Slf4j
public class SamplerServiceImpl implements ISamplerService {

    @Autowired
    private SampleTaskMapper sampleTaskMapper;
    @Autowired
    private SampleItemMapper sampleItemMapper;
    @Autowired
    private SampleItemDataMapper sampleItemDataMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISampleStoreApplyService sampleStoreApplyService;
    @Autowired
    private SampleItemAuditRecordMapper sampleItemAuditRecordMapper;
    @Autowired
    private SamplingBasisMpper samplingBasisMpper;
    @Autowired
    private SampleStoreApplyMapper sampleStoreApplyMapper;
    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private SampleItemFactorDataMapper sampleItemFactorDataMapper;
    @Autowired
    private ISampleItemFactorDataService sampleItemFactorDataService;
    @Autowired
    private DynamicParamConfMapper dynamicParamConfMapper;
    @Autowired
    private ScheduleJobMapper jobMapper;

    @Override
    public IPage<MobileSampleTaskVo> getSampleTasksForTeamLeader(PageVo pageVo, String userId) {
        Page<MobileSampleTaskVo> page = PageUtil.initMpPage(pageVo);
        sampleTaskMapper.getSampleTasksByTeamLeader(page, userId);
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            for(MobileSampleTaskVo vo : page.getRecords()) {
                List<SampleItem> itemInfos = sampleItemMapper.selectList(Wrappers.<SampleItem>lambdaQuery()
                        .select(SampleItem::getId, SampleItem::getSampleStatus, SampleItem::getSignImageUrl)
                        .eq(SampleItem::getSampleTaskId, vo.getTaskId()));
                if(itemInfos != null && !itemInfos.isEmpty()) {
                    vo.setSampleCount(itemInfos.size());
                    String signImageUrl = null;
                    for(SampleItem itemInfo : itemInfos) {
                        if(StrUtil.isNotBlank(itemInfo.getSignImageUrl())) {
                            signImageUrl = itemInfo.getSignImageUrl();
                            break;
                        }
                    }
                    vo.setSingImageUrl(signImageUrl);
                }
            }
        }
        return page;
    }

    @Override
    public Result<SampleTaskItemVo> getSampleItemsBySampleTaskId(Long taskId, String userId) {
        SampleTaskItemVo vo = new SampleTaskItemVo();
        SampleTask task = sampleTaskMapper.selectById(taskId);
        if(task != null) {
            ScheduleJob job = jobMapper.selectById(task.getJobId());
            vo.setJobId(job.getId()).setInspectionName(job.getInspectionName()).setConsignorName(job.getConsignorName())
                    .setProjectName(job.getProjectName());
            vo.setDispatchRemark(job.getJobRemark());
            vo.setInspectionLinker(job.getInspectionLinker());
        }else{
            return ResultUtil.busiError("采样任务信息不存在！");
        }
        List<MobileSampleItemVo> vos = sampleItemMapper.getSampleItemsBySampleTaskIdAndUserId(taskId, userId);
        vo.setSampleCount(vos.size());
        for(MobileSampleItemVo item : vos) {
            List<SampleFactorDataVo> factorDataVos = sampleItemFactorDataMapper.getSampleFactorDataBySampItemId(item.getSampItemId());
            String factorName = factorDataVos.stream().map(SampleFactorDataVo::getFactorName).collect(Collectors.joining("/"));
            item.setFactorName(factorName);
            if(vo.getSingImageUrl() != null && item.getSignImageUrl() != null) {
                vo.setSingImageUrl(item.getSignImageUrl());
            }
        }
        vo.setSampleItemVos(vos);
        return ResultUtil.data(vo);
    }

    @Override
    public Result<SampleItemVo> getSampleDetailBySampleNo(String sampleNo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        SampleItem sampleItem = sampleItemMapper.selectOne(Wrappers.<SampleItem>lambdaQuery()
                .eq(SampleItem::getOrganId, organId)
                .eq(SampleItem::getSampleNo, sampleNo));
        SampleItemData sampleItemData = sampleItemDataMapper.selectOne(Wrappers.<SampleItemData>lambdaQuery()
                .eq(SampleItemData::getSampleItemId, sampleItem.getId()));
        MobileSampleItemDetailVo vo = sampleItemDataMapper.getSampleDetailBySampleItemId(sampleItem.getId());
        SampleItemVo sampleItemVo = copyProperties(vo);
        if(sampleItemData != null) {
            List<String> imageList = new ArrayList<>();
            if(StrUtil.isNotBlank(sampleItemData.getCollectIamge1Id())) {
                imageList.add(sampleItemData.getCollectIamge1Id());
            }
            if(StrUtil.isNotBlank(sampleItemData.getCollectIamge2Id())) {
                imageList.add(sampleItemData.getCollectIamge2Id());
            }
            if(StrUtil.isNotBlank(sampleItemData.getCollectIamge3Id())) {
                imageList.add(sampleItemData.getCollectIamge3Id());
            }
            if(StrUtil.isNotBlank(sampleItemData.getCollectIamge4Id())) {
                imageList.add(sampleItemData.getCollectIamge4Id());
            }
            if(StrUtil.isNotBlank(vo.getCollectLeaderId())) {
                SysUser user = sysUserService.getById(vo.getCollectLeaderId());
                sampleItemVo.setCollectLeader(user.getEmpName());
            }
            if(StrUtil.isNotBlank(vo.getCollectUserId())) {
                SysUser user = sysUserService.getById(vo.getCollectUserId());
                sampleItemVo.getSampleDataVo().setCollectUser(user.getEmpName());
            }
            if(StrUtil.isNotBlank(vo.getReviewUserId())) {
                SysUser user = sysUserService.getById(vo.getReviewUserId());
                sampleItemVo.getSampleDataVo().setReviewUser(user.getEmpName());
            }
            sampleItemVo.getSampleDataVo().setImageList(imageList);
        }
        String secdClassId = vo.getSecdClassId();
        if(StrUtil.isBlank(vo.getSampleData())) {
            //拼接样品二级类别动态属性
            JSONArray sampleData = assembleDynamicParams(vo.getSecdClassId());
            sampleItemVo.getSampleDataVo().setSampleData(sampleData);
        }else{
            JSONArray sampleData = JSON.parseArray(vo.getSampleData());
            //补充下拉框和选择框数据源
            for(int i = 0; i < sampleData.size(); i++) {
                JSONObject object = sampleData.getJSONObject(i);
                JSONArray params = object.getJSONArray("params");
                for(int j = 0; j < params.size(); j++){
                    JSONObject param = params.getJSONObject(j);
                    String labelType = param.getString("labelType");
                    if("0".equals(labelType)) {
                        DynamicParamConf dpc = dynamicParamConfMapper.selectOne(Wrappers.<DynamicParamConf>lambdaQuery()
                                .eq(DynamicParamConf::getSecdClassId, secdClassId)
                                .eq(DynamicParamConf::getPKey, param.getString("key")));
                        //级联下拉框
                        if("1".equals(dpc.getLabelValue())) {
                            //采样设备
                            param.put("selectValue", laboratoryEquipmentTree(Constants.EQUIPMENT_FIRST_TYPE_SAMPLE));
                        }else if("2".equals(dpc.getLabelValue())) {
                            //校准设备
                            param.put("selectValue", laboratoryEquipmentTree(Constants.EQUIPMENT_FIRST_TYPE_CALIBRATION));
                        }else if("3".equals(dpc.getLabelValue())) {
                            //现场检测设备
                            param.put("selectValue", laboratoryEquipmentTree(Constants.EQUIPMENT_FIRST_TYPE_CHECK));
                        }
                    }else if("2".equals(labelType)) {
                        //选择框
                        DynamicParamConf dpc = dynamicParamConfMapper.selectOne(Wrappers.<DynamicParamConf>lambdaQuery()
                                .eq(DynamicParamConf::getSecdClassId, secdClassId)
                                .eq(DynamicParamConf::getPKey, param.getString("key")));
                        param.put("radioValue", Arrays.asList(dpc.getRadioValue().split(",")));
                    }else if("7".equals(labelType)) {
                        //单选下拉框
                        DynamicParamConf dpc = dynamicParamConfMapper.selectOne(Wrappers.<DynamicParamConf>lambdaQuery()
                                .eq(DynamicParamConf::getSecdClassId, secdClassId)
                                .eq(DynamicParamConf::getPKey, param.getString("key")));
                        if("0".equals(dpc.getLabelValue())) {
                            //采样依据
                            List<SamplingBasis> list = samplingBasisMpper.selectList(Wrappers.<SamplingBasis>lambdaQuery()
                                    .eq(SamplingBasis::getClassId, secdClassId.substring(0,3)));
                            if(list != null && !list.isEmpty()) {
                                List<String > sampBasis = list.stream().map(d -> d.getBasis()).collect(Collectors.toList());
                                param.put("selectValue", sampBasis);
                            }
                        }
                    }
                }
            }
            sampleItemVo.getSampleDataVo().setSampleData(sampleData);
        }
        List<SampleFactorDataVo> factorDataVos = sampleItemFactorDataMapper.getSampleFactorDataBySampItemId(vo.getSampItemId());
        String factorName = factorDataVos.stream().map(SampleFactorDataVo::getFactorName).collect(Collectors.joining("/"));
        sampleItemVo.setFactorName(factorName);
        sampleItemVo.setFactorDataVos(factorDataVos);
        return ResultUtil.data(sampleItemVo);
    }

    private SampleItemVo copyProperties(MobileSampleItemDetailVo vo) {
        SampleItemVo sampleItemVo = new SampleItemVo();
        sampleItemVo.setSampItemId(vo.getSampItemId())
                .setSampleNo(vo.getSampleNo())
                .setAuditReason(vo.getAuditReason())
                .setFactorPoint(vo.getFactorPoint())
                .setCollectLeaderId(vo.getCollectLeaderId())
                .setSampleStatus(vo.getSampleStatus())
                .setSecdClassId(vo.getSecdClassId())
                .setFrequency(vo.getFrequency())
                .setSecdClassName(vo.getSecdClassName());
        SampleJobVo sampleJobVo = new SampleJobVo();
        BeanUtils.copyProperties(vo, sampleJobVo);
        sampleItemVo.setSampleJobVo(sampleJobVo);
        SampleDataVo sampleDataVo = new SampleDataVo();
        sampleDataVo.setCollectDate(vo.getCollectDate())
                .setCollectRemark(vo.getCollectRemark())
                .setCollectUserId(vo.getCollectUserId())
                .setCollectLocation(vo.getCollectLocation())
                .setReviewUserId(vo.getReviewUserId())
                .setSpecialNote(vo.getSpecialNote())
                .setCollectDate(vo.getCollectDate())
                .setPollutantInfo(vo.getPollutantInfo());
        if(vo.getFrequency() > 1) {
            SampleItem sampleItem = sampleItemMapper.selectOne(Wrappers.<SampleItem>lambdaQuery()
                    .eq(SampleItem::getGroupKey, vo.getGroupKey()).eq(SampleItem::getFrequency, 1));
            SampleItemData sid = sampleItemDataMapper.selectOne(Wrappers.<SampleItemData>lambdaQuery()
                    .eq(SampleItemData::getSampleItemId, sampleItem.getId()));
            sampleDataVo.setCollectDate(sid.getCollectDate());
        }
        if(Constants.SPECIAL_NOTE_CLASS.contains(vo.getSecdClassId())) {
            sampleDataVo.setSpecialNoteFlag(true);
        }else{
            sampleDataVo.setSpecialNoteFlag(false);
        }
        sampleItemVo.setSampleDataVo(sampleDataVo);
        return sampleItemVo;
    }

    private JSONArray assembleDynamicParams(String secdClassId) {
        List<DynamicParamConf> paramConfs = dynamicParamConfMapper.selectList(Wrappers.<DynamicParamConf>lambdaQuery()
                .eq(DynamicParamConf::getSecdClassId, secdClassId)
                .eq(DynamicParamConf::getIsDelete, "0")
                .orderByAsc(DynamicParamConf::getGroupKey, DynamicParamConf::getSort));
        if(paramConfs == null || paramConfs.isEmpty()) {
            return null;
        }
        Map<String, List<DynamicParamConf>> map = paramConfs.stream().collect(Collectors.groupingBy(DynamicParamConf::getGroupKey));
        JSONArray sampleData = new JSONArray();
        map.forEach((k, v) -> {
            JSONObject group = new JSONObject(true);
            group.put("groupKey", k);
            group.put("groupName", v.get(0).getGroupName());
            JSONArray params = new JSONArray();
            v.forEach(item -> {
                JSONObject object = new JSONObject(true);
                object.put("key", item.getPKey());
                object.put("name", item.getPName());
                object.put("required", item.getRequired());
                object.put("numericType", item.getNumericType());
                object.put("labelType", item.getLabelType());
                object.put("promptValue", item.getPromptValue());
                object.put("value", null);
                if("0".equals(item.getLabelType())) {
                    //级联下拉框
                    if("1".equals(item.getLabelValue())) {
                        //采样设备
                        object.put("selectValue", laboratoryEquipmentTree(Constants.EQUIPMENT_FIRST_TYPE_SAMPLE));
                    }else if("2".equals(item.getLabelValue())) {
                        //校准设备
                        object.put("selectValue", laboratoryEquipmentTree(Constants.EQUIPMENT_FIRST_TYPE_CALIBRATION));
                    }else if("3".equals(item.getLabelValue())) {
                        //现场检测设备
                        object.put("selectValue", laboratoryEquipmentTree(Constants.EQUIPMENT_FIRST_TYPE_CHECK));
                    }
                }else if("2".equals(item.getLabelType())) {
                    //选择框
                    object.put("radioValue", Arrays.asList(item.getRadioValue().split(",")));
                }else if("7".equals(item.getLabelType())) {
                    //单选下拉框
                    if("0".equals(item.getLabelValue())) {
                        //采样依据
                        List<SamplingBasis> list = samplingBasisMpper.selectList(Wrappers.<SamplingBasis>lambdaQuery()
                                .eq(SamplingBasis::getClassId, secdClassId.substring(0,3)));
                        if(list != null && !list.isEmpty()) {
                            List<String > sampBasis = list.stream().map(d -> d.getBasis()).collect(Collectors.toList());
                            object.put("selectValue", sampBasis);
                        }
                    }
                }
                params.add(object);
            });
            group.put("params", params);
            sampleData.add(group);
        });
        return sampleData;
    }

    public JSONArray laboratoryEquipmentTree(Long equipmentType) {
        JSONArray tree = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<EquipmentInfo> list = equipmentInfoMapper.selectList(Wrappers.<EquipmentInfo>lambdaQuery()
                .eq(EquipmentInfo::getOrganId, loginOutputVo.getOrganId())
                .eq(EquipmentInfo::getEquipmentFirstType, equipmentType)
                .eq(EquipmentInfo::getIsDelete, "0")
                .orderByAsc(EquipmentInfo::getEquipmentName, EquipmentInfo::getEquipmentModel));
        if(list != null && !list.isEmpty()) {
            Map<String, Map<String, List<EquipmentInfo>>> map = list.stream()
                    .collect(Collectors.groupingBy(EquipmentInfo::getEquipmentName, Collectors.groupingBy(EquipmentInfo::getEquipmentModel)));
            Map<String, DictParam> dictParamMap = sysDictService.findDictMap();
            map.forEach((k, v) -> {
                JSONObject node = new JSONObject(true);
                node.put("name", k);
                JSONArray children = new JSONArray();
                v.forEach((k_1, v_1) -> {
                    JSONObject node_1 = new JSONObject(true);
                    node_1.put("name", k_1);
                    JSONArray children_1 = new JSONArray();
                    v_1.forEach(item -> {
                        EquipmentTreeVo treeVo = new EquipmentTreeVo();
                        BeanUtils.copyProperties(item, treeVo);
                        treeVo.setEquipmentFirstTypeVal(dictParamMap.get(String.valueOf(item.getEquipmentFirstType())).getDictVal());
                        if(item.getEquipmentSecondType() != null) {
                            treeVo.setEquipmentSecondTypeVal(dictParamMap.get(String.valueOf(item.getEquipmentSecondType())).getDictVal());
                        }
                        children_1.add(treeVo);
                    });
                    node_1.put("children", children_1);
                    children.add(node_1);
                });
                node.put("children", children);
                tree.add(node);
            });
        }
        return tree;
    }

    @Override
    public Result<List<EquipmentInfoVo>> listSampEquipments(String sampItemId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<EquipmentInfoVo> vos = new ArrayList<>();
        Long firstType = Constants.EQUIPMENT_FIRST_TYPE_SAMPLE;
        List<Long> subKeys = sysDictService.findDictRel().get(String.valueOf(Constants.EQUIPMENT_SECOND_TYPE));
        Map<String, DictParam> dictParamMap = sysDictService.findDictMap();
        List<DictParam> dictParams = subKeys.stream().map(k -> dictParamMap.get(String.valueOf(k))).collect(Collectors.toList());
        List<Long> secondType = new ArrayList<>();
        if(StrUtil.isNotBlank(sampItemId)) {
            SampleItem sii = sampleItemMapper.selectById(sampItemId);
            String key = sii.getSecdClassId().substring(0,3);
            if(dictParams != null && !dictParams.isEmpty()) {
                dictParams.forEach(d -> {
                    if(d.getDictDesc().contains(key)) {
                        secondType.add(d.getDictKey());
                    }
                });
            }
        }
        List<EquipmentInfo> list = equipmentInfoMapper.selectList(Wrappers.<EquipmentInfo>lambdaQuery()
                .eq(EquipmentInfo::getOrganId, loginOutputVo.getOrganId())
                .eq(EquipmentInfo::getEquipmentFirstType, firstType)
                .in(!secondType.isEmpty(), EquipmentInfo::getEquipmentSecondType, secondType)
                .eq(EquipmentInfo::getIsDelete, "0"));
        if(list != null && !list.isEmpty()) {
            vos = list.stream().map(item -> {
                EquipmentInfoVo vo = new EquipmentInfoVo();
                vo.setEquipmentName(item.getEquipmentName())
                        .setEquipmentNumber(item.getEquipmentNumber())
                        .setEquipmentModel(item.getEquipmentModel())
                        .setEquipmentFirstTypeVal(dictParamMap.get(String.valueOf(firstType)).getDictDesc())
                        .setEquipmentSecondTypeVal(dictParamMap.get(String.valueOf(item.getEquipmentSecondType())).getDictDesc());
                return vo;
            }).collect(Collectors.toList());
        }
        return ResultUtil.data(vos);
    }

    @Override
    public Result<List<EquipmentInfoVo>> listCheckEquipments(String sampItemId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<EquipmentInfoVo> vos = new ArrayList<>();
        Long firstType = Constants.EQUIPMENT_FIRST_TYPE_CHECK;
        List<Long> subKeys = sysDictService.findDictRel().get(String.valueOf(Constants.EQUIPMENT_SECOND_TYPE));
        Map<String, DictParam> dictParamMap = sysDictService.findDictMap();
        List<DictParam> dictParams = subKeys.stream().map(k -> dictParamMap.get(String.valueOf(k))).collect(Collectors.toList());
        List<Long> secondType = new ArrayList<>();
        if(StrUtil.isNotBlank(sampItemId)) {
            SampleItem sii = sampleItemMapper.selectById(sampItemId);
            String key = sii.getSecdClassId().substring(0,3);
            if(dictParams != null && !dictParams.isEmpty()) {
                dictParams.forEach(d -> {
                    if(d.getDictDesc().contains(key)) {
                        secondType.add(d.getDictKey());
                    }
                });
            }
        }
        List<EquipmentInfo> list = equipmentInfoMapper.selectList(Wrappers.<EquipmentInfo>lambdaQuery()
                .eq(EquipmentInfo::getOrganId, loginOutputVo.getOrganId())
                .eq(EquipmentInfo::getEquipmentFirstType, firstType)
                .in(!secondType.isEmpty(), EquipmentInfo::getEquipmentSecondType, secondType)
                .eq(EquipmentInfo::getIsDelete, "0"));
        if(list != null && !list.isEmpty()) {
            vos = list.stream().map(item -> {
                EquipmentInfoVo vo = new EquipmentInfoVo();
                vo.setEquipmentName(item.getEquipmentName())
                        .setEquipmentModel(item.getEquipmentModel())
                        .setEquipmentNumber(item.getEquipmentNumber())
                        .setEquipmentFirstTypeVal(dictParamMap.get(String.valueOf(firstType)).getDictDesc())
                        .setEquipmentSecondTypeVal(dictParamMap.get(String.valueOf(item.getEquipmentSecondType())).getDictDesc());
                return vo;
            }).collect(Collectors.toList());
        }
        return ResultUtil.data(vos);
    }

    @Override
    public Result<List<EquipmentInfoVo>> listCalibrationEquipments(String sampItemId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        Long supKey = Constants.EQUIPMENT_FIRST_TYPE_CALIBRATION;
        List<EquipmentInfo> list = equipmentInfoMapper.selectList(Wrappers.<EquipmentInfo>lambdaQuery()
                .eq(EquipmentInfo::getOrganId, loginOutputVo.getOrganId())
                .eq(EquipmentInfo::getEquipmentFirstType, supKey)
                .eq(EquipmentInfo::getIsDelete, "0"));
        Map<String, DictParam> dictParamMap = sysDictService.findDictMap();
        List<EquipmentInfoVo> vos = new ArrayList<>();
        if(list != null && !list.isEmpty()) {
            vos = list.stream().map(item -> {
                EquipmentInfoVo vo = new EquipmentInfoVo();
                vo.setEquipmentName(item.getEquipmentName())
                        .setEquipmentModel(item.getEquipmentModel())
                        .setEquipmentNumber(item.getEquipmentNumber())
                        .setEquipmentFirstTypeVal(dictParamMap.get(String.valueOf(supKey)).getDictVal());
                if(item.getEquipmentSecondType() != null) {
                       vo.setEquipmentSecondTypeVal(dictParamMap.get(String.valueOf(item.getEquipmentSecondType())).getDictVal());

                }
                return vo;
            }).collect(Collectors.toList());
        }
        return ResultUtil.data(vos);
    }

    @Override
    public Result<List<UserParamVo>> listReviewUsers() {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        String appRole = "2";
        List<UserParamVo> userParamVos = sysUserService.selectUsersByAppRoleAndOranId(appRole, organId);
        if(userParamVos != null) {
            userParamVos = userParamVos.stream().filter(item -> !loginOutputVo.getUserId().equals(item.getUserId())).collect(Collectors.toList());
        }
        return ResultUtil.data(userParamVos);
    }

    @Override
    public Result<Object> batchReviewSampleItem(String sampleItemId) {
        List<Long> sampleItemIds = Arrays.stream(sampleItemId.split(",")).map(v -> Long.parseLong(v)).collect(Collectors.toList());
        LocalDateTime now = LocalDateTime.now();
        String userId = (String) StpUtil.getLoginId();
        String auditFlag = "1";
        for(Long sampItemId : sampleItemIds) {
            SampleItem itemInfo = sampleItemMapper.selectById(sampItemId);
            if(itemInfo == null) {
                throw new CustomException("样品信息不存在！");
            }
            int cnt = sampleItemMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                    .set(SampleItem::getSampleStatus, "2")
                    .set(SampleItem::getUpdateTime, now)
                    .set(SampleItem::getAuditTime, now)
                    .eq(SampleItem::getId, sampItemId));
            if(cnt < 0) {
                throw new CustomException(String.format("样品 %s 复核操作失败！", itemInfo.getSampleNo()));
            }
            //保存样品复核记录
            SampleItemAuditRecord siar = new SampleItemAuditRecord();
            siar.setAuditStatus(auditFlag)
                    .setAuditTime(now).setSampleItemId(sampItemId).setAuditUserId(userId)
                    .setAuditType("0");
            sampleItemAuditRecordMapper.insert(siar);
        }
        return ResultUtil.success("样品一键复核通过成功！");
    }

    @Override
    public Result<JSONArray> checkEquimentTree() {
        JSONArray array = laboratoryEquipmentTree(Constants.EQUIPMENT_FIRST_TYPE_CHECK);
        return ResultUtil.data(array);
    }

    @Override
    public Result<JSONArray> calibrationEquipmentTree() {
        JSONArray array = laboratoryEquipmentTree(Constants.EQUIPMENT_FIRST_TYPE_CALIBRATION);
        return ResultUtil.data(array);
    }

    @Override
    public Result<Object> checkSampleNo(String sampleNo) {
        if(StrUtil.isBlank(sampleNo)) {
            return ResultUtil.validateError("样品编号不能为空！");
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        SampleItem sampleItem = sampleItemMapper.selectOne(Wrappers.<SampleItem>lambdaQuery()
                .eq(SampleItem::getOrganId, organId)
                .eq(SampleItem::getSampleNo, sampleNo));
        if(sampleItem == null) {
            return ResultUtil.busiError("无效的样品编号，请输入正确的样品编号！");
        }
        SampleItemData sampleItemData = sampleItemDataMapper.selectOne(Wrappers.<SampleItemData>lambdaQuery()
                .eq(SampleItemData::getSampleItemId, sampleItem.getId()));
        if(sampleItemData == null) {
            if(!sampleItem.getDay().equals(1) || !sampleItem.getFrequency().equals(1)) {
                String sampNo = sampleNo.substring(0, sampleNo.length() - 3)
                        +
                        String.format("%03d", Integer.valueOf(sampleNo.substring(sampleNo.length() - 3)) - 1);
                SampleItem sampleItem_1 = sampleItemMapper.selectOne(Wrappers.<SampleItem>lambdaQuery()
                        .eq(SampleItem::getOrganId, organId)
                        .eq(SampleItem::getSampleNo, sampNo));
                sampleItemData = sampleItemDataMapper.selectOne(Wrappers.<SampleItemData>lambdaQuery()
                        .eq(SampleItemData::getSampleItemId, sampleItem_1.getId()));
                if(sampleItemData == null) {
                    return ResultUtil.busiError(String.format("前置样品%s尚未采集！", sampNo));
                }
            }
        }else{
            if(!sampleItemData.getCollectUserId().equals(loginOutputVo.getUserId())) {
                return ResultUtil.busiError(String.format("样品%s已被认领！", sampleItem.getSampleNo()));
            }
        }
        JSONArray sampleData = assembleDynamicParams(sampleItem.getSecdClassId());
        if(sampleData == null) {
            return ResultUtil.busiError("未配置二级类别动态属性！");
        }
        //默认样品不可编辑
        String isEdit = "0";
        List<String> sampleStatus = Arrays.asList("0","1","5");
        if(sampleStatus.contains(sampleItem.getSampleStatus())) {
            isEdit = "1";
        }
        return ResultUtil.data(isEdit, "校验通过！");
    }

    @Override
    public Result<Object> reviewSampleItem(MobileSampItemReviewTdo reviewTdo) {
        LocalDateTime now = LocalDateTime.now();
        Long sampItemId = reviewTdo.getSampItemId();
        String auditFlag = reviewTdo.getAuditFlag();
        String auditReason = reviewTdo.getAuditReason();
        String userId = (String) StpUtil.getLoginId();
        SampleItem itemInfo = sampleItemMapper.selectById(sampItemId);
        if(itemInfo == null) {
            throw new CustomException(String.format("样品 %s 信息不存在！", sampItemId));
        }
        int cnt = 0;
        if(auditFlag.equals("1")) {
            //复核通过，流传采样组长确认
            cnt = sampleItemMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                    .set(SampleItem::getSampleStatus, "2")
                    .set(SampleItem::getUpdateTime, now)
                    .set(SampleItem::getAuditTime, now)
                    .eq(SampleItem::getId, sampItemId));
        }else if(auditFlag.equals("2")){
            //复核不通过，流转重新编辑
            cnt = sampleItemMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                    .set(SampleItem::getSampleStatus, "1")
                    .set(SampleItem::getUpdateTime, now)
                    .set(SampleItem::getAuditReason, auditReason)
                    .set(SampleItem::getAuditTime, now)
                    .eq(SampleItem::getId, sampItemId));
        }
        if(cnt < 0) {
            throw new CustomException(String.format("样品 %s 复核操作失败！", sampItemId));
        }
        //保存样品复核记录
        SampleItemAuditRecord siar = new SampleItemAuditRecord();
        siar.setAuditReason(auditReason).setAuditStatus(auditFlag)
                .setAuditTime(now).setSampleItemId(sampItemId).setAuditUserId(userId)
                .setAuditType("0");
        sampleItemAuditRecordMapper.insert(siar);
        return ResultUtil.success("复核操作成功！");
    }

    @Override
    public Result<List<SampleAuditRecordVo>> getSampleAuditRecordBySampItemId(Long sampItemId) {
        List<SampleAuditRecordVo> vos = new ArrayList<>();
        List<SampleItemAuditRecord> records = sampleItemAuditRecordMapper.selectList(Wrappers.<SampleItemAuditRecord>lambdaQuery()
                .eq(SampleItemAuditRecord::getSampleItemId, sampItemId));
        if(records != null && !records.isEmpty()) {
            vos = records.stream().map(item -> {
                SampleAuditRecordVo vo = new SampleAuditRecordVo();
                BeanUtils.copyProperties(item, vo);
                SysUser sysUser = sysUserService.getById(item.getAuditUserId());
                vo.setAuditUser(sysUser.getEmpName());
                return vo;
            }).collect(Collectors.toList());
        }
        return ResultUtil.data(vos);
    }

    @Override
    public Result<IPage<SampleTaskVo>> getSampleTasksByCondition(PageVo pageVo, String sampleStatus) {
        String userId = (String) StpUtil.getLoginId();
        Page<SampleTaskVo> page = PageUtil.initMpPage(pageVo);
        sampleTaskMapper.getSampleTasksByCondition(page, userId, sampleStatus);
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            page.getRecords().forEach(p -> {
                List<SampleItem> itemInfos = sampleItemMapper.selectList(Wrappers.<SampleItem>lambdaQuery()
                        .select(SampleItem::getId, SampleItem::getSampleStatus, SampleItem::getSignImageUrl, SampleItem::getStoreFlag, SampleItem::getFbFlag)
                        .eq(SampleItem::getSampleTaskId, p.getTaskId()));
                if(itemInfos != null && !itemInfos.isEmpty()) {
                    p.setSampleCount(itemInfos.size());
                    Integer toApproveSampleCount = itemInfos.stream().filter(item -> "3".equals(item.getSampleStatus())).collect(Collectors.toList()).size();
                    Integer toCollectSampleCount = itemInfos.stream().filter(item -> "0".equals(item.getSampleStatus())).collect(Collectors.toList()).size();
                    Integer toConfirmSampleCount = itemInfos.stream().filter(item -> "2".equals(item.getSampleStatus())).collect(Collectors.toList()).size();
                    Integer confirmedSampleCount = itemInfos.stream().filter(item -> Arrays.asList("3","4","6","7","8","9").contains(item.getSampleStatus())).collect(Collectors.toList()).size();
                    Integer toStockSampleCount = itemInfos.stream()
                            .filter(item -> "4".equals(item.getSampleStatus()) && "1".equals(item.getStoreFlag()) && "0".equals(item.getFbFlag()))
                            .collect(Collectors.toList()).size();
                    Integer stockedSampleCount = itemInfos.stream().filter(item -> Arrays.asList("6","7","8").contains(item.getSampleStatus())).collect(Collectors.toList()).size();
                    Integer toReviewSampleCount = itemInfos.stream().filter(item -> "10".equals(item.getSampleStatus())).collect(Collectors.toList()).size();
                    p.setToReviewSampleCount(toReviewSampleCount);
                    p.setToApproveSampleCount(toApproveSampleCount);
                    p.setToCollectSampleCount(toCollectSampleCount);
                    p.setToConfirmSampleCount(toConfirmSampleCount);
                    p.setConfirmedSampleCount(confirmedSampleCount);
                    p.setToStockSampleCount(toStockSampleCount);
                    p.setStockedSampleCount(stockedSampleCount);
                }
            });
        }
        return ResultUtil.data(page);
    }

    @Override
    public Result<SampleDbTaskItemVo> getSampleListByCondition(Long taskId, String sampleStatus) {
        SampleDbTaskItemVo vo = new SampleDbTaskItemVo();
        SampleTask task = sampleTaskMapper.selectById(taskId);
        if(task != null) {
            ScheduleJob job = jobMapper.selectById(task.getJobId());
            vo.setJobId(job.getId()).setInspectionName(job.getInspectionName()).setConsignorName(job.getConsignorName())
                    .setProjectName(job.getProjectName());
        }else{
            return ResultUtil.busiError("采样任务信息不存在！");
        }
        //统计任务下所有样品各状态数量
        List<SampleItem> itemInfos = sampleItemMapper.selectList(Wrappers.<SampleItem>lambdaQuery()
                .select(SampleItem::getId, SampleItem::getSampleStatus, SampleItem::getSignImageUrl, SampleItem::getStoreFlag, SampleItem::getFbFlag)
                .eq(SampleItem::getSampleTaskId, taskId));
        Integer toCollectSampleCount = itemInfos.stream().filter(item -> "0".equals(item.getSampleStatus())).collect(Collectors.toList()).size();
        Integer toConfirmSampleCount = itemInfos.stream().filter(item -> "2".equals(item.getSampleStatus())).collect(Collectors.toList()).size();
        Integer confirmedSampleCount = itemInfos.stream().filter(item -> Arrays.asList("3","4","6","7","8","9").contains(item.getSampleStatus())).collect(Collectors.toList()).size();
//        Integer toStockSampleCount = itemInfos.stream().filter(item -> Arrays.asList("4","9").contains(item.getSampleStatus())).collect(Collectors.toList()).size();
        Integer toStockSampleCount = itemInfos.stream()
                .filter(item -> "4".equals(item.getSampleStatus()) && "1".equals(item.getStoreFlag()) && "0".equals(item.getFbFlag()))
                .collect(Collectors.toList()).size();
        Integer stockedSampleCount = itemInfos.stream().filter(item -> Arrays.asList("6","7","8").contains(item.getSampleStatus())).collect(Collectors.toList()).size();
        vo.setToCollectSampleCount(toCollectSampleCount);
        vo.setToConfirmSampleCount(toConfirmSampleCount);
        vo.setConfirmedSampleCount(confirmedSampleCount);
        vo.setToStockSampleCount(toStockSampleCount);
        vo.setStockedSampleCount(stockedSampleCount);
        vo.setSampleCount(itemInfos.size());
        //当前采样组长所属样品
        String userId = (String) StpUtil.getLoginId();
        List<SampleListVo> vos = sampleItemMapper.getSampleListByCondition(taskId, userId, sampleStatus);
        if(vos != null && !vos.isEmpty()) {
            vos.forEach(item -> {
                List<SampleFactorDataVo> factorDataVos = sampleItemFactorDataMapper.getSampleFactorDataBySampItemId(item.getSampItemId());
                String factorName = factorDataVos.stream().map(SampleFactorDataVo::getFactorName).collect(Collectors.joining("/"));
                item.setFactorName(factorName);
            });
        }
        vo.setVos(vos);
        return ResultUtil.data(vo);
    }

    @Override
    public Result<IPage<SampleListVo>> getRejectedSampleList(PageVo pageVo) {
        String userId = (String) StpUtil.getLoginId();
        Page<SampleListVo> page = PageUtil.initMpPage(pageVo);
        sampleItemMapper.getRejectedSampleList(page, userId);
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            page.getRecords().forEach(p -> {
                List<SampleFactorDataVo> factorDataVos = sampleItemFactorDataMapper.getSampleFactorDataBySampItemId(p.getSampItemId());
                String factorName = factorDataVos.stream().map(SampleFactorDataVo::getFactorName).collect(Collectors.joining("/"));
                p.setFactorName(factorName);
            });
        }
        return ResultUtil.data(page);
    }

    @Override
    public Result<IPage<SampleListVo>> getSampleItemsForCollectUser(PageVo pageVo, String sampleStatus) {
        Page<SampleListVo> page = PageUtil.initMpPage(pageVo);
        String userId = (String) StpUtil.getLoginId();
        sampleItemMapper.getSampleItemsForCollectUser(page, userId, sampleStatus);
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            page.getRecords().forEach(p -> {
                List<SampleFactorDataVo> factorDataVos = sampleItemFactorDataMapper.getSampleFactorDataBySampItemId(p.getSampItemId());
                String factorName = factorDataVos.stream().map(SampleFactorDataVo::getFactorName).collect(Collectors.joining("/"));
                p.setFactorName(factorName);
            });
        }
        return ResultUtil.data(page);
    }

    @Override
    public Result<IPage<SampleTaskVo>> getSampleTasksForCollectUser(PageVo pageVo) {
        Page<SampleTaskVo> page = PageUtil.initMpPage(pageVo);
        String userId = (String) StpUtil.getLoginId();
        sampleTaskMapper.getSampleTasksForCollectUser(page, userId);
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            page.getRecords().forEach(p -> {
                //统计任务下所有样品各状态数量
                List<SampleItem> itemInfos = sampleItemMapper.selectList(Wrappers.<SampleItem>lambdaQuery()
                        .select(SampleItem::getId, SampleItem::getSampleStatus, SampleItem::getSignImageUrl)
                        .eq(SampleItem::getSampleTaskId, p.getTaskId()));
                Integer toCollectSampleCount = itemInfos.stream().filter(item -> "0".equals(item.getSampleStatus())).collect(Collectors.toList()).size();
                p.setToCollectSampleCount(toCollectSampleCount);
                p.setSampleCount(itemInfos.size());
                //当前采样员待复核样品
                List<SampleItem> items = sampleItemMapper.getSampItemsForCollectUserByTaskId(p.getTaskId(), userId);
                if(items != null && !items.isEmpty()) {
                    Integer toReviewSampleCount = items.stream().filter(item -> "10".equals(item.getSampleStatus())).collect(Collectors.toList()).size();
                    p.setToReviewSampleCount(toReviewSampleCount);
                }
            });
        }
        return ResultUtil.data(page);
    }

    @Override
    public Result<SampleDbTaskItemVo> getSampleItemsForCollectUserByTaskId(Long taskId) {
        SampleDbTaskItemVo vo = new SampleDbTaskItemVo();
        SampleTask task = sampleTaskMapper.selectById(taskId);
        if(task != null) {
            ScheduleJob job = jobMapper.selectById(task.getJobId());
            vo.setJobId(job.getId()).setInspectionName(job.getInspectionName()).setConsignorName(job.getConsignorName())
                    .setProjectName(job.getProjectName());
        }else{
            return ResultUtil.busiError("采样任务信息不存在！");
        }
        //统计任务下所有样品各状态数量
        List<SampleItem> itemInfos = sampleItemMapper.selectList(Wrappers.<SampleItem>lambdaQuery()
                .select(SampleItem::getId, SampleItem::getSampleStatus, SampleItem::getSignImageUrl)
                .eq(SampleItem::getSampleTaskId, taskId));
        Integer toCollectSampleCount = itemInfos.stream().filter(item -> "0".equals(item.getSampleStatus())).collect(Collectors.toList()).size();
        Integer toConfirmSampleCount = itemInfos.stream().filter(item -> "2".equals(item.getSampleStatus())).collect(Collectors.toList()).size();
        Integer confirmedSampleCount = itemInfos.stream().filter(item -> Arrays.asList("3","4","6","7","8","9").contains(item.getSampleStatus())).collect(Collectors.toList()).size();
        Integer toStockSampleCount = itemInfos.stream().filter(item -> Arrays.asList("4","9").contains(item.getSampleStatus())).collect(Collectors.toList()).size();
        Integer stockedSampleCount = itemInfos.stream().filter(item -> Arrays.asList("6","7","8").contains(item.getSampleStatus())).collect(Collectors.toList()).size();
        Integer toReviewSampleCount = itemInfos.stream().filter(item -> "10".equals(item.getSampleStatus())).collect(Collectors.toList()).size();
        vo.setToReviewSampleCount(toReviewSampleCount);
        vo.setToCollectSampleCount(toCollectSampleCount);
        vo.setToConfirmSampleCount(toConfirmSampleCount);
        vo.setConfirmedSampleCount(confirmedSampleCount);
        vo.setToStockSampleCount(toStockSampleCount);
        vo.setStockedSampleCount(stockedSampleCount);
        vo.setSampleCount(itemInfos.size());

        String userId = (String) StpUtil.getLoginId();
        List<SampleListVo> vos = sampleItemMapper.getToReviewSampItemsForCollectUserByTaskId(taskId, userId);
        if(vos != null && !vos.isEmpty()) {
            vos.forEach(item -> {
                List<SampleFactorDataVo> factorDataVos = sampleItemFactorDataMapper.getSampleFactorDataBySampItemId(item.getSampItemId());
                String factorName = factorDataVos.stream().map(SampleFactorDataVo::getFactorName).collect(Collectors.joining("/"));
                item.setFactorName(factorName);
            });
        }
        vo.setVos(vos);
        return ResultUtil.data(vo);
    }

    @Override
    public Result<SampleItemVo> getSampleDetailBySampItemId(Long sampItemId) {
        SampleItemData sampleItemData = sampleItemDataMapper.selectOne(Wrappers.<SampleItemData>lambdaQuery()
                .eq(SampleItemData::getSampleItemId, sampItemId));
        MobileSampleItemDetailVo vo = sampleItemDataMapper.getSampleDetailBySampleItemId(sampItemId);
        SampleItemVo sampleItemVo = copyProperties(vo);
        if(sampleItemData != null) {
            List<String> imageList = new ArrayList<>();
            if(StrUtil.isNotBlank(sampleItemData.getCollectIamge1Id())) {
                imageList.add(sampleItemData.getCollectIamge1Id());
            }
            if(StrUtil.isNotBlank(sampleItemData.getCollectIamge2Id())) {
                imageList.add(sampleItemData.getCollectIamge2Id());
            }
            if(StrUtil.isNotBlank(sampleItemData.getCollectIamge3Id())) {
                imageList.add(sampleItemData.getCollectIamge3Id());
            }
            if(StrUtil.isNotBlank(sampleItemData.getCollectIamge4Id())) {
                imageList.add(sampleItemData.getCollectIamge4Id());
            }
            if(StrUtil.isNotBlank(vo.getCollectLeaderId())) {
                SysUser user = sysUserService.getById(vo.getCollectLeaderId());
                sampleItemVo.setCollectLeader(user.getEmpName());
            }
            if(StrUtil.isNotBlank(vo.getCollectUserId())) {
                SysUser user = sysUserService.getById(vo.getCollectUserId());
                sampleItemVo.getSampleDataVo().setCollectUser(user.getEmpName());
            }
            if(StrUtil.isNotBlank(vo.getReviewUserId())) {
                SysUser user = sysUserService.getById(vo.getReviewUserId());
                sampleItemVo.getSampleDataVo().setReviewUser(user.getEmpName());
            }
            sampleItemVo.getSampleDataVo().setImageList(imageList);
        }
        String secdClassId = vo.getSecdClassId();
        if(StrUtil.isBlank(vo.getSampleData())) {
            //拼接样品二级类别动态属性
            JSONArray sampleData = assembleDynamicParams(vo.getSecdClassId());
            if(sampleData == null) {
                return ResultUtil.busiError("未配置二级类别动态属性！");
            }
            sampleItemVo.getSampleDataVo().setSampleData(sampleData);
        }else{
            JSONArray sampleData = JSON.parseArray(vo.getSampleData());
            //补充下拉框和选择框数据源
            for(int i = 0; i < sampleData.size(); i++) {
                JSONObject object = sampleData.getJSONObject(i);
                JSONArray params = object.getJSONArray("params");
                for(int j = 0; j < params.size(); j++){
                    JSONObject param = params.getJSONObject(j);
                    String labelType = param.getString("labelType");
                    if("0".equals(labelType)) {
                        DynamicParamConf dpc = dynamicParamConfMapper.selectOne(Wrappers.<DynamicParamConf>lambdaQuery()
                                .eq(DynamicParamConf::getSecdClassId, secdClassId)
                                .eq(DynamicParamConf::getPKey, param.getString("key")));
                        //级联下拉框
                        if("1".equals(dpc.getLabelValue())) {
                            //采样设备
                            param.put("selectValue", laboratoryEquipmentTree(Constants.EQUIPMENT_FIRST_TYPE_SAMPLE));
                        }else if("2".equals(dpc.getLabelValue())) {
                            //校准设备
                            param.put("selectValue", laboratoryEquipmentTree(Constants.EQUIPMENT_FIRST_TYPE_CALIBRATION));
                        }else if("3".equals(dpc.getLabelValue())) {
                            //现场检测设备
                            param.put("selectValue", laboratoryEquipmentTree(Constants.EQUIPMENT_FIRST_TYPE_CHECK));
                        }
                    }else if("2".equals(labelType)) {
                        //选择框
                        DynamicParamConf dpc = dynamicParamConfMapper.selectOne(Wrappers.<DynamicParamConf>lambdaQuery()
                                .eq(DynamicParamConf::getSecdClassId, secdClassId)
                                .eq(DynamicParamConf::getPKey, param.getString("key")));
                        param.put("radioValue", Arrays.asList(dpc.getRadioValue().split(",")));
                    }else if("7".equals(labelType)) {
                        //单选下拉框
                        DynamicParamConf dpc = dynamicParamConfMapper.selectOne(Wrappers.<DynamicParamConf>lambdaQuery()
                                .eq(DynamicParamConf::getSecdClassId, secdClassId)
                                .eq(DynamicParamConf::getPKey, param.getString("key")));
                        if("0".equals(dpc.getLabelValue())) {
                            //采样依据
                            List<SamplingBasis> list = samplingBasisMpper.selectList(Wrappers.<SamplingBasis>lambdaQuery()
                                    .eq(SamplingBasis::getClassId, secdClassId.substring(0,3)));
                            if(list != null && !list.isEmpty()) {
                                List<String > sampBasis = list.stream().map(d -> d.getBasis()).collect(Collectors.toList());
                                param.put("selectValue", sampBasis);
                            }
                        }
                    }
                }
            }
            sampleItemVo.getSampleDataVo().setSampleData(sampleData);
        }

        List<SampleFactorDataVo> factorDataVos = sampleItemFactorDataMapper.getSampleFactorDataBySampItemId(vo.getSampItemId());
        String factorName = factorDataVos.stream().map(SampleFactorDataVo::getFactorName).collect(Collectors.joining("/"));
        sampleItemVo.setFactorName(factorName);
        sampleItemVo.setFactorDataVos(factorDataVos);
        return ResultUtil.data(sampleItemVo);
    }

    @Override
    @Transactional
    public Result<Object> auditSampleItem(MobileSampleAuditTdo auditVo) {
        List<Long> sampItemIds = Arrays.stream(auditVo.getSampItemId().split(","))
                .map(v -> Long.parseLong(v)).collect(Collectors.toList());
        LocalDateTime now = LocalDateTime.now();
        String auditFlag = auditVo.getAuditFlag();
        String auditReason = auditVo.getAuditReason();
        String userId = (String) StpUtil.getLoginId();
        for(Long sampItemId : sampItemIds) {
            SampleItem itemInfo = sampleItemMapper.selectById(sampItemId);
            if(itemInfo == null) {
                throw new CustomException(String.format("样品 %s 信息不存在！", sampItemId));
            }
            int cnt = 0;
            if(auditFlag.equals("1")) {
                cnt = sampleItemMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                        .set(SampleItem::getSampleStatus, "3")
                        .set(SampleItem::getUpdateTime, now)
                        .set(SampleItem::getAuditTime, now)
                        .eq(SampleItem::getId, sampItemId));
            }else if(auditFlag.equals("2")){
                cnt = sampleItemMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                        .set(SampleItem::getSampleStatus, "5")
                        .set(SampleItem::getUpdateTime, now)
                        .set(SampleItem::getAuditReason, auditReason)
                        .set(SampleItem::getAuditTime, now)
                        .eq(SampleItem::getId, sampItemId));
            }
            if(cnt < 0) {
                throw new CustomException(String.format("样品 %s 审核操作失败！", sampItemId));
            }
            //保存样品审核记录
            SampleItemAuditRecord siar = new SampleItemAuditRecord();
            siar.setAuditReason(auditReason).setAuditStatus(auditFlag)
                    .setAuditTime(now).setSampleItemId(sampItemId).setAuditUserId(userId)
                    .setAuditType("1");
            sampleItemAuditRecordMapper.insert(siar);
        }

        return ResultUtil.success("审核操作成功！");
    }

    @Override
    @Transactional
    public Result<Object> saveSampItemDetail(MobileSampItemDetailTdo detailTDo) {
        LocalDateTime now = LocalDateTime.now();
        String userId = (String) StpUtil.getLoginId();
        SampleItem si = sampleItemMapper.selectById(detailTDo.getSampleItemId());
        if(si.getFrequency() == 1) {
            //一天多次的样品，第一次录样必须填写采样日期
            if(detailTDo.getCollectDate() == null) {
                return ResultUtil.validateError("采样日期不能为空！");
            }
        }
        SampleItemData detail = new SampleItemData();
        BeanUtils.copyProperties(detailTDo, detail);
        List<String> imageList = detailTDo.getImageList();
        if(imageList != null && !imageList.isEmpty()) {
            int size = imageList.size();
            if(0 < size) {
                detail.setCollectIamge1Id(imageList.get(0));
            }
            if(1 < size) {
                detail.setCollectIamge2Id(imageList.get(1));
            }
            if(2 < size) {
                detail.setCollectIamge3Id(imageList.get(2));
            }
            if(3 < size) {
                detail.setCollectIamge4Id(imageList.get(3));
            }
        }
        detail.setCollectUserId(userId).setCreateTime(now);
        SampleItemData dbPo = sampleItemDataMapper.selectOne(Wrappers.<SampleItemData>lambdaQuery()
                .eq(SampleItemData::getSampleItemId, detailTDo.getSampleItemId()));
        int cnt;
        if(dbPo != null) {
            detail.setId(dbPo.getId());
            cnt = sampleItemDataMapper.updateById(detail);
        }else{
            cnt = sampleItemDataMapper.insert(detail);
        }
        sampleItemMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                .set(SampleItem::getSampleStatus, "1")
                .eq(SampleItem::getId, detail.getSampleItemId()));
        List<SampleFactorDataVo> factorDataVos = detailTDo.getFactorDataVos();
        List<SampleItemFactorData> factorDatas = factorDataVos.stream().map(item -> {
            SampleItemFactorData factorData = new SampleItemFactorData();
            BeanUtils.copyProperties(item, factorData);
            return factorData;
        }).collect(Collectors.toList());
        sampleItemFactorDataService.updateBatchById(factorDatas);
        if(cnt > 0) {
            return ResultUtil.success("样品采样信息保存成功！");
        }else{
            return ResultUtil.busiError("样品采样信息保存失败！");
        }
    }

    @Override
    public Result<Object> submitSampItemDetail(Long sampItemId) {
        if(sampItemId == null) {
            return ResultUtil.busiError("样品ID不能为空！");
        }
        SampleItem itemInfo = sampleItemMapper.selectById(sampItemId);
        if(itemInfo != null) {
            SampleItemData sid = sampleItemDataMapper.selectOne(Wrappers.<SampleItemData>lambdaQuery()
                    .eq(SampleItemData::getSampleItemId, sampItemId));
            if(StrUtil.isBlank(sid.getReviewUserId())) {
                return ResultUtil.busiError("样品复核人不能为空！");
            }
            if(StrUtil.isBlank(sid.getCollectLocation())) {
                return ResultUtil.busiError("采样地址不能为空！");
            }
            if(sid.getCollectDate() == null) {
                return ResultUtil.busiError("采样日期不能为空！");
            }
            if(StrUtil.isBlank(sid.getSampleData())) {
                return ResultUtil.busiError("采样二级分类数据不能为空！");
            }
            JSONArray sampleData = JSON.parseArray(sid.getSampleData());
            for(int i = 0; i < sampleData.size(); i++) {
                JSONObject object = sampleData.getJSONObject(i);
                JSONArray params = object.getJSONArray("params");
                for(int j = 0; j < params.size(); j++){
                    JSONObject param = params.getJSONObject(j);
                    if(param.getBoolean("required")) {
                        if(StrUtil.isBlank(param.getString("value"))) {
                            return ResultUtil.busiError(String.format("%s 不能为空！", param.getString("name")));
                        }
                    }
                }
            }

            int cnt = sampleItemMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                    .set(SampleItem::getSampleStatus, "10")
                    .set(SampleItem::getUpdateTime, LocalDateTime.now())
                    .set(SampleItem::getAuditReason, null)
                    .eq(SampleItem::getId, sampItemId));
            if(cnt > 0) {
                return ResultUtil.success("样品提交复核成功！");
            }else{
                return ResultUtil.busiError("样品提交复核失败！");
            }
        }else{
            return ResultUtil.busiError("样品信息不存在！");
        }
    }

    @Override
    @Transactional
    public Result<Object> sampStoreApply(String sampItemId) {
        if(StrUtil.isBlank(sampItemId)) {
            return ResultUtil.validateError("样品编号不能为空！");
        }
        LocalDateTime now = LocalDateTime.now();
        String userId = (String) StpUtil.getLoginId();
        List<Long> sampItemIds = Arrays.stream(sampItemId.split(","))
                .map(v -> Long.parseLong(v)).collect(Collectors.toList());
        for(Long id : sampItemIds) {
            SampleItem sii = sampleItemMapper.selectById(id);
            if(sii.getFbFlag().equals("1")) {
                continue;
            }
            SampleStoreApply ssa = sampleStoreApplyService.getOne(Wrappers.<SampleStoreApply>lambdaQuery()
                    .eq(SampleStoreApply::getSampleItemId, id));
            if(ssa == null) {
                ssa = new SampleStoreApply();
                ssa.setApplyUserId(userId).setApprovalStatus("0").setCreateTime(now).setSampleItemId(id);
                int cnt = sampleStoreApplyMapper.insert(ssa);
                if(cnt > 0) {
                    cnt = sampleItemMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                            .set(SampleItem::getSampleStatus, "9")
                            .eq(SampleItem::getId, id));
                    if(cnt <= 0) {
                        throw new CustomException("样品申请入库失败！");
                    }
                }else{
                    throw new CustomException("样品申请入库失败！");
                }
            }
        }
        return ResultUtil.success("样品申请入库成功！");
    }

    @Override
    public Result<Object> doSignForSamp(MobileSignTdo tdo) {
        sampleItemMapper.update(null, Wrappers.<SampleItem>lambdaUpdate()
                .set(SampleItem::getSignImageUrl, tdo.getSignImgName())
                .eq(SampleItem::getId, tdo.getSampItemId()));
        return ResultUtil.success("签名保存成功！");
    }

}
