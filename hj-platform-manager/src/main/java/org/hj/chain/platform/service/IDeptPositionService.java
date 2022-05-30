package org.hj.chain.platform.service;

import org.hj.chain.platform.vo.DeptPositionRelVo;

import java.util.List;

public interface IDeptPositionService {


    /**
     * TODO 保存关系
     *
     * @param deptId
     * @param positionId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/13 5:49 下午
     */
    int saveRel(Long deptId, Long positionId);


    /**
     * TODO 删除关系
     *
     * @param id
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/13 5:49 下午
     */
    int delRelById(Long id);


    /**
     * TODO  根据部门查看部门职位列表
     *
     * @param deptId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/13 6:01 下午
     */
    List<DeptPositionRelVo> findRelByDeptId(Long deptId);
}
