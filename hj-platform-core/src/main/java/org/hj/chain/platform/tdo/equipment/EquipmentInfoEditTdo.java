package org.hj.chain.platform.tdo.equipment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EquipmentInfoEditTdo implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "设备ID不能为空！")
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
     * 设备一级类型
     */
    private Long equipmentFirstType;
    /**
     * 设备二级类型
     */
    private Long equipmentSecondType;
    /**
     * 设备品牌
     */
    private String equipmentBrand;
    /**
     * 设备型号
     */
    private String equipmentModel;
    /**
     * 鉴定周期：0-月；1-季度；2-半年；3-1年；4-2年；5-3年
     */
    private String checkCircle;
    /**
     * 备注
     */
    private String remark;
    /**
     * 检测日期
     */
    private LocalDate checkDate;
}
