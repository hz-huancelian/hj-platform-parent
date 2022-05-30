package org.hj.chain.platform.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.report.model.ReportCheck;
import org.hj.chain.platform.vo.report.ReportCheckVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportCheckMapper extends BaseMapper<ReportCheck> {


    /**
     * TODO 分页查询报告审核信息
     *
     * @param page
     * @param jobId
     * @param projectName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/30 4:12 下午
     */
    IPage<ReportCheckVo> findReportCheckByPage(IPage<ReportCheckVo> page,
                                               @Param("organId") String organId,
                                               @Param("jobId") String jobId,
                                               @Param("projectName") String projectName);
}
