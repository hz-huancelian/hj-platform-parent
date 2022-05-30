package org.hj.chain.platform.vo.sample;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
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
public class JobFactorSearchVo extends SampCommSearchVo implements Serializable {
    private static final long serialVersionUID = -1L;
    //因子名称
    private String factorName;
    //检测位置
    private String factorPoint;
    //分包标志 0-自检；1-分包
    private String fbFlag;
    //合样标志 0-未合样；1-已合样
    private String hyFlag;
}
