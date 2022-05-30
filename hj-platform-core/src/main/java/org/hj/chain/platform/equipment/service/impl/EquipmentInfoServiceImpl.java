package org.hj.chain.platform.equipment.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.Constants;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.equipment.entity.EquipmentInfo;
import org.hj.chain.platform.equipment.entity.EquipmentRecordInfo;
import org.hj.chain.platform.equipment.mapper.EquipmentInfoMapper;
import org.hj.chain.platform.equipment.service.IEquipmentInfoService;
import org.hj.chain.platform.equipment.service.IEquipmentRecordInfoService;
import org.hj.chain.platform.service.ISysDictService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.equipment.EquipmentInfoTdo;
import org.hj.chain.platform.tdo.equipment.EquipmentLoanTdo;
import org.hj.chain.platform.vo.DictParam;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.equipment.EquipmentInfoDisplayVo;
import org.hj.chain.platform.vo.equipment.EquipmentSearchVo;
import org.hj.chain.platform.vo.equipment.EquipmentTreeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EquipmentInfoServiceImpl extends ServiceImpl<EquipmentInfoMapper, EquipmentInfo> implements IEquipmentInfoService {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IEquipmentRecordInfoService equipmentRecordInfoService;
    @Autowired
    private ISysDictService sysDictService;

    @Override
    public IPage<EquipmentInfoDisplayVo> findByCondition(PageVo pageVo, EquipmentSearchVo sv) {
        List<EquipmentInfoDisplayVo> vos = new ArrayList<>();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        Page<EquipmentInfo> page = PageUtil.initMpPage(pageVo);
        this.page(page, Wrappers.<EquipmentInfo>lambdaQuery()
                .eq(EquipmentInfo::getOrganId, loginOutputVo.getOrganId())
                .eq(EquipmentInfo::getIsDelete, "0")
                .eq(sv.getEquipmentNumber() != null, EquipmentInfo::getEquipmentNumber, sv.getEquipmentNumber())
                .eq(sv.getEquipmentFirstType() != null, EquipmentInfo::getEquipmentFirstType, sv.getEquipmentFirstType())
                .eq(sv.getEquipmentSecondType() != null, EquipmentInfo::getEquipmentSecondType, sv.getEquipmentSecondType())
                .eq(sv.getEquipmentStatus() != null, EquipmentInfo::getEquipmentStatus, sv.getEquipmentStatus())
                .like(sv.getEquipmentName() != null, EquipmentInfo::getEquipmentName, sv.getEquipmentName())
                .orderByAsc(EquipmentInfo::getEquipmentNumber));
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            Map<String, DictParam> dictParamMap = sysDictService.findDictMap();
            vos = page.getRecords().stream().map(item -> {
                EquipmentInfoDisplayVo vo = new EquipmentInfoDisplayVo();
                BeanUtils.copyProperties(item, vo);
                String createUser = sysUserService.findUserByUserId(item.getCreateUserId()).getEmpName();
                vo.setCreateUser(createUser);
                if(item.getUpdateUserId() != null) {
                    String updateUser = sysUserService.findUserByUserId(item.getUpdateUserId()).getEmpName();
                    vo.setUpdateUser(updateUser);
                }
                vo.setEquipmentFirstTypeVal(dictParamMap.get(String.valueOf(vo.getEquipmentFirstType())).getDictVal());
                if(vo.getEquipmentSecondType() != null) {
                    vo.setEquipmentSecondTypeVal(dictParamMap.get(String.valueOf(vo.getEquipmentSecondType())).getDictVal());
                }
                return vo;
            }).collect(Collectors.toList());
        }
        return PageUtil.convertPageVo(page, vos);
    }

    @Transactional
    @Override
    public Result<Object> loan(EquipmentLoanTdo tdo) {
        EquipmentInfo equipmentInfo = this.getById(tdo.getEquipmentId());
        if(equipmentInfo == null) {
            return ResultUtil.busiError("设备不存在！");
        }
        if(!equipmentInfo.getEquipmentStatus().equals("0")) {
            return ResultUtil.busiError("该设备当前无法申请！");
        }
        if(equipmentInfo.getIsDelete().equals("1")) {
            return ResultUtil.busiError("设备已删除！");
        }
        LocalDateTime now = LocalDateTime.now();
        String userId = (String) StpUtil.getLoginId();
        BeanUtils.copyProperties(tdo, equipmentInfo);
        equipmentInfo.setEquipmentStatus("1")
                .setUpdateUserId(userId)
                .setUpdateTime(now);
        this.updateById(equipmentInfo);
        EquipmentRecordInfo equipmentRecordInfo = new EquipmentRecordInfo();
        BeanUtils.copyProperties(tdo, equipmentRecordInfo);
        equipmentRecordInfo.setType("0").setCreateUserId(userId).setCreateTime(now);
        equipmentRecordInfoService.save(equipmentRecordInfo);
        return ResultUtil.success("设备申请成功！");
    }

    @Override
    public Result<Object> add(EquipmentInfoTdo tdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        EquipmentInfo equipmentInfo = new EquipmentInfo();
        BeanUtils.copyProperties(tdo, equipmentInfo);
        equipmentInfo.setCreateUserId(loginOutputVo.getUserId())
                .setOrganId(loginOutputVo.getOrganId())
                .setCreateTime(LocalDateTime.now())
                .setIsDelete("0");
        this.save(equipmentInfo);
        return ResultUtil.success("设备添加成功！");
    }

    @Transactional
    @Override
    public Result<Object> remandById(Long equipmentId) {
        if(equipmentId == null) {
            return ResultUtil.busiError("设备ID不能为空！");
        }
        EquipmentInfo equipmentInfo = this.getById(equipmentId);
        if(equipmentInfo == null) {
            return ResultUtil.busiError("设备不存在！");
        }
        if(equipmentInfo.getEquipmentStatus().equals("0")) {
            return ResultUtil.busiError("设备已归还！");
        }
        LocalDateTime now = LocalDateTime.now();
        String userId = (String) StpUtil.getLoginId();
        EquipmentRecordInfo equipmentRecordInfo = new EquipmentRecordInfo();
        equipmentRecordInfo
                .setEquipmentId(equipmentId)
                .setType("1")
                .setUsername(equipmentInfo.getUsername())
                .setLinkmethod(equipmentInfo.getLinkmethod())
                .setTaskId(equipmentInfo.getTaskId())
                .setPoint(equipmentInfo.getPoint())
                .setCreateUserId(userId)
                .setCreateTime(now);
        equipmentRecordInfoService.save(equipmentRecordInfo);
        equipmentInfo
                .setEquipmentStatus("0")
                .setUpdateUserId(userId)
                .setUpdateTime(now)
                .setUsername(null)
                .setLinkmethod(null)
                .setPoint(null)
                .setTaskId(null);
        this.updateById(equipmentInfo);
        return ResultUtil.success("设备归还成功！");
    }

    @Override
    public Result<Object> maintenance(Long equipmentId) {
        if(equipmentId == null) {
            return ResultUtil.busiError("设备ID不能为空！");
        }
        EquipmentInfo equipmentInfo = this.getById(equipmentId);
        if(equipmentInfo == null) {
            return ResultUtil.busiError("设备不存在！");
        }
        if(!equipmentInfo.getEquipmentStatus().equals("0")) {
            return ResultUtil.busiError("设备当前不处于闲置中，无法维修！");
        }
        LocalDateTime now = LocalDateTime.now();
        String userId = (String) StpUtil.getLoginId();
        EquipmentRecordInfo equipmentRecordInfo = new EquipmentRecordInfo();
        equipmentRecordInfo
                .setEquipmentId(equipmentId)
                .setType("2")
                .setCreateUserId(userId)
                .setCreateTime(now);
        equipmentRecordInfoService.save(equipmentRecordInfo);
        equipmentInfo
                .setEquipmentStatus("2")
                .setUpdateUserId(userId)
                .setUpdateTime(now);
        this.updateById(equipmentInfo);
        return ResultUtil.success("设备申请维修成功！");
    }

    @Override
    public Result<Object> completed(Long equipmentId) {
        if(equipmentId == null) {
            return ResultUtil.busiError("设备ID不能为空！");
        }
        EquipmentInfo equipmentInfo = this.getById(equipmentId);
        if(equipmentInfo == null) {
            return ResultUtil.busiError("设备不存在！");
        }
        if(equipmentInfo.getEquipmentStatus().equals("0")) {
            return ResultUtil.busiError("设备已维修完成！");
        }
        LocalDateTime now = LocalDateTime.now();
        String userId = (String) StpUtil.getLoginId();
        EquipmentRecordInfo equipmentRecordInfo = new EquipmentRecordInfo();
        equipmentRecordInfo
                .setEquipmentId(equipmentId)
                .setType("3")
                .setCreateUserId(userId)
                .setCreateTime(now);
        equipmentRecordInfoService.save(equipmentRecordInfo);
        equipmentInfo
                .setEquipmentStatus("0")
                .setUpdateUserId(userId)
                .setLastTime(now)
                .setUpdateTime(now);
        this.updateById(equipmentInfo);
        return ResultUtil.success("设备维修完成！");
    }

    @Override
    public List<DictParam> findEquipmentFirstType() {
        List<DictParam> list = new ArrayList<>();
        Map<String, List<Long>> relMap = sysDictService.findDictRel();
        if(relMap != null && !relMap.isEmpty()) {
            List<Long> subKeys = relMap.get(String.valueOf(Constants.EQUIPMENT_FIRST_TYPE));
            if(subKeys != null && !subKeys.isEmpty()) {
                Map<String, DictParam> dictParamMap = sysDictService.findDictMap();
                subKeys.forEach(item -> {
                    DictParam dictParam = dictParamMap.get(String.valueOf(item));
                    list.add(dictParam);
                });
            }
        }
        return list;
    }

    @Override
    public List<DictParam> findEquipmentSecondType(Long rootKey) {
        List<DictParam> list = new ArrayList<>();
        Map<String, List<Long>> relMap = sysDictService.findDictRel();
        if(relMap != null && !relMap.isEmpty()) {
            if(rootKey.equals(Constants.EQUIPMENT_FIRST_TYPE_LABORATORY)) {
                rootKey = Constants.EQUIPMENT_LABORATORY;
            }else{
                rootKey = Constants.EQUIPMENT_SECOND_TYPE;
            }
            List<Long> subKeys = relMap.get(String.valueOf(rootKey));
            if(subKeys != null && !subKeys.isEmpty()) {
                Map<String, DictParam> dictParamMap = sysDictService.findDictMap();
                subKeys.forEach(item -> {
                    DictParam dictParam = dictParamMap.get(String.valueOf(item));
                    list.add(dictParam);
                });
            }
        }
        return list;
    }

    @Override
    public Result<Object> laboratoryEquipmentTree() {
        JSONArray tree = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<EquipmentInfo> list = this.list(Wrappers.<EquipmentInfo>lambdaQuery()
                .eq(EquipmentInfo::getOrganId, loginOutputVo.getOrganId())
                .eq(EquipmentInfo::getEquipmentFirstType, Constants.EQUIPMENT_FIRST_TYPE_LABORATORY)
                .eq(EquipmentInfo::getIsDelete, "0")
                .orderByAsc(EquipmentInfo::getEquipmentName, EquipmentInfo::getEquipmentModel));
        if(list != null && !list.isEmpty()) {
            Map<String, DictParam> dictParamMap = sysDictService.findDictMap();
            Map<String, Map<String, List<EquipmentInfo>>> map = list.stream()
                    .collect(Collectors.groupingBy(EquipmentInfo::getEquipmentName, Collectors.groupingBy(EquipmentInfo::getEquipmentModel)));
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
        return ResultUtil.data(tree);
    }


}
