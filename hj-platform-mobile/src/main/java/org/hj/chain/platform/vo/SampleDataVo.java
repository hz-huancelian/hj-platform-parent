package org.hj.chain.platform.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/30
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SampleDataVo implements Serializable {
    private static final long serialVersionUID = -3683532429495431403L;
    //录样图片地址列表
    private List<String> imageList;
    //采样人userId
    private String collectUserId;
    //采样人
    private String collectUser;
    //复核人ID
    private String reviewUserId;
    //复核人
    private String reviewUser;
    //采样日期
    private LocalDate collectDate;
    //录样位置
    private String collectLocation;
    /**
     * 采样数据-二级类别 json存储
     */
    private JSONArray sampleData;
    //采样备注
    private String collectRemark;
    //特别说明标记
    private Boolean specialNoteFlag;
    /**
     * 采样数据-特别说明值
     */
    private String specialNote;
    //污染物信息
    private String pollutantInfo;
}
