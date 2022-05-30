package org.hj.chain.platform.factor.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 导入vo
 * @Iteration : 1.0
 * @Date : 2021/5/7  4:40 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/07    create
 */
@Data
public class FactorCheckImportVo implements Serializable {
    private static final long serialVersionUID = 4422768786895150610L;

    //检测编号
    @ExcelProperty(value = "检测编号")
    private String standardCode;
    /**
     * CMA能力(0:无 1:有)
     */
    @ExcelProperty(value = "CMA")
    private String cmaFlg;

    /**
     * CNAS能力(0:无 1:有)
     */
    @ExcelProperty(value = "CNAS")
    private String cnasFlg;


    //检测费用
    @ExcelProperty(value = "检测费(元)")
    private BigDecimal price;
}