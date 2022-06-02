package org.hj.chain.platform.factor.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.factor.entity.FactorRain;
import org.hj.chain.platform.factor.mapper.FactorRainMapper;
import org.hj.chain.platform.factor.service.FactorRainService;
import org.hj.chain.platform.model.FactorClassInfo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.factor.FactorRainSearchVo;
import org.hj.chain.platform.vo.factor.FactorRainVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : zzl
 * @description 雨水因子服务实现类
 * @Date : 2022.6.1
 */
@Slf4j
@Service
public class FactorRainServiceImpl extends ServiceImpl<FactorRainMapper, FactorRain> implements FactorRainService {

    @Autowired
    private FactorRainService factorRainService;

    @Autowired
    private FactorRainMapper factorRainMapper;

    @Override
    public IPage<FactorRainVo> findFactorRainByCondition(PageVo pageVo, FactorRainSearchVo fr) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        // 保证雨水表数据同步性，需先删除在将查询出的数据插入
        List<FactorRain> factorRainIds = factorRainMapper.findFactorsRainByIdCondition();
        if (factorRainIds != null && !factorRainIds.isEmpty()){
            List<Long> ids = factorRainIds.stream().map(item -> item.getId()).collect(Collectors.toList());
            int count = factorRainMapper.delete(Wrappers.<FactorRain>lambdaQuery().in(FactorRain::getId, ids));
            if (count > 0) {
                log.info("雨水因子表数据删除成功!");
            }
        }
        // 批量插入
        List<FactorRainVo> factorRainVos = factorRainMapper.findFactorsRainByCondition(loginOutputVo.getOrganId(), fr);
        List<FactorRain> factorRains = factorRainVos.stream().map(d -> {
            FactorRain factorRain = new FactorRain();
            BeanUtils.copyProperties(d, factorRain);
            factorRain.setCreateTime(LocalDateTime.now());
            factorRain.setIsDeleted(0);
            return factorRain;
        }).collect(Collectors.toList());
        factorRainService.saveBatch(factorRains);
        Page<FactorRainVo> page = PageUtil.initMpPage(pageVo);
        this.baseMapper.findFactorsRainByCondition(page, loginOutputVo.getOrganId(), fr);
        return page;
    }

    @Override
    public IPage<FactorClassInfo> findFactorClassInfoCondition(PageVo pageVo) {
        Page<FactorClassInfo> page = PageUtil.initMpPage(pageVo);
        this.baseMapper.findFactorsClassInfoCondition(page);
        return page;
    }

}
