package org.hj.chain.platform.vo.report;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hj.chain.platform.vo.sample.SampleFactorDataParam;
import org.hj.chain.platform.vo.samplebak.ReportEquipVo;
import org.hj.chain.platform.vo.samplebak.ReportSampleVo;
import org.hj.chain.platform.word.ReportTableData;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/25  11:59 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/25    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportGenDataVo implements Serializable {
    private static final long serialVersionUID = 5074173659058001863L;
    //报告主键ID
    private Long reportId;

    //图片地址
    private String imagePath;

    //报告内容表
    private ReportTableData tableData;

    //样品ID：样品性状，采集时间
    private Map<String, ReportSampleVo> sampleVoMap;

    //二级分类下 样品
    private Map<String, List<String>> secdClassSampMap;
    //报告关联的二级分类对应的名称
    private Map<String, String> secdClassNameMap;

    //样品下实验室:因子key-因子val 集合
    private Map<String, Map<String, String>> sampIdLaborFactorValMap;
    //二级分类下实验室的 因子名称和单位
    private Map<String, LinkedHashMap<String, FactorParam>> secdClassLaborFactorMap;
    //样品下实验室:检测因子项主键ID-子因子val 集合
    private Map<String, Map<Long, LinkedHashMap<String, String>>> sampIdLaborSubFactorValMap;

    //样品ID-均值类型
    private Map<String, String> meanTypeMap;

    //同系因子主键key 对应的同系套餐名称
    private Map<Long, String> checkFactorIdHomeFactorNameMap;

    //现场采样因子信息
    private Map<String, List<SampleFactorDataParam>> sceneSampleFactorDataMap;

    //二级类别下实验室设备编号列表(现场)
    private Map<String, List<ReportEquipVo>> sceneEquipVoMap;

    //二级类别下实验室设备编号列表(实验室)
    private Map<String, List<ReportEquipVo>> laborEquipVoMap;

}