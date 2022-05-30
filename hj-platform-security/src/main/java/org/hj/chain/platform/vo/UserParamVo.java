package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/8/1  5:44 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/08/01    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserParamVo implements Serializable {

    private static final long serialVersionUID = 4509049797912510070L;

    //用户ID
    private String userId;

    private Long deptId;

    private String deptName;

    private Long postId;

    //用户账号
    private String username;

    //用户名称
    private String empName;

    private String postIds;

    //职位名称
    private String postNames;
}