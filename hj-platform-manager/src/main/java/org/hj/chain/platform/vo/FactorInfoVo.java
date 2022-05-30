package org.hj.chain.platform.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 因子展示Vo
 * @Iteration : 1.0
 * @Date : 2021/5/5  9:31 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/05    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FactorInfoVo implements Serializable {
    private static final long serialVersionUID = 4481916276016497796L;

    //因子编号
    private String id;

    //因子名称
    private String name;

    //所属一级分类
    private String classId;

    //数据录入环节（1：采样 2：检样）
    private String dataEntryStep;

    //检出限（能检测出的最小颗粒度）
    private String detectionLimit;
}