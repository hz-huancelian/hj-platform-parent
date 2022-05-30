package org.hj.chain.platform.sample.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.sample.entity.SampleStoreApply;
import org.hj.chain.platform.tdo.sample.SampleStoreTdo;
import org.hj.chain.platform.vo.sample.SampleSearchVo;
import org.hj.chain.platform.vo.sample.SampleVo;

public interface ISampleStoreApplyService extends IService<SampleStoreApply> {

    IPage<SampleVo> getSampStoreApplyList(PageVo pageVo, SampleSearchVo sv);

    Result<Object> doStoreSample(SampleStoreTdo tdo);

    Result<Object> batchDoStoreSample(SampleStoreTdo tdo);
}
