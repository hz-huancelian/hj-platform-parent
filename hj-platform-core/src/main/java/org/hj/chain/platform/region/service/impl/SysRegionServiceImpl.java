package org.hj.chain.platform.region.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.region.entity.SysRegion;
import org.hj.chain.platform.region.mapper.SysRegionMapper;
import org.hj.chain.platform.region.service.ISysRegionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-13
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-13
 */
@Service
public class SysRegionServiceImpl extends ServiceImpl<SysRegionMapper, SysRegion> implements ISysRegionService {

    @Override
    public Result<List<SysRegion>> getFirstLevel() {
        List<SysRegion> list = this.baseMapper.selectList(Wrappers.<SysRegion>lambdaQuery()
                .select(SysRegion::getRegionCode, SysRegion::getRegionName)
                .eq(SysRegion::getLevel, 1));
        return ResultUtil.data(list);
    }
}
