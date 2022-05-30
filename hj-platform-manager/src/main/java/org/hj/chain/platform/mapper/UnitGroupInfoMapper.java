package org.hj.chain.platform.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.model.UnitGroupInfo;
import org.springframework.stereotype.Repository;

@DS("platform")
@Repository
public interface UnitGroupInfoMapper extends BaseMapper<UnitGroupInfo> {
}
