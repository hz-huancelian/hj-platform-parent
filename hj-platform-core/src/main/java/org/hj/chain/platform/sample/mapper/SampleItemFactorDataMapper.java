package org.hj.chain.platform.sample.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.sample.entity.SampleItemFactorData;
import org.hj.chain.platform.vo.sample.SampleFactorDataParam;
import org.hj.chain.platform.vo.sample.SampleFactorDataVo;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SampleItemFactorDataMapper extends BaseMapper<SampleItemFactorData> {
    List<SampleFactorDataVo> getSampleFactorDataBySampItemId(Long sampItemId);


    /**
     * TODO 根据样品编号查询因子数据信息
     *
     * @param sampleNos
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/29 2:28 下午
     */
    List<SampleFactorDataParam> findFactorDataDetailsBySampleNos(@Param("list") List<String> sampleNos,
                                                                 @Param("organId") String organId);
}
