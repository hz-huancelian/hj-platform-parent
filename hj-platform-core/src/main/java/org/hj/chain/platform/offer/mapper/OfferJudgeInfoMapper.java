package org.hj.chain.platform.offer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.offer.entity.OfferJudgeInfo;
import org.hj.chain.platform.vo.approval.OfferJudgeApprovalSearchVo;
import org.hj.chain.platform.vo.approval.OfferJudgeApprovalVo;
import org.hj.chain.platform.vo.offer.OfferJudgeInfoVo;
import org.hj.chain.platform.vo.statistics.OfferJudgeTaskStatisVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 因子检测分包信息 Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface OfferJudgeInfoMapper extends BaseMapper<OfferJudgeInfo> {


    /**
     * TODO 分包判断列表
     *
     * @param page
     * @param organId
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/10 11:46 上午
     */
    IPage<OfferJudgeApprovalVo> findOfferJudgeApprovalsByCondition(IPage<OfferJudgeApprovalVo> page, @Param("organId") String organId, @Param("sv") OfferJudgeApprovalSearchVo sv);

    /**
     * todo 待处理分包判断数
     *
     * @return
     * @Param organId
     */
    int pendingOfferJudgeCnt(String organId);

    OfferJudgeTaskStatisVo offerJudgeCntForCurrMonth(String organId);

    OfferJudgeTaskStatisVo offerJudgeCntForCurrYear(String organId);

    /**
     * TODO 报价单外包因子信息集合
     *
     * @param offerId
     * @param organId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/9/7 7:40 下午
     */
    List<OfferJudgeInfoVo> findOfferJudgeInfoByCondition(@Param("offerId") String offerId, @Param("organId") String organId);

    List<OfferJudgeInfo> selectListByOrganId(@Param("organId") String organId,@Param("type") Integer type);
}
