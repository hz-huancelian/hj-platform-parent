package org.hj.chain.platform.check.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.check.entity.CheckTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.vo.check.CheckTaskInfoSearchVo;
import org.hj.chain.platform.vo.check.CheckTaskInfoVo;
import org.hj.chain.platform.vo.check.CheckTaskSearchVo;
import org.hj.chain.platform.vo.check.CheckTaskVo;
import org.hj.chain.platform.vo.statistics.CompleteTaskVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 检测任务信息 Mapper 接口
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Repository
public interface CheckTaskMapper extends BaseMapper<CheckTask> {

    IPage<CheckTaskInfoVo> findByCondition(IPage<CheckTaskInfoVo> page,
                                           @Param("organId") String organId,
                                           @Param("sv") CheckTaskInfoSearchVo sv);

    IPage<CheckTaskVo> findCheckTaskForManageByCondition(Page<CheckTaskVo> page,
                                                         @Param("organId") String organId,
                                                         @Param("sv") CheckTaskSearchVo sv);

    IPage<CheckTaskVo> findCheckTaskForEmpByCondition(Page<CheckTaskVo> page,
                                                      @Param("userId") String userId,
                                                      @Param("sv") CheckTaskSearchVo sv);

    IPage<CheckTaskVo> findCheckTaskForDeptByCondition(Page<CheckTaskVo> page,
                                                       @Param("list") List<Long> deptIds,
                                                       @Param("sv") CheckTaskSearchVo sv);

    IPage<CheckTaskInfoVo> findForDeptByCondition(Page<CheckTaskInfoVo> page,
                                                  @Param("organId") String organId,
                                                  @Param("list") List<Long> deptIds,
                                                  @Param("sv") CheckTaskInfoSearchVo sv);

    CompleteTaskVo completeTaskForCurrMonth(String organId);

    CompleteTaskVo completeTaskForCurrYear(String organId);

    List<Map<String, Object>> checkTaskCntForCurrMonth(String organId);

    List<Map<String, Object>> checkTaskCntForCurrYear(String organId);

    int selectCountForUnAssignCheckTask(String organId);
}
