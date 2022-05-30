package org.hj.chain.platform.report.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.report.mapper.ReportBaseInfoMapper;
import org.hj.chain.platform.report.model.ReportBaseInfo;
import org.hj.chain.platform.report.service.IReportBaseInfoService;
import org.hj.chain.platform.tdo.report.ReportBaseInfoTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportBaseInfoServiceImpl extends ServiceImpl<ReportBaseInfoMapper, ReportBaseInfo> implements IReportBaseInfoService {

    @Override
    public Result<Object> save(ReportBaseInfoTdo tdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        ReportBaseInfo rbi = this.getOne(Wrappers.<ReportBaseInfo>lambdaQuery()
                .eq(ReportBaseInfo::getOrganId, loginOutputVo.getOrganId()));
        LocalDateTime now = LocalDateTime.now();
        if(rbi != null) {
            BeanUtils.copyProperties(tdo, rbi);
            rbi.setUpdateUserId(loginOutputVo.getUserId()).setUpdateTime(now);
            this.updateById(rbi);
        }else{
            rbi = new ReportBaseInfo();
            BeanUtils.copyProperties(tdo, rbi);
            rbi.setOrganId(loginOutputVo.getOrganId()).setCreateUserId(loginOutputVo.getUserId()).setCreateTime(now);
            this.save(rbi);
        }
        return ResultUtil.success("报告基础模板信息保存成功！");
    }

    @Override
    public Result<ReportBaseInfo> getByOrganId() {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        ReportBaseInfo rbi = this.getOne(Wrappers.<ReportBaseInfo>lambdaQuery()
                .eq(ReportBaseInfo::getOrganId, loginOutputVo.getOrganId()));
        return ResultUtil.data(rbi);
    }
}
