package org.hj.chain.platform.vo.dept;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

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
public class SysPositionVo implements Serializable {

    private static final long serialVersionUID = -3046619272723919164L;

    //职位ID（主键）
    private Long positionId;

    //部门ID
    private Long deptId;
    //职位名称
    private String positionName;

    //平台职位
    private Long platformPositionId;

    //平台职位
    private String platformPositionVal;

    //职位描述
    private String positionDesc;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}