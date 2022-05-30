package org.hj.chain.platform.word;

import com.deepoove.poi.data.DocxRenderData;
import com.deepoove.poi.data.PictureRenderData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hj.chain.platform.vo.contract.CusContBaseInfoVo;
import org.hj.chain.platform.vo.contract.OwnerContBaseInfoVo;

import java.math.BigDecimal;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/18  2:40 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/18    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ContractData {

    //合同编号
    private String contCode;

    //报价单号
    private String offerId;

    //合同控制号
    private String contControlId;


    //公司LOGO
    private PictureRenderData organLogo;

    /**
     * 签定时间
     */
    private String signDate;

    /**
     * 签定地点
     */
    private String signLocation;

    //支付方式
    private String payMethod;

    //支付描述（一次）
    private String payDesc1;
    //支付描述（分期）
    private String payDesc2;
    //支付描述（其他）
    private String payDesc3;

    //小写
    private String smallTotalAmount;

    //总金额(大写)
    private String totalAmount;


    //乙方信息
    private OwnerContBaseInfoVo partB;

    //甲方
    private CusContBaseInfoVo partA;

    //共几年
    private String totalYear;

    //时间周期
    private String datePeriod;

    //开始年
    private String startYear;
    //开始月
    private String startMonth;
    //开始日
    private String startDay;
    //结束年
    private String endYear;
    //结束月
    private String endMonth;
    //结束日
    private String endDay;

    //外包信息
    private String judgeInfo;
    //报价单
    private DocxRenderData offerTable;
}