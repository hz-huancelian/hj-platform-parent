package org.hj.chain.platform.factor.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.factor.entity.FactorCheckStandard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.vo.factor.FactorCheckStandardVo;
import org.hj.chain.platform.vo.factor.FactorSearchVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 因子检测能力、费用表 Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface FactorCheckStandardMapper extends BaseMapper<FactorCheckStandard> {


    /**
     * TODO 条件查询检测标准列表
     *
     * @param page
     * @param organId
     * @param checkStandardIds
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 12:32 上午
     */
    IPage<FactorCheckStandardVo> findCheckStandardsByCondition(IPage<FactorCheckStandardVo> page,
                                                               @Param("organId") String organId,
                                                               @Param("checkStandardIds") List<String> checkStandardIds,
                                                               @Param("sv") FactorSearchVo sv);

}
