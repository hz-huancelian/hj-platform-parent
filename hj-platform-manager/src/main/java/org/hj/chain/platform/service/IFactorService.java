package org.hj.chain.platform.service;

import org.hj.chain.platform.model.FactorMethodSubfactorInfo;
import org.hj.chain.platform.vo.*;

import java.util.List;

public interface IFactorService {


    /**
     * TODO 根据检测标准ID查看检测标准
     *
     * @param id
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/5 11:01 下午
     */
    FactorMethodInfoVo findFactorMethodById(String id);


    /**
     * TODO 根据因子ID查看因子关联信息
     *
     * @param factorId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 12:51 下午
     */
    FactorInfoVo findFactorInfoByFactorId(String factorId);


    /**
     * TODO 查找所有一级分类
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/5 11:07 下午
     */
    List<FactorClassInfoVo> findFstClass();


    /**
     * TODO 选择一级分类下的二级分类
     *
     * @param fstClassId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/5 11:08 下午
     */
    List<FactorClassInfoVo> findSecdClassByClassId(String fstClassId);

    /**
      * TODO 根据检测标准ID查看子因子信息
      * @Author chh
      * @param id
      * @Date 2021-05-15 21:25
      * @Iteration 1.0
      */
    List<FactorMethodSubfactorInfo> findSebFactorById(String id);

    List<FactorClassInfoVo> getAllFactorSecClass();
}
