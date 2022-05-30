package org.hj.chain.platform.factor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.factor.service.IFactorGroupService;
import org.hj.chain.platform.tdo.factor.FactorGroupAddTdo;
import org.hj.chain.platform.tdo.factor.FactorGroupModifyTdo;
import org.hj.chain.platform.vo.factor.FactorGroupItemVo;
import org.hj.chain.platform.vo.factor.FactorGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 因子套餐服务
 * @Iteration : 1.0
 * @Date : 2021/5/6  11:51 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/06    create
 */
@RestController
@RequestMapping("/factor/group")
public class FactorGroupController {

    @Autowired
    private IFactorGroupService factorGroupService;


    /**
     * TODO 分页查询套餐信息
     *
     * @param pageVo
     * @param groupName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 9:06 上午
     */
    @RequestMapping(value = "/findPageByCondition", method = RequestMethod.GET)
    public Result<IPage<FactorGroupVo>> findPageByCondition(@ModelAttribute PageVo pageVo,
                                                            @RequestParam String groupName,
                                                            @RequestParam String groupType,
                                                            @RequestParam String authType) {

        IPage<FactorGroupVo> page = factorGroupService.findPageByCondition(pageVo, groupName, groupType, authType);
        return ResultUtil.data(page);
    }


    /**
     * TODO  查看自由因子套餐
     *
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/3/15 9:34 上午
     */
    @RequestMapping(value = "/findFreeFactorGroup", method = RequestMethod.GET)
    public Result<List<FactorGroupVo>> findFreeFactorGroup() {
        List<FactorGroupVo> list = factorGroupService.findList("0");
        return ResultUtil.data(list);
    }


    /**
     * TODO  查看同系套餐
     *
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/3/15 9:34 上午
     */
    @RequestMapping(value = "/findHomFactorGroup", method = RequestMethod.GET)
    public Result<List<FactorGroupVo>> findHomFactorGroup() {
        List<FactorGroupVo> list = factorGroupService.findList("1");
        return ResultUtil.data(list);
    }


    /**
     * TODO 新增套餐
     *
     * @param addTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:53 下午
     */
    @RequestMapping(value = "/saveFactorGroup", method = RequestMethod.POST)
    public Result<Object> saveFactorGroup(@Validated @RequestBody FactorGroupAddTdo addTdo) {
        return factorGroupService.saveFactorGroup(addTdo);
    }

    /**
     * TODO 修改套餐
     *
     * @param modifyTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:54 下午
     */
    @RequestMapping(value = "/modifyFactorGroupByGroupId", method = RequestMethod.POST)
    public Result<Object> modifyFactorGroupByGroupId(@Validated @RequestBody FactorGroupModifyTdo modifyTdo) {

        return factorGroupService.modifyFactorGroupByGroupId(modifyTdo);
    }


    /**
     * TODO 根据套餐ID查看套餐信息
     *
     * @param groupId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/6 11:57 下午
     */
    @RequestMapping(value = "/findCheckStandardsByGroupId/{groupId}", method = RequestMethod.GET)
    public Result<List<FactorGroupItemVo>> findCheckStandardsByGroupId(@PathVariable Long groupId) {

        if (groupId == null) {
            return ResultUtil.validateError("套餐ID不能为空！");
        }

        List<FactorGroupItemVo> details = factorGroupService.findDetailByGroupId(groupId);
        return ResultUtil.data(details);
    }


    @RequestMapping(value = "/copyByGroupId/{groupId}", method = RequestMethod.GET)
    public Result<Object> copyByGroupId(@PathVariable Long groupId) {

        if (groupId == null) {
            return ResultUtil.validateError("套餐ID不能为空！");
        }

        return factorGroupService.copyByGroupId(groupId);
    }

    /**
     * TODO 删除套餐
     *
     * @param groupId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 11:39 上午
     */
    @RequestMapping(value = "/delByGroupId/{groupId}", method = RequestMethod.GET)
    public Result<Object> delByGroupId(@PathVariable Long groupId) {

        if (groupId == null) {
            return ResultUtil.validateError("套餐ID不能为空！");
        }

        return factorGroupService.delByGroupId(groupId);
    }
}