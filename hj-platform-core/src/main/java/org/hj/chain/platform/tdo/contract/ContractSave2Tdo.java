package org.hj.chain.platform.tdo.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
public class ContractSave2Tdo implements Serializable {
    private static final long serialVersionUID = 5211922420617359489L;
    @NotNull(message = "合同ID不能为空！")
    private Long contId;
    @NotBlank(message = "合同文件地址不能为空！")
    private String contFileId;
    @NotBlank(message = "签定时间不能为空！")
    private String signDate;
    /**
     * 有效期（年）
     */
    @NotBlank(message = "有效期不能为空！")
    private String validity;

}
