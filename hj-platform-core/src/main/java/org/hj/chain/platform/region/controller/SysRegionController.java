package org.hj.chain.platform.region.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.region.entity.SysRegion;
import org.hj.chain.platform.region.service.ISysRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Project : hj-platform-parent
 * @Description : TODO 行政区划管理
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-13
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-13
 */
@RestController
@RequestMapping("/sysRegion")
public class SysRegionController {
    @Autowired
    private ISysRegionService sysRegionService;

    /**
      * TODO 获取省份
      * @Author chh
      * @param
      * @Date 2021-05-13 17:44
      * @Iteration 1.0
      */
    @RequestMapping(value = "/getFirstLevel", method = RequestMethod.GET)
    public Result<List<SysRegion>> getFirstLevel() {
        return sysRegionService.getFirstLevel();
    }

    /**
      * TODO 根据区域Code获取下级区域列表
      * @Author chh
      * @param regionCode
      * @Date 2021-05-13 17:49
      * @Iteration 1.0
      */
    @RequestMapping(value = "/getSubRegionByRegionCode/{regionCode}", method = RequestMethod.GET)
    public Result<List<SysRegion>> getSubRegionByRegionCode(@PathVariable String regionCode) {
        if(StrUtil.isBlank(regionCode)) {
            return ResultUtil.validateError("区域Code不能为空！");
        }
        List<SysRegion> list = sysRegionService.list(Wrappers.<SysRegion>lambdaQuery()
                .select(SysRegion::getRegionCode, SysRegion::getRegionName)
                .eq(SysRegion::getParentCode, regionCode));
        return ResultUtil.data(list);
    }
}
