package org.hj.chain.platform.vo.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysUserVo implements Serializable {
    private static final long serialVersionUID = -4509049797912510070L;
    //用户编号
    private String userId;
    //职位编码
    private List<String> postCodes;
    //用户名
    private String username;
    //员工名称
    private String empName;
}
