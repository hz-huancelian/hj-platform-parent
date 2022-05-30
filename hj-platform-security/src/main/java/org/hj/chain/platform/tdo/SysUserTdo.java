package org.hj.chain.platform.tdo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysUserTdo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;


    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户名称
     */
//    private String username;

    /**
     * 用户类型
     */
    private String userType;


    //真实名字
    private String empName;

    //员工编号
    private String empCode;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;


    //0-否；1-是（默认否）
    private String isAppLogin;

    //app角色
    private List<String> appRoles;

    //角色ID集合
    private List<Long> roleIds;

    //职位ID集合
    private List<Long> postIds;

    //机构ID
    private String organId;


    //备注
    private String remark;


}
