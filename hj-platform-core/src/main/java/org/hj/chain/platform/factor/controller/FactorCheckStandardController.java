package org.hj.chain.platform.factor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.factor.excel.service.IFactorStandardExcelService;
import org.hj.chain.platform.factor.service.IFactorCheckStandardService;
import org.hj.chain.platform.tdo.factor.FactorCheckStandardModifyTdo;
import org.hj.chain.platform.vo.factor.FactorCheckStandardVo;
import org.hj.chain.platform.vo.factor.FactorSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 因子检测标准服务
 * @Iteration : 1.0
 * @Date : 2021/5/6  10:41 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/06    create
 */
@RestController
@RequestMapping(value = "/factor")
public class FactorCheckStandardController {

    @Autowired
    private IFactorCheckStandardService factorCheckStandardService;

    @Autowired
    private IFactorStandardExcelService factorStandardExportService;

    /**
     * TODO 分页查询
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 10:44 上午
     */
    @RequestMapping(value = "/findCheckStandardsByCondition", method = RequestMethod.GET)
    public Result<IPage<FactorCheckStandardVo>> findCheckStandardsByCondition(@ModelAttribute PageVo pageVo,
                                                                              @ModelAttribute FactorSearchVo sv) {
        IPage<FactorCheckStandardVo> page = factorCheckStandardService.findCheckStandardsByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }


    /**
     * TODO 修改能量表
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 10:47 上午
     */
    @RequestMapping(value = "/modifyCheckStandardById", method = RequestMethod.POST)
    public Result<Object> modifyCheckStandardById(@RequestBody FactorCheckStandardModifyTdo tdo) {
        return factorCheckStandardService.modifyCheckStandardById(tdo);
    }


    /**
     * TODO 导出
     *
     * @param response
     * @param sheetName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 5:14 下午
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(HttpServletResponse response,
                       @RequestParam String sheetName) throws IOException {
        factorStandardExportService.export(response, sheetName);
    }


    /**
     * 文件上传
     * <p>
     * 1. 创建excel对应的实体对象
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器
     * <p>
     * 3. 直接读即可
     */
    @RequestMapping(value = "importExcel", method = RequestMethod.POST)
    @ResponseBody
    public void importExcel(HttpServletResponse response,
                            @RequestParam("file") MultipartFile file) throws IOException {

        factorStandardExportService.importExcel(response, file);
    }
}