package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 部门职务关系
 * @Iteration : 1.0
 * @Date : 2021/5/13  5:44 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/13    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DeptPositionRelVo implements Serializable {
    private static final long serialVersionUID = 8619668536370323337L;

    //主键ID
    private Long id;

    //职位ID
    private Long positionId;

    //职位字典
    private String positionVal;
}