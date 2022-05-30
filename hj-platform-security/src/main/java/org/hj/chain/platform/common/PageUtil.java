package org.hj.chain.platform.common;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Arrays;
import java.util.List;


/**
 * @Description: 分页工具
 * @Param:
 * @return:
 * @Author: lijinku
 * @Iteration : 1.0
 * @Date: 2019/7/26 1:27 AM
 */
public class PageUtil {

    /**
     * @Description: 转换分页信息
     * @Param: [page, records]
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<T>
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2019/8/3 1:00 AM
     */
    public static <T,R> Page<T> convertPageVo(Page<R> page, List<T> records){
        Page<T> pageRs = new Page<>();
        pageRs.setRecords(records)
                .setPages(page.getPages())
                .setCurrent(page.getCurrent())
                .setTotal(page.getTotal())
                .setSize(page.getSize());
        return pageRs;
    }

    /**
     * Mybatis-Plus分页封装
     *
     * @param page
     * @returnß
     */
    public static <T> Page<T> initMpPage(PageVo page) {

        Page p = null;
        int pageNumber = page.getPageNumber();
        int pageSize = page.getPageSize();
        String sort = page.getSort();
        String order = page.getOrder();

        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        if (StrUtil.isNotBlank(sort)) {
            Boolean isAsc = false;
            if (StrUtil.isBlank(order)) {
                isAsc = false;
            } else {
                if ("desc".equals(order.toLowerCase())) {
                    isAsc = false;
                } else if ("asc".equals(order.toLowerCase())) {
                    isAsc = true;
                }
            }
            p = new Page<T>(pageNumber, pageSize);
            OrderItem item = new OrderItem();
            item.setAsc(isAsc);
            item.setColumn(sort);
            p.setOrders(Arrays.asList(item));

        } else {
            p = new Page<T>(pageNumber, pageSize);
        }
        return p;
    }


}
