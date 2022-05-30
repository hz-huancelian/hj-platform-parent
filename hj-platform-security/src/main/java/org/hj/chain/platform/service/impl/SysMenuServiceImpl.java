package org.hj.chain.platform.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hj.chain.platform.Constants;
import org.hj.chain.platform.StringUtils;
import org.hj.chain.platform.mapper.SysMenuMapper;
import org.hj.chain.platform.model.SysMenu;
import org.hj.chain.platform.service.ISysMenuService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.SysMenuTdo;
import org.hj.chain.platform.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/7/25  4:07 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/25    create
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService {
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private ISysUserService sysUserService;

    @Override
    public List<SysMenuVo> selectMenusByUser(String userId) {
        UserVo userVo = sysUserService.findUserByUserId(userId);
        if (userVo != null) {
            List<SysMenuVo> menuVos;
            if (userVo.getUserType().equals("0")) {
                menuVos = sysMenuMapper.selectMenuAll();
            } else {
                menuVos = sysMenuMapper.selectMenusByUser(userId);

            }
            return menuVos;
        }
        return null;
    }

    @Override
    public List<TreeSelect> findMenusTreeByUser(String userId) {
        List<SysMenuVo> vos = selectMenusByUser(userId);
        if (vos != null && !vos.isEmpty()) {
//            List<SysMenuVo> sysMenuVoList = vos.stream().filter(item -> item.getVisible().equals("0")).collect(Collectors.toList());
            List<SysMenuVo> sysMenuVos = buildMenuSlfTree(vos);
            return sysMenuVos.stream().map(TreeSelect::new).collect(Collectors.toList());
//            return buildMenuTree(vos);
        }

        return null;
    }


    /**
     * TODO 构建menu树
     *
     * @param menuVos
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/26 5:13 下午
     */
    private List<TreeSelect> buildMenuTree(List<SysMenuVo> menuVos) {
        List<Long> tempList = new ArrayList<>();
        menuVos.stream().forEach(item -> {
            tempList.add(item.getMenuId());
        });

        List<TreeSelect> returnList = new ArrayList<>();
        Map<Long, TreeSelect> parentMenuMap = new HashMap<>();
        menuVos.stream().forEach(item -> {
            Long menuId = item.getMenuId();
            Long parentId = item.getParentId();
            String menuName = item.getMenuName();
            if (!tempList.contains(parentId)) {
                //顶级顶点
                TreeSelect treeSelect = new TreeSelect();
                treeSelect.setId(menuId)
                        .setLabel(menuName);
                parentMenuMap.put(menuId, treeSelect);
                returnList.add(treeSelect);

            } else {
                TreeSelect treeSelect = parentMenuMap.get(parentId);
                List<TreeSelect> children = treeSelect.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    treeSelect.setChildren(children);
                }
                TreeSelect child = new TreeSelect();
                child.setId(menuId)
                        .setLabel(menuName);
                children.add(child);
            }

        });
        return returnList;
    }

    @Override
    public List<SysMenuVo> selectMenuList(String userId, String menuName, String status) {
        UserVo userVo = sysUserService.findUserByUserId(userId);
        if (userVo != null) {
            if (userVo.getUserType().equals("0")) {
                return sysMenuMapper.selectMenuList(userId, menuName, status);
            }
        }
        return null;
    }


    @Override
    public Set<String> selectPermsByUserId(String userId) {
        List<String> perms = sysMenuMapper.selectPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StrUtil.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public Map<String, String> selectPermsAll(String userId) {
        LinkedHashMap<String, String> section = new LinkedHashMap<>();
        List<SysMenuVo> permissions = selectMenusByUser(userId);
        if (permissions != null && !permissions.isEmpty()) {
            for (SysMenuVo menu : permissions) {
                section.put(menu.getPath(), MessageFormat.format(PREMISSION_STRING, menu.getPerms()));
            }
        }
        return section;
    }

    @Override
    public int deleteMenuById(Long menuId) {
        int count = sysMenuMapper.delete(Wrappers.<SysMenu>lambdaUpdate()
                .eq(SysMenu::getMenuId, menuId));
        return count;
    }

    @Override
    public SysMenuVo selectMenuById(Long menuId) {
        SysMenu dbPo = sysMenuMapper.selectById(menuId);
        if (dbPo != null) {
            SysMenuVo vo = new SysMenuVo();
            vo.setComponent(dbPo.getComponent())
                    .setIcon(dbPo.getIcon())
                    .setMenuId(menuId)
                    .setMenuName(dbPo.getMenuName())
                    .setMenuType(dbPo.getMenuType())
                    .setOrderNum(dbPo.getOrderNum())
                    .setParentId(dbPo.getParentId())
                    .setPerms(dbPo.getPerms())
                    .setRemark(dbPo.getRemark())
                    .setPath(dbPo.getPath())
                    .setStatus(dbPo.getStatus());
            return vo;
        }
        return null;
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return sysMenuMapper.selectMenuListByRoleId(roleId);
    }


    @Override
    public int insertMenu(SysMenuTdo menu) {
        String menuName = menu.getMenuName();
        Long parentId = menu.getParentId();
        SysMenu dbPo = sysMenuMapper.selectOne(Wrappers.<SysMenu>lambdaQuery()
                .select(SysMenu::getMenuId)
                .eq(SysMenu::getMenuName, menuName)
                .eq(SysMenu::getParentId, parentId));

        if (dbPo != null) {
            return -1;
        }

        LocalDateTime now = LocalDateTime.now();
        SysMenu po = new SysMenu();
        po.setComponent(menu.getComponent())
                .setIcon(menu.getIcon())
                .setMenuName(menuName)
                .setMenuType(menu.getMenuType())
                .setOrderNum(menu.getOrderNum())
                .setParentId(menu.getParentId())
                .setPerms(menu.getPerms())
                .setIsCache(menu.getIsCache())
                .setIsFrame(menu.getIsFrame())
                .setVisible(menu.getVisible())
                .setStatus("0")
                .setRemark(menu.getRemark())
                .setCreateTime(now)
                .setPath(menu.getPath());

        int count = sysMenuMapper.insert(po);

        return count;
    }

    @Override
    public int updateMenu(SysMenuTdo menu) {
        Long menuId = menu.getMenuId();
        String menuName = menu.getMenuName();
        Long parentId = menu.getParentId();
        SysMenu dbPo = sysMenuMapper.selectOne(Wrappers.<SysMenu>lambdaQuery()
                .select(SysMenu::getMenuId)
                .eq(SysMenu::getMenuName, menuName)
                .eq(SysMenu::getParentId, parentId));

        if (dbPo != null && !dbPo.getMenuId().equals(menuId)) {
            return -1;
        }

        LocalDateTime now = LocalDateTime.now();
        SysMenu po = new SysMenu();
        po.setComponent(menu.getComponent())
                .setIcon(menu.getIcon())
                .setMenuName(menuName)
                .setMenuType(menu.getMenuType())
                .setOrderNum(menu.getOrderNum())
                .setParentId(menu.getParentId())
                .setPerms(menu.getPerms())
                .setPath(menu.getPath())
                .setIsCache(menu.getIsCache())
                .setIsFrame(menu.getIsFrame())
                .setVisible(menu.getVisible())
                .setStatus(menu.getStatus())
                .setRemark(menu.getRemark())
                .setUpdateTime(now)
                .setMenuId(menuId);

        int count = sysMenuMapper.updateById(po);
        return count;
    }

    @Override
    public List<RouterVo> getRouters() {
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String userId = (String) tokenInfo.getLoginId();
        List<SysMenuVo> sysMenuVos = selectMenusByUser(userId);

        if (sysMenuVos != null) {
            List<SysMenuVo> sysMenuTreeVos = buildMenuSlfTree(sysMenuVos);
            List<RouterVo> routerVos = buildMenus(sysMenuTreeVos);
            return routerVos;
        }
        return null;
    }


    /**
     * TODO 构建路由
     *
     * @param menus
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 11:04 下午
     */
    private List<RouterVo> buildMenus(List<SysMenuVo> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenuVo menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
            List<SysMenuVo> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && Constants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/inner");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = StringUtils.replaceEach(menu.getPath(), new String[]{Constants.HTTP, Constants.HTTPS}, new String[]{"", ""});
                children.setPath(routerPath);
                children.setComponent(Constants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }


    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenuVo menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenuVo menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = StringUtils.replaceEach(routerPath, new String[]{Constants.HTTP, Constants.HTTPS}, new String[]{"", ""});
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && Constants.TYPE_DIR.equals(menu.getMenuType())
                && Constants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenuVo menu) {
        String component = Constants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = Constants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = Constants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenuVo menu) {
        return menu.getParentId().intValue() == 0 && Constants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(Constants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenuVo menu) {
        return menu.getIsFrame().equals(Constants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenuVo menu) {
        return menu.getParentId().intValue() != 0 && Constants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public List<SysMenuVo> buildMenuSlfTree(List<SysMenuVo> menus) {
        List<SysMenuVo> returnList = new ArrayList<SysMenuVo>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysMenuVo dept : menus) {
            tempList.add(dept.getMenuId());
        }
        for (Iterator<SysMenuVo> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenuVo menu = (SysMenuVo) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }


    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenuVo> list, SysMenuVo t) {
        // 得到子节点列表
        List<SysMenuVo> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenuVo tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenuVo> getChildList(List<SysMenuVo> list, SysMenuVo t) {
        List<SysMenuVo> tlist = new ArrayList<SysMenuVo>();
        Iterator<SysMenuVo> it = list.iterator();
        while (it.hasNext()) {
            SysMenuVo n = (SysMenuVo) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenuVo> list, SysMenuVo t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}