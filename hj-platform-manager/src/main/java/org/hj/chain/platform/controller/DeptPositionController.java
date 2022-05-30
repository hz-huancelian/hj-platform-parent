package org.hj.chain.platform.controller;

import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.service.IDeptPositionService;
import org.hj.chain.platform.vo.DeptPositionRelVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/13  10:56 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/13    create
 */
@RestController
@RequestMapping("/platform/dp")
public class DeptPositionController {

    @Autowired
    private IDeptPositionService deptPositionService;

    @RequestMapping(value = "/findPositionIdByDeptId/{deptId}", method = RequestMethod.GET)
    public Result<Map<Long, String>> findPositionIdByDeptId(@PathVariable Long deptId) {
        List<DeptPositionRelVo> rels = deptPositionService.findRelByDeptId(deptId);
        if (rels != null && !rels.isEmpty()) {
            Map<Long, String> map = rels.stream().collect(Collectors.toMap(DeptPositionRelVo::getPositionId, DeptPositionRelVo::getPositionVal));
            return ResultUtil.data(map);
        }

        return ResultUtil.data(new HashMap<>());
    }

}