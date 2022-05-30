package com.hj.chin.platform.sys;

import ch.qos.logback.core.net.SyslogOutputStream;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.hj.chain.platform.annotation.DeptAnnotation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/14  9:23 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/14    create
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@DeptAnnotation(deptkey = 1000)
public class AnnotationTest {


    @DeptAnnotation(deptkey = 10001)
    public void testA() {

        DeptAnnotation annotation = AnnotationUtils.findAnnotation(this.getClass(), DeptAnnotation.class);

        long deptkey = annotation.deptkey();
        System.out.println(deptkey);

//        this.getClass().
//
//        DeptAnnotation annotation1 = AnnotationUtils.findAnnotation(this.getClass().getDeclaredMethods(), DeptAnnotation.class);
//        System.out.println(annotation1.deptkey());
    }


    public static void main(String[] args) {

        new AnnotationTest().testA();
    }
}