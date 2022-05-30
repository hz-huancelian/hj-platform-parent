package org.hj.chain.platform.contract.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.FileUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.contract.service.IContractInfoService;
import org.hj.chain.platform.tdo.contract.ContractAuditTdo;
import org.hj.chain.platform.tdo.contract.ContractSave2Tdo;
import org.hj.chain.platform.tdo.contract.ContractSaveTdo;
import org.hj.chain.platform.vo.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;


/**
 * @Project : hj-platform-parent
 * @Description : TODO 合同管理
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-09
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-09
 */
@RestController
@RequestMapping("/cont")
public class ContractController {
    @Autowired
    private IContractInfoService contractInfoService;
    @Value("${file.ht.upload}")
    private String uploadContPath;

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
                .setContStatus(StrUtil.trimToNull(sv.getContStatus()))
                .setSupContCode(StrUtil.trimToNull(sv.getSupContCode()));
        sv.setContStatusIds(Arrays.asList("0", "1", "2", "3", "6"));
        IPage<ContractInfoVo> vos = contractInfoService.findContInfosByCondition(pageVo, sv);
        return ResultUtil.data(vos);
    }


    /**
     * TODO 分页查询历史合同列表 （作废和完成）
     *
     * @param pageVo
     * @param sv
     * @Author chh
     * @Date 2021-05-09 18:05
     * @Iteration 1.0
     */
    @RequestMapping(value = "/findHisContInfosByCondition", method = RequestMethod.GET)
    public Result<IPage<ContractInfoVo>> findHisContInfosByCondition(@ModelAttribute PageVo pageVo,
                                                                     @ModelAttribute ContractSearchVo sv) {
        sv.setContCode(StrUtil.trimToNull(sv.getContCode()))
                .setContName(StrUtil.trimToNull(sv.getContName()))
                .setContStatus(StrUtil.trimToNull(sv.getContStatus()));
        sv.setContStatusIds(Arrays.asList("4", "5"));
        IPage<ContractInfoVo> vos = contractInfoService.findContInfosByCondition(pageVo, sv);
        return ResultUtil.data(vos);
    }


    /**
     * TODO 分页查询分包合同列表
     *
     * @param pageVo
     * @param sv
     * @Author chh
     * @Date 2021-05-09 18:05
     * @Iteration 1.0
     */
    @RequestMapping(value = "/findSubContInfosByCondition", method = RequestMethod.GET)
    public Result<IPage<SubcontractVo>> findSubContInfosByCondition(@ModelAttribute PageVo pageVo,
                                                                    @ModelAttribute ContractSearchVo sv) {
        sv.setContCode(StrUtil.trimToNull(sv.getContCode()))
                .setContName(StrUtil.trimToNull(sv.getContName()))
                .setContStatus(StrUtil.trimToNull(sv.getContStatus()));
        IPage<SubcontractVo> vos = contractInfoService.findSubContInfosByCondition(pageVo, sv);
        return ResultUtil.data(vos);
    }

    /**
     * TODO 合同完善信息查看
     *
     * @param contId
     * @Author chh
     * @Date 2021-05-09 18:39
     * @Iteration 1.0
     */
    @RequestMapping(value = "/findContPerfectInfoById/{contId}", method = RequestMethod.GET)
    public Result<ContPerfectQryVo> findContPerfectInfoById(@PathVariable Long contId) {
        if (contId == null) {
            return ResultUtil.validateError("合同ID不能为空！");
        }
        return contractInfoService.findContPerfectInfoById(contId);
    }

    /**
     * TODO 合同信息完善（在线制作）
     *
     * @param tdo
     * @Author chh
     * @Date 2021-05-09 18:39
     * @Iteration 1.0
     */
    @RequestMapping(value = "/saveCont", method = RequestMethod.POST)
    public Result<Object> saveContractInfo(@Validated @RequestBody ContractSaveTdo tdo) {
        return contractInfoService.pefectContOnline(tdo);
    }

    /**
     * TODO 合同文件上传
     *
     * @param file
     * @Author chh
     * @Date 2021-05-10 23:04
     * @Iteration 1.0
     */
    @RequestMapping(value = "/uploadContFile", method = RequestMethod.POST)
    public Result<Object> uploadContFile(@RequestParam("file") MultipartFile file) {
        String fileName = FileUtil.fileUpload(file, uploadContPath);
        if (fileName == null) {
            return ResultUtil.busiError("程序错误，请重新上传！");
        }
        return ResultUtil.data(fileName);
    }

    /**
     * TODO 合同信息完善（本地上传）
     *
     * @param tdo
     * @Author chh
     * @Date 2021-05-09 19:10
     * @Iteration 1.0
     */
    @RequestMapping(value = "/saveCont2", method = RequestMethod.POST)
    public Result<Object> saveContractInfo2(@Validated @RequestBody ContractSave2Tdo tdo) {

        return contractInfoService.perfectContByUpload(tdo);
    }


    /**
     * TODO 合同提交审核
     *
     * @param contId 合同ID
     * @Author chh
     * @Date 2021-05-09 19:14
     * @Iteration 1.0
     */
    @RequestMapping(value = "/submitCont/{contId}", method = RequestMethod.GET)
    public Result<Object> submitCont(@PathVariable Long contId) {
        if (contId == null) {
            return ResultUtil.validateError("合同ID不能为空！");
        }
        return contractInfoService.submitCont(contId);
    }

    /**
     * TODO 合同审核
     *
     * @param tdo
     * @Author chh
     * @Date 2021-05-09 18:55
     * @Iteration 1.0
     */
    @RequestMapping(value = "/auditCont", method = RequestMethod.POST)
    public Result<Object> auditCont(@Validated @RequestBody ContractAuditTdo tdo) {
        return contractInfoService.auditCont(tdo);
    }

    /**
     * TODO 合同作废
     *
     * @param contId 合同ID
     * @Author chh
     * @Date 2021-05-09 18:58
     * @Iteration 1.0
     */
    @RequestMapping(value = "/invalidCont/{contId}", method = RequestMethod.GET)
    public Result<Object> invalidCont(@PathVariable Long contId) {
        if (contId == null) {
            return ResultUtil.validateError("合同ID不能为空！");
        }
        return contractInfoService.invalidCont(contId);
    }

    /**
     * TODO 合同恢复
     *
     * @param contId 合同ID
     * @Author chh
     * @Date 2021-05-09 18:59
     * @Iteration 1.0
     */
    @RequestMapping(value = "/restoreCont/{contId}", method = RequestMethod.GET)
    public Result<Object> restoreCont(@PathVariable Long contId) {
        if (contId == null) {
            return ResultUtil.validateError("合同ID不能为空！");
        }
        return contractInfoService.restoreCont(contId);
    }

    /**
     * TODO 合同附件
     *
     * @param contId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/22 9:10 下午
     */
    @RequestMapping(value = "/findContFileById/{contId}", method = RequestMethod.GET)
    public Result<String> findContFileById(@PathVariable Long contId) {
        if (contId == null) {
            return ResultUtil.validateError("合同ID不能为空！");
        }
        return contractInfoService.findContFileById(contId);
    }


    @RequestMapping(value = "/uploadSubContFile", method = RequestMethod.POST)
    public Result<Object> uploadSubContFile(@RequestParam("file") MultipartFile file,
                                            @RequestParam Long id,
                                            @RequestParam String signDate,
                                            @RequestParam String validity) {
        if (id == null) {
            return ResultUtil.validateError("合同ID不能为空！");
        }

        if (StrUtil.isBlank(signDate)) {
            return ResultUtil.validateError("签订日期不能为空！");
        }

        if (StrUtil.isBlank(validity)) {
            return ResultUtil.validateError("有效期不能为空！");
        }

       return  contractInfoService.uploadSubContFile(file,id,signDate,validity);

    }

    @RequestMapping(value = "/checkSupContCode", method = RequestMethod.GET)
    public Result<Object> checkSupContCode(@RequestParam String supContCode) {
        if(StrUtil.isBlank(supContCode)) {
            return ResultUtil.validateError("主合同编号不能为空！");
        }
        return contractInfoService.checkSupContCode(supContCode);
    }

}
