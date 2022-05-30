package org.hj.chain.platform.vo.equipment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EquipmentTreeVo {
    private Long id;
    /**
     * 设备编号
     */
    private String equipmentNumber;
    /**
     * 设备名称
     */
    private String equipmentName;
    /**
     * 设备类别
     */
    private String equipmentFirstTypeVal;
    /**
     * 检测类别
     */
    private String equipmentSecondTypeVal;
    /**
     * 设备品牌
     */
    private String equipmentBrand;
    /**
     * 设备型号
     */
    private String equipmentModel;
}
