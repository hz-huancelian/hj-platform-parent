package org.hj.chain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.model.CheckFactorInfo;
import org.hj.chain.platform.vo.MobileCheckFactorInfoVo;
import org.hj.chain.platform.vo.MobileSampleFactorVo;
import org.hj.chain.platform.vo.MobileSampleItemVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface CheckFactorInfoMapper extends BaseMapper<CheckFactorInfo> {

    IPage<MobileSampleItemVo> getCheckFactorByIds(IPage<MobileCheckFactorInfoVo> page,
                                                  @Param("list") List<Long> list);
}
