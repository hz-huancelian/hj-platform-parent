package org.hj.chain.platform.tdo.dept;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/6/29  4:49 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/29    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysPositionAddTdo implements Serializable {

    private static final long serialVersionUID = -3046619272723919164L;
    //部门ID
    @NotNull(message = "部门ID不能为空")
    private Long deptId;
    //职位名称
    @NotBlank(message = "职位名称不能为空")
    private String positionName;
    //平台职位
    @NotNull(message = "平台职位不能为空")
    private Long platformPositionId;
    //职位描述
    private String positionDesc;


}