package org.hj.chain.platform.sample.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.sample.entity.SampleStoreApply;
import org.hj.chain.platform.vo.sample.SampleSearchVo;
import org.hj.chain.platform.vo.sample.SampleVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/6
 */
@Repository
public interface SampleStoreApplyMapper extends BaseMapper<SampleStoreApply> {

    Page<SampleVo> getSampStoreApplyList(Page<SampleVo> page, @Param("organId") String organId,
                                         @Param("sv") SampleSearchVo sv);


    /**
     * TODO 根据样品编号获取申请人信息
     *
     * @param sampleNos
     * @param organId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/5/20 6:06 下午
     */
    List<String> findApplyUsersBySampleNos(@Param("list") List<String> sampleNos,
                                           @Param("organId") String organId);
}
