package org.hj.chain.platform.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.model.UnitGroupRelatedInfo;
import org.hj.chain.platform.vo.UnitInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@DS("platform")
@Repository
public interface UnitGroupRelatedInfoMapper extends BaseMapper<UnitGroupRelatedInfo> {


    /**
     * TODO 根据单位分组ID查看关联的单位列表
     *
     * @param groupId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 1:22 上午
     */
    List<UnitInfoVo> findUnitListByGroupId(String groupId);
}
