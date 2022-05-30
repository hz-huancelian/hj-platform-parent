package org.hj.chain.platform.offer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.offer.entity.OfferInfo;
import org.hj.chain.platform.tdo.offer.OfferAddTdo;
import org.hj.chain.platform.tdo.offer.OfferModifyTdo;
import org.hj.chain.platform.vo.offer.*;
import org.hj.chain.platform.word.OfferTableData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
public interface IOfferInfoService extends IService<OfferInfo> {


    /**
     * TODO  分页查看保单信息
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:17 上午
     */
    IPage<OfferInfoVo> findOfferInfosByCondition(PageVo pageVo, OfferSearchVo sv);

    /**
     * TODO  分页查看历史保单信息
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:17 上午
     */
    IPage<OfferInfoVo> findHistoryOfferInfosByCondition(PageVo pageVo, OfferSearchVo sv);


    /**
     * TODO 新增报价单
     *
     * @param addTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:18 下午
     */
    Result<Object> saveOffer(OfferAddTdo addTdo);

    /**
     * TODO 修改报价单
     *
     * @param modifyTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:25 下午
     */
    Result<Object> modifyOfferByOfferId(OfferModifyTdo modifyTdo);


    /**
     * TODO 提交审核
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/10 12:47 上午
     */
    Result<Object> commitByOfferId(String offerId);


    /**
     * TODO  删除报价单
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:32 下午
     */
    Result<Object> delByOfferId(String offerId);


    /**
     * TODO 复制报价单
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:33 下午
     */
    Result<Object> copyOfferByOfferId(String offerId);

    /**
     * TODO 根据报价单查看详情信息
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/10 12:42 上午
     */
    Result<OfferDetailVo> findOfferDetailsByOfferId(String offerId);

    /**
     * TODO 根据报价单号查看报价单关联因子能力表
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 3:11 下午
     */
    List<JudgeOfferFactorVo> findJudgeOfferFactorVosByOfferId(String offerId);

    /**
     * TODO 根据报价单号查询检测因子
     *
     * @param offerId
     * @Author chh
     * @Date 2021-05-12 21:48
     * @Iteration 1.0
     */
    List<OfferFactorVo> getOfferFactorByOfferId(String offerId);

    /**
     * TODO 根据报价单号查询
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 3:38 下午
     */
    List<JudgeOfferFactorQryVo> findJudgeOfferFactorQryVoByOfferId(String offerId);

    /**
     * TODO 查询报价单下载内容信息
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/19 10:03 上午
     */
    OfferTableData findDownloadOfferInfoById(String offerId);


    /**
     * TODO excel下载信息
      * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/3/24 1:53 下午
     */
    Map<String,Object> findDownloadExcelOfferInfoById(String offerId);

    /**
     * TODO 查询监测计划下因子信息（按检测类别排序）
     * @param offerPlanId
     * @return
     */
    List<OfferPlanFactorVo> getFactorsByOfferPlanId(Long offerPlanId);

    /**
     * TODO 查询多个监测计划下因子信息（按检测类别排序）
     * @param offerPlanIds
     * @return
     */
    List<OfferPlanFactorVo> getFactorsByOfferPlanIds(List<Long> offerPlanIds);
}
