package org.hj.chain.platform.dataimport.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(25)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EquipmentExportEntity implements Serializable {
    @ExcelProperty("设备编号")
    private String equipmentNumber;
    @ExcelProperty("设备名称")
    private String equipmentName;
    @ExcelProperty("设备类别")
    private String equipmentFirstType;
    @ExcelProperty("检测类别")
    private String equipmentSecondType;
    @ExcelProperty("设备品牌")
    private String equipmentBrand;
    @ExcelProperty("设备型号")
    private String equipmentModel;
    @ExcelProperty("鉴定周期")
    private String checkCircle;
    @ExcelProperty("备注")
    private String remark;
}
