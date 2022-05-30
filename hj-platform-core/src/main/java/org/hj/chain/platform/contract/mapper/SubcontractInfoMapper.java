package org.hj.chain.platform.contract.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.contract.entity.SubcontractInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.vo.contract.ContractSearchVo;
import org.hj.chain.platform.vo.contract.SubcontractVo;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface SubcontractInfoMapper extends BaseMapper<SubcontractInfo> {


    /**
     * TODO 分页查询分包合同列表
     *
     * @param page
     * @param organId
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/15 11:43 上午
     */
    IPage<SubcontractVo> findPageByCondition(IPage<SubcontractVo> page,
                                             @Param("organId") String organId,
                                             @Param("sv") ContractSearchVo sv);

}
