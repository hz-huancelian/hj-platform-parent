package org.hj.chain.platform.vo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 保单查询信息
 * @Iteration : 1.0
 * @Date : 2021/5/6  11:13 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/06    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OfferSearchVo implements Serializable {
    private static final long serialVersionUID = 5211922420617359489L;

    //报价单号
    private String id;

    /**
     * 项目名称
     */
    private String projectName;

    //报价单状态
    private String status;
}