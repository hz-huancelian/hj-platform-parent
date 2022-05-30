package org.hj.chain.platform.vo.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class CusContBaseInfoListVo extends CusContBaseInfoVo implements Serializable {
    private static final long serialVersionUID = 5211922420617359489L;
    //删除状态：0：未删除；1：已删除
    private String delStatus;
    //创建人
    private String createUserId;

    //创建时间
    private LocalDateTime createTime;
}
