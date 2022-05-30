package org.hj.chain.platform.vo.check;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CheckFactorSubsetVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //同系物套餐子因子ID（保存时不能为空）
    private Long id;
    private String factorName;
    //检测标准ID
    private String checkStandardId;
    //标准号
    private String standardNo;
    //检测标准
    private String standardName;
    private String unitName;
    //[{"name":"", "v1":"", "v2":""},{}] 注：v1：实数；v2：指数（保存时不能为空）
    private String checkSubRes;
}
