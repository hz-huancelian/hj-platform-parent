package org.hj.chain.platform.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.model.FactorClassInfo;
import org.springframework.stereotype.Repository;

@DS("platform")
@Repository
public interface FactorClassInfoMapper extends BaseMapper<FactorClassInfo> {
}
