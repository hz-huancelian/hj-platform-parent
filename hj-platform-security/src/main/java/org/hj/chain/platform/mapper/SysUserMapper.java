package org.hj.chain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.vo.SysUserDeptVo;
import org.hj.chain.platform.vo.UserParamVo;
import org.hj.chain.platform.vo.UserVo;
import org.hj.chain.platform.vo.UserQryVo;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {


    /**
     * TODO 分页查询用户列表信息
     *
     * @param page
     * @param qryVo
     * @param organId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 4:13 下午
     */
    IPage<UserVo> selectUserListByCondition(IPage<UserVo> page, @Param("sv") UserQryVo qryVo, @Param("organId") String organId);

    List<UserVo> selectUserListByConditionNoPage(String organId);

    /**
     * TODO 根据用户名查看用户信息
     *
     * @param username
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 4:31 下午
     */
    UserVo selectUserByLoginName(@Param("username") String username);

    /**
     * TODO 根据手机号查询用户详情
     *
     * @param phonenumber
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 4:35 下午
     */
    UserVo selectUserByPhoneNumber(@Param("phonenumber") String phonenumber);

    /**
     * TODO 根据邮箱查询用户详情
     *
     * @param email
     * @param organId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 4:39 下午
     */
    UserVo selectUserByEmail(@Param("email") String email, @Param("organId") String organId);


    /**
     * TODO 根据用户ID查看用户详情信息
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 4:41 下午
     */
    UserVo selectUserByUserId(String userId);

    /**
     * TODO 根据部门IDs查看下面所有的员工信息
     *
     * @param deptIds
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 5:50 下午
     */
    List<UserParamVo> findUserParamByDeptIds(@Param("list") List<Long> deptIds);

    /**
     * TODO 条件查询用户信息
     *
     * @param empName
     * @param deptId
     * @param postId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 5:59 下午
     */
    List<UserParamVo> selectUsersByCondition(@Param("empName") String empName,
                                             @Param("organId") String organId,
                                             @Param("deptId") Long deptId,
                                             @Param("postId") Long postId);

    List<SysUserDeptVo> deptUserCnt(String organId);

    List<UserParamVo> selectUserParamByRoleKeyAndOrganId(@Param("roleKey") String roleKey,
                                                         @Param("organId") String organId);

    List<UserParamVo> selectUserParamByAppRoleAndOrganId(@Param("appRole") String appRole,
                                                         @Param("organId") String organId);

    List<UserParamVo> selectUserParamByRoleKeysAndOrganId(@Param("list") List<String> roleKeys,
                                                          @Param("organId") String organId);
}
