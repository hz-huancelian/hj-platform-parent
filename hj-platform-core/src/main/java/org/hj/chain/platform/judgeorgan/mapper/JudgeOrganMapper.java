package org.hj.chain.platform.judgeorgan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.judgeorgan.model.JudgeOrgan;
import org.hj.chain.platform.vo.judgeorgan.JudgeOrganVo;
import org.springframework.stereotype.Repository;

@Repository
public interface JudgeOrganMapper extends BaseMapper<JudgeOrgan> {


    /**
     * TODO 分页查询
     *
     * @param page
     * @param organId
     * @param judgeOrganName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 9:55 上午
     */
    IPage<JudgeOrganVo> findByCondition(IPage<JudgeOrganVo> page,
                                        @Param("organId") String organId,
                                        @Param("judgeOrganName") String judgeOrganName);
}
