package org.hj.chain.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.model.SysUserRole;
import org.hj.chain.platform.tdo.SysUserTdo;
import org.hj.chain.platform.vo.UserParamVo;
import org.hj.chain.platform.vo.UserPwdResetVo;
import org.hj.chain.platform.vo.UserQryVo;
import org.hj.chain.platform.vo.UserVo;

import java.util.List;

/**
 * 用户 业务层
 *
 * @author ruoyi
 */
public interface ISysUserService extends IService<SysUser> {
    /**
     * 根据条件分页查询用户列表
     *
     * @param pageVo 分页条件
     * @param qryVo  用户信息
     * @return 用户信息集合信息
     */
    IPage<UserVo> selectUserListByCondition(PageVo pageVo, UserQryVo qryVo);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    UserVo selectUserByLoginName(String username);

    /**
     * 通过手机号码查询用户
     *
     * @param phoneNumber 手机号码
     * @return 用户对象信息
     */
    UserVo selectUserByPhoneNumber(String phoneNumber);

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户对象信息
     */
    UserVo selectUserByEmail(String email);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    UserVo findUserByUserId(String userId);

    /**
     * 通过用户ID查询用户和角色关联
     *
     * @param userId 用户ID
     * @return 用户和角色关联列表
     */
    List<SysUserRole> selectUserRoleByUserId(String userId);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserById(String userId);

    /**
     * 批量删除用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws Exception 异常
     */
    int deleteUserByIds(String ids);


    /**
     * 注册用户信息
     *
     * @param user     用户信息
     * @param username 用户名
     * @return 结果
     */
    boolean registerUser(SysUserTdo user, String username);

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUserByUserId(SysUserTdo user);


    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    void insertUserAuth(String userId, List<Long> roleIds);

    /**
     * 修改用户密码信息
     *
     * @param resetVo 用户信息
     * @return 结果
     */
    int resetUserPwd(UserPwdResetVo resetVo);
//
//    /**
//     * 校验用户名称是否唯一
//     *
//     * @param userId    用户ID
//     * @param loginName 登录名称
//     * @return 结果
//     */
//    int checkLoginNameUnique(String userId, String loginName);

    void insertUserPost(String userId, List<Long> postIds);

    /**
     * 校验手机号码是否唯一
     *
     * @param userId 用户ID
     * @param phone  用户信息
     * @return 结果
     */
    int checkPhoneUnique(String userId, String phone);

    /**
     * 校验email是否唯一
     *
     * @param userId 用户ID
     * @param email  用户信息
     * @return 结果
     */
    int checkEmailUnique(String userId, String email);


    /**
     * 根据用户ID查询用户所属角色组
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<String> selectUserRoleGroup(String userId);

    /**
     * TODO 根据用户ID查询用户所属权限组
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 6:20 下午
     */
    List<String> selectUserPermsGroup(String userId);


    /**
     * 用户状态修改
     *
     * @param userId 用户信息ID
     * @param status 用户信息状态
     * @return 结果
     */
    int changeStatus(String userId, String status);

    /**
     * TODO 重置机构密码
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 9:12 下午
     */
    int resetOriganPassword(String userId);

    /**
     * TODO 使用户无效
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/3 2:29 下午
     */
    int invalidUserByUserId(String userId);


    /**
     * TODO 根据用户ID查看app角色信息
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/2 6:12 下午
     */
    List<String> selectAppRolesByUserId(String userId);

    /**
     * TODO 添加用户app角色
     *
     * @param userId
     * @param appRoles
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 11:47 上午
     */
    int addAppRole(String userId, List<String> appRoles);

    /**
     * TODO 删除用户app角色关系
     *
     * @param userId
     * @param appRoles
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 11:48 上午
     */
    int delAppRole(String userId, List<String> appRoles);

    /**
     * TODO 根据部门ID集合查看下面所有有效的用户名信息
     *
     * @param deptIds
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 5:18 下午
     */
    List<UserParamVo> selectUsersByDeptIds(List<Long> deptIds);


    /**
     * TODO 条件查询用户列表信息
     *
     * @param empName
     * @param deptId
     * @param postId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 5:57 下午
     */
    List<UserParamVo> selectUsersByCondition(String empName, Long deptId, Long postId);

    List<UserParamVo> selectUsersByRoleKeyAndOrganId(String roleKey, String organId);

    List<UserParamVo> selectUsersByAppRoleAndOranId(String appRole, String organId);

    /**
     * TODO 启用用户
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/11/4 3:07 下午
     */
    int enableUserByUserId(String userId);
}
