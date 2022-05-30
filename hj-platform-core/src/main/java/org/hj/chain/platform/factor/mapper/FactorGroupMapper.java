package org.hj.chain.platform.factor.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.factor.entity.FactorGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.vo.factor.FactorGroupVo;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

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
public interface FactorGroupMapper extends BaseMapper<FactorGroup> {


    /**
     * TODO 分页查询因子套餐
     *
     * @param page
     * @param groupName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 9:01 上午
     */
    IPage<FactorGroupVo> findPageByCondition(IPage<FactorGroupVo> page,
                                             @Param("organId") String organId,
                                             @Param("createBy") String createBy,
                                             @Param("groupName") String groupName,
                                             @Param("groupType") String groupType,
                                             @Param("authType") String authType);


    /**
     * TODO 根据分组类型查看因子套餐列表
     *
     * @param groupType
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/3/15 9:26 上午
     */
    List<FactorGroupVo> findListByGroupType(@Param("groupType") String groupType,
                                            @Param("username") String username);

}
