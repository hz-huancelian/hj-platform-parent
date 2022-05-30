package com.hj.chin.platform.sys;

import javafx.print.Printer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2022/3/24  3:19 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2022/03/24    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class BigDecimalTest {


    public static void main(String[] args) {

        String a = "12|13|";

        String[] split = a.split("\\|");
        Arrays.stream(split).forEach(System.out::println);
        AtomicReference<BigDecimal> singleAmount = new AtomicReference<BigDecimal>(new BigDecimal(0));

        BigDecimal decimal = new BigDecimal(100);
        singleAmount.accumulateAndGet(decimal,BigDecimal::add);
        System.out.println(singleAmount.get());
    }
}