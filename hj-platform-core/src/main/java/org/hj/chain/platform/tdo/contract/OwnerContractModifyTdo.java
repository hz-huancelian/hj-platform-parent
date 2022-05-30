package org.hj.chain.platform.tdo.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerContractModifyTdo extends OwnerContractAddTdo implements Serializable {

    private static final long serialVersionUID = 1L;

    //主键ID
    private Long id;


}
