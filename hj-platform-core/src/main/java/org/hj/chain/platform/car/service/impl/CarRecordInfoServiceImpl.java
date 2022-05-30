package org.hj.chain.platform.car.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.car.entity.CarRecordInfo;
import org.hj.chain.platform.car.mapper.CarRecordInfoMapper;
import org.hj.chain.platform.car.service.ICarRecordInfoService;
import org.springframework.stereotype.Service;

@Service
public class CarRecordInfoServiceImpl extends ServiceImpl<CarRecordInfoMapper, CarRecordInfo> implements ICarRecordInfoService {
}
