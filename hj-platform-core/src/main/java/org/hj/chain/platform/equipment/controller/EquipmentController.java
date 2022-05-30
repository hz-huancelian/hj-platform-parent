package org.hj.chain.platform.equipment.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.equipment.entity.EquipmentInfo;
import org.hj.chain.platform.equipment.entity.EquipmentRecordInfo;
import org.hj.chain.platform.equipment.service.IEquipmentInfoService;
import org.hj.chain.platform.equipment.service.IEquipmentRecordInfoService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.equipment.EquipmentInfoEditTdo;
import org.hj.chain.platform.tdo.equipment.EquipmentInfoTdo;
import org.hj.chain.platform.tdo.equipment.EquipmentLoanTdo;
import org.hj.chain.platform.vo.DictParam;
import org.hj.chain.platform.vo.equipment.EquipmentInfoDisplayVo;
import org.hj.chain.platform.vo.equipment.EquipmentRecordInfoDisplayVo;
import org.hj.chain.platform.vo.equipment.EquipmentSearchVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : chh
 * @Project : hj-platform-parent
 * @description TODO 设备管理
 * @Iteration : 1.0
 * @Date : 2021/6/2  11:26 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh          2021/02/02    create
 */
@RestController
@RequestMapping("/equipment")
public class EquipmentController {
    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private IEquipmentRecordInfoService equipmentRecordInfoService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * todo 设备一级类型集合
     * @return
     */
    @RequestMapping(value = "/findEquipmentFirstType", method = RequestMethod.GET)
    public Result<List<DictParam>> findEquipmentFirstType() {
        List<DictParam> list = equipmentInfoService.findEquipmentFirstType();
        return ResultUtil.data(list);
    }

    /**
     * todo 根据设备一级类型查询对于二级类型
     * @param rootKey
     * @return
     */
    @RequestMapping(value = "/findEquipmentSecondType/{rootKey}", method = RequestMethod.GET)
    public Result<List<DictParam>> findEquipmentSecondType(@PathVariable Long rootKey) {
        if(rootKey == null) {
            return ResultUtil.validateError("设备一级类型值不能为空！");
        }
        List<DictParam> list = equipmentInfoService.findEquipmentSecondType(rootKey);
        return ResultUtil.data(list);
    }

    /**
     * todo 条件分页查询设备列表
     * @param pageVo
     * @param sv
     * @return
     */
    @RequestMapping(value = "/findByCondition", method = RequestMethod.GET)
    public Result<IPage<EquipmentInfoDisplayVo>> findByCondition(@ModelAttribute PageVo pageVo,
                                                                 @ModelAttribute EquipmentSearchVo sv) {
        sv.setEquipmentNumber(StrUtil.trimToNull(sv.getEquipmentNumber()));
        sv.setEquipmentName(StrUtil.trimToNull(sv.getEquipmentName()));
        sv.setEquipmentStatus(StrUtil.trimToNull(sv.getEquipmentStatus()));
        IPage<EquipmentInfoDisplayVo> vos = equipmentInfoService.findByCondition(pageVo, sv);
        return ResultUtil.data(vos);
    }

    /**
     * todo 根据设备ID删除设备
     * @param id
     * @return
     */
    @RequestMapping(value = "/removeById/{id}", method = RequestMethod.DELETE)
    public Result<Object> removeById(@PathVariable Long id) {
        if(id == null) {
            return ResultUtil.validateError("设备ID不能为空！");
        }
        EquipmentInfo equipmentInfo = equipmentInfoService.getById(id);
        if(equipmentInfo == null) {
            return ResultUtil.busiError("设备信息不存在！");
        }
        String userId = (String) StpUtil.getLoginId();
        equipmentInfoService.update(Wrappers.<EquipmentInfo>lambdaUpdate()
                .set(EquipmentInfo::getIsDelete, "1")
                .set(EquipmentInfo::getUpdateTime, LocalDateTime.now())
                .set(EquipmentInfo::getUpdateUserId, userId)
                .eq(EquipmentInfo::getId, id));
        return ResultUtil.success("设备删除成功！");
    }

    /**
     * todo 新增设备信息
     * @param tdo
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<Object> add(@Validated @RequestBody EquipmentInfoTdo tdo) {
        return equipmentInfoService.add(tdo);
    }


    /**
     * todo 根据设备ID更新设备信息
     * @param tdo
     * @return
     */
    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public Result<Object> updateById(@Validated @RequestBody EquipmentInfoEditTdo tdo) {
        EquipmentInfo equipmentInfo = equipmentInfoService.getById(tdo.getId());
        if(equipmentInfo == null) {
            return ResultUtil.busiError("设备信息不存在！");
        }
        if(equipmentInfo.getIsDelete().equals("1")) {
            return ResultUtil.busiError("已删除设备无法更新信息！");
        }
        equipmentInfo = new EquipmentInfo();
        BeanUtils.copyProperties(tdo, equipmentInfo);
        equipmentInfo.setUpdateTime(LocalDateTime.now())
                .setUpdateUserId((String) StpUtil.getLoginId());
        equipmentInfoService.updateById(equipmentInfo);
        return ResultUtil.success("设备信息更新成功！");
    }

    /**
     * todo 根据设备ID获取设备记录信息
     * @param pageVo
     * @param equipmentId
     * @return
     */
    @RequestMapping(value = "/findRecordsById/{equipmentId}", method = RequestMethod.GET)
    public Result<IPage<EquipmentRecordInfoDisplayVo>> findRecordsById(@ModelAttribute PageVo pageVo,
                                                                       @PathVariable Long equipmentId) {
        if(equipmentId == null) {
            return ResultUtil.validateError("设备ID不能为空！");
        }
        List<EquipmentRecordInfoDisplayVo> vos = new ArrayList<>();
        Page<EquipmentRecordInfo> page = PageUtil.initMpPage(pageVo);
        equipmentRecordInfoService.page(page, Wrappers.<EquipmentRecordInfo>lambdaQuery()
                .eq(EquipmentRecordInfo::getEquipmentId, equipmentId)
                .orderByDesc(EquipmentRecordInfo::getCreateTime));
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            vos = page.getRecords().stream().map(item -> {
                EquipmentRecordInfoDisplayVo vo = new EquipmentRecordInfoDisplayVo();
                BeanUtils.copyProperties(item, vo);
                String createUser = sysUserService.findUserByUserId(item.getCreateUserId()).getEmpName();
                vo.setCreateUser(createUser);
                return vo;
            }).collect(Collectors.toList());
        }
        return ResultUtil.data(PageUtil.convertPageVo(page, vos));
    }

    /**
     * todo 申请设备
     * @param tdo
     * @return
     */
    @RequestMapping(value = "/loan", method = RequestMethod.POST)
    public Result<Object> loan(@Validated @RequestBody EquipmentLoanTdo tdo) {
        return equipmentInfoService.loan(tdo);
    }

    /**
     * todo 归还设备
     * @param equipmentId
     * @return
     */
    @RequestMapping(value = "/remand/{equipmentId}", method = RequestMethod.GET)
    public Result<Object> remandById(@PathVariable Long equipmentId) {
        return equipmentInfoService.remandById(equipmentId);
    }

    /**
     * 维修设备
     * @param equipmentId
     * @return
     */
    @RequestMapping(value = "/maintenance/{equipmentId}", method = RequestMethod.GET)
    public Result<Object> maintenance(@PathVariable Long equipmentId) {
        return equipmentInfoService.maintenance(equipmentId);
    }

    /**
     * 设备维修完成
     * @param equipmentId
     * @return
     */
    @RequestMapping(value = "/completed/{equipmentId}", method = RequestMethod.GET)
    public Result<Object> completed(@PathVariable Long equipmentId) {
        return equipmentInfoService.completed(equipmentId);
    }

    /**
     * TODO 实验室设备树状图
     * @return
     */
    @GetMapping("/laboratoryEquipmentTree")
    public Result<Object> laboratoryEquipmentTree() {
        return equipmentInfoService.laboratoryEquipmentTree();
    }
}
