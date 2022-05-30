package org.hj.chain.platform.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.equipment.entity.EquipmentInfo;
import org.hj.chain.platform.vo.statistics.EquipmentStatusStatictisVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EquipmentInfoMapper extends BaseMapper<EquipmentInfo> {
    List<Map<String, Object>> equipmentCnt(String organId);

    List<EquipmentStatusStatictisVo> equipmentStatsuCnt(String organId);
}
