package org.hj.chain.platform.report.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/6/8  9:34 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/08    create
 */
@Component
public class BeanTools implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }


    /**
     * TODO 获取bean
     *
     * @param clazz
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/6/8 9:36 下午
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }
}