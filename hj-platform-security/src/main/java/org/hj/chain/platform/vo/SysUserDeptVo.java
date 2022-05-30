package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysUserDeptVo implements Serializable {
    private static final long serialVersionUID = -7304699473300983590L;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 部门人数
     */
    private Integer cnt;
}
