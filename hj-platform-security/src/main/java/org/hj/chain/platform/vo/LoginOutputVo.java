package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 登录返回使用
 *
 * @Author: lijinku
 * @Iteration : 1.0
 * @Date: 2021/4/28 4:25 下午
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LoginOutputVo implements Serializable {

    private static final long serialVersionUID = 4509049797912510070L;
    //用户编号
    private String userId;

    //部门ID
    private Long deptId;

    //职位编码
    private List<String> postCodes;

    //用户名
    private String username;

    //用户昵称
    private String empName;

    //用户类型（0、管理员；1、普通用户）
    private String userType;

    //手机号码
    private String phone;

    //机构ID
    private String organId;

    //头像
    private String avatarPath;

    //机构名称
    private String organName;

    //帐号状态（0、正常；1、禁用）
    private String userStatus;

    //资源信息
    private Set<String> resources;

    //部门名称
    private String deptName;

    //角色信息
    private List<String> roles;

    //app角色列表
    private List<String> appRoles;

}
