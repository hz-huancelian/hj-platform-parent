package org.hj.chain.platform.car.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.car.entity.CarInfo;
import org.hj.chain.platform.car.entity.CarRecordInfo;
import org.hj.chain.platform.car.mapper.CarInfoMapper;
import org.hj.chain.platform.car.service.ICarInfoService;
import org.hj.chain.platform.car.service.ICarRecordInfoService;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.car.CarInfoTdo;
import org.hj.chain.platform.tdo.car.CarScheduTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.car.CarInfoDisplayVo;
import org.hj.chain.platform.vo.car.CarInfoSearchVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarInfoServiceImpl extends ServiceImpl<CarInfoMapper, CarInfo> implements ICarInfoService {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ICarRecordInfoService carRecordInfoService;

    @Override
    public IPage<CarInfoDisplayVo> findByCondition(PageVo pageVo, CarInfoSearchVo sv) {
        List<CarInfoDisplayVo> vos = new ArrayList<>();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        Page<CarInfo> page = PageUtil.initMpPage(pageVo);
        this.page(page, Wrappers.<CarInfo>lambdaQuery()
                .eq(CarInfo::getOrganId, loginOutputVo.getOrganId())
                .eq(CarInfo::getIsDelete, "0")
                .eq(sv.getCarBaseNumber() != null, CarInfo::getCarBaseNumber, sv.getCarBaseNumber())
                .eq(sv.getCarNumber() != null, CarInfo::getCarNumber, sv.getCarNumber())
                .eq(sv.getCarStatus() != null, CarInfo::getCarStatus, sv.getCarStatus())
                .orderByAsc(CarInfo::getCarBaseNumber));
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            vos = page.getRecords().stream().map(item -> {
                CarInfoDisplayVo vo = new CarInfoDisplayVo();
                BeanUtils.copyProperties(item, vo);
                String createUser = sysUserService.findUserByUserId(item.getCreateUserId()).getEmpName();
                vo.setCreateUser(createUser);
                if(item.getUpdateUserId() != null) {
                    String updateUser = sysUserService.findUserByUserId(item.getUpdateUserId()).getEmpName();
                    vo.setUpdateUser(updateUser);
                }
                return vo;
            }).collect(Collectors.toList());
        }
        return PageUtil.convertPageVo(page, vos);
    }

    @Override
    public Result<Object> add(CarInfoTdo tdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        CarInfo carInfo = new CarInfo();
        BeanUtils.copyProperties(tdo, carInfo);
        carInfo.setCreateUserId(loginOutputVo.getUserId())
                .setOrganId(loginOutputVo.getOrganId())
                .setCreateTime(LocalDateTime.now())
                .setIsDelete("0");
        this.save(carInfo);
        return ResultUtil.success("车辆信息添加成功！");
    }

    @Transactional
    @Override
    public Result<Object> carScheduling(CarScheduTdo tdo) {
        String carStatus = tdo.getCarStatus();
        String location = tdo.getLocation();
        String username = tdo.getUsername();
        String linkmethod = tdo.getLinkmethod();
        if(!carStatus.equals("2")) {
            if(StrUtil.isBlank(location)) {
                return ResultUtil.busiError("车辆位置不能为空！");
            }
            if(carStatus.equals("1")) {
                if(StrUtil.isBlank(username)) {
                    return ResultUtil.busiError("使用人员不能为空！");
                }
                if(StrUtil.isBlank(linkmethod)) {
                    return ResultUtil.busiError("使用人员联系方式不能为空！");
                }
            }
        }
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LocalDateTime now = LocalDateTime.now();
        CarRecordInfo cri = new CarRecordInfo();
        BeanUtils.copyProperties(tdo, cri);
        cri.setCreateUserId(loginOutputVo.getUserId()).setCreateTime(now);
        carRecordInfoService.save(cri);
        this.update(Wrappers.<CarInfo>lambdaUpdate()
                .set(CarInfo::getCarStatus, carStatus)
                .set(CarInfo::getLocation, location)
                .set(CarInfo::getUsername, username)
                .set(CarInfo::getLinkmethod, linkmethod)
                .set(CarInfo::getUpdateTime, now)
                .set(CarInfo::getUpdateUserId, loginOutputVo.getUserId())
                .eq(CarInfo::getId, tdo.getCarId()));
        return ResultUtil.success("车辆调度成功！");
    }
}
