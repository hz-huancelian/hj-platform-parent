package org.hj.chain.platform.vo.judgetask;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class JudgeTaskSearchVo implements Serializable {
    private static final long serialVersionUID = -1L;
    //报价单ID
    private String offerId;
    //项目名称
    private String projectName;
    //技术评审任务状态：0-待处理；1-需要评审；2-不需要评审；3-作废；4-评审中；5-评审完成；6-评审通过
    private String taskStatus;
}
