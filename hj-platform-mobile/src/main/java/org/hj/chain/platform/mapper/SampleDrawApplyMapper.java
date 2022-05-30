package org.hj.chain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.model.SampleDrawApply;
import org.hj.chain.platform.vo.MobileSampleFactorVo;
import org.hj.chain.platform.vo.MobileSampleItemVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 样品出库申请：可以申请多个样品 Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface SampleDrawApplyMapper extends BaseMapper<SampleDrawApply> {

    IPage<MobileSampleItemVo> findSampleItemByCondition(IPage<MobileSampleItemVo> page,
                                                        @Param("organId") String organId);
}
