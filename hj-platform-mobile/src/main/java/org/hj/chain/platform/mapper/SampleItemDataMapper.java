package org.hj.chain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.model.SampleItemData;
import org.hj.chain.platform.vo.MobileSampleItemDetailVo;
import org.springframework.stereotype.Repository;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Repository
public interface SampleItemDataMapper extends BaseMapper<SampleItemData> {

    MobileSampleItemDetailVo getSampleDetailBySampleItemId(Long sampItemId);
}
