package org.hj.chain.platform.factor.excel.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hj.chain.platform.DateUtils;
import org.hj.chain.platform.common.ExcelUtils;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.factor.entity.FactorCheckStandard;
import org.hj.chain.platform.factor.excel.ExcelErrorVo;
import org.hj.chain.platform.factor.excel.ExcelFactorCheckListener;
import org.hj.chain.platform.factor.excel.FactorCheckExportVo;
import org.hj.chain.platform.factor.excel.FactorCheckImportVo;
import org.hj.chain.platform.factor.excel.service.IFactorStandardExcelService;
import org.hj.chain.platform.factor.mapper.FactorCheckStandardMapper;
import org.hj.chain.platform.factor.service.IFactorCheckStandardService;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.vo.FactorMethodInfoVo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/7  4:57 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/07    create
 */
@Service
public class FactorStandardExcelServiceImpl implements IFactorStandardExcelService {

    @Autowired
    private FactorCheckStandardMapper factorCheckStandardMapper;
    @Autowired
    private IFactorCheckStandardService factorCheckStandardService;
    @Autowired
    private IFactorService factorService;

    @Override
    public void export(HttpServletResponse response, String sheetName) throws IOException {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<FactorCheckStandard> standards = factorCheckStandardMapper.selectList(Wrappers.<FactorCheckStandard>lambdaQuery()
                .eq(FactorCheckStandard::getOrganId, organId));
        List<FactorCheckExportVo> vos = new ArrayList<>();
        if (standards != null && !standards.isEmpty()) {
            vos = standards.stream().map(item -> {
                FactorCheckExportVo vo = new FactorCheckExportVo();
                vo.setStandardCode(item.getStandardCode());
                vo.setCmaFlg((item.getCmaFlg().equals("0") ? "N" : "Y"));
                vo.setCnasFlg((item.getCnasFlg().equals("0") ? "N" : "Y"));
                vo.setPrice(item.getPrice());
                FactorMethodInfoVo methodInfoVo = factorService.findFactorMethodById(item.getStandardCode());
                if (methodInfoVo != null) {
                    vo.setFactorName(methodInfoVo.getFactorName());
                    vo.setCheckClass(methodInfoVo.getClassName());
                    vo.setStandardNo(methodInfoVo.getStandardNo());
                    vo.setStandardName(methodInfoVo.getStandardName());
                    vo.setAnalysisMethod(methodInfoVo.getAnalysisMethod());
                    String status = methodInfoVo.getStatus();
                    //0-现行；1-自定义标准 2试行；3暂行；4废止
                    String methodStatus = "废止";
                    if (status.equals("0")) {
                        methodStatus = "现行";
                    } else if (status.equals("1")) {
                        methodStatus = "自定义标准";
                    } else if (status.equals("2")) {
                        methodStatus = "试行";
                    } else if (status.equals("3")) {
                        methodStatus = "暂行";
                    }
                    vo.setMethodStatus(methodStatus);
                    vo.setFactorUnit(methodInfoVo.getDefaultUnitName());
                }
                return vo;
            }).collect(Collectors.toList());

        }
        String dateStr = DateUtils.getDefaultFormatDateStr(LocalDateTime.now());
        ExcelUtils.write(response, FactorCheckExportVo.class, vos, "导出模版" + dateStr, "因子信息模板");
    }

    @Override
    public void importExcel(HttpServletResponse response, MultipartFile file) throws IOException {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<ExcelErrorVo> errCodes = new ArrayList<>();
        EasyExcel.read(file.getInputStream(), FactorCheckImportVo.class, new ExcelFactorCheckListener(factorCheckStandardService, loginOutputVo.getOrganId(), errCodes)).sheet().doRead();
        if (!errCodes.isEmpty()) {
            String dateStr = DateUtils.getDefaultFormatDateStr(LocalDateTime.now());
            ExcelUtils.write(response, ExcelErrorVo.class, errCodes, "导入异常" + dateStr, "错误详情");
        }
    }
}