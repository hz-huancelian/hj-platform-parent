package org.hj.chain.platform.equipment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.equipment.entity.EquipmentInfo;
import org.hj.chain.platform.tdo.equipment.EquipmentInfoTdo;
import org.hj.chain.platform.tdo.equipment.EquipmentLoanTdo;
import org.hj.chain.platform.vo.DictParam;
import org.hj.chain.platform.vo.equipment.EquipmentInfoDisplayVo;
import org.hj.chain.platform.vo.equipment.EquipmentSearchVo;

import java.util.List;

public interface IEquipmentInfoService extends IService<EquipmentInfo> {

    IPage<EquipmentInfoDisplayVo> findByCondition(PageVo pageVo, EquipmentSearchVo sv);

    Result<Object> add(EquipmentInfoTdo tdo);

    Result<Object> loan(EquipmentLoanTdo tdo);

    Result<Object> remandById(Long equipmentId);

    Result<Object> maintenance(Long equipmentId);

    Result<Object> completed(Long equipmentId);

    List<DictParam> findEquipmentFirstType();

    List<DictParam> findEquipmentSecondType(Long rootKey);

    Result<Object> laboratoryEquipmentTree();
}
