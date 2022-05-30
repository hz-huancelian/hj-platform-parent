package org.hj.chain.platform.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.contract.entity.CusContractBaseInfo;
import org.hj.chain.platform.tdo.contract.CusContBaseAddTdo;
import org.hj.chain.platform.tdo.contract.CusContBaseModifyTdo;
import org.hj.chain.platform.vo.contract.CusContBaseInfoListVo;
import org.hj.chain.platform.vo.contract.CusContBaseInfoVo;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
public interface ICusContractBaseInfoService extends IService<CusContractBaseInfo> {


    /**
     * TODO 客户合同基本信息新增
     *
     * @param addTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 11:03 上午
     */
    Long addCusContBase(CusContBaseAddTdo addTdo);


    /**
     * TODO 客户合同基本信息修改
     *
     * @param modifyTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 11:04 上午
     */
    Result<Object> modifyCusContBaseById(CusContBaseModifyTdo modifyTdo);

    /**
     * TODO 分页查询
     *
     * @param pageVo
     * @param companyName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 11:10 上午
     */
    IPage<CusContBaseInfoListVo> findByCondition(PageVo pageVo, String companyName);


    /**
     * TODO 根据主键ID查看详情
     *
     * @param id
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 11:11 上午
     */
    CusContBaseInfoVo findById(Long id);


    /**
     * TODO 根据公司名称模糊查询
     *
     * @param companyName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 11:13 上午
     */
    Map<Long, String> fuzzyQuery(String companyName);

    /**
     * todo 根据客户ID删除客户信息
     * @param id
     * @return
     */
    Result<Object> removeById(Long id);
}
