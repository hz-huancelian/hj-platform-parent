package org.hj.chain.platform.judgeorgan.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.tdo.judgeorgan.JudgeOrganModifyTdo;
import org.hj.chain.platform.tdo.judgeorgan.JudgeOrganTdo;
import org.hj.chain.platform.vo.judgeorgan.JudgeOrganListVo;
import org.hj.chain.platform.vo.judgeorgan.JudgeOrganVo;

import java.util.List;

public interface IJudgeOrganService {


    /**
     * TODO 新增分包机构
     *
     * @param addTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 12:44 上午
     */
    Result<Object> addJudgeOrgan(JudgeOrganTdo addTdo);


    /**
     * TODO 修改分包机构
     *
     * @param modifyTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 12:44 上午
     */
    Result<Object> modifyJudgeOrganById(JudgeOrganModifyTdo modifyTdo);

    /**
     * TODO 机构删除
     *
     * @param id
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 9:21 上午
     */
    Result<Object> delById(String id);

    /**
     * TODO 分页查询
     *
     * @param pageVo
     * @param judgeOrganName
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 9:20 上午
     */
    IPage<JudgeOrganVo> findPageByCondition(PageVo pageVo, String judgeOrganName);


    /**
     * TODO 查询有效的分包机构列表
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 9:58 上午
     */
    List<JudgeOrganListVo> findValidList();

}
