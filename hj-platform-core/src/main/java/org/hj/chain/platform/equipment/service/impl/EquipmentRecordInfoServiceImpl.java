package org.hj.chain.platform.equipment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.equipment.entity.EquipmentRecordInfo;
import org.hj.chain.platform.equipment.mapper.EquipmentRecordInfoMapper;
import org.hj.chain.platform.equipment.service.IEquipmentRecordInfoService;
import org.springframework.stereotype.Service;

@Service
public class EquipmentRecordInfoServiceImpl extends ServiceImpl<EquipmentRecordInfoMapper, EquipmentRecordInfo>
        implements IEquipmentRecordInfoService {
}
