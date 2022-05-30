package org.hj.chain.platform.offer.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.offer.entity.OfferInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.vo.approval.OfferApprovalSearchVo;
import org.hj.chain.platform.vo.approval.OfferApprovalVo;
import org.hj.chain.platform.vo.offer.OfferContVo;
import org.hj.chain.platform.vo.offer.OfferInfoVo;
import org.hj.chain.platform.vo.offer.OfferSearchVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface OfferInfoMapper extends BaseMapper<OfferInfo> {

    /**
     * TODO 分页查询报价单列表
     *
     * @param page
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 4:14 下午
     */
    IPage<OfferInfoVo> findOfferInfosByCondition(IPage<OfferInfoVo> page,
                                                 @Param("organId") String organId,
                                                 @Param("deptIds") List<Long> deptIds,
                                                 @Param("userId") String userId,
                                                 @Param("sv") OfferSearchVo sv);

    /**
     * TODO 分页查询历史报价单列表
     *
     * @param page
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 4:14 下午
     */
    IPage<OfferInfoVo> findHistoryOfferInfosByCondition(IPage<OfferInfoVo> page,
                                                 @Param("organId") String organId,
                                                 @Param("deptIds") List<Long> deptIds,
                                                 @Param("userId") String userId,
                                                 @Param("sv") OfferSearchVo sv);


    /**
     * TODO 查看审批列表信息
     *
     * @param page
     * @param organId
     * @param deptIds
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/9 9:06 上午
     */
    IPage<OfferApprovalVo> findOfferApprovalsByCondition(IPage<OfferApprovalVo> page,
                                                         @Param("organId") String organId,
                                                         @Param("deptIds") List<Long> deptIds,
                                                         @Param("userId") String userId,
                                                         @Param("sv") OfferApprovalSearchVo sv);


    /**
     * TODO 查询合同报单相关信息
     *
     * @param offerId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/15 5:26 下午
     */
    OfferContVo findOfferContInfoByOfferId(@Param("offerId") String offerId);
}
