package org.hj.chain.platform.offer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.offer.entity.OfferPlanFactor;
import org.hj.chain.platform.vo.offer.OfferPlanFactorVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferPlanFactorMapper extends BaseMapper<OfferPlanFactor> {
    /**
     * TODO 根据报价单号查看报价因子机构关联关系
     *
     * @param offerId
     * @param organId
     * @Iteration : 3.0
     */
    List<OfferPlanFactorVo> findJudgeOfferFactorByOfferId(@Param("offerId") String offerId,  @Param("organId") String organId);

    List<OfferPlanFactorVo> getFactorsByOfferPlanId(Long offerPlanId);

    List<OfferPlanFactorVo> getFactorsByOfferPlanIds(@Param("list") List<Long> offerPlanIds);
}
