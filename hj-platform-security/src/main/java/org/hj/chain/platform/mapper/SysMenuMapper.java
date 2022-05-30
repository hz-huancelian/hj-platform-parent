package org.hj.chain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hj.chain.platform.model.SysMenu;
import org.hj.chain.platform.vo.SysMenuVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * TODO 根据用户ID查询菜单
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/25 4:40 下午
     */
    List<SysMenuVo> selectMenusByUser(String userId);


    /**
     * 查询系统所有菜单（含按钮）
     *
     * @return 菜单列表
     */
    List<SysMenuVo> selectMenuAll();


    /**
     * 查询系统菜单列表
     *
     * @param userId   用户ID
     * @param menuName 菜单名称
     * @param status   用户状态
     * @return 菜单列表
     */
    List<SysMenuVo> selectMenuList(@Param("userId") String userId, @Param("menuName") String menuName, @Param("status") String status);


    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectPermsByUserId(String userId);


    /**
     * 校验菜单名称是否唯一
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @return 结果
     */
    SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);


    /**
     * TODO 关联的菜单ID
      * @param roleId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 4:19 下午
     */
    List<Long> selectMenuListByRoleId(Long roleId);

}
