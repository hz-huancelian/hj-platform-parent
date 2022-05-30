package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserQryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 员工名称
     */
    private String empName;

    /**
     * 手机号码
     */
    private String phonenumber;


    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;


}
