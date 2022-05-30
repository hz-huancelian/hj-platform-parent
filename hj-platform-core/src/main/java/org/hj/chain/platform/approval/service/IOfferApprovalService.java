package org.hj.chain.platform.approval.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.tdo.approval.OfferJudgeTdo;
import org.hj.chain.platform.vo.approval.*;

import java.util.List;

public interface IOfferApprovalService {


    /**
     * TODO 分页查询报价单
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/9 8:50 上午
     */
    IPage<OfferApprovalVo> findOfferByCondition(PageVo pageVo, OfferApprovalSearchVo sv);


    /**
     * TODO 审核 ：0-通过；1-失败
     *
     * @param offerId
     * @param isPass
     * @param remark
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/9 8:53 上午
     */
    Result<Object> check(String offerId, Integer isPass, String remark);


    /**
     * TODO 根据报价单号查看报价单审批信息
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/8 11:16 下午
     */
    Result<List<OfferApprovalRecordVo>> findOfferApprovalRecordByOfferId(String offerId);


    /**
     * TODO 分包判断列表
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/10 11:47 上午
     */
    IPage<OfferJudgeApprovalVo> findOfferJudgeByCondition(PageVo pageVo, OfferJudgeApprovalSearchVo sv);

    /**
     * TODO 分包
     *
     * @param judgeTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/11 10:50 下午
     */
    Result<Object> judge(OfferJudgeTdo judgeTdo);
}
