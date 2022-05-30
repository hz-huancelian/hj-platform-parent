package org.hj.chain.platform.component;

import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.service.IFactorService;
import org.hj.chain.platform.service.ISysDictService;
import org.hj.chain.platform.vo.DictParam;
import org.hj.chain.platform.vo.FactorClassInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO 字典工具
 * @Iteration : 1.0
 * @Date : 2021/5/8  7:28 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/08    create
 */
@Slf4j
@Component
public class DictUtils {

    @Autowired
    private ISysDictService sysDictService;

    @Autowired
    private IFactorService factorService;


    /**
     * TODO 获取所有字典包括无效效
     *
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 7:34 上午
     */
    public Map<Long, String> getAllDictMap() {
        Map<Long, String> dictMap = new HashMap<>();
        //平台
        Map<String, DictParam> dictParamMap = sysDictService.findDictMap();
        if (dictParamMap != null && !dictParamMap.isEmpty()) {
            for (Map.Entry<String, DictParam> entry : dictParamMap.entrySet()) {
                dictMap.put(Long.parseLong(entry.getKey()), entry.getValue().getDictVal());
            }
        }

        return dictMap;
    }


    /**
     * TODO 获取所有有效字典
     *
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 7:34 上午
     */
    public Map<Long, String> getValidDictMap(Long rootKey) {
        Map<Long, String> dictMap = new HashMap<>();
        Map<String, List<Long>> relMap = sysDictService.findDictRel();
        if (relMap != null && !relMap.isEmpty()) {
            List<Long> subKeys = relMap.get(String.valueOf(rootKey));
            //平台
            if (subKeys != null && !subKeys.isEmpty()) {
                Map<String, DictParam> dictParamMap = sysDictService.findDictMap();
                subKeys.stream().forEach(item -> {
                    DictParam dictParam = dictParamMap.get(String.valueOf(item));
                    if (dictParam != null && dictParam.getStatus().equals("0")) {
                        dictMap.put(item, dictParam.getDictVal());
                    }
                });
            }

        }

        return dictMap;
    }


    /**
     * TODO 一级、二级分类字典
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/10 10:42 上午
     */
    public Map<String, String> getFactorClassMap() {
        Map<String, String> resMap = new HashMap<>();
        List<FactorClassInfoVo> vos = factorService.findFstClass();
        if (vos != null && !vos.isEmpty()) {
            vos.stream().forEach(item -> {
                resMap.put(item.getId(), item.getName());
                List<FactorClassInfoVo> childrens = factorService.findSecdClassByClassId(item.getId());
                if (childrens != null && !childrens.isEmpty()) {
                    childrens.stream().forEach(subItem -> {
                        resMap.put(subItem.getId(), subItem.getName());
                    });
                }
            });
        }

        return resMap;
    }
}