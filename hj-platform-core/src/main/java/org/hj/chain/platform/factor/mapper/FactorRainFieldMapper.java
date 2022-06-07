package org.hj.chain.platform.factor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.factor.entity.FactorRainField;
import org.hj.chain.platform.vo.factor.FactorRainFieldSearchVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : zzl
 * @description 二级分类
 * @Date : 2022.6.7
 */
@Repository
public interface FactorRainFieldMapper extends BaseMapper<FactorRainField> {

    /**
     * @author : zzl
     * @description 查询二级类别字段
     * @Date : 2022.6.7
     */
    List<FactorRainField> findFactorsClassInfoCondition(@Param("ff") FactorRainFieldSearchVo ff);

}
