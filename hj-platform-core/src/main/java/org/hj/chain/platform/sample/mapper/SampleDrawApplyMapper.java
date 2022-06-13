package org.hj.chain.platform.sample.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.sample.entity.SampleDrawApply;
import org.hj.chain.platform.vo.sample.SampleSearchVo;
import org.hj.chain.platform.vo.sample.SampleVo;
import org.springframework.stereotype.Repository;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Repository
public interface SampleDrawApplyMapper extends BaseMapper<SampleDrawApply> {
    Page<SampleVo> getSampDrawApplyList(Page<SampleVo> page, @Param("organId") String organId, @Param("sv") SampleSearchVo sv);

    Integer selectCountToDrawSample(String organId);

    Integer selectCountByOrganIdForCurrMonth(String organId);

    Integer selectCountByOrganIdForCurrYear(String organId);
}
