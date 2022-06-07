package org.hj.chain.platform.factor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.factor.entity.FactorRainField;
import org.hj.chain.platform.model.FactorClassInfo;
import org.hj.chain.platform.vo.factor.FactorRainFieldSearchVo;
import org.hj.chain.platform.fileresource.entity.FileResource;
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
    List<FactorRainField> findFactorsClassInfoCondition(@Param("secdClassId") String secdClassId);

    /**
     * @author : zzl
     * @description 查询文件名称
     * @Date : 2022.6.7
     */
    FileResource findClassInfoFileCondition(@Param("ff") FactorRainFieldSearchVo ff, @Param("organId") String organId);

    /**
     * @author : zzl
     * @description 查询分类id
     * @Date : 2022.6.7
     */
    FactorClassInfo findFactorClassIdInfoCondition(@Param("fileName") String fileName);

}
