package org.hj.chain.platform.factor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.factor.entity.FactorRain;
import org.hj.chain.platform.vo.factor.FactorRainFieldSearchVo;
import org.hj.chain.platform.vo.factor.FactorRainFieldVo;
import org.hj.chain.platform.vo.factor.FactorRainSearchVo;
import org.hj.chain.platform.vo.factor.FactorRainVo;

import java.util.List;

public interface FactorRainService extends IService<FactorRain> {

    /**
     * @author : zzl
     * @description 分页查询雨水因子
     * @Date : 2022.6.1
     */
    IPage<FactorRainVo> findFactorRainByCondition(PageVo pageVo, FactorRainSearchVo fr);

    /**
     * @author : zzl
     * @description 查询二级类别字段
     * @Date : 2022.6.7
     */
    Result<List<FactorRainFieldVo>> findFactorClassInfoCondition(FactorRainFieldSearchVo ff);

}
