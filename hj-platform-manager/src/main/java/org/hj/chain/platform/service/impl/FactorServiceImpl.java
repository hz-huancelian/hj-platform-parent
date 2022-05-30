package org.hj.chain.platform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hj.chain.platform.mapper.*;
import org.hj.chain.platform.model.*;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 因子缓存服务
 * @Iteration : 1.0
 * @Date : 2021/5/6  12:49 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/06    create
 */
@Service
public class FactorServiceImpl implements IFactorService {

    @Autowired
    private FactorClassInfoMapper factorClassInfoMapper;
    @Autowired
    private FactorMethodInfoMapper factorMethodInfoMapper;
    @Autowired
    private FactorInfoMapper factorInfoMapper;
    @Autowired
    private UnitInfoMapper unitInfoMapper;
    @Autowired
    private UnitGroupRelatedInfoMapper unitGroupRelatedInfoMapper;
    @Autowired
    private FactorMethodSubfactorInfoMapper factorMethodSubfactorInfoMapper;


    @Cacheable(value = "factor:method", key = "#id")
    @Override
    public FactorMethodInfoVo findFactorMethodById(String id) {
        FactorMethodInfo dbMethod = factorMethodInfoMapper.selectOne(Wrappers.<FactorMethodInfo>lambdaQuery()
                .select(FactorMethodInfo::getId,
                        FactorMethodInfo::getClassId,
                        FactorMethodInfo::getDataEntryStep,
                        FactorMethodInfo::getDetectionLimit,
                        FactorMethodInfo::getFactorName,
                        FactorMethodInfo::getAnalysisMethod,
                        FactorMethodInfo::getAnalysisMethodCgy,
                        FactorMethodInfo::getStandardNo,
                        FactorMethodInfo::getStandardSource,
                        FactorMethodInfo::getStandardName,
                        FactorMethodInfo::getDefaultUnitId,
                        FactorMethodInfo::getUnitGroupId,
                        FactorMethodInfo::getUnitName,
                        FactorMethodInfo::getStatus
                ).eq(FactorMethodInfo::getId, id));
        if (dbMethod != null) {
            FactorMethodInfoVo vo = new FactorMethodInfoVo();
            vo.setId(dbMethod.getId())
                    .setStandardName(dbMethod.getStandardName())
                    .setStandardNo(dbMethod.getStandardNo())
                    .setStandardSource(dbMethod.getStandardSource())
                    .setAnalysisMethod(dbMethod.getAnalysisMethod())
                    .setAnalysisMethodCgy(dbMethod.getAnalysisMethodCgy())
                    .setDataEntryStep(dbMethod.getDataEntryStep())
                    .setDetectionLimit(dbMethod.getDetectionLimit())
                    .setFactorName(dbMethod.getFactorName())
                    .setStatus(dbMethod.getStatus())
                    .setDefaultUnitName(dbMethod.getUnitName())
                    .setClassId(dbMethod.getClassId());
            FactorClassInfo dbClass = factorClassInfoMapper.selectOne(Wrappers.<FactorClassInfo>lambdaQuery()
                    .select(FactorClassInfo::getId,
                            FactorClassInfo::getName)
                    .eq(FactorClassInfo::getId, dbMethod.getClassId()));
            if (dbClass != null) {
                vo.setClassName(dbClass.getName());
            }
//            String defaultUnitId = dbMethod.getDefaultUnitId();
//            vo.setDefaultUnitId(defaultUnitId);
//
//            UnitInfo dbUnit = unitInfoMapper.selectOne(Wrappers.<UnitInfo>lambdaQuery()
//                    .select(UnitInfo::getName)
//                    .eq(UnitInfo::getId, defaultUnitId));
//            if (dbUnit != null) {
//                vo.setDefaultUnitName(dbUnit.getName());
//            }
//            String unitGroupId = dbMethod.getUnitGroupId();
//            List<UnitInfoVo> unitVos = unitGroupRelatedInfoMapper.findUnitListByGroupId(unitGroupId);
//            vo.setUnitVos(unitVos);
            return vo;
        }
        return null;
    }

    @Cacheable(value = "factor:item", key = "#factorId")
    @Override
    public FactorInfoVo findFactorInfoByFactorId(String factorId) {
        FactorInfo dbPo = factorInfoMapper.selectOne(Wrappers.<FactorInfo>lambdaQuery()
                .select(FactorInfo::getId,
                        FactorInfo::getName,
                        FactorInfo::getClassId,
                        FactorInfo::getDataEntryStep,
                        FactorInfo::getDetectionLimit)
                .eq(FactorInfo::getId, factorId));

        if (dbPo != null) {
            FactorInfoVo vo = new FactorInfoVo();
            vo.setId(factorId)
                    .setClassId(dbPo.getClassId())
                    .setName(dbPo.getName())
                    .setDataEntryStep(dbPo.getDataEntryStep())
                    .setDetectionLimit(dbPo.getDetectionLimit());
            return vo;
        }
        return null;
    }


    @Cacheable(value = "factor:fst:class")
    @Override
    public List<FactorClassInfoVo> findFstClass() {
        List<FactorClassInfo> classInfos = factorClassInfoMapper.selectList(Wrappers.<FactorClassInfo>lambdaQuery()
                .select(FactorClassInfo::getId,
                        FactorClassInfo::getName)
                .eq(FactorClassInfo::getLevel, "1"));
        if (classInfos != null && !classInfos.isEmpty()) {
            List<FactorClassInfoVo> classInfoVos = classInfos.stream().map(item -> {
                FactorClassInfoVo classInfoVo = new FactorClassInfoVo();
                classInfoVo.setId(item.getId())
                        .setName(item.getName());
                return classInfoVo;
            }).collect(Collectors.toList());
            return classInfoVos;
        }
        return null;
    }

    @Cacheable(value = "factor:secd:class", key = "#fstClassId")
    @Override
    public List<FactorClassInfoVo> findSecdClassByClassId(String fstClassId) {
        List<FactorClassInfo> classInfos = factorClassInfoMapper.selectList(Wrappers.<FactorClassInfo>lambdaQuery()
                .select(FactorClassInfo::getId,
                        FactorClassInfo::getName)
                .eq(FactorClassInfo::getParentId, fstClassId));
        if (classInfos != null && !classInfos.isEmpty()) {
            List<FactorClassInfoVo> classInfoVos = classInfos.stream().map(item -> {
                FactorClassInfoVo classInfoVo = new FactorClassInfoVo();
                classInfoVo.setId(item.getId())
                        .setName(item.getName());
                return classInfoVo;
            }).collect(Collectors.toList());
            return classInfoVos;
        }
        return null;
    }

    @Override
    public List<FactorClassInfoVo> getAllFactorSecClass() {
        List<FactorClassInfoVo> vos = new ArrayList<>();
        List<FactorClassInfo> classInfos = factorClassInfoMapper.selectList(Wrappers.<FactorClassInfo>lambdaQuery()
                .select(FactorClassInfo::getId,
                        FactorClassInfo::getName)
                .eq(FactorClassInfo::getLevel, "2")
                .orderByAsc(FactorClassInfo::getId));
        if (classInfos != null && !classInfos.isEmpty()) {
            vos = classInfos.stream().map(item -> {
                FactorClassInfoVo classInfoVo = new FactorClassInfoVo();
                classInfoVo.setId(item.getId())
                        .setName(item.getName());
                return classInfoVo;
            }).collect(Collectors.toList());
        }
        return vos;
    }

    @Cacheable(value = "factor:subFactor", key = "#id")
    @Override
    public List<FactorMethodSubfactorInfo> findSebFactorById(String id) {
        List<FactorMethodSubfactorInfo> list = factorMethodSubfactorInfoMapper.selectList(Wrappers.<FactorMethodSubfactorInfo>lambdaQuery()
                .eq(FactorMethodSubfactorInfo::getFactorMethodId, id));
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return null;
    }
}