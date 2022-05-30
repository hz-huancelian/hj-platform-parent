package org.hj.chain.platform.vo.check;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CheckUserVo implements Serializable {
    private static final long serialVersionUID = -2246263873921510276L;

    //用户ID
    private String userId;

    //员工编号(用户名)
    private String username;

    //员工名称
    private String empName;

    //待完成因子数
    private Integer cnt;
}
