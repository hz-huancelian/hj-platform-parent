package org.hj.chain.platform.sample.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.sample.entity.SampleItemData;
import org.hj.chain.platform.vo.sample.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Repository
public interface SampleItemDataMapper extends BaseMapper<SampleItemData> {

    SampleDataVo getSampleDataBySampItemId(Long sampItemId);

    SampleDetailVo getSampleDetailBySampItemId(Long sampItemId);

    /**
     * TODO 根据样品编号获取相关样品基础数据
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/7 5:57 下午
     */
    List<SampleDataParam> findSampleDatasBySampleNos(@Param("list") List<String> sampleNos,
                                                     @Param("organId") String organId);

    /**
     * TODO 根据样品编号获取相关样品详细数据
     *
     * @param sampleNos
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/8 2:39 下午
     */
    List<SampleDataDetailParam> findSampleDataDetailsBySampleNos(@Param("list") List<String> sampleNos,
                                                                 @Param("organId") String organId);

    SampleItemDetailVo getSampleDetailBySampleItemId(Long sampItemId);
}
