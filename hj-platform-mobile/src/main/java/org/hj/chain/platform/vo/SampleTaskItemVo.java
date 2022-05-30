package org.hj.chain.platform.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/4/15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleTaskItemVo extends JobCommonVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //厂方签字图片
    private String singImageUrl;
    //受检单位联系人
    private String inspectionLinker;
    //调度备注
    private String dispatchRemark;
    List<MobileSampleItemVo> sampleItemVos;
}
