package org.hj.chain.platform.contract.service;

import org.hj.chain.platform.Result;
import org.hj.chain.platform.contract.entity.OwnerContractBaseInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.tdo.contract.OwnerContractAddTdo;
import org.hj.chain.platform.tdo.contract.OwnerContractModifyTdo;
import org.hj.chain.platform.vo.contract.OwnerContBaseInfoVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
public interface IOwnerContractBaseInfoService extends IService<OwnerContractBaseInfo> {

    /**
     * TODO 合同添加
     *
     * @param addTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/21 4:41 下午
     */
    Result<Object> addCont(OwnerContractAddTdo addTdo);


    /**
     * TODO 合同修改
     *
     * @param modifyTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/21 4:42 下午
     */
    Result<Object> modifyContById(OwnerContractModifyTdo modifyTdo);


    /**
     * TODO 通过ID查看
     *
     * @param id
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/21 4:43 下午
     */
    OwnerContBaseInfoVo findById(Long id);


    /**
     * TODO 通过机构ID查看
     *
     * @param organId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/21 4:43 下午
     */
    OwnerContBaseInfoVo findByOrganId(String organId);

}
