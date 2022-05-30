package org.hj.chain.platform.judgeorgan.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.judgeorgan.service.IJudgeOrganService;
import org.hj.chain.platform.tdo.judgeorgan.JudgeOrganModifyTdo;
import org.hj.chain.platform.tdo.judgeorgan.JudgeOrganTdo;
import org.hj.chain.platform.vo.judgeorgan.JudgeOrganListVo;
import org.hj.chain.platform.vo.judgeorgan.JudgeOrganVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/14  10:03 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/14    create
 */
@RestController
@RequestMapping("/judge/organ")
public class JudgeOrganController {

    @Autowired
    private IJudgeOrganService judgeOrganService;

    /**
     * TODO 分页查询
     *
     * @param pageVo
     * @param judgeOrganName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 10:08 上午
     */
    @RequestMapping(value = "/findPageByCondition", method = RequestMethod.GET)
    public Result<IPage<JudgeOrganVo>> findPageByCondition(@ModelAttribute PageVo pageVo,
                                                           @RequestParam String judgeOrganName) {
        IPage<JudgeOrganVo> page = judgeOrganService.findPageByCondition(pageVo, judgeOrganName);

        return ResultUtil.data(page);

    }

    /**
     * TODO 新增分包机构
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 10:09 上午
     */
    @RequestMapping(value = "/addJudgeOrgan", method = RequestMethod.POST)
    public Result<Object> addJudgeOrgan(@Validated @RequestBody JudgeOrganTdo tdo) {
        return judgeOrganService.addJudgeOrgan(tdo);
    }

    /**
     * TODO 修改分包机构
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 10:12 上午
     */
    @RequestMapping(value = "/modifyJudgeOrganById", method = RequestMethod.POST)
    public Result<Object> modifyJudgeOrganById(@Validated @RequestBody JudgeOrganModifyTdo tdo) {
        return judgeOrganService.modifyJudgeOrganById(tdo);
    }


    /**
     * TODO 删除分包机构
     *
     * @param id
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 10:13 上午
     */
    @RequestMapping(value = "/delById/{id}", method = RequestMethod.GET)
    public Result<Object> delById(@PathVariable String id) {

        if (StrUtil.isBlank(id)) {
            return ResultUtil.validateError("分包机构ID不能为空！");
        }

        return judgeOrganService.delById(id);
    }


    /**
     * TODO 查询有效的机构列表
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 10:15 上午
     */
    @RequestMapping(value = "/findValidList", method = RequestMethod.GET)
    public Result<List<JudgeOrganListVo>> findValidList() {

        List<JudgeOrganListVo> validList = judgeOrganService.findValidList();
        return ResultUtil.data(validList);
    }

}