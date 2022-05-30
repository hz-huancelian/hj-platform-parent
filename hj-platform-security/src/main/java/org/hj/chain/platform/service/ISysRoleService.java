package org.hj.chain.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.tdo.RoleMenuRelTdo;
import org.hj.chain.platform.tdo.SysRoleTdo;
import org.hj.chain.platform.vo.RoleQueryVo;
import org.hj.chain.platform.vo.SysRoleVo;

import java.util.List;
import java.util.Set;

/**
 * 角色业务层
 *
 * @author ruoyi
 */
public interface ISysRoleService {
    /**
     * 根据条件分页查询角色数据
     *
     * @param queryVo 角色信息
     * @return 角色数据集合信息
     */
    IPage<SysRoleVo> selectRoleListByCondition(PageVo pageVo, RoleQueryVo queryVo);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectRoleKeys(String userId);


    /**
     * 根据用户ID查询关联的角色ID
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Long> selectRoleIdsByUserId(String userId);

    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRoleVo> selectRolesByUserId(String userId);

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    List<SysRoleVo> selectRoleAll();

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    SysRoleVo selectRoleById(Long roleId);

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    boolean deleteRoleById(Long roleId);

    /**
     * 批量删除角色用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws Exception 异常
     */
    int deleteRoleByIds(String ids);

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int insertRole(SysRoleTdo role);

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int updateRole(SysRoleTdo role);

    /**
     * 修改数据权限范围信息
     *
     * @param roleId    角色信息ID
     * @param dataScope 数据范围
     * @return 结果
     */
    int authDataScope(Long roleId, String dataScope);

    /**
     * 校验角色名称是否唯一
     *
     * @param roleId   角色信息Id
     * @param roleName 角色名称
     * @return 结果
     */
    int checkRoleNameUnique(Long roleId, String roleName);

    /**
     * 校验角色权限是否唯一
     *
     * @param roleId  角色信息Id
     * @param roleKey 角色key
     * @return 结果
     */
    int checkRoleKeyUnique(Long roleId, String roleKey);

    /**
     * 角色状态修改
     *
     * @param roleId 角色信息ID
     * @param status 角色状态
     * @return 结果
     */
    int changeStatus(Long roleId, String status);

    /**
     * 取消授权用户角色
     *
     * @param userId 用户Id
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteAuthUser(String userId, Long roleId);


    /**
     * TODO 根据用户ID和权限码查看最大角色范围
     *
     * @param userId
     * @param path
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/29 10:06 上午
     */
    String findMaxRoleScopeByPerms(String userId, String path);

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    int deleteAuthUsers(Long roleId, String userIds);

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    int insertAuthUsers(Long roleId, String userIds);

    /**
     * TODO 保存角色菜单关系
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 7:20 下午
     */
    int saveRoleMenuRel(RoleMenuRelTdo tdo);
}
