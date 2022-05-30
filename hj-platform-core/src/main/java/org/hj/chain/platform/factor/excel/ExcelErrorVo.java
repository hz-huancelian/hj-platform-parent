package org.hj.chain.platform.factor.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 错误信息
 * @Iteration : 1.0
 * @Date : 2021/5/12  5:03 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/12    create
 */
@Data
public class ExcelErrorVo implements Serializable {
    private static final long serialVersionUID = -1809246030832478733L;

    @ExcelProperty(value = "错误标示")
    private String errLine;

    @ExcelProperty(value = "错误描述")
    private String errDesc;
}