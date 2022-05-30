package org.hj.chain.platform.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.model.Organ;
import org.hj.chain.platform.vo.OrganSearchVo;
import org.hj.chain.platform.vo.OrganVo;
import org.springframework.stereotype.Repository;

@DS("platform")
@Repository
public interface OrganMapper extends BaseMapper<Organ> {


    /**
     * TODO 分页查询
     *
     * @param page
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 3:08 下午
     */
    IPage<OrganVo> findOrgansByCondition(IPage<OrganVo> page, @Param("sv") OrganSearchVo sv);
}
