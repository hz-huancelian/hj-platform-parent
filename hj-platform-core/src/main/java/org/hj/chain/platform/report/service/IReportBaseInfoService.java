package org.hj.chain.platform.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.report.model.ReportBaseInfo;
import org.hj.chain.platform.tdo.report.ReportBaseInfoTdo;

public interface IReportBaseInfoService extends IService<ReportBaseInfo> {
    Result<Object> save(ReportBaseInfoTdo tdo);

    Result<ReportBaseInfo> getByOrganId();
}
