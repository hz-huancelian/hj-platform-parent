package org.hj.chain.platform.word;

import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.expression.Name;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 报告数据
 * @Iteration : 1.0
 * @Date : 2021/5/18  3:49 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/18    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportTableData {

    //报告编号
    private String reportCode;

    //报告日期
    private String reportDate;

    //证书编号
    private String certCode;

    //证书图片
    private PictureRenderData certImg;

    //公司LOGO
    private PictureRenderData organLogo;

    //项目名称
    private String projectName;

    //检测类型
    private String checkType;

    //委托人单位
    private String consignorName;

    //机构名称
    private String ownerOrganName;

    //地址
    private String address;

    //邮编
    private String postCode;

    //网址
    private String webAddress;

    //电话
    private String tel;

    //传真
    private String fax;

    private String explains;

    private ReportMainData mainData;

    //固定列数
    private int fixColumNum;

    //动态列数
    private int dynColumNum;

    //行数
    private int rowNum;


}