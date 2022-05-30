package org.hj.chain.platform.offer.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.offer.service.IOfferInfoService;
import org.hj.chain.platform.tdo.offer.OfferAddTdo;
import org.hj.chain.platform.tdo.offer.OfferModifyTdo;
import org.hj.chain.platform.vo.offer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报单服务
 * @Iteration : 1.0
 * @Date : 2021/5/6  11:26 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/06    create
 */
@RestController
@RequestMapping("/offer")
public class OfferController {

    @Autowired
    private IOfferInfoService offerInfoService;


    /**
     * TODO 分页查询报价单列表
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:29 下午
     */
    @RequestMapping(value = "/findOfferInfosByCondition", method = RequestMethod.GET)
    public Result<IPage<OfferInfoVo>> findOfferInfosByCondition(@ModelAttribute PageVo pageVo,
                                                                @ModelAttribute OfferSearchVo sv) {
        List<String> offerStatus = Arrays.asList("0", "1", "2");
        sv.setId(StrUtil.trimToNull(sv.getId()))
                .setProjectName(StrUtil.trimToNull(sv.getProjectName()))
                .setStatus(StrUtil.trimToNull(sv.getStatus()));
        if (sv.getStatus() != null && !offerStatus.contains(sv.getStatus())) {
            return ResultUtil.validateError("报价单查询状态值只支持：草稿、待审核、审核未通过！");
        }
        IPage<OfferInfoVo> offerVos = offerInfoService.findOfferInfosByCondition(pageVo, sv);
        return ResultUtil.data(offerVos);
    }

    /**
     * TODO 分页查询历史报价单列表
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:29 下午
     */
    @RequestMapping(value = "/findHistoryOfferInfosByCondition", method = RequestMethod.GET)
    public Result<IPage<OfferInfoVo>> findHistoryOfferInfosByCondition(@ModelAttribute PageVo pageVo,
                                                                       @ModelAttribute OfferSearchVo sv) {
        List<String> offerStatus = Arrays.asList("3", "4");
        sv.setId(StrUtil.trimToNull(sv.getId()))
                .setProjectName(StrUtil.trimToNull(sv.getProjectName()))
                .setStatus(StrUtil.trimToNull(sv.getStatus()));
        if (sv.getStatus() != null && !offerStatus.contains(sv.getStatus())) {
            return ResultUtil.validateError("报价单查询状态值只支持：审核通过、作废！");
        }
        IPage<OfferInfoVo> offerVos = offerInfoService.findHistoryOfferInfosByCondition(pageVo, sv);
        return ResultUtil.data(offerVos);
    }


    /**
     * TODO 新增报价单信息
     *
     * @param addTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:31 下午
     */
    @RequestMapping(value = "/saveOffer", method = RequestMethod.POST)
    public Result<Object> saveOffer(@Validated @RequestBody OfferAddTdo addTdo) {

        return offerInfoService.saveOffer(addTdo);
    }

    /**
     * TODO 修改报价单
     *
     * @param modifyTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:32 下午
     */
    @RequestMapping(value = "/modifyOfferByOfferId", method = RequestMethod.POST)
    public Result<Object> modifyOfferByOfferId(@Validated @RequestBody OfferModifyTdo modifyTdo) {

        return offerInfoService.modifyOfferByOfferId(modifyTdo);
    }


    /**
     * TODO 根据报价单号删除报价单
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:35 下午
     */
    @RequestMapping(value = "/delByOfferId/{offerId}", method = RequestMethod.GET)
    public Result<Object> delByOfferId(@PathVariable String offerId) {
        if (StrUtil.isBlank(offerId)) {
            return ResultUtil.validateError("报价单ID不能为空！");
        }

        return offerInfoService.delByOfferId(offerId);
    }


    /**
     * TODO 提交审核
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/10 12:46 上午
     */
    @RequestMapping(value = "/commitByOfferId/{offerId}", method = RequestMethod.GET)
    public Result<Object> commitByOfferId(@PathVariable String offerId) {
        if (StrUtil.isBlank(offerId)) {
            return ResultUtil.validateError("报价单ID不能为空！");
        }

        return offerInfoService.commitByOfferId(offerId);
    }


    /**
     * TODO 复制报价单
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:37 下午
     */
    @RequestMapping(value = "/copyOfferByOfferId/{offerId}", method = RequestMethod.GET)
    public Result<Object> copyOfferByOfferId(@PathVariable String offerId) {
        if (StrUtil.isBlank(offerId)) {
            return ResultUtil.validateError("报价单ID不能为空！");
        }

        return offerInfoService.copyOfferByOfferId(offerId);
    }


    /**
     * TODO 根据报价单号查看报价单详情
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/10 12:41 上午
     */
    @RequestMapping(value = "/findOfferDetailsByOfferId/{offerId}", method = RequestMethod.GET)
    public Result<OfferDetailVo> findOfferDetailsByOfferId(@PathVariable String offerId) {
        if (StrUtil.isBlank(offerId)) {
            return ResultUtil.validateError("报价单ID不能为空！");
        }

        return offerInfoService.findOfferDetailsByOfferId(offerId);
    }

    /**
     * TODO 查询报价单中相关因子的能力表
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 3:17 下午
     */
    @RequestMapping(value = "/findJudgeOfferFactorVosByOfferId/{offerId}", method = RequestMethod.GET)
    public Result<List<JudgeOfferFactorVo>> findJudgeOfferFactorVosByOfferId(@PathVariable String offerId) {
        if (StrUtil.isBlank(offerId)) {
            return ResultUtil.validateError("报价单ID不能为空！");
        }

        List<JudgeOfferFactorVo> voList = offerInfoService.findJudgeOfferFactorVosByOfferId(offerId);
        return ResultUtil.data(voList);
    }


    /**
     * TODO 根据ID查看分包详情
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 3:38 下午
     */
    @RequestMapping(value = "/findJudgeOfferFactorQryVoByOfferId/{offerId}", method = RequestMethod.GET)
    public Result<List<JudgeOfferFactorQryVo>> findJudgeOfferFactorQryVoByOfferId(@PathVariable String offerId) {
        if (StrUtil.isBlank(offerId)) {
            return ResultUtil.validateError("报价单ID不能为空！");
        }

        List<JudgeOfferFactorQryVo> voList = offerInfoService.findJudgeOfferFactorQryVoByOfferId(offerId);
        return ResultUtil.data(voList);
    }

}