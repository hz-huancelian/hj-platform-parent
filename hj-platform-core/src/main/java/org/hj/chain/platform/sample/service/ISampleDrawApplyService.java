package org.hj.chain.platform.sample.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.sample.entity.SampleDrawApply;
import org.hj.chain.platform.vo.sample.SampleSearchVo;
import org.hj.chain.platform.vo.sample.SampleVo;
import org.hj.chain.platform.word.HandoverTableData;

import java.util.List;

public interface ISampleDrawApplyService extends IService<SampleDrawApply> {

    IPage<SampleVo> getSampDrawApplyList(PageVo pageVo, SampleSearchVo sv);

    Result<Object> doDrawSample(String drawApplyId);


    /**
     * TODO 根据样品获取交接单数据
     *
     * @param sampIds
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/4/7 5:22 下午
     */
    HandoverTableData findHandoverTableDataBySampIds(List<String> sampIds);
}
