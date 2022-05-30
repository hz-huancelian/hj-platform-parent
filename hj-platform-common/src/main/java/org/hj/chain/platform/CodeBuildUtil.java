package org.hj.chain.platform;

import cn.hutool.core.util.StrUtil;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description : 编号生成工具
 * @Author : chh
 * @Iteration :
 * @Date : 2021-04-27
 * @ModificationHistory Who          When          What
 */
public class CodeBuildUtil {

    public final static Map<String, String> map = Arrays.stream(new Object[][]{
            {"002003,002004", "G"},
            {"002002,0020021,0020022,", "Q"},
            {"002001,002005,", "A"},
            {"001001,001002,001003,001004,", "W"},
            {"003001,003002", "T"},
            {"005001,006001", "S"},
            {"004001,004002,004003,004004,004005", "N"}
    }).collect(Collectors.toMap(kv -> (String) kv[0], kv -> (String) kv[1]));

    /**
     * 报价单编号生成
     * @param prefix 机构简称
     * @param sort   机构整年报价单顺序号
     * @param sort  整年报价单（报价单总量）顺序号（跨年清零）
     * @return
     */
    public static String genOfferCode(String prefix, int sort) {
        return new StringBuilder()
                .append("BJ")
                .append(prefix)
                .append(LocalDate.now().toString().replaceAll("-", "").substring(2))
                .append("-")
                .append(String.format("%05d", sort))
                .toString();
    }

    /**
     * 合同编号生成
     *
     * @param prefix 机构简称
     * @param date   合同签订日期：YYMMDD
     * @param sort   机构整年合同顺序号
     * @param date  合同签订日期：YYMMDD
     * @param sort  整年合同（合同总量）顺序号（跨年清零）
     * @return
     */
    public static String genContractCode(String prefix, String date, int sort) {
        return new StringBuilder()
                .append("H")
                .append(prefix)
                .append(date)
                .append("-")
                .append(String.format("%05d", sort))
                .toString();
    }

    /**
     * 任务编号生成
     * @param prefix 机构简称
     * @param sort  当日任务顺序号（跨日清零）
     * @return
     */
    public static String genTaskCode(String prefix, int sort) {
        StringBuilder sb = new StringBuilder();
        sb.append("R").append(prefix)
                .append(LocalDate.now().toString().replaceAll("-", "").substring(2));
        if (sort < 10) {
            sb.append(String.format("%02d", sort));
        } else {
            sb.append(sort);
        }
        return sb.toString();
    }

    /**
     * 报告编号生成
     * @param prefix
     * @param sort
     * @param prefix 机构编号
     * @param sort 当日报告顺序号（跨日清零）
     * @return
     */
    public static String genReportCode(String prefix, int sort) {
        StringBuilder sb = new StringBuilder();
        sb.append("B").append(prefix)
                .append(LocalDate.now().toString().replaceAll("-", "").substring(2));
        if (sort < 10) {
            sb.append(String.format("%02d", sort));
        } else {
            sb.append(sort);
        }
        return sb.toString();
    }

    /**
     * @Description: TODO 生成用户编号
     * @return: java.lang.String
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/27 5:04 下午
     */
    public static String genUserId() {
        String uuid = StrUtil.uuid();
        return uuid.replace("-", "");
    }
    /**
     * @Author chh
     * @Description //TODO
     * @Date 2021-04-30 14:13
     * @Iteration 1.0
     * @param prefix 机构编号
     * @param type 二级分类别号：G无组织废气、Q锅（窑）炉废气/有组织废气、A环境空气/室内空气/；W:水；T:土壤、底泥；S:固废/污泥和生活垃圾；N：噪声。
      * @return
      */
//    public static String genSampleCode(String prefix, int sort, String type, int pointSort, int day, int frequency) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Y")
//                .append(prefix)
//                .append(LocalDate.now().toString().replaceAll("-","").substring(2))
//                .append(String.format("%02d", sort))
//                .append(getTypeBySedClassId(type))
//                .append(pointSort)
//                .append("-").append(day)
//                .append("-").append(frequency);
//        return sb.toString();
//    }

    public static String genSampleCode(String prefix, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix)
                .append(LocalDate.now().toString().replaceAll("-","").substring(2))
                .append(getTypeBySedClassId(type));
        return sb.toString();
    }

    public static String getTypeBySedClassId(String sci) {
        String val = null;
        for(Map.Entry<String, String> entry : map.entrySet()) {
            List<String> keys = Arrays.asList(entry.getKey().split(","));
            if(keys.contains(sci)) {
                val = entry.getValue();
                break;
            }
        }
        return val;
    }
}
