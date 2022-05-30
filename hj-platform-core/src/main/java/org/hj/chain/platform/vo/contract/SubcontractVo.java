package org.hj.chain.platform.vo.contract;

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
public class SubcontractVo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 合同ID
     */
    private Long id;

    /**
     * 报价单ID
     */
    private String offerId;

    /**
     * 合同编号
     */
    private String contCode;

    /**
     * 项目名称
     */
    private String projectName;


    //分包机构名称
    private String judgeOrganName;


    /**
     * 合同制作人
     */
    private String makeUserId;

    /**
     * 合同状态：0：待完善 1:待审核，2审核失败，3:待制作，4：完成，5-已作废；6-待提交
     */
    private String contStatus;

    /**
     * 签定时间
     */
    private String signDate;

    /**
     * 合同文件ID
     */
    private String contFileId;

}
