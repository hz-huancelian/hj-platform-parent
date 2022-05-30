package org.hj.chain.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.CustomException;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.mapper.SysDictMapper;
import org.hj.chain.platform.mapper.SysDictRootMapper;
import org.hj.chain.platform.model.SysDict;
import org.hj.chain.platform.model.SysDictRoot;
import org.hj.chain.platform.service.ISysDictService;
import org.hj.chain.platform.vo.DictParam;
import org.hj.chain.platform.vo.SysDictRootVo;
import org.hj.chain.platform.vo.SysDictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 字典服务
 * @Iteration : 1.0
 * @Date : 2021/5/7  9:36 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/07    create
 */
@Service
public class SysDictServiceImpl implements ISysDictService {

    @Autowired
    private SysDictRootMapper sysDictRootMapper;
    @Autowired
    private SysDictMapper sysDictMapper;

    //初始字典
    private static final Long INIT_TREE_KEY = 1001L;

    @Cacheable(value = "dict:organ:map")
    @Override
    public Map<String, DictParam> findDictMap() {
        List<SysDictRoot> dictTrees = sysDictRootMapper.selectList(Wrappers.<SysDictRoot>lambdaQuery()
                .select(SysDictRoot::getDictKey,
                        SysDictRoot::getDictVal,
                        SysDictRoot::getDictDesc));

        if (dictTrees != null && !dictTrees.isEmpty()) {
            Map<String, DictParam> map = new HashMap<>();
            Map<Long, DictParam> dictParamMap = dictTrees.stream().collect(Collectors.toMap(SysDictRoot::getDictKey, item -> {
                        DictParam dictParam = new DictParam();
                        dictParam.setDictVal(item.getDictVal())
                                .setDictKey(item.getDictKey())
                                .setDictDesc(item.getDictDesc())
                                .setStatus("0");
                        return dictParam;

                    }
            ));
            List<SysDict> dicts = sysDictMapper.selectList(Wrappers.<SysDict>lambdaQuery()
                    .select(SysDict::getDictKey,
                            SysDict::getDictVal,
                            SysDict::getDictDesc,
                            SysDict::getDictStatus)
                    .in(SysDict::getParentDictKey, dictParamMap.keySet()));

            if (dicts != null && !dicts.isEmpty()) {
                Map<Long, DictParam> subParamMap = dicts.stream().collect(Collectors.toMap(SysDict::getDictKey, item -> {
                            DictParam dictParam = new DictParam();
                            dictParam.setDictVal(item.getDictVal())
                                    .setDictKey(item.getDictKey())
                                    .setDictDesc(item.getDictDesc())
                                    .setStatus("0");
                            return dictParam;

                        }
                ));

                dictParamMap.putAll(subParamMap);
            }
            dictParamMap.forEach((k, v) -> {
                map.put(String.valueOf(k), v);
            });
            return map;
        }
        return null;
    }

    @Cacheable(value = "dict:organ:rel")
    @Override
    public Map<String, List<Long>> findDictRel() {
        List<SysDictRoot> dictTrees = sysDictRootMapper.selectList(Wrappers.<SysDictRoot>lambdaQuery()
                .select(SysDictRoot::getDictKey));

        if (dictTrees != null && !dictTrees.isEmpty()) {
            List<Long> dictKeys = dictTrees.stream().map(item -> item.getDictKey()).collect(Collectors.toList());
            List<SysDict> dicts = sysDictMapper.selectList(Wrappers.<SysDict>lambdaQuery()
                    .select(SysDict::getDictKey,
                            SysDict::getParentDictKey)
                    .in(SysDict::getParentDictKey, dictKeys));

            if (dicts != null && !dicts.isEmpty()) {

                Map<String, List<Long>> resMap = new HashMap<>();
                Map<Long, List<SysDict>> listMap = dicts.stream().collect(Collectors.groupingBy(SysDict::getParentDictKey));
                listMap.forEach((parentKey, items) -> {
                    List<Long> subKeys = items.stream().map(item -> item.getDictKey()).collect(Collectors.toList());
                    List<Long> listKeys = resMap.get(parentKey);
                    if (listKeys != null) {
                        listKeys.addAll(subKeys);
                    } else {
                        resMap.put(String.valueOf(parentKey), subKeys);
                    }
                });
                return resMap;
            }

        }
        return null;
    }

    @CacheEvict(value = {"dict:organ:rel", "dict:organ:map"})
    @Override
    public int addLeafDict(Long parentDictKey, String dictVal, String dictDesc) {
        SysDictRoot dbPo = sysDictRootMapper.selectOne(Wrappers.<SysDictRoot>lambdaQuery()
                .select(SysDictRoot::getId)
                .eq(SysDictRoot::getDictKey, parentDictKey));
        if (dbPo == null) {
            throw new CustomException("父ID在在库中不存在！");
        }
        Integer count = sysDictMapper.selectCount(Wrappers.<SysDict>lambdaQuery()
                .eq(SysDict::getParentDictKey, parentDictKey));
        Long dictKey = parentDictKey * 100 + (count + 1);

        LocalDateTime now = LocalDateTime.now();
        SysDict dict = new SysDict();
        dict.setDictKey(dictKey)
                .setDictVal(dictVal)
                .setDictStatus("0")
                .setDictDesc(dictDesc)
                .setParentDictKey(parentDictKey)
                .setCreateTime(now);
        return sysDictMapper.insert(dict);
    }

    @CacheEvict(value = {"dict:organ:rel", "dict:organ:map"})
    @Override
    public int addTreeDict(String dictVal, String dictDesc) {
        Integer count = sysDictRootMapper.selectCount(Wrappers.emptyWrapper());
        Long dictKey = count == 0 ? INIT_TREE_KEY : (count + INIT_TREE_KEY);
        LocalDateTime now = LocalDateTime.now();
        SysDictRoot dictTree = new SysDictRoot();
        dictTree.setDictKey(dictKey)
                .setDictVal(dictVal)
                .setDictDesc(dictDesc)
                .setCreateTime(now);
        return sysDictRootMapper.insert(dictTree);
    }

    @CacheEvict(value = "dict:organ:map")
    @Override
    public int updateLeafDict(Long id, String dictVal, String dictDesc) {
        SysDict dbPo = sysDictMapper.selectOne(Wrappers.<SysDict>lambdaQuery()
                .select(SysDict::getId)
                .eq(SysDict::getDictKey, id));
        if (dbPo == null) {
            return -1;
        }
        LocalDateTime now = LocalDateTime.now();
        SysDict po = new SysDict();
        po.setId(id)
                .setDictVal(dictVal)
                .setDictDesc(dictDesc)
                .setUpdateTime(now);

        return sysDictMapper.updateById(po);
    }

    @CacheEvict(value = "dict:organ:map")
    @Override
    public int updateTreeDict(Long id, String dictVal, String dictDesc) {
        SysDictRoot dbPo = sysDictRootMapper.selectOne(Wrappers.<SysDictRoot>lambdaQuery()
                .select(SysDictRoot::getId)
                .eq(SysDictRoot::getId, id));
        if (dbPo == null) {
            return -1;
        }


        LocalDateTime now = LocalDateTime.now();
        SysDictRoot po = new SysDictRoot();
        po.setId(id)
                .setDictVal(dictVal)
                .setDictDesc(dictDesc)
                .setUpdateTime(now);

        return sysDictRootMapper.updateById(po);
    }

    @CacheEvict(value = "dict:organ:map")
    @Override
    public int delLeafDictById(Long id) {
        SysDict dbPo = sysDictMapper.selectOne(Wrappers.<SysDict>lambdaQuery()
                .select(SysDict::getId)
                .eq(SysDict::getId, id));
        if (dbPo == null) {
            return -1;
        }


        return sysDictMapper.update(null, Wrappers.<SysDict>lambdaUpdate()
                .set(SysDict::getDictStatus, "1")
                .set(SysDict::getUpdateTime, LocalDateTime.now())
                .eq(SysDict::getId, id));
    }

    @Override
    public Result<Page<SysDictRootVo>> findPageByCondition(PageVo pageVo, String dictVal) {
        dictVal = StrUtil.trimToNull(dictVal);
        Page<SysDictRootVo> page = PageUtil.initMpPage(pageVo);
        sysDictRootMapper.findPageByCondition(page, dictVal);
        return ResultUtil.data(page);
    }

    @Override
    public List<SysDictVo> findDictVosById(Long parentDictKey) {
        List<SysDict> list = sysDictMapper.selectList(Wrappers.<SysDict>lambdaQuery()
                .select(SysDict::getId,
                        SysDict::getDictKey,
                        SysDict::getDictStatus,
                        SysDict::getDictDesc,
                        SysDict::getDictVal)
                .eq(SysDict::getParentDictKey, parentDictKey));
        if (list != null && !list.isEmpty()) {
            List<SysDictVo> vos = list.stream().map(item -> {
                SysDictVo vo = new SysDictVo();
                vo.setId(item.getId())
                        .setDictDesc(item.getDictDesc())
                        .setDictKey(item.getDictKey())
                        .setDictStatus(item.getDictStatus())
                        .setDictVal(item.getDictVal());
                return vo;
            }).collect(Collectors.toList());
            return vos;
        }
        return null;
    }

    @CacheEvict(value = "dict:organ:map")
    @Override
    public int enableLeafDictById(Long id) {
        SysDict dbPo = sysDictMapper.selectOne(Wrappers.<SysDict>lambdaQuery()
                .select(SysDict::getId)
                .eq(SysDict::getId, id));
        if (dbPo != null) {
            LocalDateTime now = LocalDateTime.now();
            return sysDictMapper.update(null, Wrappers.<SysDict>lambdaUpdate()
                    .set(SysDict::getDictStatus, "0")
                    .set(SysDict::getUpdateTime, now)
                    .eq(SysDict::getId, id));
        }
        return -1;
    }
}