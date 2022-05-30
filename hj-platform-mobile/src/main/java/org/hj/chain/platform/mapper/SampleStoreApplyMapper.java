package org.hj.chain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.model.SampleStoreApply;
import org.hj.chain.platform.vo.MobileSampleItemVo;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 样品入库申请 Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface SampleStoreApplyMapper extends BaseMapper<SampleStoreApply> {

    IPage<MobileSampleItemVo> findSampleItemByCondition(IPage<MobileSampleItemVo> page,
                                                        @Param("organId") String organId);
}
