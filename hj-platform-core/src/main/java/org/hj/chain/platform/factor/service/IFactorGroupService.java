package org.hj.chain.platform.factor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.factor.entity.FactorGroup;
import org.hj.chain.platform.tdo.factor.FactorGroupAddTdo;
import org.hj.chain.platform.tdo.factor.FactorGroupModifyTdo;
import org.hj.chain.platform.vo.factor.FactorCheckStandardVo;
import org.hj.chain.platform.vo.factor.FactorGroupItemVo;
import org.hj.chain.platform.vo.factor.FactorGroupVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
public interface IFactorGroupService extends IService<FactorGroup> {


    /**
     * TODO 分页查询套餐因子
     *
     * @param pageVo
     * @param groupName
     * @param authType
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 8:52 上午
     */
    IPage<FactorGroupVo> findPageByCondition(PageVo pageVo, String groupName, String groupType, String authType);

    /**
     * TODO 查寻同系或分组因子套餐
     *
     * @param groupType
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/3/15 9:16 上午
     */
    List<FactorGroupVo> findList(String groupType);

    /**
     * TODO 新增
     *
     * @param addTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:46 下午
     */
    Result<Object> saveFactorGroup(FactorGroupAddTdo addTdo);


    /**
     * TODO 修改
     *
     * @param modifyTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:47 下午
     */
    Result<Object> modifyFactorGroupByGroupId(FactorGroupModifyTdo modifyTdo);


    /**
     * TODO 根据套餐ID查看套餐列表
     *
     * @param groupId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:48 下午
     */
    List<FactorGroupItemVo> findDetailByGroupId(Long groupId);

    /**
     * TODO 根据套餐ID查看套餐列表
     *
     * @param groupId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:48 下午
     */
    IPage<FactorGroupItemVo> findDetailByGroupId(PageVo pageVo, Long groupId);


    /**
     * TODO 流式查询
     *
     * @param groupId
     * @param limit
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/3/15 9:58 上午
     */
    void scan(Long groupId, int limit) throws Exception;


    /**
     * TODO 删除因子套餐
     *
     * @param groupId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 11:34 上午
     */
    Result<Object> delByGroupId(Long groupId);

    /**
     * TODO 查询列表信息
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/16 7:49 下午
     */
    List<FactorGroupVo> findList();

    /**
     * TODO 套餐复制
     *
     * @param groupId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/3/6 2:06 下午
     */
    Result<Object> copyByGroupId(Long groupId);
}
