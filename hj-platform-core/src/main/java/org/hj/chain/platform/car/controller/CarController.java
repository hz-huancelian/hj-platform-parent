package org.hj.chain.platform.car.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.car.entity.CarInfo;
import org.hj.chain.platform.car.service.ICarInfoService;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.tdo.car.CarInfoEditTdo;
import org.hj.chain.platform.tdo.car.CarInfoTdo;
import org.hj.chain.platform.tdo.car.CarScheduTdo;
import org.hj.chain.platform.vo.car.CarInfoDisplayVo;
import org.hj.chain.platform.vo.car.CarInfoSearchVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private ICarInfoService carInfoService;

    /**
     * todo 条件分页查询车辆列表
     * @param pageVo
     * @param sv
     * @return
     */
    @RequestMapping(value = "/findByCondition", method = RequestMethod.GET)
    public Result<IPage<CarInfoDisplayVo>> findByCondition(@ModelAttribute PageVo pageVo,
                                                           @ModelAttribute CarInfoSearchVo sv) {
        sv.setCarBaseNumber(StrUtil.trimToNull(sv.getCarBaseNumber()));
        sv.setCarNumber(StrUtil.trimToNull(sv.getCarNumber()));
        sv.setCarStatus(StrUtil.trimToNull(sv.getCarStatus()));
        IPage<CarInfoDisplayVo> vos = carInfoService.findByCondition(pageVo, sv);
        return ResultUtil.data(vos);
    }

    /**
     * todo 根据设备ID删除车辆
     * @param id
     * @return
     */
    @RequestMapping(value = "/removeById/{id}", method = RequestMethod.DELETE)
    public Result<Object> removeById(@PathVariable Long id) {
        if(id == null) {
            return ResultUtil.validateError("车辆ID不能为空！");
        }
        CarInfo carInfo = carInfoService.getById(id);
        if(carInfo == null) {
            return ResultUtil.busiError("车辆信息不存在！");
        }
        String userId = (String) StpUtil.getLoginId();
        carInfoService.update(Wrappers.<CarInfo>lambdaUpdate()
                .set(CarInfo::getIsDelete, "1")
                .set(CarInfo::getUpdateTime, LocalDateTime.now())
                .set(CarInfo::getUpdateUserId, userId)
                .eq(CarInfo::getId, id));
        return ResultUtil.success("车辆删除成功！");
    }

    /**
     * todo 新增车辆信息
     * @param tdo
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<Object> add(@Validated @RequestBody CarInfoTdo tdo) {
        return carInfoService.add(tdo);
    }

    /**
     * todo 根据车辆ID更新车辆信息
     * @param tdo
     * @return
     */
    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public Result<Object> updateById(@Validated @RequestBody CarInfoEditTdo tdo) {
        CarInfo carInfo = carInfoService.getById(tdo.getId());
        if(carInfo == null) {
            return ResultUtil.busiError("车辆信息不存在！");
        }
        if(carInfo.getIsDelete().equals("1")) {
            return ResultUtil.busiError("已删除车辆无法更新信息！");
        }
        carInfo = new CarInfo();
        BeanUtils.copyProperties(tdo, carInfo);
        carInfo.setUpdateTime(LocalDateTime.now())
                .setUpdateUserId((String) StpUtil.getLoginId());
        carInfoService.updateById(carInfo);
        return ResultUtil.success("车辆信息更新成功！");
    }

    /**
     * todo 车辆调度
     * @param tdo
     * @return
     */
    @RequestMapping(value = "/carScheduling", method = RequestMethod.POST)
    public Result<Object> carScheduling(@Validated @RequestBody CarScheduTdo tdo) {
        return carInfoService.carScheduling(tdo);
    }
}
