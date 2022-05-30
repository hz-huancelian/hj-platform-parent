package org.hj.chain.platform.tdo.organ;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 注册机构
 * @Iteration : 1.0
 * @Date : 2021/4/30  9:23 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/04/30    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RegOrganTdo implements Serializable {
    private static final long serialVersionUID = 7092472485523258498L;

    //机构编号
    @NotBlank(message = "机构编号不能为空！")
    private String organId;

    //机构名称
    @NotBlank(message = "机构名称不能为空！")
    private String organName;

    //机构联系方式
    @NotBlank(message = "机构联系方式不能为空！")
    private String organPhone;

    @NotBlank(message = "机构法人不能为空！")
    private String organJurPerson;
}