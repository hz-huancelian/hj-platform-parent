package org.hj.chain.platform.tdo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 菜单权限表 sys_menu
 *
 * @author ruoyi
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysMenuTdo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;


    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private String orderNum;

    //组建路径
    private String component;

    /**
     * 菜单URL(路由)
     */
    private String path;


    /**
     * 类型（M目录 C菜单 F按钮）
     */
    private String menuType;


    /**
     * 是否为外链（0是 1否）
     */
    private String isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    private String isCache;

    /**
     * 显示状态（0显示 1隐藏）
     */
    private String visible;

    /**
     * 权限字符串
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    //状态
    private String status;


    //备注
    private String remark;


}
