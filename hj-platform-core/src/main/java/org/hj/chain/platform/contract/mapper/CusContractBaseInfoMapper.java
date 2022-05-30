package org.hj.chain.platform.contract.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.contract.entity.CusContractBaseInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.vo.contract.CusContBaseInfoListVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface CusContractBaseInfoMapper extends BaseMapper<CusContractBaseInfo> {

    /**
     * TODO 分页查询
     *
     * @param page
     * @param organId
     * @param companyName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 11:31 上午
     */
    IPage<CusContBaseInfoListVo> findPageByCondition(IPage<CusContBaseInfoListVo> page,
                                                     @Param("organId") String organId,
                                                     @Param("deptIds") List<Long> deptIds,
                                                     @Param("userId") String userId,
                                                     @Param("companyName") String companyName);

    int newCusCountForCurrMonth(String organId);

    int newCusCountForCurrYear(String organId);
}
