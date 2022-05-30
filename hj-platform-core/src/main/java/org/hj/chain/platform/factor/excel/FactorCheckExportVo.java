package org.hj.chain.platform.factor.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 导出vo
 * @Iteration : 1.0
 * @Date : 2021/5/7  4:40 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/07    create
 */
@Data
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(15)
public class FactorCheckExportVo implements Serializable {
    private static final long serialVersionUID = 4422768786895150610L;

    //检测编号
    @ExcelProperty(value = "检测编号")
    private String standardCode;

    //检测对象
    @ExcelProperty(value = "检测对象")
    private String checkClass;

    //检测因子
    @ExcelProperty(value = "检测因子")
    private String factorName;

    //标准检测名称
    @ExcelProperty(value = "标准号")
    private String standardNo;

    //检测标准
    @ExcelProperty(value = "检测标准")
    @ColumnWidth(100)
    private String standardName;

    //标准分析方法
    @ExcelProperty(value = "分析方法")
    private String analysisMethod;

    //方法状态0-现行，1-废弃
    @ExcelProperty(value = "方法状态")
    private String methodStatus;

    @ExcelProperty(value = "因子单位")
    private String factorUnit;

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