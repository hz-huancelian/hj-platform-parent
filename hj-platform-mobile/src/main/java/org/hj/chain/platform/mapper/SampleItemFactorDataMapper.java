package org.hj.chain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.model.SampleItemFactorData;
import org.hj.chain.platform.vo.SampleFactorDataVo;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SampleItemFactorDataMapper extends BaseMapper<SampleItemFactorData> {
    List<SampleFactorDataVo> getSampleFactorDataBySampItemId(Long sampItemId);
}
