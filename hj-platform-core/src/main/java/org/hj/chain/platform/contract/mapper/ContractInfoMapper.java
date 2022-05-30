package org.hj.chain.platform.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.contract.entity.ContractInfo;
import org.hj.chain.platform.vo.contract.ContractInfoVo;
import org.hj.chain.platform.vo.contract.ContractSearchVo;
import org.hj.chain.platform.vo.statistics.ValidContractForPass11MonthVo;
import org.hj.chain.platform.vo.statistics.ValidContractVo;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface ContractInfoMapper extends BaseMapper<ContractInfo> {


    /**
     * TODO 分页查询
     *
     * @param page
     * @param organId
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 11:41 下午
     */
    IPage<ContractInfoVo> findPageByCondition(IPage<ContractInfoVo> page,
                                              @Param("organId") String organId,
                                              @Param("deptIds") List<Long> deptIds,
                                              @Param("userId") String userId,
                                              @Param("sv") ContractSearchVo sv);

    ValidContractVo validContractsForCurrMonth(String organId);

    ValidContractVo validContractsForCurrYear(String organId);

    List<ValidContractForPass11MonthVo> validContractsForPass11Month(String organId);

    int selectCountForJudge(String organId);

    List<ValidContractForPass11MonthVo> ownerValidContractsForPass11Month(@Param("organId") String organId,
                                                                          @Param("userId") String userId);

    List<Map<String, Object>> sortContAmountForCurrMonth(String organId);

    List<Map<String, Object>> sortContAmountForCurrYear(String organId);

    List<Map<String, Object>> sortContNumForCurrMonth(String organId);

    List<Map<String, Object>> sortContNumForCurrYear(String organId);
}
