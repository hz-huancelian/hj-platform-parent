package org.hj.chain.platform.sys.tdo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserEditTdo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //用户编号
    @NotBlank(message = "用户ID不能为空！")
    private String userId;
    //员工名称
    @NotNull(message = "员工名称不能为空！")
    private String empName;

    //联系电话
    @NotBlank(message = "手机号不能为空！")
    private String phone;

}
