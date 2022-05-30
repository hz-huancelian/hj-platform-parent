package org.hj.chain.platform.judge.mapper;

import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.judge.entity.JudgeRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface JudgeRecordMapper extends BaseMapper<JudgeRecord> {

    int selectCountForReview(@Param("organId") String organId,
                             @Param("userId") String userId);
}
