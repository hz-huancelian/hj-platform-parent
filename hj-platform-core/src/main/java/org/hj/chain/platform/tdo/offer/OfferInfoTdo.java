package org.hj.chain.platform.tdo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class OfferInfoTdo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 0-委托检测；1-样品送检
     */
    private String checkType;

    /**
     * 认证类型（1：CMA 2：CNAS）
     */
    private String certificationType;

    /**
     * 委托单位
     */
    private String consignorName;

    /**
     * 委托联系人
     */
    private String consignorLinker;

    /**
     * 委托联系方式
     */
    private String consignorLinkerPhone;

    /**
     * 完成日期    yyyy-MM-dd
     */
    private String finishDate;


    //备注
    private String remark;

    //说明
    private String explains;


}
