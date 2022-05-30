package org.hj.chain.platform.car.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.car.entity.CarInfo;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.tdo.car.CarInfoTdo;
import org.hj.chain.platform.tdo.car.CarScheduTdo;
import org.hj.chain.platform.vo.car.CarInfoDisplayVo;
import org.hj.chain.platform.vo.car.CarInfoSearchVo;

public interface ICarInfoService extends IService<CarInfo> {
    IPage<CarInfoDisplayVo> findByCondition(PageVo pageVo, CarInfoSearchVo sv);

    Result<Object> add(CarInfoTdo tdo);

    Result<Object> carScheduling(CarScheduTdo tdo);
}
