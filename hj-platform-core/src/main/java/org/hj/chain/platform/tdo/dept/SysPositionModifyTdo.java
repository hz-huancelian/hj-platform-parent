package org.hj.chain.platform.tdo.dept;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class SysPositionModifyTdo extends SysPositionAddTdo implements Serializable {

    private static final long serialVersionUID = -3046619272723919164L;

    //职位ID（主键）
    @NotNull(message = "主键ID不能为空")
    private Long positionId;


}