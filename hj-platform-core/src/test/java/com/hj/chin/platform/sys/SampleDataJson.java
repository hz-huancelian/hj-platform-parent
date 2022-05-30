package com.hj.chin.platform.sys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2022/4/7  10:37 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/04/07    create
 */
public class SampleDataJson {

    @Test
    public void factorJson() {
        String str = "{\"checkEquipment\":\"\",\"calibrationEquipment\":\"\",\"positioningOne\":\"\",\"positioningTwo\":\"\",\"positioningThree\":\"\",\"theoreticalVal\":\"\",\"groundConditions\":\"\",\"100004-0002\":\"19\"}";
        Map<String, Object> parse = (Map) JSON.parse(str);
        parse.forEach((key,val)->{
            System.out.println(key);
            System.out.println(val);
        });
        System.out.println(parse);

    }

    @Test
    public void jsonParse() {

        String str = "{\"collectDate\":{\"numericType\":\"1\",\"value\":[{\"desc\":\"采样日期\",\"val\":\"2022-04-25\"}]},\"collectTime\":{\"numericType\":\"1\",\"value\":[{\"desc\":\"采样时间\",\"val\":\"2022-04-25 14:27:15\"}]},\"sampBasis\":{\"numericType\":\"1\",\"value\":[{\"desc\":\"采样依据\",\"val\":\"HJ 91.1-2019 污水监测技术规范\"}]},\"sampleNum\":{\"numericType\":\"0\",\"value\":[{\"desc\":\"样品数量\",\"val\":\"1\"}]},\"collectRemark\":{\"numericType\":\"1\",\"value\":[{\"desc\":\"备注\",\"val\":\"1\"}]},\"organoleptic\":{\"numericType\":\"1\",\"value\":[{\"desc\":\"感观描述\",\"val\":\"同\"}]},\"processConditions\":{\"numericType\":\"1\",\"value\":[{\"desc\":\"工艺工况\",\"val\":\"我们\"}]},\"wastewater\":{\"numericType\":\"1\",\"value\":[{\"desc\":\"废水处理设施运行情况\",\"val\":\"正常\"}]}}";

        Map<String, JSONObject> parse = (Map) JSON.parse(str);
        parse.forEach((key, val) -> {
            System.out.println(key);
            System.out.println(val);

//            SampleDataItemParam value = val.toJavaObject(SampleDataItemParam.class);
//            System.out.println(value);
        });

        System.out.println(parse);
    }
}