package org.hj.chain.platform.contract.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.RegexUtils;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.contract.service.ICusContractBaseInfoService;
import org.hj.chain.platform.tdo.contract.CusContBaseAddTdo;
import org.hj.chain.platform.tdo.contract.CusContBaseModifyTdo;
import org.hj.chain.platform.vo.contract.CusContBaseInfoListVo;
import org.hj.chain.platform.vo.contract.CusContBaseInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 客户合同基础信息交互
 * @Iteration : 1.0
 * @Date : 2021/5/14  11:47 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/14    create
 */
@RestController
@RequestMapping("/cus/contract")
public class CusContractBaseController {

    @Autowired
    private ICusContractBaseInfoService cusContractBaseInfoService;

    /**
     * TODO 分页查询
     *
     * @param pageVo
     * @param companyName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 11:50 上午
     */
    @RequestMapping(value = "/findByCondition", method = RequestMethod.GET)
    public Result<IPage<CusContBaseInfoListVo>> findByCondition(@ModelAttribute PageVo pageVo,
                                                                @RequestParam String companyName) {

        IPage<CusContBaseInfoListVo> page = cusContractBaseInfoService.findByCondition(pageVo, companyName);
        return ResultUtil.data(page);

    }

    /**
     * TODO 客户合同基本信息新增
     *
     * @param addTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 11:51 上午
     */
    @RequestMapping(value = "/addCusContBase", method = RequestMethod.POST)
    public Result<Object> addCusContBase(@Validated @RequestBody CusContBaseAddTdo addTdo) {
        //企业税号验证
//        if(!RegexUtils.isLicense18(addTdo.getTaxNumber())) {
//            return ResultUtil.validateError("纳税人识别号/统一社会信用代码格式不正确，请重新输入！");
//        }
        //银行账号校验
//        if(!RegexUtils.checkBankCard(addTdo.getBankNo())) {
//            return ResultUtil.validateError("银行账号格式不正确，请重新输入！");
//        }
        Long res = cusContractBaseInfoService.addCusContBase(addTdo);
        if (res < 0) {
            return ResultUtil.busiError("该公司已经登记！");
        }
        return ResultUtil.success("新增成功！");
    }

    /**
     * TODO 客户合同基本信息修改
     *
     * @param modifyTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 11:51 上午
     */
    @RequestMapping(value = "/modifyCusContBaseById", method = RequestMethod.POST)
    public Result<Object> modifyCusContBaseById(@Validated @RequestBody CusContBaseModifyTdo modifyTdo) {
        return cusContractBaseInfoService.modifyCusContBaseById(modifyTdo);
    }

    /**
     * TODO 根据ID查看合同基本信息详情
     *
     * @param id
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 11:55 上午
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Result<CusContBaseInfoVo> findById(@PathVariable Long id) {
        if (id == null) {
            return ResultUtil.validateError("主键ID不能为空！");
        }
        CusContBaseInfoVo infoVo = cusContractBaseInfoService.findById(id);
        return ResultUtil.data(infoVo);

    }

    @RequestMapping(value = "/fuzzyQuery/{companyName}", method = RequestMethod.GET)
    public Result<Map<Long, String>> fuzzyQuery(@PathVariable String companyName) {
        if (StrUtil.isBlank(companyName)) {
            return ResultUtil.validateError("公司名称不能为空！");
        }
        Map<Long, String> cusContMap = cusContractBaseInfoService.fuzzyQuery(companyName);
        return ResultUtil.data(cusContMap);

    }

    @RequestMapping(value = "/removeById/{id}", method = RequestMethod.DELETE)
    public Result<Object> removeById(@PathVariable Long id) {
        if(id == null) {
            return ResultUtil.validateError("客户ID不能为空！");
        }
        return cusContractBaseInfoService.removeById(id);
    }
}