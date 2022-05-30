package org.hj.chain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.model.SampleItem;
import org.hj.chain.platform.vo.MobileSampleItemVo;
import org.hj.chain.platform.vo.SampleListVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 采集因子取样表 Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface SampleItemMapper extends BaseMapper<SampleItem> {

    List<MobileSampleItemVo> getSampleItemsBySampleTaskIdAndUserId(@Param("taskId") Long taskId, @Param("userId") String userId);

    List<SampleListVo> getSampleListByCondition(@Param("taskId") Long taskId,
                                                @Param("userId") String userId,
                                                @Param("sampleStatus") String sampleStatus);

    IPage<SampleListVo> getRejectedSampleList(IPage<SampleListVo> page,
                                             @Param("userId") String userId);

    IPage<SampleListVo> getSampleItemsForCollectUser(Page<SampleListVo> page,
                                                     @Param("userId") String userId,
                                                     @Param("sampleStatus") String sampleStatus);

    List<SampleItem> getSampItemsForCollectUserByTaskId(@Param("taskId") Long taskId,
                                                        @Param("userId") String userId);

    List<SampleListVo> getToReviewSampItemsForCollectUserByTaskId(@Param("taskId") Long taskId,
                                                                  @Param("userId") String userId);
}
