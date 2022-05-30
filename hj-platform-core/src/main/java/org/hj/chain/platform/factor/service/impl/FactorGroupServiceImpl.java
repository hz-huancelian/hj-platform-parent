package org.hj.chain.platform.factor.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.ibatis.cursor.Cursor;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.component.DictUtils;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.factor.entity.FactorCheckStandard;
import org.hj.chain.platform.factor.entity.FactorGroup;
import org.hj.chain.platform.factor.entity.FactorGroupItem;
import org.hj.chain.platform.factor.mapper.FactorCheckStandardMapper;
import org.hj.chain.platform.factor.mapper.FactorGroupMapper;
import org.hj.chain.platform.factor.mapper.FactorGroupItemMapper;
import org.hj.chain.platform.factor.service.IFactorGroupService;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.tdo.factor.FactorGroupAddTdo;
import org.hj.chain.platform.tdo.factor.FactorGroupModifyTdo;
import org.hj.chain.platform.tdo.factor.FactorItemTdo;
import org.hj.chain.platform.vo.FactorMethodInfoVo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.factor.FactorGroupItemVo;
import org.hj.chain.platform.vo.factor.FactorGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
@Service
public class FactorGroupServiceImpl extends ServiceImpl<FactorGroupMapper, FactorGroup> implements IFactorGroupService {

    @Autowired
    private FactorGroupMapper factorGroupMapper;
    @Autowired
    private FactorGroupItemMapper factorGroupItemMapper;
    @Autowired
    private FactorCheckStandardMapper factorCheckStandardMapper;
    @Autowired
    private IFactorService factorService;
    @Autowired
    private DictUtils dictUtils;

    @Override
    public IPage<FactorGroupVo> findPageByCondition(PageVo pageVo, String groupName, String groupType, String authType) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        groupName = StrUtil.trimToNull(groupName);
        authType = StrUtil.trimToNull(authType);
        groupType = StrUtil.trimToNull(groupType);
        Page<FactorGroupVo> page = PageUtil.initMpPage(pageVo);
        factorGroupMapper.findPageByCondition(page, organId, loginOutputVo.getUsername(), groupName, groupType, authType);
        return page;
    }

    @Override
    public List<FactorGroupVo> findList(String groupType) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        return factorGroupMapper.findListByGroupType(groupType, loginOutputVo.getUsername());
    }

    @Transactional
    @Override
    public Result<Object> saveFactorGroup(FactorGroupAddTdo addTdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        @NotEmpty(message = "至少选择一个因子") List<FactorItemTdo> factorStandardIds = addTdo.getFactorStandardIds();
        LocalDateTime now = LocalDateTime.now();
        FactorGroup po = new FactorGroup();
        po.setGroupName(addTdo.getGroupName());
        po.setFactorNum(factorStandardIds.size())
                .setOrganId(organId)
                .setGroupType(addTdo.getGroupType())
                .setGroupCost(addTdo.getGroupCost())
                .setCreateBy(loginOutputVo.getUsername())
                .setRemark(addTdo.getRemark())
                .setAuthType(addTdo.getAuthType());
        po.setCreateTime(now);
        String groupDesc = factorStandardIds.stream().map(item -> item.getFactorName()).collect(Collectors.joining(","));
        po.setGroupDesc(groupDesc);
        factorGroupMapper.insert(po);
        Long groupId = po.getId();
        List<FactorGroupItem> factorGroupItems = factorStandardIds.stream().map(item -> {
            FactorGroupItem factorGroupItem = new FactorGroupItem();
            factorGroupItem.setGroupId(groupId)
                    .setCheckStandardId(item.getCheckStandardId())
                    .setSecdClassId(item.getSecdClassId())
                    .setCreateTime(now);
            return factorGroupItem;
        }).collect(Collectors.toList());
        SqlHelper.executeBatch(FactorGroupItem.class, this.log, factorGroupItems, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
            String sqlStatement = SqlHelper.getSqlStatement(FactorGroupItemMapper.class, SqlMethod.INSERT_ONE);
            sqlSession.insert(sqlStatement, entity);
        });
        return ResultUtil.success("新增成功！");
    }

    @Transactional
    @Override
    public Result<Object> modifyFactorGroupByGroupId(FactorGroupModifyTdo modifyTdo) {
        LocalDateTime now = LocalDateTime.now();
        @NotNull(message = "套餐ID不能为空") Long groupId = modifyTdo.getGroupId();
        @NotEmpty(message = "至少选择一个因子") List<FactorItemTdo> factorStandardIds = modifyTdo.getFactorStandardIds();
        FactorGroup groupPo = new FactorGroup();
        groupPo.setId(groupId)
                .setFactorNum(factorStandardIds.size())
                .setGroupName(modifyTdo.getGroupName())
                .setGroupCost(modifyTdo.getGroupCost())
                .setGroupType(modifyTdo.getGroupType())
                .setRemark(modifyTdo.getRemark())
                .setUpdateTime(now);
        String groupDesc = factorStandardIds.stream().map(item -> item.getFactorName()).collect(Collectors.joining(","));
        groupPo.setGroupDesc(groupDesc);
        int res = factorGroupMapper.updateById(groupPo);
        if (res < 1) {
            return ResultUtil.busiError("套餐ID关联的套餐在库中不存在！");
        }

        factorGroupItemMapper.delete(Wrappers.<FactorGroupItem>lambdaQuery().eq(FactorGroupItem::getGroupId, groupId));
        List<FactorGroupItem> factorGroupItems = factorStandardIds.stream().map(item -> {
            FactorGroupItem factorGroupItem = new FactorGroupItem();
            factorGroupItem.setGroupId(groupId)
                    .setCheckStandardId(item.getCheckStandardId())
                    .setSecdClassId(item.getSecdClassId())
                    .setCreateTime(now);
            return factorGroupItem;
        }).collect(Collectors.toList());
        SqlHelper.executeBatch(FactorGroupItem.class, this.log, factorGroupItems, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
            String sqlStatement = SqlHelper.getSqlStatement(FactorGroupItemMapper.class, SqlMethod.INSERT_ONE);
            sqlSession.insert(sqlStatement, entity);
        });
        return ResultUtil.success("修改成功");
    }

    @Override
    public List<FactorGroupItemVo> findDetailByGroupId(Long groupId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<FactorGroupItem> items = factorGroupItemMapper.selectList(Wrappers.<FactorGroupItem>lambdaQuery()
                .select(FactorGroupItem::getCheckStandardId,
                        FactorGroupItem::getSecdClassId)
                .eq(FactorGroupItem::getGroupId, groupId));
        if (items != null && !items.isEmpty()) {
            List<String> standardCodes = items.stream().map(item -> item.getCheckStandardId()).collect(Collectors.toList());
            List<FactorCheckStandard> checkStandards = factorCheckStandardMapper.selectList(Wrappers.<FactorCheckStandard>lambdaQuery()
                    .select(FactorCheckStandard::getStandardCode,
                            FactorCheckStandard::getPrice,
                            FactorCheckStandard::getCmaFlg,
                            FactorCheckStandard::getCnasFlg,
                            FactorCheckStandard::getExtAssistFlg)
                    .in(FactorCheckStandard::getStandardCode, standardCodes)
                    .eq(FactorCheckStandard::getOrganId, organId));

            if (checkStandards != null && !checkStandards.isEmpty()) {
                Map<String, FactorCheckStandard> standardMap = checkStandards.stream().collect(Collectors.toMap(FactorCheckStandard::getStandardCode, Function.identity()));
                List<FactorGroupItemVo> standardVoList = items.stream().map(item -> {
                    String checkStandardId = item.getCheckStandardId();
                    FactorCheckStandard checkStandard = standardMap.get(checkStandardId);
                    FactorGroupItemVo standardVo = new FactorGroupItemVo();
                    standardVo.setStandardCode(checkStandardId);
                    if (checkStandard != null) {
                        standardVo.setPrice(checkStandard.getPrice())
                                .setCmaFlg(checkStandard.getCmaFlg())
                                .setCnasFlg(checkStandard.getCnasFlg())
                                .setExtAssistFlg(checkStandard.getExtAssistFlg());

                    }
                    FactorMethodInfoVo infoVo = factorService.findFactorMethodById(checkStandardId);
                    if (infoVo != null) {
                        standardVo.setClassName(infoVo.getClassName())
                                .setClassName(infoVo.getClassName())
                                .setStandardName(infoVo.getStandardName())
                                .setFactorName(infoVo.getFactorName())
                                .setStandardNo(infoVo.getStandardNo())
                                .setAnalysisMethod(infoVo.getAnalysisMethod())
                                .setMethodStatus(infoVo.getStatus())
                                .setClassId(infoVo.getClassId());

                    }
                    standardVo.setSecdClassId(item.getSecdClassId());
                    standardVo.setSecdClassName(dictUtils.getFactorClassMap().get(item.getSecdClassId()));
                    return standardVo;
                }).collect(Collectors.toList());
                return standardVoList;
            }
        }
        return null;
    }


    @Override
    public IPage<FactorGroupItemVo> findDetailByGroupId(PageVo pageVo, Long groupId) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Page<FactorGroupItem> page = PageUtil.initMpPage(pageVo);
        Page<FactorGroupItem> items = factorGroupItemMapper.selectPage(page, Wrappers.<FactorGroupItem>lambdaQuery()
                .select(FactorGroupItem::getCheckStandardId,
                        FactorGroupItem::getSecdClassId)
                .eq(FactorGroupItem::getGroupId, groupId));
        List<FactorGroupItem> records = items.getRecords();
        if (records != null && !records.isEmpty()) {
            List<String> standardCodes = records.stream().map(item -> item.getCheckStandardId()).collect(Collectors.toList());
            List<FactorCheckStandard> checkStandards = factorCheckStandardMapper.selectList(Wrappers.<FactorCheckStandard>lambdaQuery()
                    .select(FactorCheckStandard::getStandardCode,
                            FactorCheckStandard::getPrice,
                            FactorCheckStandard::getCmaFlg,
                            FactorCheckStandard::getCnasFlg,
                            FactorCheckStandard::getExtAssistFlg)
                    .in(FactorCheckStandard::getStandardCode, standardCodes)
                    .eq(FactorCheckStandard::getOrganId, organId));

            if (checkStandards != null && !checkStandards.isEmpty()) {
                Map<String, FactorCheckStandard> standardMap = checkStandards.stream().collect(Collectors.toMap(FactorCheckStandard::getStandardCode, Function.identity()));
                List<FactorGroupItemVo> standardVoList = records.stream().map(item -> {
                    String checkStandardId = item.getCheckStandardId();
                    FactorCheckStandard checkStandard = standardMap.get(checkStandardId);
                    FactorGroupItemVo standardVo = new FactorGroupItemVo();
                    standardVo.setStandardCode(checkStandardId);
                    if (checkStandard != null) {
                        standardVo.setPrice(checkStandard.getPrice())
                                .setCmaFlg(checkStandard.getCmaFlg())
                                .setCnasFlg(checkStandard.getCnasFlg())
                                .setExtAssistFlg(checkStandard.getExtAssistFlg());

                    }
                    FactorMethodInfoVo infoVo = factorService.findFactorMethodById(checkStandardId);
                    if (infoVo != null) {
                        standardVo.setClassName(infoVo.getClassName())
                                .setClassName(infoVo.getClassName())
                                .setStandardName(infoVo.getStandardName())
                                .setFactorName(infoVo.getFactorName())
                                .setStandardNo(infoVo.getStandardNo())
                                .setAnalysisMethod(infoVo.getAnalysisMethod())
                                .setClassId(infoVo.getClassId());

                    }
                    standardVo.setSecdClassId(item.getSecdClassId());
                    standardVo.setSecdClassName(dictUtils.getFactorClassMap().get(item.getSecdClassId()));
                    return standardVo;
                }).collect(Collectors.toList());
                Page<FactorGroupItemVo> resPage = PageUtil.convertPageVo(page, standardVoList);
                return resPage;
            }
        }
        return PageUtil.convertPageVo(page, Collections.EMPTY_LIST);
    }

    @Transactional
    @Override
    public void scan(Long groupId, int limit) throws Exception {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        try (Cursor<FactorGroupItem> cursor = factorGroupItemMapper.scan(limit, groupId)) {
            cursor.forEach(groupItem -> {
                FactorCheckStandard checkStandard = factorCheckStandardMapper.selectOne(Wrappers.<FactorCheckStandard>lambdaQuery()
                        .select(FactorCheckStandard::getStandardCode,
                                FactorCheckStandard::getPrice,
                                FactorCheckStandard::getCmaFlg,
                                FactorCheckStandard::getCnasFlg,
                                FactorCheckStandard::getExtAssistFlg)
                        .in(FactorCheckStandard::getStandardCode, groupItem.getCheckStandardId())
                        .eq(FactorCheckStandard::getOrganId, organId));

                if (checkStandard != null) {
                    FactorGroupItemVo standardVo = new FactorGroupItemVo();
                    standardVo.setStandardCode(checkStandard.getStandardCode());
                    if (checkStandard != null) {
                        standardVo.setPrice(checkStandard.getPrice())
                                .setCmaFlg(checkStandard.getCmaFlg())
                                .setCnasFlg(checkStandard.getCnasFlg())
                                .setExtAssistFlg(checkStandard.getExtAssistFlg());

                    }
                    FactorMethodInfoVo infoVo = factorService.findFactorMethodById(checkStandard.getStandardCode());
                    if (infoVo != null) {
                        standardVo.setClassName(infoVo.getClassName())
                                .setClassName(infoVo.getClassName())
                                .setStandardName(infoVo.getStandardName())
                                .setFactorName(infoVo.getFactorName())
                                .setStandardNo(infoVo.getStandardNo())
                                .setAnalysisMethod(infoVo.getAnalysisMethod())
                                .setClassId(infoVo.getClassId());

                    }
                    standardVo.setSecdClassId(groupItem.getSecdClassId());
                    standardVo.setSecdClassName(dictUtils.getFactorClassMap().get(groupItem.getSecdClassId()));
                }

            });
        }
    }


    @Transactional
    @Override
    public Result<Object> delByGroupId(Long groupId) {
        LocalDateTime now = LocalDateTime.now();
        factorGroupMapper.update(null, Wrappers.<FactorGroup>lambdaUpdate()
                .set(FactorGroup::getStatus, '1')
                .set(FactorGroup::getUpdateTime, now)
                .eq(FactorGroup::getId, groupId));
//        factorGroupMapper.deleteById(groupId);
//        factorItemMapper.delete(Wrappers.<FactorItem>lambdaQuery().eq(FactorItem::getGroupId, groupId));
        return ResultUtil.success("删除成功！");
    }

    @Override
    public List<FactorGroupVo> findList() {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<FactorGroup> groups = factorGroupMapper.selectList(Wrappers.<FactorGroup>lambdaQuery()
                .select(FactorGroup::getId,
                        FactorGroup::getGroupDesc,
                        FactorGroup::getAuthType,
                        FactorGroup::getGroupName,
                        FactorGroup::getFactorNum,
                        FactorGroup::getRemark)
                .eq(FactorGroup::getOrganId, organId));
        if (groups != null && !groups.isEmpty()) {
            List<FactorGroupVo> groupVos = groups.stream().map(item -> {
                FactorGroupVo groupVo = new FactorGroupVo();
                groupVo.setGroupId(item.getId())
                        .setFactorNum(item.getFactorNum())
                        .setGroupName(item.getGroupName())
                        .setGroupDesc(item.getGroupDesc())
                        .setAuthType(item.getAuthType())
                        .setRemark(item.getRemark());
                return groupVo;
            }).collect(Collectors.toList());
            return groupVos;
        }
        return null;
    }


    @Transactional
    @Override
    public Result<Object> copyByGroupId(Long groupId) {
        FactorGroup dbGroup = factorGroupMapper.selectOne(Wrappers.<FactorGroup>lambdaQuery()
                .select(FactorGroup::getAuthType,
                        FactorGroup::getFactorNum,
                        FactorGroup::getGroupCost,
                        FactorGroup::getGroupDesc,
                        FactorGroup::getGroupName,
                        FactorGroup::getGroupType,
                        FactorGroup::getOrganId,
                        FactorGroup::getCreateBy,
                        FactorGroup::getRemark)
                .eq(FactorGroup::getId, groupId));
        if (dbGroup != null) {
            List<FactorGroupItem> factorGroupItems = factorGroupItemMapper.selectList(Wrappers.<FactorGroupItem>lambdaQuery()
                    .select(FactorGroupItem::getSecdClassId,
                            FactorGroupItem::getCheckStandardId)
                    .eq(FactorGroupItem::getGroupId, groupId));

            if (factorGroupItems != null && !factorGroupItems.isEmpty()) {

                LocalDateTime now = LocalDateTime.now();
                dbGroup.setCreateTime(now);

                factorGroupMapper.insert(dbGroup);
                Long ngroupId = dbGroup.getId();
                factorGroupItems.stream().forEach(item -> {
                    item.setGroupId(ngroupId)
                            .setCreateTime(now);
                });
                SqlHelper.executeBatch(FactorGroupItem.class, this.log, factorGroupItems, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
                    String sqlStatement = SqlHelper.getSqlStatement(FactorGroupItemMapper.class, SqlMethod.INSERT_ONE);
                    sqlSession.insert(sqlStatement, entity);
                });

            }
        }
        return ResultUtil.success("复制成功");
    }
}
