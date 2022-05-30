package org.hj.chain.platform.vo.equipment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EquipmentSearchVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 设备编号
     */
    private String equipmentNumber;
    /**
     * 设备名称
     */
    private String equipmentName;
    /**
     * 设备一级类型
     */
    private Long equipmentFirstType;
    /**
     * 设备二级类型
     */
    private Long equipmentSecondType;
    /**
     * 设备状态：0-闲置中；1-使用中；2-维修中
     */
    private String equipmentStatus;
}
