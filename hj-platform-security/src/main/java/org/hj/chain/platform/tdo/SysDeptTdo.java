package org.hj.chain.platform.tdo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 部门表 sys_dept
 *
 * @author ruoyi
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysDeptTdo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 显示顺序
     */
    private String orderNum;

    //状态：0-正常；1-停用
    private String status;

    /**
     * 负责人
     */
    private String leader;

    //备注
    private String remark;


}
