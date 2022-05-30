package org.hj.chain.platform.sample.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sample_store_list")
public class SampleStoreList implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 取样ID
     */
    private Long sampleItemId;

    //库存位置
    private String storeLocation;

    /**
     * 送样人
     */
    private String sendUser;

    /**
     * 入库时间
     */
    private LocalDateTime storeTime;

}
