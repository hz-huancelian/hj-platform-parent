package org.hj.chain.platform.offer.mapper;

import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.offer.entity.OfferFactorInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.vo.offer.OfferFactorVo;
import org.hj.chain.platform.vo.samplebak.SampleFactorIdParam;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 检测因子项 Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface OfferFactorInfoMapper extends BaseMapper<OfferFactorInfo> {

    List<OfferFactorVo> getOfferFactorByOfferId(String offerId);


    /**
     * TODO 根据报价单号查看报价因子机构关联关系
     *
     * @param offerId
     * @param organId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 3:58 下午
     */
    List<OfferFactorVo> findJudgeOfferFactorByOfferId(@Param("offerId") String offerId, @Param("organId") String organId);


    /**
     * TODO 根据样品ID查询以检测因子升序的集合
     *
     * @param sampleIds
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/18 4:09 下午
     */
    List<SampleFactorIdParam> findOrderSampleIdBySampleIds(@Param("list") List<String> sampleIds);

}
