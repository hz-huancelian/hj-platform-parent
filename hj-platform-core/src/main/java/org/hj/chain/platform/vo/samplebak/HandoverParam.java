package org.hj.chain.platform.vo.samplebak;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/23  3:42 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/23    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class HandoverParam {

    //委托单位
    private String consignorName;

    //入库时间
    private LocalDateTime storeTime;

    //送样人
    private String sendUser;
}