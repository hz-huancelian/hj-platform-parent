package org.hj.chain.platform.region.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.region.entity.SysRegion;

import java.util.List;

public interface ISysRegionService extends IService<SysRegion> {

    Result<List<SysRegion>> getFirstLevel();
}
