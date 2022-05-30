package org.hj.chain.platform.vo.sample;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/13
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleTaskSearchVo extends SampCommSearchVo implements Serializable {
    private static final long serialVersionUID = 3845353614442144308L;
    //采样任务状态 0-待合样；1-已合样；2-采样中；3-已完成
    private String sampTaskStatus;
    //项目名称
    private String projectName;
}
