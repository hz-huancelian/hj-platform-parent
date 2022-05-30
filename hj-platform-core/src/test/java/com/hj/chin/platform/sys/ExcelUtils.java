package com.hj.chin.platform.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hj.chain.platform.common.MonitorFreqEnum;
import org.junit.jupiter.api.Test;

import javax.sound.midi.Soundbank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/15  10:37 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/15    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ExcelUtils {

    @Test
    public void testDate(){
        MonitorFreqEnum[] values = MonitorFreqEnum.values();
        Arrays.stream(values).forEach(item-> System.out.println(item));
        String strs = "中国、n";
        System.out.println(strs.substring(3));

        String contDate = "2021-07-21";
        String s = contDate.replaceAll("-", "");
        System.out.println(s);

        System.out.println("10\u00B9");

        String ss = "yyy/aa";
        String[] split = ss.split("/");
        System.out.println(split[0]);
        System.out.println(split[1]);
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = now.toLocalDate();
        System.out.println(localDate.toString());

        Set<Integer>  set = new HashSet<>();
        set.add(1);
        set.add(3);
        set.add(7);
        set.add(5);
        set.add(4);
        set.stream().forEach(item-> System.out.println(item));
        List<Integer> list = new ArrayList<>(set);
        System.out.println(list);
        int i = list.indexOf(5);
        System.out.println(i);
    }


    @Test
    public void genExcel() {

        List<String> list = new ArrayList<>();
        list.add("aa");
        list.add("bb");
        list.add("cc");
        list.add("dd");
        List<String> list2 = list;
        list2.add("ee");

        System.out.println(list2.size());
        System.out.println(list.size());

        String sampCode = "YJSBE210523W030";
        sampCode = sampCode.substring(sampCode.length() - 4);

        System.out.println(sampCode);
    }
}