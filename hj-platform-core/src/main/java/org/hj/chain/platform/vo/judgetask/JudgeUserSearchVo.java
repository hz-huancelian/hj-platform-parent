package org.hj.chain.platform.vo.judgetask;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class JudgeUserSearchVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户名
     */
    private String empName;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 职位ID
     */
    private Long postId;
}
