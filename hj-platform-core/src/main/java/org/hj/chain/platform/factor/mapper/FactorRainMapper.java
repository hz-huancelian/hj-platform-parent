package org.hj.chain.platform.factor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.factor.entity.FactorRain;
import org.hj.chain.platform.vo.factor.FactorRainSearchVo;
import org.hj.chain.platform.vo.factor.FactorRainVo;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author : zzl
 * @description 因子服务
 * @Date : 2022.5.31
 */
@Repository
public interface FactorRainMapper extends BaseMapper<FactorRain> {

    /**
     * @author : zzl
     * @description 查询雨水因子
     * @Date : 2022.6.1
     */
    List<FactorRainVo> findFactorsRainByCondition(@Param("organId") String organId,
                                                 @Param("fr") FactorRainSearchVo fr);

    /**
     * @author : zzl
     * @description 分页查询雨水因子
     * @Date : 2022.6.1
     */
    IPage<FactorRainVo> findFactorsRainByCondition(IPage<FactorRainVo> page,
                                                      @Param("organId") String organId,
                                                      @Param("fr") FactorRainSearchVo fr);

    /**
     * @author : zzl
     * @description 查询雨水表数据
     * @Date : 2022.6.1
     */
    List<FactorRain> findFactorsRainByIdCondition();

}
