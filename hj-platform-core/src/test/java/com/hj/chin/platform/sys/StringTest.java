package com.hj.chin.platform.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hj.chain.platform.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/11/10  5:16 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/11/10    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class StringTest {


    @Test
    public void testSubString(){
        String str = "YNJSB211203N-0-1";
        String res = str.substring(0, str.lastIndexOf("-"));
        System.out.println(res);

        Map<String,String> strMap = new HashMap<>();
        strMap.put("2021-09-10","A");
        strMap.put("2021-09-09","B");
        strMap.put("2021-09-11","C");

        strMap.forEach((key,val)->{
            System.out.println(key);
        });

        LocalDate now = LocalDate.now();
        String dateStr = DateUtils.getDateStr(now, "yyyy年MM月dd日");
        System.out.println(dateStr);

        List<String> aList = new ArrayList<>();
        aList.add("aa");
        aList.add("aa");
        aList.add("aa");
        aList.add("aa");
        List<String> bList = new ArrayList<>();
        bList.add("bb");
        bList.add("bb");
        bList.add("bb");

        List<String> cList = new ArrayList<>();
        cList.add("bb");
        cList.add("bb");
        cList.add("bb");
        List<List<String>> totalList = new ArrayList<>();
        totalList.add(aList);
        totalList.add(bList);
        totalList.add(cList);

        int sum = totalList.stream().mapToInt(item -> item.size()).sum();
        System.out.println(sum);
    }
}