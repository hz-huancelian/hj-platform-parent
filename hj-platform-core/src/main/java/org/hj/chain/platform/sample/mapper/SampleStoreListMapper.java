package org.hj.chain.platform.sample.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.sample.entity.SampleStoreList;
import org.hj.chain.platform.vo.samplebak.HandoverParam;
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
public interface SampleStoreListMapper extends BaseMapper<SampleStoreList> {


    /**
     * TODO 根据样品编号获取委托单位和入库信息
     *
     * @param sampleItemNo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/7 6:34 下午
     */
    HandoverParam findHandoverParamBySampNo(@Param("sampleItemNo") String sampleItemNo,
                                            @Param("organId") String organId);

    /**
     * TODO 根据样品编号集合获取委托单位和入库信息
     *
     * @param sampleItemNos
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/5/20 5:51 下午
     */
    List<HandoverParam> findHandoverParamBySampNos(@Param("list") List<String> sampleItemNos,
                                                   @Param("organId") String organId);

}
