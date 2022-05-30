package org.hj.chain.platform.service;


import org.hj.chain.platform.tdo.SysDeptTdo;
import org.hj.chain.platform.vo.SysDeptVo;
import org.hj.chain.platform.vo.TreeSelect;

import java.util.List;

/**
 * 部门管理 服务层
 *
 * @author ruoyi
 */
public interface ISysDeptService {
    /**
     * TODO 查询部门管理数据
     *
     * @param deptName 部门名称
     * @param status   部门状态
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/24 4:26 下午
     */
    List<SysDeptVo> selectDeptList(String deptName, String status);


    /**
     * TODO 查询所有部门
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 6:25 下午
     */
    List<SysDeptVo> selectAllDepts();


    /**
     * 查询部门人数
     *
     * @param parentId 父部门ID
     * @return 结果
     */
    int selectDeptCount(Long parentId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkDeptExistUser(Long deptId);


    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    int insertDept(SysDeptTdo dept);

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    int updateDept(SysDeptTdo dept);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    SysDeptVo selectDeptById(Long deptId);

    /**
     * 根据ID删除部门信息（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    int deleteByDeptId(Long deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param parentId 部门信息ID
     * @param deptName 部门名称
     * @return 结果
     */
    int checkDeptNameUnique(Long parentId, String deptName);

    /**
     * TODO 获取部门树
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/29 1:31 下午
     */
    List<TreeSelect> selectDeptTrees();


    /**
     * TODO 根据部门ID获取子部门ID集合
     *
     * @param deptId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 4:39 下午
     */
    List<Long> selectChildDeptIdsByDeptId(Long deptId);
}
