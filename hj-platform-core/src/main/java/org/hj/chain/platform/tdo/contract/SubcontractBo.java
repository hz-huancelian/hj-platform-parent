package org.hj.chain.platform.tdo.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 分包合同
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SubcontractBo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报价单ID
     */
    private String offerId;


    //分包机构ID
    private String judgeOrganId;


}
