package org.hj.chain.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.model.DynamicParamConf;
import org.hj.chain.platform.tdo.DynamicParamConfTdo;
import org.hj.chain.platform.vo.FactorSecdClassVo;

import java.util.List;

public interface IDynamicParamConfService extends IService<DynamicParamConf> {
    Result<Object> add(DynamicParamConfTdo tdo);

    Result<List<FactorSecdClassVo>> getAllFactorSecdClass();
}
