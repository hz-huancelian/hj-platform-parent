package org.hj.chain.platform.factor.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;
import org.hj.chain.platform.factor.entity.FactorGroupItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface FactorGroupItemMapper extends BaseMapper<FactorGroupItem> {

    /**
     * TODO 游标查询
     *
     * @param limit
     * @param groupId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/3/15 9:47 上午
     */
    Cursor<FactorGroupItem> scan(@Param("limit") int limit, @Param("groupId") Long groupId);

}
