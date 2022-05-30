package org.hj.chain.platform.tdo.judgeorgan;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 分包机构修改
 * @Iteration : 1.0
 * @Date : 2021/5/12  3:03 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/12    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class JudgeOrganModifyTdo extends JudgeOrganTdo implements Serializable {
    private static final long serialVersionUID = 2903389319782954017L;

    //主键ID
    @NotBlank(message = "机构ID不能为空")
    private String id;
}