package org.hj.chain.platform.vo.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-09
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ContractSearchVo implements Serializable {
    private static final long serialVersionUID = 5211922420617359489L;
    /**
     * 合同编号
     */
    private String contCode;
    /**
     * 项目名称
     */
    private String contName;
    /**
     * 合同状态：0：待制作 1:已制作，2:待审核；3审核通过；4:审核失败5：已作废
     */
    private String contStatus;

    //合同状态列表
    private List<String> contStatusIds;

    /**
     * 主合同编号
     */
    private String supContCode;
}
