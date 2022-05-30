package org.hj.chain.platform.approval.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.contract.service.IContractInfoService;
import org.hj.chain.platform.vo.approval.ContractAuditRecordVo;
import org.hj.chain.platform.vo.contract.ContractInfoVo;
import org.hj.chain.platform.vo.contract.ContractSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/21  5:03 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/21    create
 */
@RestController
@RequestMapping("/approval/contract")
public class ContApprovalController {

    @Autowired
    private IContractInfoService contractInfoService;

    /**
     * TODO 分页查询合同列表
     *
     * @param pageVo
     * @param sv
     * @Author chh
     * @Date 2021-05-09 18:05
     * @Iteration 1.0
     */
    @RequestMapping(value = "/findContInfosByCondition", method = RequestMethod.GET)
    public Result<IPage<ContractInfoVo>> findContInfosByCondition(@ModelAttribute PageVo pageVo,
                                                                  @ModelAttribute ContractSearchVo sv) {
        sv.setContCode(StrUtil.trimToNull(sv.getContCode()))
                .setContName(StrUtil.trimToNull(sv.getContName()))
                .setContStatus(StrUtil.trimToNull(sv.getContStatus()));
        sv.setContStatus("1");
        IPage<ContractInfoVo> vos = contractInfoService.findContApprovalsByCondition(pageVo, sv);
        return ResultUtil.data(vos);
    }


    @RequestMapping(value = "/findAuditRecordsByContId/{contId}", method = RequestMethod.GET)
    public Result<List<ContractAuditRecordVo>> findContInfosByCondition(@PathVariable Long contId) {

        if (contId == null) {
            return ResultUtil.validateError("合同ID不能为空！");
        }
        return contractInfoService.findAuditRecordsByContId(contId);
    }
}