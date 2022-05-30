package org.hj.chain.platform.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.model.PlatformUser;
import org.springframework.stereotype.Repository;

@DS("platform")
@Repository
public interface PlatformUserMapper extends BaseMapper<PlatformUser> {
}
