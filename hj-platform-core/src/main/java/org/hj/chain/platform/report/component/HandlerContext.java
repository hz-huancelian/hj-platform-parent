package org.hj.chain.platform.report.component;

import org.hj.chain.platform.report.biz.IReportStrategy;

import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/6/8  9:26 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/06/08    create
 */
public class HandlerContext {

    //二级类别对应的bean
    private Map<String, Class> handlerMap;

    public HandlerContext(Map<String, Class> handlerMap) {
        this.handlerMap = handlerMap;
    }

    /**
     * TODO 获取SpringBean
     *
     * @param secdClassId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/6/8 9:41 下午
     */
    public IReportStrategy getInstance(String secdClassId) {
        return BeanTools.getBean((Class<IReportStrategy>) handlerMap.get(secdClassId));
    }
}