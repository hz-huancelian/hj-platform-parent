package org.hj.chain.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.tdo.MobileSampleStoreTdo;
import org.hj.chain.platform.vo.MobileSampleItemVo;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-10
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-10
 */
public interface ISampleManagerService {
    IPage<MobileSampleItemVo> findSampleItemByCondition(PageVo pageVo, String sampStatus);

    Result<Object> storeSample(MobileSampleStoreTdo tdo);

    Result<Object> drawSample(String drawApplyId);
}
