package org.hj.chain.platform.common.controller;

import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.MonitorFreqEnum;
import org.hj.chain.platform.common.MonitorFreqVo;
import org.hj.chain.platform.common.service.ICommonService;
import org.hj.chain.platform.component.DictUtils;
import org.hj.chain.platform.mapper.SysUserMapper;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.vo.UserParamVo;
import org.hj.chain.platform.vo.sys.SysUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 公共的服务（字典的获取）
 * @Iteration : 1.0
 * @Date : 2021/5/8  7:51 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/08    create
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private DictUtils dictUtils;
    @Autowired
    private ICommonService commonService;

    /**
     * TODO 查询字典
     *
     * @param rootKey
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 8:05 上午
     */
    @RequestMapping(value = "/findDictMap/{rootKey}", method = RequestMethod.GET)
    public Result<Map<Long, String>> findDictMap(@PathVariable Long rootKey) {

        Map<Long, String> validDictMap = dictUtils.getValidDictMap(rootKey);
        return ResultUtil.data(validDictMap);

    }

    /**
     * TODO 获取采样组长列表
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/30 3:52 下午
     */
    @RequestMapping(value = "/findSampLeaders", method = RequestMethod.GET)
    public Result<List<UserParamVo>> findSampLeaders() {
//        return commonService.findUsersByDeptId();
        return commonService.findSampLeaders();
    }

    /**
     * TODO 获取检测员列表
     *
     * @param
     * @Author: chh
     * @Iteration : 1.0
     * @Date: 2021/7/30 5:52 下午
     */
    @RequestMapping(value = "/findCheckEmps", method = RequestMethod.GET)
    public Result<List<UserParamVo>> findCheckEmps() {
//        return commonService.findUsersByDeptId();
        return commonService.findCheckEmps();
    }


    /**
     * TODO 查询监测频次
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2022/3/16 9:05 下午
     */
    @RequestMapping(value = "/findMonitorFreq", method = RequestMethod.GET)
    public Result<List<MonitorFreqVo>> findMonitorFreq() {

        return commonService.findMonitorFreqs();
    }
}