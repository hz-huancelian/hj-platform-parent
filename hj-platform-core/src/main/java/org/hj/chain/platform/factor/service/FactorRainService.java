package org.hj.chain.platform.factor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.factor.entity.FactorRain;
import org.hj.chain.platform.model.FactorClassInfo;
import org.hj.chain.platform.vo.factor.FactorRainSearchVo;
import org.hj.chain.platform.vo.factor.FactorRainVo;

public interface FactorRainService extends IService<FactorRain> {

    /**
     * @author : zzl
     * @description 分页查询雨水因子
     * @Date : 2022.6.1
     */
    IPage<FactorRainVo> findFactorRainByCondition(PageVo pageVo, FactorRainSearchVo fr);

    /**
     * @author : zzl
     * @description 分页查询采样类别
     * @Date : 2022.6.2
     */
    IPage<FactorClassInfo> findFactorClassInfoCondition(PageVo pageVo);

}
