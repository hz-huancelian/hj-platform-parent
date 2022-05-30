package org.hj.chain.platform.tdo.equipment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EquipmentInfoTdo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 设备编号
     */
    @NotBlank(message = "设备编号不能为空！")
    private String equipmentNumber;
    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空！")
    private String equipmentName;
    /**
     * 设备一级类型
     */
    @NotNull(message = "设备一级类型不能为空！")
    private Long equipmentFirstType;
    /**
     * 设备二级类型
     */
    private Long equipmentSecondType;
    /**
     * 设备品牌
     */
    @NotBlank(message = "设备品牌不能为空！")
    private String equipmentBrand;
    /**
     * 设备型号
     */
    @NotBlank(message = "设备型号不能为空！")
    private String equipmentModel;
    /**
     * 鉴定周期：0-月；1-季度；2-半年；3-1年；4-2年；5-3年
     */
    @NotBlank(message = "鉴定周期不能为空！")
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
