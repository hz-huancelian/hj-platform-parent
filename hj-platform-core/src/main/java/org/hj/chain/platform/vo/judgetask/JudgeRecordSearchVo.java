package org.hj.chain.platform.vo.judgetask;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class JudgeRecordSearchVo implements Serializable {
    private static final long serialVersionUID = -1L;
    //报价单ID
    private String offerId;
    //项目名称
    private String projectName;
    //技术评审任务状态：0-待处理；1-通过；2-驳回
    private String recordStatus;
}
