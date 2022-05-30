package org.hj.chain.platform.tdo.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class ContractBo implements Serializable {

    private static final long serialVersionUID = 1L;

//    /**
//     * 默认报价项目名称
//     */
//    private String contName;

    /**
     * 报价单ID
     */
    private String offerId;

    /**
     * 合同制作人 创建人
     */
    private String makeUserId;

    /**
     * 合同审核人
     */
    private String contCheckUserId;

    //创建用户的部门ID
    private Long deptId;


}
