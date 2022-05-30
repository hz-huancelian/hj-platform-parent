package org.hj.chain.platform.report.component;

import cn.hutool.core.lang.ClassScaner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/6/8  8:26 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/08    create
 */
@Component
public class HandlerProcessor implements BeanFactoryPostProcessor {

    //扫描路径
    private static final String PACKAGE_SCAN = "org.hj.chain.platform.report.biz";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, Class> handlerMap = new HashMap<>();
        ClassScaner.scanPackageByAnnotation(PACKAGE_SCAN, SecdClassType.class).forEach(clazz -> {
            String type = clazz.getAnnotation(SecdClassType.class).value();
            if (type != null) {
                String[] secdClasses = type.split(",");
                Arrays.stream(secdClasses).forEach(secdClass -> {
                    handlerMap.put(secdClass, clazz);
                });
            }
        });

        //初始化handlerContext,并注册到Spring容器中
        HandlerContext handlerContext = new HandlerContext(handlerMap);
        beanFactory.registerSingleton(HandlerContext.class.getName(), handlerContext);
    }
}