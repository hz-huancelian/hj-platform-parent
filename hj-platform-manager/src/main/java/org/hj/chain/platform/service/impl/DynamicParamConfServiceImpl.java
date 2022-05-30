package org.hj.chain.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.mapper.DynamicParamConfMapper;
import org.hj.chain.platform.mapper.FactorClassInfoMapper;
import org.hj.chain.platform.mapper.SamplingBasisMpper;
import org.hj.chain.platform.model.DynamicParamConf;
import org.hj.chain.platform.model.FactorClassInfo;
import org.hj.chain.platform.model.SamplingBasis;
import org.hj.chain.platform.service.IDynamicParamConfService;
import org.hj.chain.platform.tdo.DynamicParamConfTdo;
import org.hj.chain.platform.vo.FactorSecdClassVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * @Date : 2022/3/8
 */
@Service
public class DynamicParamConfServiceImpl extends ServiceImpl<DynamicParamConfMapper, DynamicParamConf>
        implements IDynamicParamConfService {

    @Autowired
    private SamplingBasisMpper samplingBasisMpper;
    @Autowired
    private FactorClassInfoMapper factorClassInfoMapper;

    @Override
    public Result<Object> add(DynamicParamConfTdo tdo) {
        if("0".equals(tdo.getLabelType())) {
            if(StrUtil.isBlank(tdo.getLabelValue())) {
                return ResultUtil.validateError("下拉框标签值不能为空！");
            }
        }
        if("2".equals(tdo.getLabelType())) {
            if(StrUtil.isBlank(tdo.getRadioValue())) {
                return ResultUtil.validateError("选择框标签值不能为空！");
            }
        }
        DynamicParamConf dynamicParamConf = new DynamicParamConf();
        BeanUtils.copyProperties(tdo, dynamicParamConf);
        this.save(dynamicParamConf);
        return ResultUtil.success("保存成功！");
    }

    @Override
    public Result<List<FactorSecdClassVo>> getAllFactorSecdClass() {
        List<FactorSecdClassVo> vos = new ArrayList<>();
        List<FactorClassInfo> factorClassInfos = factorClassInfoMapper.selectList(Wrappers.<FactorClassInfo>lambdaQuery()
                .select(FactorClassInfo::getId, FactorClassInfo::getName, FactorClassInfo::getLevel, FactorClassInfo::getParentId)
                .orderByAsc(FactorClassInfo::getId));
        if(factorClassInfos != null && !factorClassInfos.isEmpty()) {
            List<FactorClassInfo> classInfos = factorClassInfos.stream().filter(v -> "1".equals(v.getLevel())).collect(Collectors.toList());
            if(classInfos != null && !classInfos.isEmpty()) {
                Map<String, String> map = new HashMap<>();
                classInfos.forEach(data -> map.put(data.getId(), data.getName()));
                List<FactorClassInfo> secdClassInfos = factorClassInfos.stream().filter(d -> "2".equals(d.getLevel())).collect(Collectors.toList());
                if(secdClassInfos != null && !secdClassInfos.isEmpty()) {
                    vos = secdClassInfos.stream().map(item -> {
                        FactorSecdClassVo vo = new FactorSecdClassVo();
                        vo.setSecdClassId(item.getId())
                                .setSecdClassName(item.getName())
                                .setClassId(item.getParentId())
                                .setClassName(map.get(item.getParentId()));
                        return vo;
                    }).collect(Collectors.toList());
                }
            }
        }
        return ResultUtil.data(vos);
    }

    /**
     * TODO 根据一级类别获取采样依据列表
     * @param classId
     * @return
     */
    public List<String> findSamplingBasisByClassId(String classId) {
        List<String> list = new ArrayList<>();
        List<SamplingBasis> samplingBases = samplingBasisMpper.selectList(Wrappers.<SamplingBasis>lambdaQuery()
                .eq(SamplingBasis::getClassId, classId));
        if(samplingBases != null && !samplingBases.isEmpty()) {
            list = samplingBases.stream().map(v -> v.getBasis()).collect(Collectors.toList());
        }
        return list;
    }


}
