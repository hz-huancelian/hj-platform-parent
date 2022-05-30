package org.hj.chain.platform.factor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.factor.entity.FactorCheckStandard;
import org.hj.chain.platform.tdo.factor.FactorCheckStandardModifyTdo;
import org.hj.chain.platform.vo.factor.FactorCheckStandardVo;
import org.hj.chain.platform.vo.factor.FactorSearchVo;

/**
 * <p>
 * 因子检测能力、费用表 服务类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
public interface IFactorCheckStandardService extends IService<FactorCheckStandard> {


    /**
     * TODO 条件查询检测信息列表
     *
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/5 11:23 下午
     */
    IPage<FactorCheckStandardVo> findCheckStandardsByCondition(PageVo pageVo, FactorSearchVo sv);


    /**
     * TODO 修改能量表
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 12:05 上午
     */
    Result<Object> modifyCheckStandardById(FactorCheckStandardModifyTdo tdo);


}
