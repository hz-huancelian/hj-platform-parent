package org.hj.chain.platform.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.report.model.ReportSign;
import org.hj.chain.platform.vo.report.ReportSignVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportSignMapper extends BaseMapper<ReportSign> {

    /**
     * TODO 分页查询报告签发列表
     *
     * @param page
     * @param jobId
     * @param projectName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/30 4:14 下午
     */
    IPage<ReportSignVo> findReportSignByPage(IPage<ReportSignVo> page,
                                             @Param("organId") String organId,
                                             @Param("jobId") String jobId,
                                             @Param("projectName") String projectName);
}
