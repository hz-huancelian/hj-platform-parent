package org.hj.chain.platform.organ.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.organ.service.IOrganService;
import org.hj.chain.platform.tdo.UserPasswordResetTdo;
import org.hj.chain.platform.tdo.organ.RegOrganTdo;
import org.hj.chain.platform.vo.OrganSearchVo;
import org.hj.chain.platform.vo.OrganVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 机构注册相关服务
 * @Iteration : 1.0
 * @Date : 2021/5/7  12:38 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/07    create
 */
@RestController
@RequestMapping("/organ")
public class OrganController {

    @Autowired
    private IOrganService organService;


    /**
     * TODO 分页查询机构信息
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 3:16 下午
     */
    @RequestMapping(value = "/findOrgansByCondition", method = RequestMethod.GET)
    public Result<IPage<OrganVo>> findOrgansByCondition(@ModelAttribute PageVo pageVo,
                                                        @ModelAttribute OrganSearchVo sv) {
        IPage<OrganVo> page = organService.findOrgansByCondition(pageVo, sv);
        return ResultUtil.data(page);
    }

    /**
     * TODO 注册机构用户
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/30 9:34 上午
     */
    @RequestMapping(value = "/registOrgan", method = RequestMethod.POST)
    public Result<Object> save(@Validated @RequestBody RegOrganTdo tdo) {
        Result<Object> result = organService.registOrigan(tdo);
        return result;
    }


    /**
     * TODO 修改密码
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/28 2:26 下午
     */
    @RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
    public Result<Object> modifyPassword(@Validated @RequestBody UserPasswordResetTdo tdo) {
        return organService.modifyPassword(tdo.getUserId(), tdo.getOldPassword(), tdo.getNewPassword());
    }


}