package org.hj.chain.platform.vo.judgeorgan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 分包机构
 * @Iteration : 1.0
 * @Date : 2021/5/12  3:03 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/12    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class JudgeOrganVo implements Serializable {
    private static final long serialVersionUID = 2903389319782954017L;

    //主键ID
    private String id;

    //分包机构名称
    private String judgeOrganName;

    //分包机构联系人
    private String judgeOrganLinker;

    //分包机构联系人电话
    private String judgeOrganLinkerPhone;

    //工单接受邮箱
    private String email;

    //法人
    private String legalPerson;

    //开户行
    private String bankName;

    //开户行账号
    private String bankNumber;

    //备注
    private String remark;

    //是否删除
    private String isDelete;

    //创建人
    private String createUserId;

    private LocalDateTime createTime;
}