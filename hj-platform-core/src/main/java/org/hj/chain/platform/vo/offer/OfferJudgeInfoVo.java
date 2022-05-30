package org.hj.chain.platform.vo.offer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报价单外包因子
 * @Iteration : 1.0
 * @Date : 2021/9/7  7:36 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/09/07    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OfferJudgeInfoVo implements Serializable {
    private static final long serialVersionUID = -7093283907780313813L;
    //检测因子ID
    private String checkStandardId;

    //二级类别
    private String secdClassId;

    //分包机构名称
    private String judgeOrganName;
}