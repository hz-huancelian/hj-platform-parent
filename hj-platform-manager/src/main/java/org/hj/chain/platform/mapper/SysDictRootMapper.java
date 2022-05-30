package org.hj.chain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.model.SysDictRoot;
import org.hj.chain.platform.vo.SysDictRootVo;
import org.springframework.stereotype.Repository;

@Repository
public interface SysDictRootMapper extends BaseMapper<SysDictRoot> {


    /**
     * @Description: TODO 分页查询字典节点信息
     * @Param: [page, organId, dictDesc]
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.ls.erp.chemerp.vo.sys.SysDictTreeVo>
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/3/31 6:26 下午
     */
    IPage<SysDictRootVo> findPageByCondition(IPage<SysDictRootVo> page, @Param("dictVal") String dictVal);
}
