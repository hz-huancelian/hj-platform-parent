package org.hj.chain.platform.vo.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ValidContractForPass11MonthVo implements Serializable {
    private static final long serialVersionUID = -7304699473300983590L;
    /**
     * 合同数
     */
    private Integer contactNum;

    /**
     * 合同总额
     */
    private BigDecimal contactMoney;

    /**
     * 月份
     */
    private String xNameData;
}
