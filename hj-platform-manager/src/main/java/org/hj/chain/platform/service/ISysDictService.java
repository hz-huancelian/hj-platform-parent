package org.hj.chain.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.vo.DictParam;
import org.hj.chain.platform.vo.SysDictRootVo;
import org.hj.chain.platform.vo.SysDictVo;

import java.util.List;
import java.util.Map;

public interface ISysDictService {


    /**
     * TODO 根据机构ID获取机构下的字典信息
     *
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 9:30 下午
     */
    Map<String, DictParam> findDictMap();


    /**
     * TODO 查询字典关系
     *
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 10:20 下午
     */
    Map<String, List<Long>> findDictRel();


    /**
     * TODO 添加叶子字典
     *
     * @param parentDictKey
     * @param dictVal
     * @param dictDesc
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 9:32 下午
     */
    int addLeafDict(Long parentDictKey, String dictVal, String dictDesc);

    /**
     * TODO 添加树字典
     *
     * @param dictVal
     * @param dictDesc
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 5:43 上午
     */
    int addTreeDict(String dictVal, String dictDesc);

    /**
     * TODO 修改子字典
     *
     * @param id
     * @param dictVal
     * @param dictDesc
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 9:33 下午
     */
    int updateLeafDict(Long id, String dictVal, String dictDesc);


    /**
     * TODO 修改树字典
     *
     * @param id
     * @param dictVal
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 6:02 上午
     */
    int updateTreeDict(Long id, String dictVal, String dictDesc);


    /**
     * TODO 删除字典
     *
     * @param id
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 9:35 下午
     */
    int delLeafDictById(Long id);

    /**
     * TODO 分页查询字典树
     *
     * @param pageVo
     * @param dictVal
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 6:24 上午
     */
    Result<Page<SysDictRootVo>> findPageByCondition(PageVo pageVo, String dictVal);

    /**
     * TODO 根据父key查所有叶子节点
     *
     * @param parentDictKey
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 6:33 上午
     */
    List<SysDictVo> findDictVosById(Long parentDictKey);

    /**
     * TODO 恢复子节点
     *
     * @param id
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 6:40 上午
     */
    int enableLeafDictById(Long id);
}
