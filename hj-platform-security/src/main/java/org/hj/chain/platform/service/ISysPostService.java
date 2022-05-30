package org.hj.chain.platform.service;


import org.hj.chain.platform.tdo.SysPostTdo;
import org.hj.chain.platform.vo.SysPostVo;

import java.util.List;

/**
 * 岗位信息 服务层
 *
 * @author ruoyi
 */
public interface ISysPostService {
    /**
     * 查询岗位信息集合
     *
     * @param postName 岗位名称
     * @param postCode 岗位编码
     * @param status   状态
     * @return 岗位信息集合
     */
    List<SysPostVo> selectPostList(String postName, String postCode, String status);

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    List<SysPostVo> selectPostAll();

    /**
     * 根据用户ID查询岗位
     *
     * @param userId 用户ID
     * @return 岗位列表
     */
    List<SysPostVo> selectPostsByUserId(String userId);



    /**
     * 根据用户ID查询岗位
     *
     * @param userId 用户ID
     * @return 岗位列表
     */
    List<Long> selectPostIdsByUserId(String userId);

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    SysPostVo selectPostById(Long postId);

    /**
     * 批量删除岗位信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws Exception 异常
     */
    int deletePostByIds(String ids);


    /**
     * TODO 删除岗位信息
     *
     * @param id
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/29 1:16 下午
     */
    int deletePostById(Long id);

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    int insertPost(SysPostTdo post);

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    int updatePost(SysPostTdo post);


    /**
     * 校验岗位名称
     *
     * @param postId   岗位ID
     * @param postName 岗位信息
     * @return 结果
     */
    int checkPostNameUnique(Long postId, String postName);

    /**
     * 校验岗位编码
     *
     * @param postId   岗位ID
     * @param postCode 岗位信息
     * @return 结果
     */
    int checkPostCodeUnique(Long postId, String postCode);

    /**
     * TODO 根据用户
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 11:56 上午
     */
    List<String> selectUserPostGroup(String userId);
}
