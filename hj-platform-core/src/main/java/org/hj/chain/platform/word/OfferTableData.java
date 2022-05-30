package org.hj.chain.platform.word;

import com.deepoove.poi.expression.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/17  9:34 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/17    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OfferTableData {

    //报价单ID
    private String offerId;

    //机构名称
    private String organName;

    //乙方
    private String ownerOrganName;

    //客户名称
    private String consignorName;

    //报价人
    private String createUserName;

    //联系人
    private String consignorLinker;

    //联系电话
    private String consignorLinkerPhone;

    //检测类型
    private String checkType;

    //报价时间
    private String offerTime;

    //检测费用
    private String checkAmount;

    //报告编制费
    private String reportAmount;

    //报告加急费
    private String expediteAmount;

    //采样费
    private String laborAmount;

    //差旅费
    private String tripAmount;

    //税额
    private String taxAmount;

    //总计
    private String sysAmount;

    //优惠价
    private String draftAmount;

    //说明
    private String explains;

    @Name("offer_table")
    private FactorData factorData;

    //大写金额
    private String capitalAmount;
}