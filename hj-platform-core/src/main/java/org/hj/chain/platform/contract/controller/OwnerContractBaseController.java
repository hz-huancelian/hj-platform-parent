package org.hj.chain.platform.contract.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.contract.service.ICusContractBaseInfoService;
import org.hj.chain.platform.contract.service.IOwnerContractBaseInfoService;
import org.hj.chain.platform.tdo.contract.CusContBaseAddTdo;
import org.hj.chain.platform.tdo.contract.CusContBaseModifyTdo;
import org.hj.chain.platform.tdo.contract.OwnerContractAddTdo;
import org.hj.chain.platform.tdo.contract.OwnerContractModifyTdo;
import org.hj.chain.platform.vo.contract.CusContBaseInfoListVo;
import org.hj.chain.platform.vo.contract.CusContBaseInfoVo;
import org.hj.chain.platform.vo.contract.OwnerContBaseInfoVo;
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
@RequestMapping("/owner/contract")
public class OwnerContractBaseController {

    @Autowired
    private IOwnerContractBaseInfoService ownerContractBaseInfoService;


    /**
     * TODO 机构合同基本信息新增
     *
     * @param addTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 11:51 上午
     */
    @RequestMapping(value = "/addOwnerContBase", method = RequestMethod.POST)
    public Result<Object> addOwnerContBase(@Validated @RequestBody OwnerContractAddTdo addTdo) {
        return ownerContractBaseInfoService.addCont(addTdo);
    }

    /**
     * TODO 机构合同基本信息修改
     *
     * @param modifyTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 11:51 上午
     */
    @RequestMapping(value = "/modifyOwnerContBaseById", method = RequestMethod.POST)
    public Result<Object> modifyOwnerContBaseById(@Validated @RequestBody OwnerContractModifyTdo modifyTdo) {
        return ownerContractBaseInfoService.modifyContById(modifyTdo);
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
    public Result<OwnerContBaseInfoVo> findById(@PathVariable Long id) {
        if (id == null) {
            return ResultUtil.validateError("主键ID不能为空！");
        }
        OwnerContBaseInfoVo infoVo = ownerContractBaseInfoService.findById(id);
        return ResultUtil.data(infoVo);

    }

    /**
     * TODO 根据ID查看合同基本信息详情
     *
     * @param organId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 11:55 上午
     */
    @RequestMapping(value = "/findByOrganId/{organId}", method = RequestMethod.GET)
    public Result<OwnerContBaseInfoVo> findByOrganId(@PathVariable String organId) {
        if (StrUtil.isBlank(organId)) {
            return ResultUtil.validateError("机构ID不能为空！");
        }
        OwnerContBaseInfoVo infoVo = ownerContractBaseInfoService.findByOrganId(organId);
        return ResultUtil.data(infoVo);

    }

}