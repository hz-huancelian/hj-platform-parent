package org.hj.chain.platform.approval.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.approval.service.IOfferApprovalService;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.tdo.approval.OfferJudgeTdo;
import org.hj.chain.platform.vo.approval.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报价单审批控制
 * @Iteration : 1.0
 * @Date : 2021/5/9  8:20 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/09    create
 */
@RestController
@RequestMapping("/approval/offer")
public class OfferApprovalController {
    @Autowired
    private IOfferApprovalService offerApprovalService;

    /**
     * TODO 分页查询审批信息
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/9 8:59 上午
     */
    @RequestMapping(value = "/findOfferByCondition", method = RequestMethod.GET)
    public Result<IPage<OfferApprovalVo>> findOfferByCondition(@ModelAttribute PageVo pageVo,
                                                               @ModelAttribute OfferApprovalSearchVo sv) {

        IPage<OfferApprovalVo> page = offerApprovalService.findOfferByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }


    /**
     * TODO 报价单审核
     *
     * @param offerId
     * @param isPass  0-通过；1-失败
     * @param remark
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/9 9:02 上午
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public Result<Object> check(@RequestParam String offerId,
                                @RequestParam Integer isPass,
                                @RequestParam String remark) {
        if (StrUtil.isBlank(offerId)) {
            return ResultUtil.validateError("报价单号不能为空！");
        }

        if (isPass == null) {
            return ResultUtil.validateError("审批状态不能为空！");
        }

        return offerApprovalService.check(offerId, isPass, remark);
    }


    /**
     * TODO 根据报价单号查看报价单审批记录
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/8 11:25 下午
     */
    @RequestMapping(value = "/findOfferApprovalRecordByOfferId/{offerId}", method = RequestMethod.GET)
    public Result<List<OfferApprovalRecordVo>> findOfferApprovalRecordByOfferId(@PathVariable String offerId) {

        if (StrUtil.isBlank(offerId)) {
            return ResultUtil.validateError("报价单号不能为空！");
        }
        return offerApprovalService.findOfferApprovalRecordByOfferId(offerId);
    }

    /**
     * TODO 分包判断查看
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/10 11:54 上午
     */
    @RequestMapping(value = "/findOfferJudgeByCondition", method = RequestMethod.GET)
    public Result<IPage<OfferJudgeApprovalVo>> findOfferJudgeByCondition(@ModelAttribute PageVo pageVo,
                                                                         @ModelAttribute OfferJudgeApprovalSearchVo sv) {

        IPage<OfferJudgeApprovalVo> page = offerApprovalService.findOfferJudgeByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }


    /**
     * TODO 分包
     *
     * @param judgeTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/11 10:48 下午
     */
    @RequestMapping(value = "/judge", method = RequestMethod.POST)
    public Result<Object> judge(@RequestBody OfferJudgeTdo judgeTdo) {
        return offerApprovalService.judge(judgeTdo);
    }

}