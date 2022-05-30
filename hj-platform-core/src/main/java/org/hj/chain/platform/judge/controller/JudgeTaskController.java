package org.hj.chain.platform.judge.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.judge.service.IJudgeTaskService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.judgetask.JudgeRecordAuditTdo;
import org.hj.chain.platform.tdo.judgetask.JudgeTaskAuditTdo;
import org.hj.chain.platform.vo.UserParamVo;
import org.hj.chain.platform.vo.judgetask.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/judgeTask")
public class JudgeTaskController {
    @Autowired
    private IJudgeTaskService judgeTaskService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * todo 技术负责人条件分页查询技术评审任务
     * @param pageVo
     * @param sv
     * @return
     */
    @RequestMapping(value = "/findJudgeTaskForTecManagerByCondition", method = RequestMethod.GET)
    public Result<IPage<JudgeTaskVo>> findJudgeTaskForTecManagerByCondition(@ModelAttribute PageVo pageVo,
                                                               @ModelAttribute JudgeTaskSearchVo sv) {
        sv.setProjectName(StrUtil.trimToNull(sv.getProjectName()));
        sv.setOfferId(StrUtil.trimToNull(sv.getOfferId()));
        sv.setTaskStatus(StrUtil.trimToNull(sv.getTaskStatus()));
        return judgeTaskService.findJudgeTaskForTecManagerByCondition(pageVo, sv);
    }

    /**
     * todo 查询参与评审人员
     * @return
     */
//    @RequestMapping(value = "/findAuditUser", method = RequestMethod.GET)
//    public Result<List<UserParamVo>> findAuditUserByCondition(@ModelAttribute JudgeUserSearchVo sv) {
//        List<UserParamVo> users = sysUserService.selectUsersByCondition(sv.getEmpName(), sv.getDeptId(), sv.getPostId());
//        return ResultUtil.data(users);
//    }

    @RequestMapping(value = "/findAuditUser", method = RequestMethod.GET)
    public Result<List<UserParamVo>> findAuditUser() {
        return judgeTaskService.findAuditUser();
    }

    /**
     * todo 技术负责人评审
     * @param tdo
     * @return
     */
    @RequestMapping(value = "/doAuditTask", method = RequestMethod.POST)
    public Result<Object> doAudit(@Validated @RequestBody JudgeTaskAuditTdo tdo) {
        return judgeTaskService.doAuditTask(tdo);
    }

    /**
     * todo 根据评审任务ID查询评审记录
     * @param judgeTaskId
     * @return
     */
    @RequestMapping(value = "/getJudgeRecordByJudgeTaskId/{judgeTaskId}", method = RequestMethod.GET)
    public Result<List<JudgeRecordVo>> getJudgeRecordByJudgeTaskId(@PathVariable Long judgeTaskId) {
        return judgeTaskService.getJudgeRecordByJudgeTaskId(judgeTaskId);
    }

    /**
     * todo 部门负责人分页查询评审任务
     * @param pageVo
     * @param sv
     * @return
     */
    @RequestMapping(value = "/findJudgeTaskForDeptManagerByCondition", method = RequestMethod.GET)
    public Result<IPage<JudgeRecordVo>> findJudgeTaskForDeptManagerByCondition(@ModelAttribute PageVo pageVo,
                                                                               @ModelAttribute JudgeRecordSearchVo sv) {
        sv.setProjectName(StrUtil.trimToNull(sv.getProjectName()));
        sv.setOfferId(StrUtil.trimToNull(sv.getOfferId()));
        sv.setRecordStatus(StrUtil.trimToNull(sv.getRecordStatus()));
        return judgeTaskService.findJudgeTaskForDeptManagerByCondition(pageVo, sv);
    }

    /**
     * todo 部门负责人进行评审
     * @param tdo
     * @return
     */
    @RequestMapping(value = "/doAuditRecord", method = RequestMethod.POST)
    public Result<Object> doAuditRecord(@Validated @RequestBody JudgeRecordAuditTdo tdo) {
        return judgeTaskService.doAuditRecord(tdo);
    }
}
