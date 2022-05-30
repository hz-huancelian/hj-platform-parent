package org.hj.chain.platform.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Exrickx
 */
@Data
public class PageVo implements Serializable {

    private static final long serialVersionUID = 1L;
    //页号
    private int pageNumber;

    //页面大小
    private int pageSize;

    //排序字段
    private String sort;

    //排序方式 asc/desc
    private String order;
}
