package org.hj.chain.platform.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.model.FactorMethodInfo;
import org.hj.chain.platform.vo.FactorMethodRelVo;
import org.springframework.stereotype.Repository;

@DS("platform")
@Repository
public interface FactorMethodInfoMapper extends BaseMapper<FactorMethodInfo> {

    /**
     * TODO 分页查询
     *
     * @param page
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 1:11 下午
     */
    IPage<FactorMethodRelVo> findPage(IPage<FactorMethodRelVo> page);
}
