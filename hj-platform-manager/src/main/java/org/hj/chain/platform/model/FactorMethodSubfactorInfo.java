package org.hj.chain.platform.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-15
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_factor_method_subfactor_info")
public class FactorMethodSubfactorInfo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    private String id;
    //检测因子ID
    private String factorId;
    //因子检测方法ID
    private String factorMethodId;
    //子因子名称
    private String name;
}
