package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserVo implements Serializable {
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
     * 部门名称
     */
    private String deptName;

    /**
     * 用户名称
     */
    private String username;

    //密码
    private String password;

    /**
     * 用户类型
     */
    private String userType;

    //员工编号
    private String empCode;

    //用户昵称
    private String empName;

    /**
     * 用户邮箱
     */
    private String email;


    /**
     * 用户头像
     */
    private String avatarPath;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 用户性别
     */
    private String sex;


    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;
    /**
     * 最后登录IP
     */
    private String loginIp;


    private String organId;


    //职位名称
    private String postNames;

    //角色名称
    private String roleNames;


    //备注
    private String remark;

    //0-否；1-是（默认否）
    private String isAppLogin;

    private LocalDateTime createTime;


}
