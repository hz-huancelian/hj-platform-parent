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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SampleTaskPointVo implements Serializable {
    private static final long serialVersionUID = -1L;
    //采样任务ID
    private Long sampleTaskId;
    //检测位置
    private String factorPoint;
    //采样组长
    private String sampleUsers;
    //采样组长ID
    private String sampleUserIds;
}
