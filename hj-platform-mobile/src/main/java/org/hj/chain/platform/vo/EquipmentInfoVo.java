package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EquipmentInfoVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    /**
     * 设备编号
     */
    private String equipmentNumber;
    /**
     * 设备名称
     */
    private String equipmentName;
//    /**
//     * 设备一级类型
//     */
//    private Long equipmentFirstType;
    /**
     * 设备一级类型字典值
     */
    private String equipmentFirstTypeVal;
//    /**
//     * 设备二级类型
//     */
//    private Long equipmentSecondType;
    /**
     * 设备二级类型字典值
     */
    private String equipmentSecondTypeVal;
    /**
     * 设备型号
     */
    private String equipmentModel;
}
