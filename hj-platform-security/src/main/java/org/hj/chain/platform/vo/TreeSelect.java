package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/7/25  11:02 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/25    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TreeSelect implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键ID
    private Long id;

    //父主键ID
    private Long parentId;

    //节点名称
    private String label;
//
//    //组件
//    private String component;

    //子节点
    private List<TreeSelect> children;


    public TreeSelect() {

    }


    public TreeSelect(SysDeptVo dept) {
        this.id = dept.getDeptId();
        this.parentId = dept.getParentId();
        this.label = dept.getDeptName();
        this.children = dept.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }


    public TreeSelect(SysMenuVo menuVo) {
        this.id = menuVo.getMenuId();
        this.parentId = menuVo.getParentId();
        this.label = menuVo.getMenuName();
        this.children = menuVo.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }
}