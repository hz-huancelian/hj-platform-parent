package org.hj.chain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.model.SysRole;
import org.hj.chain.platform.vo.RoleQueryVo;
import org.hj.chain.platform.vo.SysRoleVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {


    /**
     * TODO 分页查询角色信息
     *
     * @param page
     * @param queryVo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/25 9:32 下午
     */
    IPage<SysRoleVo> selectRoleListByCondition(IPage<SysRoleVo> page, @Param("sv") RoleQueryVo queryVo);


    /**
     * TODO 根据用户ID查看角色key
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/25 9:55 下午
     */
    List<String> selectRoleKeysByUserId(@Param("userId") String userId);


    /**
     * TODO 根据用户ID查看关联的角色ID
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/2 1:57 下午
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") String userId);


    /**
     * TODO 根据用户ID查看
     *
     * @param organId
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/25 10:10 下午
     */
    List<SysRoleVo> selectRolesByUserId(@Param("organId") String organId, @Param("userId") String userId);

    /**
     * TODO 根据用户Id和path查询最大的角色范围
     *
     * @param userId
     * @param perms
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/29 10:17 上午
     */
    String findMaxRoleScopeByPerms(@Param("userId") String userId, @Param("perms") String perms);
}
