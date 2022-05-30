package org.hj.chain.platform.dataimport.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EquipmentImportEntity implements Serializable {
    /**
     * 设备编号
     */
    @ExcelProperty(index = 0)
    private String equipmentNumber;
    /**
     * 设备名称
     */
    @ExcelProperty(index = 1)
    private String equipmentName;
    /**
     * 设备一级类型
     */
    @ExcelProperty(index = 2)
    private String equipmentFirstType;
    /**
     * 设备二级类型
     */
    @ExcelProperty(index = 3)
    private String equipmentSecondType;
    /**
     * 设备品牌
     */
    @ExcelProperty(index = 4)
    private String equipmentBrand;
    /**
     * 设备型号
     */
    @ExcelProperty(index = 5)
    private String equipmentModel;
    /**
     * 鉴定周期：0-月；1-季度；2-半年；3-1年；4-2年；5-3年
     */
    @ExcelProperty(index = 6)
    private String checkCircle;
    /**
     * 备注
     */
    @ExcelProperty(index = 7)
    private String remark;
}
